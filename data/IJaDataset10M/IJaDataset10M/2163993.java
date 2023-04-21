package net.ddaniels.jmetest.boxgame;

import jmetest.renderer.TestEnvMap;
import com.jme.bounding.BoundingBox;
import com.jme.bounding.OrientedBoundingBox;
import com.jme.image.Texture;
import com.jme.math.Vector3f;
import com.jme.scene.SharedMesh;
import com.jme.scene.shape.Box;
import com.jme.scene.state.TextureState;
import com.jme.system.DisplaySystem;
import com.jme.util.TextureManager;

public abstract class VisibleGameEntity extends AbstractGameEntity {

    private static Box meshData;

    private static TextureState ts;

    private SharedMesh model;

    private TextureState modelTS;

    public VisibleGameEntity(String name, String textureFile) {
        model = new SharedMesh(name, getMeshData());
        model.setModelBound(new OrientedBoundingBox());
        model.updateModelBound();
        modelTS = getTextureState();
        model.setRenderState(modelTS);
        model.updateRenderState();
        this.attachChild(model);
    }

    public SharedMesh getModel() {
        return model;
    }

    private static TextureState getTextureState() {
        if (ts == null) {
            DisplaySystem display = DisplaySystem.getDisplaySystem();
            ts = display.getRenderer().createTextureState();
            Texture t0 = TextureManager.loadTexture(TestEnvMap.class.getClassLoader().getResource("jmetest/data/images/Monkey.jpg"), Texture.MM_LINEAR_LINEAR, Texture.FM_LINEAR);
            t0.setWrap(Texture.WM_WRAP_S_WRAP_T);
            ts.setTexture(t0);
        }
        return ts;
    }

    private static Box getMeshData() {
        if (meshData == null) {
            meshData = new Box("basicBox", new Vector3f(), new Vector3f(5, 5, 5));
        }
        return meshData;
    }
}
