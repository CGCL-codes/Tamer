package cz.cube.mtheory.test.nkd;

import com.jme.app.SimpleGame;
import com.jme.bounding.BoundingBox;
import com.jme.math.Vector3f;
import com.jme.renderer.ColorRGBA;
import com.jme.scene.state.MaterialState;

public class TerrainContinuousTest extends SimpleGame {

    private TerrainContinuous tc;

    public static void main(String[] args) {
        TerrainContinuousTest app = new TerrainContinuousTest();
        app.start();
    }

    @Override
    protected void simpleInitGame() {
        display.setTitle("www.outshine.de");
        display.setVSyncEnabled(true);
        buildTerrain();
        MaterialState ms = display.getRenderer().createMaterialState();
        ms.setEmissive(new ColorRGBA(1.0f, 1.0f, 1.0f, 0.5f));
        tc.setRenderState(ms);
        tc.setLocalTranslation(0, 0, -10000);
        rootNode.attachChild(tc);
    }

    private void buildTerrain() {
        Vector3f terrainScale = new Vector3f(10f, 1f, 10f);
        tc = new TerrainContinuous("Terrain", terrainScale, false, cam);
        tc.setModelBound(new BoundingBox());
        tc.updateModelBound();
    }

    @Override
    public void simpleRender() {
        super.simpleRender();
    }
}
