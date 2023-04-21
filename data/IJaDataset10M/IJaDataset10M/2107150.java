package org.xith3d.demos;

import org.jagatoo.input.InputSystem;
import org.jagatoo.input.devices.components.Key;
import org.jagatoo.input.devices.components.Keys;
import org.jagatoo.input.events.KeyReleasedEvent;
import org.openmali.FastMath;
import org.openmali.vecmath2.Colorf;
import org.openmali.vecmath2.Vector3f;
import org.xith3d.base.Xith3DEnvironment;
import org.xith3d.demos.utils.FlagFactory;
import org.xith3d.input.FirstPersonInputHandler;
import org.xith3d.loop.CanvasFPSListener;
import org.xith3d.render.Canvas3D;
import org.xith3d.resources.ResourceLocator;
import org.xith3d.scenegraph.BranchGroup;
import org.xith3d.scenegraph.DirectionalLight;
import org.xith3d.scenegraph.SceneGraph;
import org.xith3d.scenegraph.View;
import org.xith3d.test.Xith3DTest;
import org.xith3d.test.util.TestUtils;
import org.xith3d.utility.commandline.BasicApplicationArguments;
import org.xith3d.utility.events.WindowClosingRenderLoopEnder;

@Xith3DTest.Description(fulltext = { "Very cool flag simulation." }, authors = { "Abdul Bezrati (aka JavaCoolDude)", "Marvin Froehlich (aka Qudus)" })
public class FlagSimulation extends Xith3DTest {

    private FlagFactory flagFactory;

    @Override
    public void onKeyReleased(KeyReleasedEvent e, Key key) {
        switch(key.getKeyID()) {
            case C:
                flagFactory.switchFillWire();
                break;
            case SPACE:
                flagFactory.resetFlag();
                break;
            case ESCAPE:
                this.end();
                break;
        }
    }

    public void createSceneGraph(SceneGraph sg) {
        BranchGroup scene = new BranchGroup();
        this.flagFactory = new FlagFactory("eagle.jpg", "usa.jpg");
        scene.addChild(new DirectionalLight(true, Colorf.WHITE, new Vector3f(-1f, -1f, -1f)));
        scene.addChild(flagFactory.getBackGround());
        scene.addChild(flagFactory.getFlag());
        sg.addPerspectiveBranch(scene);
        this.addUpdatable(flagFactory);
    }

    public FlagSimulation(BasicApplicationArguments arguments) throws Throwable {
        super(arguments.getMaxFPS());
        Xith3DEnvironment env = new Xith3DEnvironment(10f, 5f, 60f, 10f, 5f, 0f, 0f, 1f, 0f, this);
        View view = env.getView();
        view.setFieldOfView(FastMath.toRad(22.5f));
        view.setBackClipDistance(10000f);
        ResourceLocator resLoc = TestUtils.createResourceLocator();
        resLoc.createAndAddTSL("textures");
        createSceneGraph(env);
        Canvas3D canvas = createCanvas(arguments.getCanvasConstructionInfo(), getClass().getSimpleName());
        canvas.setBackgroundColor(Colorf.BLACK);
        env.addCanvas(canvas);
        canvas.addWindowClosingListener(new WindowClosingRenderLoopEnder(this));
        this.addFPSListener(new CanvasFPSListener(canvas));
        InputSystem.getInstance().registerNewKeyboardAndMouse(canvas.getPeer());
        FirstPersonInputHandler fpih = new FirstPersonInputHandler(env.getView(), canvas, 1.0f, 1.0f, arguments.getMouseYInverted(), 3.0f);
        fpih.getBindingsManager().createDefaultBindings();
        fpih.getBindingsManager().unbind(Keys.SPACE);
        InputSystem.getInstance().addInputHandler(fpih);
    }

    public static void main(String[] args) throws Throwable {
        FlagSimulation test = new FlagSimulation(parseCommandLine(args));
        test.begin();
    }
}
