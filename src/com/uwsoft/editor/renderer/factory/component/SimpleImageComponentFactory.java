/*
 * ******************************************************************************
 *  * Copyright 2015 See AUTHORS file.
 *  *
 *  * Licensed under the Apache License, Version 2.0 (the "License");
 *  * you may not use this file except in compliance with the License.
 *  * You may obtain a copy of the License at
 *  *
 *  *   http://www.apache.org/licenses/LICENSE-2.0
 *  *
 *  * Unless required by applicable law or agreed to in writing, software
 *  * distributed under the License is distributed on an "AS IS" BASIS,
 *  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  * See the License for the specific language governing permissions and
 *  * limitations under the License.
 *  *****************************************************************************
 */

package com.uwsoft.editor.renderer.factory.component;

import box2dLight.RayHandler;

import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.physics.box2d.World;
import com.uwsoft.editor.renderer.components.DimensionsComponent;
import com.uwsoft.editor.renderer.components.PolygonComponent;
import com.uwsoft.editor.renderer.components.TextureRegionComponent;
import com.uwsoft.editor.renderer.data.MainItemVO;
import com.uwsoft.editor.renderer.data.ProjectInfoVO;
import com.uwsoft.editor.renderer.data.ResolutionEntryVO;
import com.uwsoft.editor.renderer.data.SimpleImageVO;
import com.uwsoft.editor.renderer.factory.EntityFactory;
import com.uwsoft.editor.renderer.resources.IResourceRetriever;
import com.uwsoft.editor.renderer.utils.ComponentRetriever;

/**
 * Created by azakhary on 5/22/2015.
 */
public class SimpleImageComponentFactory extends ComponentFactory {

   private TextureRegionComponent textureRegionComponent;

    public SimpleImageComponentFactory(RayHandler rayHandler, World world, IResourceRetriever rm) {
        super(rayHandler, world, rm);
    }

    public void createComponents(Entity root, Entity entity, MainItemVO vo) {
        createCommonComponents( entity, vo, EntityFactory.IMAGE_TYPE);
        textureRegionComponent = createTextureRegionComponent(entity, (SimpleImageVO) vo);
        createParentNodeComponent(root, entity);
        createNodeComponent(root, entity);
    }

    @Override
    protected DimensionsComponent createDimensionsComponent(Entity entity, MainItemVO vo) {
        DimensionsComponent component = new DimensionsComponent();

        entity.add(component);

        return component;
    }

    protected TextureRegionComponent createTextureRegionComponent(Entity entity, SimpleImageVO vo) {
        TextureRegionComponent component = new TextureRegionComponent();
        component.regionName = vo.imageName;
        component.region = rm.getTextureRegion(vo.imageName);
        component.isRepeat = vo.isRepeat;
        component.isPolygon = vo.isPolygon;

        ResolutionEntryVO resolutionEntryVO = rm.getLoadedResolution();
        ProjectInfoVO projectInfoVO = rm.getProjectVO();
        float multiplier = resolutionEntryVO.getMultiplier(rm.getProjectVO().originalResolution);

        DimensionsComponent dimensionsComponent = ComponentRetriever.get(entity, DimensionsComponent.class);

        PolygonComponent polygonComponent = ComponentRetriever.get(entity, PolygonComponent.class);
        if(component.isPolygon && polygonComponent != null && polygonComponent.vertices != null) {
            component.setPolygonSprite(polygonComponent);
            dimensionsComponent.setPolygon(polygonComponent);
        }
        dimensionsComponent.width = (float) component.region.getRegionWidth() * multiplier / projectInfoVO.pixelToWorld;
        dimensionsComponent.height = (float) component.region.getRegionHeight() * multiplier / projectInfoVO.pixelToWorld;


        entity.add(component);

        return component;
    }
}
