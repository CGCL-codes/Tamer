package jmeTuts;

import java.util.logging.Level;
import java.util.logging.Logger;
import com.jme.app.BaseGame;
import com.jme.app.SimpleGame;
import com.jme.image.Texture;
import com.jme.input.FirstPersonHandler;
import com.jme.input.InputHandler;
import com.jme.input.KeyBindingManager;
import com.jme.input.KeyInput;
import com.jme.input.MouseInput;
import com.jme.input.joystick.JoystickInput;
import com.jme.light.PointLight;
import com.jme.math.Vector3f;
import com.jme.renderer.Camera;
import com.jme.renderer.ColorRGBA;
import com.jme.scene.Node;
import com.jme.scene.Spatial;
import com.jme.scene.Text;
import com.jme.scene.state.BlendState;
import com.jme.scene.state.LightState;
import com.jme.scene.state.TextureState;
import com.jme.scene.state.WireframeState;
import com.jme.scene.state.ZBufferState;
import com.jme.system.DisplaySystem;
import com.jme.system.JmeException;
import com.jme.util.TextureManager;
import com.jme.util.Timer;
import com.jme.util.geom.Debugger;

/**
 * Used to demonstrate the inner workings of SimpleGame
 * 
 * @author Lorenxs
 *
 */
public class HelloSimpleGame extends BaseGame {

    private static final Logger logger = Logger.getLogger(HelloSimpleGame.class.getName());

    public static void main(String[] args) {
        HelloSimpleGame app = new HelloSimpleGame();
        app.setConfigShowMode(ConfigShowMode.AlwaysShow);
        app.start();
    }

    /** The camera from which we see throu */
    protected Camera cam;

    /** The root of the scene graph */
    protected Node rootNode;

    /** Handles keyboard/mouse input */
    protected InputHandler input;

    /** High resolution timer for jme */
    protected Timer timer;

    /** The root node for text */
    protected Node fpsNode;

    /** Displays information at the bottom of the screen */
    protected Text fps;

    /** Time per Frame */
    protected float tpf;

    /** Whether or not the renderer should display bounds */
    protected boolean showBounds = false;

    /** A wirestate to turn on or off for the rootNode */
    protected WireframeState wireState;

    /** A lightstate to turn on or off for the rootNode */
    protected LightState lightState;

    /** Location for the bottom text's font */
    public static String fontLocation = "/com/jme/app/defaultfont.tga";

    /**
	 * Cleans up the keyboard
     * @see com.jme.app.AbstractGame#cleanup()
	 */
    @Override
    protected void cleanup() {
        logger.info("Cleaning up resources");
        KeyInput.destroyIfInitalized();
        MouseInput.destroyIfInitalized();
        JoystickInput.destroyIfInitalized();
    }

    /**
	 * Creates a rootNode, lighting, statistic text and other basic render states.
	 * Called in BaseGame.start() after initSystem();
     * @see com.jme.app.AbstractGame#initGame()
	 */
    @Override
    protected void initGame() {
        rootNode = new Node("rootNode");
        wireState = display.getRenderer().createWireframeState();
        wireState.setEnabled(false);
        rootNode.setRenderState(wireState);
        ZBufferState buf = display.getRenderer().createZBufferState();
        buf.setEnabled(true);
        buf.setFunction(ZBufferState.TestFunction.LessThanOrEqualTo);
        rootNode.setRenderState(buf);
        BlendState as1 = display.getRenderer().createBlendState();
        as1.setBlendEnabled(true);
        as1.setSourceFunction(BlendState.SourceFunction.SourceAlpha);
        as1.setDestinationFunction(BlendState.DestinationFunction.One);
        as1.setTestEnabled(true);
        as1.setTestFunction(BlendState.TestFunction.GreaterThan);
        as1.setEnabled(true);
        TextureState font = display.getRenderer().createTextureState();
        font.setTexture(TextureManager.loadTexture(SimpleGame.class.getResource(fontLocation), Texture.MinificationFilter.BilinearNearestMipMap, Texture.MagnificationFilter.Bilinear));
        font.setEnabled(true);
        fps = new Text("FPS label", "");
        fps.setCullHint(Spatial.CullHint.Never);
        fps.setTextureCombineMode(Spatial.TextureCombineMode.Replace);
        fpsNode = new Node("FPS node");
        fpsNode.attachChild(fps);
        fpsNode.setRenderState(font);
        fpsNode.setRenderState(as1);
        fpsNode.setCullHint(Spatial.CullHint.Never);
        PointLight light = new PointLight();
        light.setDiffuse(new ColorRGBA(1, 1, 1, 1));
        light.setAmbient(new ColorRGBA(.5f, .5f, .5f, 1f));
        light.setLocation(new Vector3f(100, 100, 100));
        light.setEnabled(true);
        lightState = display.getRenderer().createLightState();
        lightState.setEnabled(true);
        lightState.attach(light);
        rootNode.setRenderState(lightState);
        simpleInitGame();
        rootNode.updateGeometricState(0, true);
        rootNode.updateRenderState();
        fpsNode.updateGeometricState(0, true);
        fpsNode.updateRenderState();
    }

    private void simpleInitGame() {
    }

    /**
	 * Creates display, sets up camera and binds keys. Called directly after the dialog box
	 * @see com.jme.app.AbstractGame#initSystem()
	 */
    @Override
    protected void initSystem() {
        try {
            display = DisplaySystem.getDisplaySystem(settings.getRenderer());
            display.createWindow(settings.getWidth(), settings.getHeight(), settings.getDepth(), settings.getFrequency(), settings.isFullscreen());
            cam = display.getRenderer().createCamera(display.getWidth(), display.getHeight());
        } catch (JmeException e) {
            logger.log(Level.SEVERE, "Could not create a displaySystem", e);
            System.exit(1);
        }
        display.getRenderer().setBackgroundColor(ColorRGBA.black);
        cam.setFrustumPerspective(45.0f, (float) display.getWidth() / display.getHeight(), 1, 1000);
        Vector3f loc = new Vector3f(0, 0, 25);
        Vector3f left = new Vector3f(-1, 0, 0);
        Vector3f up = new Vector3f(0, 1, 0);
        Vector3f dir = new Vector3f(0, 0, -1);
        cam.setFrame(loc, left, up, dir);
        cam.update();
        display.getRenderer().setCamera(cam);
        FirstPersonHandler firstPersonHandler = new FirstPersonHandler(cam);
        firstPersonHandler.getKeyboardLookHandler().setActionSpeed(10);
        firstPersonHandler.getMouseLookHandler().setActionSpeed(1);
        input = firstPersonHandler;
        timer = Timer.getTimer();
        display.setTitle("SimpleGame");
        KeyBindingManager kbm = KeyBindingManager.getKeyBindingManager();
        kbm.set("toggle_wire", KeyInput.KEY_T);
        kbm.set("toggle_lights", KeyInput.KEY_L);
        kbm.set("toggle_bounds", KeyInput.KEY_B);
        kbm.set("camera_out", KeyInput.KEY_C);
        kbm.set("exit", KeyInput.KEY_ESCAPE);
    }

    @Override
    protected void reinit() {
    }

    /**
	 * Called every frame, after update()
	 * @param unused in this implementation
     * @see com.jme.app.AbstractGame#render(float interpolation)
	 */
    @Override
    protected void render(float interpolation) {
        display.getRenderer().clearBuffers();
        display.getRenderer().draw(rootNode);
        if (showBounds) Debugger.drawBounds(rootNode, display.getRenderer());
        display.getRenderer().draw(fpsNode);
        simpleRender();
    }

    private void simpleRender() {
    }

    /**
	 * Called every frame
	 * @param interpolation unused in this implementation
	 * @see com.jme.app.AbstractGame#update(float interpolation)
	 */
    @Override
    protected void update(float interpolation) {
        timer.update();
        tpf = timer.getTimePerFrame();
        input.update(tpf);
        fps.print("FPS: " + (int) timer.getFrameRate());
        simpleUpdate();
        rootNode.updateGeometricState(tpf, true);
        if (KeyBindingManager.getKeyBindingManager().isValidCommand("toggle_wire", false)) {
            wireState.setEnabled(!wireState.isEnabled());
            rootNode.updateRenderState();
        }
        if (KeyBindingManager.getKeyBindingManager().isValidCommand("toggle_lights", false)) {
            lightState.setEnabled(!lightState.isEnabled());
            rootNode.updateRenderState();
        }
        if (KeyBindingManager.getKeyBindingManager().isValidCommand("toggle_bounds", false)) {
            showBounds = !showBounds;
        }
        if (KeyBindingManager.getKeyBindingManager().isValidCommand("camera_out", false)) {
            logger.info("Camera at: " + display.getRenderer().getCamera().getLocation());
        }
        if (KeyBindingManager.getKeyBindingManager().isValidCommand("exit", false)) {
            finish();
        }
    }

    private void simpleUpdate() {
    }
}
