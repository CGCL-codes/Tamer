package org.xith3d.benchmarks;

import java.io.IOException;
import org.jagatoo.input.InputSystem;
import org.jagatoo.input.devices.components.Key;
import org.jagatoo.input.events.KeyPressedEvent;
import org.jagatoo.input.events.KeyReleasedEvent;
import org.jagatoo.loaders.models.bsp.BSPClusterManager;
import org.jagatoo.loaders.models.bsp.BSPPrototypeLoader;
import org.openmali.types.twodee.Sized2iRO;
import org.openmali.vecmath2.Colorf;
import org.xith3d.base.Xith3DEnvironment;
import org.xith3d.input.FirstPersonInputHandler;
import org.xith3d.input.modules.fpih.FPIHInputAction;
import org.xith3d.loaders.models.Model;
import org.xith3d.loaders.models.ModelLoader;
import org.xith3d.render.Canvas3D;
import org.xith3d.render.RenderPass;
import org.xith3d.resources.ResourceLocator;
import org.xith3d.scenegraph.BSPTreeGroup;
import org.xith3d.scenegraph.BranchGroup;
import org.xith3d.scenegraph.SceneGraph;
import org.xith3d.scenegraph.View;
import org.xith3d.test.Xith3DTest;
import org.xith3d.test.util.TestUtils;
import org.xith3d.ui.hud.HUD;
import org.xith3d.ui.hud.utils.HUDFont;
import org.xith3d.ui.hud.widgets.Label;
import org.xith3d.ui.text2d.TextAlignment;
import org.xith3d.utility.camera.flight.CameraFlight;
import org.xith3d.utility.camera.flight.CameraFlightListener;
import org.xith3d.utility.commandline.BasicApplicationArguments;
import org.xith3d.utility.events.WindowClosingRenderLoopEnder;

@Xith3DTest.Description(fulltext = { "In this benchmark a CameraFlight through a Quake3 map is tested." }, authors = { "Marvin Froehlich (aka Qudus)" })
public class Q3FlightBenchmark extends Xith3DTest implements CameraFlightListener {

    private BSPClusterManager clusterManager;

    protected View view;

    protected FirstPersonInputHandler fpHandler;

    private CameraFlight camFlight = null;

    private SceneGraph sceneGraph;

    private HUD hud;

    @Override
    protected void exit() {
        super.exit();
    }

    private void startFlight() {
        if (hud != null) sceneGraph.removeHUD(hud);
        try {
            fpHandler.setSuspended(true);
            long t0 = System.currentTimeMillis();
            camFlight = new CameraFlight(ResourceLocator.getInstance().getResource("pdmq3duel5/inter_engine_benchmark--2006-09-29.cflt"));
            camFlight.addCameraFlightListener(this);
            camFlight.start(getGameMicroTime() + (System.currentTimeMillis() - t0));
            System.out.println("CameraFlight started.");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void stopFlight() {
        camFlight = null;
        fpHandler.updateViewInverse();
        fpHandler.setSuspended(false);
        if (hud != null) sceneGraph.addHUD(hud);
    }

    @Override
    public void onKeyPressed(KeyPressedEvent e, Key key) {
        switch(key.getKeyID()) {
            case F4:
                if (clusterManager != null) {
                    clusterManager.setPVSUsage(!clusterManager.isPVSUsed());
                    System.out.println("PVS: " + (clusterManager.isPVSUsed() ? "on" : "off"));
                }
                break;
            case Y:
                fpHandler.flipMouseYAxis();
                break;
        }
    }

    @Override
    public void onKeyReleased(KeyReleasedEvent e, Key key) {
        switch(key.getKeyID()) {
            case F10:
            case SPACE:
                if (camFlight == null) {
                    startFlight();
                } else {
                    stopFlight();
                }
                break;
            case ESCAPE:
                System.out.println("average FPS: " + ((float) this.getIterationsCount() / getTimingMode().getSecondsAsFloat(getGameTime())));
                this.end();
                break;
        }
    }

    /**
     * This event is fired, when the CameraFlight ended.
     * 
     * @param frames frames rendered during the flight
     * @param millis milliseconds needed for the flight
     * @param averageFPS average FPS for the flight
     */
    public void onCameraFlightEnded(long frames, long millis, float averageFPS) {
        System.out.println("CameraFlight ended. (average FPS: " + averageFPS + ")");
    }

    @Override
    protected void prepareNextFrame(long gameTime, long frameTime, TimingMode timingMode) {
        if (camFlight != null) {
            camFlight.updateCamera(view, gameTime, timingMode);
        }
        super.prepareNextFrame(gameTime, frameTime, timingMode);
    }

    private HUD createHUD(SceneGraph sg, Sized2iRO resolution) {
        this.hud = new HUD(resolution.getWidth(), resolution.getHeight(), 800f, 600f);
        Label hint2 = new Label(hud.getResX(), hud.getResY(), "Hit \"Y\" to invert the mouse y-axis.", HUDFont.getFont("Verdana", HUDFont.BOLD, 18), Colorf.WHITE, TextAlignment.BOTTOM_LEFT);
        hint2.setPadding(40, 10, 10, 10);
        hud.getContentPane().addWidget(hint2);
        Label hint1 = new Label(hud.getResX(), hud.getResY(), "Hit \"SPACE\" to start the flight, ESC to exit.", HUDFont.getFont("Verdana", HUDFont.BOLD, 18), Colorf.WHITE, TextAlignment.BOTTOM_LEFT);
        hint1.setPadding(10);
        hud.getContentPane().addWidget(hint1);
        sg.addHUD(hud);
        return (hud);
    }

    protected Model loadLevel() throws Exception {
        final ResourceLocator resLoc = ResourceLocator.getInstance();
        BSPPrototypeLoader.loadNormals = false;
        Model scene = ModelLoader.getInstance().loadModel(resLoc.getResource("pdmq3duel5/pdmq3duel5.bsp"));
        if (scene.getMainGroup() instanceof BSPTreeGroup) {
            this.clusterManager = (BSPClusterManager) ((BSPTreeGroup) scene.getMainGroup()).getBSPVisibilityUpdater();
        }
        return (scene);
    }

    protected RenderPass createSceneGraph(Xith3DEnvironment env, Sized2iRO resolution) throws Exception {
        final RenderPass rp = env.addPerspectiveBranch(new BranchGroup(loadLevel()));
        rp.getConfig().setFrontClipDistance(0.1f);
        rp.getConfig().setBackClipDistance(2000.0f);
        rp.setClipperEnabled(false);
        rp.getConfig().setOpaqueSortingPolicy(org.xith3d.render.Renderer.OpaqueSortingPolicy.SORT_NONE);
        this.sceneGraph = env;
        createHUD(env, resolution);
        return (rp);
    }

    public Q3FlightBenchmark(BasicApplicationArguments arguments) throws Throwable {
        super((Float) null);
        Xith3DEnvironment env = new Xith3DEnvironment(2f, 2.5f, -4f, 0f, 2f, 0f, 0f, 1f, 0f, this);
        this.view = env.getView();
        ResourceLocator.setSingletonInstance(TestUtils.createResourceLocator().getSubLocator("levels/quake3/"));
        createSceneGraph(env, arguments.getResolution());
        Canvas3D canvas = createCanvas(arguments.getCanvasConstructionInfo(), getClass().getSimpleName());
        canvas.disableLighting();
        env.addCanvas(canvas);
        canvas.addWindowClosingListener(new WindowClosingRenderLoopEnder(this));
        InputSystem.getInstance().registerNewKeyboardAndMouse(canvas.getPeer());
        this.fpHandler = FirstPersonInputHandler.createDefault(env.getView(), canvas, arguments.getMouseYInverted());
        fpHandler.getBindingsManager().createDefaultBindings();
        fpHandler.getBindingsManager().unbind(FPIHInputAction.CROUCH);
        InputSystem.getInstance().addInputHandler(fpHandler);
        System.out.println("Hit \"Y\" to invert the mouse y-axis. Hit ESC to exit.");
    }

    public static void main(String[] args) throws Throwable {
        Q3FlightBenchmark test = new Q3FlightBenchmark(parseCommandLine(args));
        test.begin();
    }
}
