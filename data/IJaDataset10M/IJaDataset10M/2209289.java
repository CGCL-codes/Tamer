package vn.edu.hcmup.fit.k34.c104.arpostcard.scenes;

import java.io.IOException;
import java.io.InputStream;
import javax.microedition.khronos.opengles.GL10;
import vn.edu.hcmup.fit.k34.c104.arpostcard.Config;
import vn.edu.hcmup.fit.k34.c104.arpostcard.R;
import vn.edu.hcmup.fit.k34.c104.arpostcard.Util;
import com.threed.jpct.Loader;
import com.threed.jpct.Object3D;
import com.threed.jpct.Texture;
import com.threed.jpct.TextureManager;

public class FishesScene implements IScene {

    Object3D meshes[], fish;

    @Override
    public Object3D getRoot() {
        return fish;
    }

    @Override
    public void setVisibility(boolean mode) {
        if (fish == null) return;
        for (Object3D obj : meshes) {
            obj.setVisibility(mode);
        }
    }

    @Override
    public void loadScene() {
        if (fish != null) return;
        InputStream is = Config.mResources.openRawResource(R.raw.shark);
        meshes = Loader.load3DS(is, 0.07f);
        try {
            is.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        fish = Util.pack(meshes);
    }

    @Override
    public void onDrawFrame(GL10 gl) {
    }

    @Override
    public void onUpdate() {
    }

    @Override
    public boolean onPick(Object3D obj) {
        return false;
    }
}
