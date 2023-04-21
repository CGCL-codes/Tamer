package openwar.DB;

import com.jme3.asset.AssetManager;
import com.jme3.material.Material;
import com.jme3.math.Vector3f;
import com.jme3.renderer.queue.RenderQueue.ShadowMode;
import com.jme3.scene.Spatial;
import com.jme3.texture.Texture;

/**
 *
 * @author kehl
 */
public class Model {

    String refName, file, diffuse;

    Vector3f scale;

    Spatial model;

    Texture diffuseTex;

    Material mat;

    public Model(String r, String f, String d, Vector3f s) {
        refName = r;
        file = f;
        diffuse = d;
        scale = s;
    }

    public Model(String r, String f, String d) {
        this(r, f, d, new Vector3f(1, 1, 1));
    }

    public void createData(AssetManager as) {
        mat = new Material(as, "Common/MatDefs/Misc/Unshaded.j3md");
        model = as.loadModel("models/" + file);
        model.setMaterial(mat);
        model.setShadowMode(ShadowMode.CastAndReceive);
        model.setLocalScale(scale);
        if (diffuse != null) mat.setTexture("ColorMap", as.loadTexture("models/" + diffuse));
    }
}
