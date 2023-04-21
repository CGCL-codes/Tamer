package org.xith3d.test.scenegraph;

import java.io.IOException;
import java.net.URL;
import org.jagatoo.input.InputSystem;
import org.jagatoo.input.devices.components.Key;
import org.jagatoo.input.events.KeyReleasedEvent;
import org.jagatoo.loaders.textures.locators.TextureStreamLocator;
import org.xith3d.base.Xith3DEnvironment;
import org.xith3d.input.FirstPersonInputHandler;
import org.xith3d.loaders.texture.TextureLoader;
import org.xith3d.loop.CanvasFPSListener;
import org.xith3d.render.Canvas3D;
import org.xith3d.resources.ResourceLocator;
import org.xith3d.scenegraph.BranchGroup;
import org.xith3d.scenegraph.primitives.SkyBox;
import org.xith3d.scenegraph.primitives.Sphere;
import org.xith3d.test.Xith3DTest;
import org.xith3d.test.util.TestUtils;
import org.xith3d.utility.commandline.BasicApplicationArguments;
import org.xith3d.utility.events.WindowClosingRenderLoopEnder;

@Xith3DTest.Description(fulltext = { "Simple test of the SkyBox class." }, authors = { "Marvin Froehlich (aka Qudus)" })
public class SkyBoxTest extends Xith3DTest {

    @Override
    public void onKeyReleased(KeyReleasedEvent e, Key key) {
        switch(key.getKeyID()) {
            case ESCAPE:
                this.end();
                break;
        }
    }

    private static boolean check(URL baseURL, String name) {
        URL url = null;
        try {
            url = new URL(baseURL, name + "/front.png");
        } catch (IOException e) {
            return (false);
        }
        try {
            url.openStream();
        } catch (IOException e) {
            return (false);
        }
        return (true);
    }

    /**
     * Creates a SkyBox.
     */
    public static SkyBox createSkyBox(URL baseURL, String name) {
        final long t0 = TestUtils.dumpAction("Creating SkyBox from \"" + baseURL + name + "\"...");
        TextureStreamLocator tsl = TextureLoader.getInstance().addTextureStreamLocator(baseURL);
        final String ext = check(baseURL, name) ? "png" : "jpg";
        SkyBox sb = new SkyBox(name + "/front." + ext, name + "/right." + ext, name + "/back." + ext, name + "/left." + ext, name + "/top." + ext, name + "/bottom." + ext);
        TextureLoader.getInstance().removeTextureStreamLocator(tsl);
        TestUtils.dumpDoneIn(t0);
        return (sb);
    }

    public SkyBoxTest(BasicApplicationArguments arguments) throws Throwable {
        super(arguments.getMaxFPS());
        Xith3DEnvironment env = new Xith3DEnvironment(this);
        ResourceLocator resLoc = TestUtils.createResourceLocator();
        resLoc.createAndAddTSL("textures");
        env.addRenderPass(createSkyBox(resLoc.getResource("skyboxes/"), "normal"));
        BranchGroup scene = new BranchGroup();
        scene.addChild(new Sphere(64, 64, "deathstar.jpg"));
        env.addPerspectiveBranch(scene);
        Canvas3D canvas = createCanvas(arguments.getCanvasConstructionInfo(), getClass().getSimpleName());
        env.addCanvas(canvas);
        canvas.addWindowClosingListener(new WindowClosingRenderLoopEnder(this));
        this.addFPSListener(new CanvasFPSListener(canvas));
        InputSystem.getInstance().registerNewKeyboardAndMouse(canvas.getPeer());
        FirstPersonInputHandler fpHandler = FirstPersonInputHandler.createDefault(env.getView(), canvas, arguments.getMouseYInverted());
        fpHandler.getBindingsManager().createDefaultBindings();
        InputSystem.getInstance().addInputHandler(fpHandler);
    }

    public static void main(String[] args) throws Throwable {
        SkyBoxTest test = new SkyBoxTest(parseCommandLine(args));
        test.begin();
    }
}
