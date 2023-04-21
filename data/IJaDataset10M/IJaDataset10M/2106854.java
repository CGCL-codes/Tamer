package org.test.xith3d;

import org.xith3d.render.base.Xith3DEnvironment;
import org.xith3d.render.canvas.Canvas3DWrapper;
import org.xith3d.render.CanvasPeer;
import org.xith3d.render.RenderOptions;
import org.xith3d.render.Option;
import org.xith3d.render.loop.RenderLoop;
import org.xith3d.loaders.scene.Scene;
import org.xith3d.scenegraph.*;
import org.xith3d.geometry.GeoSphere;
import org.xith3d.geometry.Sphere;
import org.test.xith3d.loaders.ModelLoader;
import org.test.xith3d.loaders.ModelLoaderFactory;
import javax.vecmath.Tuple3f;
import javax.vecmath.Vector3f;
import javax.vecmath.Color3f;

/**
 * Created by IntelliJ IDEA.
 * User: Pablo
 * Date: Jan 30, 2007
 * Time: 9:57:50 PM
 * To change this template use File | Settings | File Templates.
 */
public class BasicXith3DTest {

    protected static final String OBJ_MODEL_FILENAME = "resources/models/obj/battletank_obj/battletank.obj";

    protected static final String DAE_MODEL_FILENAME = "duck.dae";

    protected static final String TDS_MODEL_FILENAME = "resources/models/3ds/tank.3ds";

    public Group loadScene() {
        Group scene = null;
        ModelLoader loader = ModelLoaderFactory.createModelLoader(ModelLoaderFactory.ModelLoaderType.DAE_LOADER);
        long start = System.currentTimeMillis();
        scene = loader.loadScene(DAE_MODEL_FILENAME);
        System.out.println("Load took: " + ((System.currentTimeMillis() - start) / 1000.0) + " seconds");
        return scene;
    }

    public void showBasicScene() {
        Xith3DEnvironment environment = new Xith3DEnvironment();
        Canvas3DWrapper canvas = Canvas3DWrapper.createStandalone(CanvasPeer.OpenGLLayer.JOGL, Canvas3DWrapper.Resolution.RES_800X600, Canvas3DWrapper.ColorDepth.B16, "BasicScene");
        environment.addCanvas(canvas);
        canvas.setRenderOption(Option.USE_TEXTURES, true);
        Group scene = loadScene();
        BranchGroup bg = new BranchGroup();
        bg.addChild(scene);
        environment.addBranchGraph(bg);
        environment.getView().lookAt(new Vector3f(50, 35, 50), new Vector3f(0, 0, 0), new Vector3f(0, 1, 0));
        Light light = new AmbientLight(true);
        light.setColor(new Color3f(30, 0, 100));
        bg.addChild(light);
        GeoSphere sph = new GeoSphere(8, GeometryArray.COLOR_3 | GeometryArray.NORMALS, 7f);
        TransformGroup transformGroup = new TransformGroup();
        transformGroup.addChild(sph);
        Transform3D t3d = new Transform3D();
        t3d.setTranslation(20, 0, 0);
        transformGroup.setTransform(t3d);
        bg.addChild(transformGroup);
        RenderLoop renderLoop = new RenderLoop() {

            protected void onFPSCountIntervalHit(double fps) {
                System.out.println("fps = " + fps);
            }
        };
        renderLoop.setMaxFPS(50);
        renderLoop.addRenderEngine(environment);
        renderLoop.begin();
    }

    public static void main(String[] args) {
        new BasicXith3DTest().showBasicScene();
    }
}
