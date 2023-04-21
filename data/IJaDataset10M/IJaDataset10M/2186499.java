package TWLSlick;

import de.matthiasmann.twl.GUI;
import org.lwjgl.opengl.Display;
import org.newdawn.slick.Input;
import org.newdawn.slick.util.InputAdapter;

/**
 * Forwards input events from Slick to TWL
 * 
 * @author Matthias Mann
 */
class TWLInputForwarder extends InputAdapter {

    private final Input input;

    private final GUI gui;

    public TWLInputForwarder(GUI gui, Input input) {
        if (gui == null) {
            throw new NullPointerException("gui");
        }
        if (input == null) {
            throw new NullPointerException("input");
        }
        this.gui = gui;
        this.input = input;
    }

    @Override
    public void mouseWheelMoved(int change) {
        gui.handleMouseWheel(change);
        input.consumeEvent();
    }

    @Override
    public void mousePressed(int button, int x, int y) {
        gui.handleMouse(x, y, button, true);
        input.consumeEvent();
    }

    @Override
    public void mouseReleased(int button, int x, int y) {
        gui.handleMouse(x, y, button, false);
        input.consumeEvent();
    }

    @Override
    public void mouseMoved(int oldX, int oldY, int newX, int newY) {
        gui.handleMouse(newX, newY, -1, false);
        input.consumeEvent();
    }

    @Override
    public void mouseDragged(int oldx, int oldy, int newX, int newY) {
        mouseMoved(oldx, oldy, newX, newY);
    }

    @Override
    public void keyPressed(int key, char c) {
        gui.handleKey(key, c, true);
        input.consumeEvent();
    }

    @Override
    public void keyReleased(int key, char c) {
        gui.handleKey(key, c, false);
        input.consumeEvent();
    }

    @Override
    public void mouseClicked(int button, int x, int y, int clickCount) {
        input.consumeEvent();
    }

    @Override
    public void inputStarted() {
        gui.updateTime();
        if (!Display.isActive()) {
            gui.clearKeyboardState();
            gui.clearMouseState();
            if (gui.getRootPane() instanceof RootPane) {
                ((RootPane) gui.getRootPane()).keyboardFocusLost();
            }
        }
    }

    @Override
    public void inputEnded() {
        gui.handleKeyRepeat();
    }
}
