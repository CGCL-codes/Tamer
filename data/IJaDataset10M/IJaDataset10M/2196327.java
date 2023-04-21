package sdljava.event;

import sdljava.x.swig.*;
import java.util.Map;
import java.util.HashMap;

/**
 * The current state of the mouse buttons.
 *
 * 
 * @version $Id: MouseButtonState.java,v 1.4 2005/01/19 03:09:12 ivan_ganza Exp $
 */
public class MouseButtonState {

    public static final int SDL_BUTTON_LEFT = SWIG_SDLEventConstants.SDL_BUTTON_LEFT;

    public static final int SDL_BUTTON_MIDDLE = SWIG_SDLEventConstants.SDL_BUTTON_MIDDLE;

    public static final int SDL_BUTTON_RIGHT = SWIG_SDLEventConstants.SDL_BUTTON_RIGHT;

    public static final int SDL_BUTTON_WHEELUP = SWIG_SDLEventConstants.SDL_BUTTON_WHEELUP;

    public static final int SDL_BUTTON_WHEELDOWN = SWIG_SDLEventConstants.SDL_BUTTON_WHEELDOWN;

    /**
     * cache of MouseButtonState instances, one for each possible
     * mods values
     *
     */
    static Map buttonStateCache = new HashMap();

    /**
     * Current button state values (possibly ORed together)
     *
     */
    int buttons;

    /**
     * Creates a new <code>MouseButtonState</code> instance.
     *
     * @param buttons an <code>int</code> value
     */
    protected MouseButtonState(int buttons) {
        this.buttons = buttons;
    }

    /**
     * Get the MouseButtonState instance identified by mods.  This method
     * creates the MouseButtonState instance and caches it if it didn't
     * already exist.  Once created we won't need to create new
     * MouseButtonState object instances each time a keyboard event occurs.
     *
     * @param mods valid button state (possibly OR'd together)
     * @return The singleton MouseButtonState instance
     */
    public static MouseButtonState get(int buttons) {
        MouseButtonState mod = (MouseButtonState) buttonStateCache.get(new Integer(buttons));
        if (mod != null) return mod;
        mod = new MouseButtonState(buttons);
        buttonStateCache.put(new Integer(buttons), mod);
        return mod;
    }

    /**
     * Get if the left mouse button is pressed
     *
     * @return if the left mouse button is pressed
     */
    public boolean buttonLeft() {
        return ((buttons & button(SDL_BUTTON_LEFT)) != 0);
    }

    /**
     * Get if the middle mouse button is pressed
     *
     * @return if the middle mouse button is pressed
     */
    public boolean buttonMiddle() {
        return ((buttons & button(SDL_BUTTON_MIDDLE)) != 0);
    }

    /**
     * Get if the right mouse button is pressed
     *
     * @return if the middle mouse button is pressed
     */
    public boolean buttonRight() {
        return ((buttons & button(SDL_BUTTON_RIGHT)) != 0);
    }

    /**
     * Get if the wheel is up
     *
     * @return if the wheel is in the up
     */
    public boolean wheelUp() {
        return ((buttons & button(SDL_BUTTON_WHEELUP)) != 0);
    }

    /**
     * Get if the wheel is down
     *
     * @return if the wheel is down
     */
    public boolean wheelDown() {
        return ((buttons & button(SDL_BUTTON_WHEELDOWN)) != 0);
    }

    public int getState() {
        return this.buttons;
    }

    final int button(int x) {
        return (SDLEvent.SDL_PRESSED << (x - 1));
    }

    /**
     * Return a string represenation of this object
     *
     * @return a String represenation of this object
     */
    public String toString() {
        StringBuffer buf = new StringBuffer();
        buf.append("MouseButtonState[");
        if (buttonLeft()) buf.append(", buttonLeft");
        if (buttonMiddle()) buf.append(", buttonMiddle");
        if (buttonRight()) buf.append(", buttonRight");
        if (wheelUp()) buf.append(", wheelUP");
        if (wheelDown()) buf.append(", wheelDown");
        buf.append("]");
        return buf.toString();
    }
}
