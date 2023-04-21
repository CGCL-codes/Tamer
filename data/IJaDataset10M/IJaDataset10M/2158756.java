package jmetest.flagrushtut;

import java.util.logging.Level;
import java.util.logging.Logger;
import com.jme.app.BaseGame;
import com.jme.bounding.BoundingBox;
import com.jme.image.Texture;
import com.jme.input.KeyBindingManager;
import com.jme.input.KeyInput;
import com.jme.math.Vector3f;
import com.jme.renderer.Camera;
import com.jme.renderer.ColorRGBA;
import com.jme.scene.Node;
import com.jme.scene.shape.Sphere;
import com.jme.scene.state.TextureState;
import com.jme.system.DisplaySystem;
import com.jme.system.JmeException;
import com.jme.util.TextureManager;
import com.jme.util.Timer;

/**
 * <code>Tutorial 2</code> shows how to build our own Application 
 * framework for Flag Rush.
 * For Flag Rush Tutorial Series.
 * @author Mark Powell
 */
public class Lesson2 extends BaseGame {

    private static final Logger logger = Logger.getLogger(Lesson2.class.getName());

    protected Timer timer;

    private Camera cam;

    private Node scene;

    private TextureState ts;

    private int width, height, depth, freq;

    private boolean fullscreen;

    /**
	 * Main entry point of the application
	 */
    public static void main(String[] args) {
        Lesson2 app = new Lesson2();
        app.setConfigShowMode(ConfigShowMode.AlwaysShow, Lesson2.class.getClassLoader().getResource("jmetest/data/images/FlagRush.png"));
        app.start();
    }

    /**
	 * During an update we only look for the escape button and update the timer
	 * to get the framerate.
	 * 
	 * @see com.jme.app.BaseGame#update(float)
	 */
    protected void update(float interpolation) {
        timer.update();
        interpolation = timer.getTimePerFrame();
        if (KeyBindingManager.getKeyBindingManager().isValidCommand("exit")) {
            finished = true;
        }
    }

    /**
	 * draws the scene graph
	 * 
	 * @see com.jme.app.BaseGame#render(float)
	 */
    protected void render(float interpolation) {
        display.getRenderer().clearBuffers();
        display.getRenderer().draw(scene);
    }

    /**
	 * initializes the display and camera.
	 * 
	 * @see com.jme.app.BaseGame#initSystem()
	 */
    protected void initSystem() {
        width = settings.getWidth();
        height = settings.getHeight();
        depth = settings.getDepth();
        freq = settings.getFrequency();
        fullscreen = settings.isFullscreen();
        try {
            display = DisplaySystem.getDisplaySystem(settings.getRenderer());
            display.createWindow(width, height, depth, freq, fullscreen);
            cam = display.getRenderer().createCamera(width, height);
        } catch (JmeException e) {
            logger.log(Level.SEVERE, "Could not create displaySystem", e);
            System.exit(1);
        }
        display.getRenderer().setBackgroundColor(ColorRGBA.black.clone());
        cam.setFrustumPerspective(45.0f, (float) width / (float) height, 1, 1000);
        Vector3f loc = new Vector3f(0.0f, 0.0f, 25.0f);
        Vector3f left = new Vector3f(-1.0f, 0.0f, 0.0f);
        Vector3f up = new Vector3f(0.0f, 1.0f, 0.0f);
        Vector3f dir = new Vector3f(0.0f, 0f, -1.0f);
        cam.setFrame(loc, left, up, dir);
        cam.update();
        timer = Timer.getTimer();
        display.getRenderer().setCamera(cam);
        KeyBindingManager.getKeyBindingManager().set("exit", KeyInput.KEY_ESCAPE);
    }

    /**
	 * initializes the scene
	 * 
	 * @see com.jme.app.BaseGame#initGame()
	 */
    protected void initGame() {
        scene = new Node("Scene graph node");
        cam.update();
        Sphere s = new Sphere("Sphere", 30, 30, 25);
        s.setLocalTranslation(new Vector3f(0, 0, -40));
        s.setModelBound(new BoundingBox());
        s.updateModelBound();
        ts = display.getRenderer().createTextureState();
        ts.setEnabled(true);
        ts.setTexture(TextureManager.loadTexture(Lesson2.class.getClassLoader().getResource("jmetest/data/images/Monkey.jpg"), Texture.MinificationFilter.Trilinear, Texture.MagnificationFilter.Bilinear));
        s.setRenderState(ts);
        scene.attachChild(s);
        scene.updateGeometricState(0.0f, true);
        scene.updateRenderState();
    }

    /**
	 * will be called if the resolution changes
	 * 
	 * @see com.jme.app.BaseGame#reinit()
	 */
    protected void reinit() {
        display.recreateWindow(width, height, depth, freq, fullscreen);
    }

    protected void quit() {
        super.quit();
        System.exit(0);
    }

    /**
	 * clean up the textures.
	 * 
	 * @see com.jme.app.BaseGame#cleanup()
	 */
    protected void cleanup() {
        ts.deleteAll();
    }
}
