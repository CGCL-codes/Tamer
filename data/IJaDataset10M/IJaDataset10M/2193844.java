package com.ardor3d.example.renderer;

import com.ardor3d.example.ExampleBase;
import com.ardor3d.example.Purpose;
import com.ardor3d.math.ColorRGBA;
import com.ardor3d.math.Quaternion;
import com.ardor3d.renderer.state.MaterialState;
import com.ardor3d.renderer.state.MaterialState.ColorMaterial;
import com.ardor3d.renderer.state.MaterialState.MaterialFace;
import com.ardor3d.scenegraph.Mesh;
import com.ardor3d.scenegraph.shape.Dome;
import com.ardor3d.scenegraph.shape.Quad;

/**
 * A demonstration using MaterialState to set lighting equation parameters.
 */
@Purpose(htmlDescriptionKey = "com.ardor3d.example.renderer.MaterialFaceExample", thumbnailPath = "com/ardor3d/example/media/thumbnails/renderer_MaterialFaceExample.jpg", maxHeapMemory = 64)
public class MaterialFaceExample extends ExampleBase {

    public static void main(final String[] args) {
        start(MaterialFaceExample.class);
    }

    @Override
    public void initExample() {
        _root.attachChild(createFloor());
        _root.attachChild(createSky());
        _lightState.setTwoSidedLighting(true);
    }

    private Mesh createFloor() {
        final Mesh floor = new Quad("Floor", 100, 100);
        floor.updateModelBound();
        floor.setDefaultColor(ColorRGBA.GREEN);
        floor.setTranslation(0, -5, -20);
        floor.setRotation(new Quaternion(-1, 0, 0, 1));
        final MaterialState ms = new MaterialState();
        floor.setRenderState(ms);
        ms.setColorMaterial(ColorMaterial.Diffuse);
        ms.setColorMaterialFace(MaterialFace.FrontAndBack);
        return floor;
    }

    private Mesh createSky() {
        final Dome sky = new Dome("Sky", 30, 30, 10);
        sky.updateModelBound();
        sky.setSolidColor(ColorRGBA.RED);
        sky.setTranslation(0, 10, -20);
        final MaterialState ms = new MaterialState();
        sky.setRenderState(ms);
        ms.setColorMaterial(ColorMaterial.Diffuse);
        ms.setColorMaterialFace(MaterialFace.Front);
        ms.setShininess(MaterialFace.FrontAndBack, 100);
        ms.setSpecular(MaterialFace.Front, ColorRGBA.RED);
        ms.setSpecular(MaterialFace.Back, ColorRGBA.WHITE);
        ms.setDiffuse(MaterialFace.Back, ColorRGBA.BLUE);
        return sky;
    }
}
