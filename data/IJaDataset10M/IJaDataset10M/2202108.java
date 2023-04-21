package input;

import com.jme.input.AbsoluteMouse;
import com.jme.math.Vector3f;
import com.jme.scene.state.BlendState;
import com.jme.scene.state.TextureState;
import com.jme.system.DisplaySystem;
import fileHandling.TextureLoader;

/**
 * This will be used as the mouse-cursor. Extending {@link AbsoluteMouse}, this provides functionality 
 * to change the cursor on the fly and to set this mouse visible or invisible.
 * 
 * @author Wasserleiche
 */
public class Mouse extends AbsoluteMouse {

    private static final long serialVersionUID = 1L;

    public static final int CROSSHAIR = 0;

    public static final int MENU = 1;

    public static final int HUD = 2;

    public static final int NO_CURSOR = 3;

    /** The path containing all cursor-textures. */
    private final String CURSOR_PATH = "data/textures/gui/cursors/";

    /** The last cursor that has been used. */
    private int lastCursor;

    /** The current {@link TextureState} that is applied to the mouse. */
    private TextureState cursorState;

    /**
	 * Constructs a new Mouse. This initializes a {@link Menu}-cursor and the {@link BlendState} for this 
	 * Mouse. The position of the mouse will be set to the middle of the screen.
	 * @param display The current {@link DisplaySystem}.
	 */
    public Mouse(DisplaySystem display) {
        super("Ingame Mouse", display.getWidth(), display.getHeight());
        setCursor(MENU);
        BlendState bs = DisplaySystem.getDisplaySystem().getRenderer().createBlendState();
        bs.setBlendEnabled(true);
        bs.setSourceFunction(BlendState.SourceFunction.SourceAlpha);
        bs.setDestinationFunction(BlendState.DestinationFunction.OneMinusSourceAlpha);
        bs.setTestEnabled(true);
        bs.setTestFunction(BlendState.TestFunction.GreaterThan);
        setRenderState(bs);
        getLocalTranslation().set(new Vector3f(display.getWidth() / 2, display.getHeight() / 2, 0));
    }

    /**
	 * Changes the cursor-texture and updates the last cursor.
	 * @param cursor The new cursor-type to be set.
	 */
    public void setCursor(int cursor) {
        if (cursor != NO_CURSOR) lastCursor = cursor;
        switch(cursor) {
            case CROSSHAIR:
                {
                    createCursorState("ingameCursor.png");
                    break;
                }
            case MENU:
                {
                    createCursorState("menuCursor.png");
                    break;
                }
            case HUD:
                {
                    createCursorState("menuCursor.png");
                    break;
                }
            case NO_CURSOR:
                {
                    createCursorState("noCursor.png");
                    break;
                }
        }
        updateRenderState();
    }

    /**
	 * Applies the {@link Texture} of the given file to this mouse.
	 * @param cursorFile The file-name of the {@link Texture} to be applied.
	 */
    private void createCursorState(String cursorFile) {
        cursorState = TextureLoader.loadTexture(CURSOR_PATH + cursorFile);
        setRenderState(cursorState);
    }

    /**
	 * Sets this mouse visible or invisible.
	 * @param vis true, if this mouse shall become invisible. false, if not.
	 */
    public void setVisible(boolean vis) {
        if (vis) setCursor(lastCursor); else setCursor(NO_CURSOR);
        updateRenderState();
    }
}
