package gameStates;

import java.util.ArrayList;
import java.util.List;
import input.FengJMEInputHandler;
import input.Mouse;
import input.PlayerKeyInput;
import org.fenggui.Display;
import org.fenggui.IWidget;
import org.fenggui.render.lwjgl.LWJGLBinding;
import com.jme.image.Texture;
import com.jme.input.InputHandler;
import com.jme.math.Vector3f;
import com.jme.renderer.Renderer;
import com.jme.scene.state.RenderState;
import com.jme.scene.state.TextureState;
import com.jme.system.DisplaySystem;

/**
 * This is the basic class for all {@link GameState}s that display elements of the graphical user 
 * interface.
 * 
 * @author Wasserleiche
 */
public class GUIState extends SimpleGameState {

    /** The {@link Display} that shall be used to display FengGUI-Widgets. */
    protected Display guiDisp;

    /** The input-handler that is used for FengGUI. */
    protected FengJMEInputHandler guiInput;

    /** The {@link InputHandler} that is used for JME. */
    protected InputHandler input;

    /** The {@link Mouse} to be displayed in this {@link GameState}. */
    protected Mouse mouse;

    /** The {@link TextureState} that is used as default. (don't ask me what this is for... forgot it) */
    protected TextureState defaultTextureState;

    protected boolean showMouse;

    /** Contains all {@link IWidget}s that shall be displayed when the {@link GameState} is active. */
    protected List<IWidget> widgets;

    protected PlayerKeyInput playerKeyInput;

    /**
	 * Constructs a new GUIState with the given information.
	 * @param name The {@link GameState}-Name.
	 * @param showMouse true, if this GUIState should show a mouse-cursor. false, if not.
	 */
    public GUIState(String name, boolean showMouse) {
        super(name);
        this.showMouse = showMouse;
        widgets = new ArrayList<IWidget>();
        guiDisp = new Display(new LWJGLBinding());
        guiInput = new FengJMEInputHandler(guiDisp);
        Texture defTex = TextureState.getDefaultTexture().createSimpleClone();
        defaultTextureState = DisplaySystem.getDisplaySystem().getRenderer().createTextureState();
        defTex.setScale(new Vector3f(1f, 1f, 1f));
        defaultTextureState.setTexture(defTex);
        initCursor();
        playerKeyInput = new PlayerKeyInput(rootNode);
        input = new InputHandler();
        input.addToAttachedHandlers(playerKeyInput);
    }

    /**
	 * Initializes the mouse-cursor (only if showMouse is true). If there is already a mouse, it will be 
	 * removed and a new mouse will be set up.
	 */
    public void initCursor() {
        if (!showMouse) return;
        if (mouse != null) {
            rootNode.detachChild(mouse);
            mouse.registerWithInputHandler(null);
        }
        mouse = new Mouse(DisplaySystem.getDisplaySystem());
        rootNode.attachChild(mouse);
        mouse.registerWithInputHandler(guiInput);
        mouse.setCursor(Mouse.HUD);
    }

    @Override
    public void render(float tpf) {
        for (RenderState r : Renderer.defaultStateList) r.apply();
        defaultTextureState.apply();
        DisplaySystem.getDisplaySystem().getRenderer().draw(rootNode);
        guiDisp.display();
    }

    @Override
    public void update(float tpf) {
        if (mouse != null) mouse.updateRenderState();
        if (!isActive()) return;
        input.update(tpf);
        guiInput.update(tpf);
        rootNode.updateGeometricState(tpf, true);
        rootNode.updateRenderState();
    }

    /**
	 * Enables or disables this GUIState. The input, mouse and all windows will also be enabled/disabled.
	 */
    @Override
    public void setActive(boolean active) {
        super.setActive(active);
        guiInput.setEnabled(active);
        if (showMouse) {
            mouse.setVisible(active);
            input.setEnabled(active);
        }
        if (active) {
            for (IWidget w : widgets) {
                guiDisp.addWidget(w);
            }
        } else guiDisp.removeAllWidgets();
        rootNode.updateRenderState();
    }

    @Override
    public void cleanup() {
        setActive(false);
        input.removeAllFromAttachedHandlers();
        input.clearActions();
        input.removeAllActions();
    }

    /**
	 * Restores this GUIState. The {@link Display} will be cleared as well as the windows. The size of the 
	 * {@link Display} will be updated to the size of {@link DisplaySystem} and the cursor will be 
	 * (re-)initialized.
	 */
    @Override
    public void restore() {
        guiDisp.removeAllWidgets();
        widgets.clear();
        guiDisp.setSize(DisplaySystem.getDisplaySystem().getWidth(), DisplaySystem.getDisplaySystem().getHeight());
        initCursor();
    }

    /**
	 * Returns all {@link IWidget}s of this GUIState.
	 * @return All {@link IWidget}s.
	 */
    public List<IWidget> getWidgets() {
        return widgets;
    }

    /**
	 * Returns the input-handler for FengGUI.
	 * @return The FengGUI-input-handler.
	 */
    public FengJMEInputHandler getInput() {
        return guiInput;
    }

    /**
	 * Returns the {@link Display} for FengGUI-Widgets.
	 * @return The FengGUI-display.
	 */
    public Display getGUIDisplay() {
        return guiDisp;
    }
}
