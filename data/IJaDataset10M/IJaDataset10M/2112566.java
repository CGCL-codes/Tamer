package javag;

import java.awt.Graphics2D;
import java.awt.GraphicsConfiguration;
import java.awt.RenderingHints;
import javag.keyboard.Keyboard;
import javax.media.j3d.Appearance;
import javax.media.j3d.Canvas3D;
import javax.media.j3d.GraphicsContext3D;
import com.sun.j3d.utils.universe.SimpleUniverse;

/**
 * The Game3D class provides base functionality to set up and configure a game that will use a 3D engine.<br/>
 * @author travisb
 */
@SuppressWarnings("serial")
public class GameControler3D extends GameControler {

    private Canvas3D canvas;

    private GraphicsContext3D gc = null;

    /**
	 * Constructs a new Game3D instance with the given title.
	 * 
	 * @param title Title for the game.
	 */
    public GameControler3D(String title) {
        super(title);
        GraphicsConfiguration config = SimpleUniverse.getPreferredConfiguration();
        canvas = new Canvas3D(config, false);
        getContentPane().add(canvas);
        canvas.stopRenderer();
        canvas.addKeyListener(Keyboard.initKeyListener());
        canvas.addMouseListener(this);
        canvas.addMouseMotionListener(this);
        new SimpleUniverse(canvas);
    }

    public void restoreScreen() {
        System.out.println("Resotre called");
        gc = null;
    }

    public String getGameControlerType() {
        return "3D";
    }

    public boolean getBufferHasAlphaChannel() {
        return true;
    }

    public void freshScene() {
        if (gc == null) {
            gc = canvas.getGraphicsContext3D();
            gc.setAppearance(new Appearance());
        }
    }

    /**
	 * Overridden from base class.
	 * 
	 * @see javag.GameControler#render
	 */
    protected void render() {
        gc.clear();
        if (getGame() != null) {
            getGame().render(getPostRenderGraphics());
        }
    }

    public Graphics2D getPostRenderGraphics() {
        Graphics2D graphics = canvas.getGraphics2D();
        graphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        graphics.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        return graphics;
    }

    public void swap() {
        canvas.getGraphics2D().flush(false);
        canvas.swap();
    }
}
