package jme3test.scene;

import com.jme3.app.SimpleApplication;
import com.jme3.asset.plugins.HttpZipLocator;
import com.jme3.asset.plugins.ZipLocator;
import com.jme3.light.AmbientLight;
import com.jme3.light.DirectionalLight;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
import com.jme3.scene.Geometry;
import com.jme3.scene.Spatial;
import com.jme3.scene.shape.Sphere;
import com.jme3.util.SkyFactory;
import java.io.File;

public class TestSceneLoading extends SimpleApplication {

    private Sphere sphereMesh = new Sphere(32, 32, 10, false, true);

    private Geometry sphere = new Geometry("Sky", sphereMesh);

    private static boolean useHttp = false;

    public static void main(String[] args) {
        TestSceneLoading app = new TestSceneLoading();
        app.start();
    }

    @Override
    public void simpleUpdate(float tpf) {
        sphere.setLocalTranslation(cam.getLocation());
    }

    public void simpleInitApp() {
        this.flyCam.setMoveSpeed(10);
        rootNode.attachChild(SkyFactory.createSky(assetManager, "Textures/Sky/Bright/BrightSky.dds", false));
        File file = new File("wildhouse.zip");
        if (!file.exists()) {
            useHttp = true;
        }
        if (useHttp) {
            assetManager.registerLocator("http://jmonkeyengine.googlecode.com/files/wildhouse.zip", HttpZipLocator.class.getName());
        } else {
            assetManager.registerLocator("wildhouse.zip", ZipLocator.class.getName());
        }
        Spatial scene = assetManager.loadModel("main.scene");
        AmbientLight al = new AmbientLight();
        scene.addLight(al);
        DirectionalLight sun = new DirectionalLight();
        sun.setDirection(new Vector3f(0.69077975f, -0.6277887f, -0.35875428f).normalizeLocal());
        sun.setColor(ColorRGBA.White.clone().multLocal(2));
        scene.addLight(sun);
        rootNode.attachChild(scene);
    }
}
