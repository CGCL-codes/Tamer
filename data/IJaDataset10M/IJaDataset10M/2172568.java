package net.cevn.input;

import java.util.ArrayList;
import java.util.Iterator;
import org.lwjgl.input.Mouse;

/**
 * The <code>MouseManager</code> class manages mouse events and converts mouse movement and button press into events.
 * Parts of this class were based on code posted by amoeba on the FengGUI forum on August 14 2007. Each
 * game state has its own mouse manager so that mouse events do not carry over to idle states.
 * 
 * @author Christopher Field <cfield2@gmail.com>
 * @version
 * @since 0.0.1
 */
public class MouseManager {

    /**
	 * The dragged event ID.
	 */
    public static final int DRAGGED = 0;

    /**
	 * The moved event ID.
	 */
    public static final int MOVED = 1;

    /**
	 * The pressed event ID.
	 */
    public static final int PRESSED = 2;

    /**
	 * The released event ID.
	 */
    public static final int RELEASED = 3;

    /**
	 * The wheel up event ID.
	 */
    public static final int WHEEL_UP = 4;

    /**
	 * The wheel down event ID.
	 */
    public static final int WHEEL_DOWN = 5;

    /**
	 * The list of listeners for mouse events.
	 */
    private ArrayList<MouseListener> listeners = new ArrayList<MouseListener>();

    /**
	 * The X coordinate in pixels for the mouse the last time <code>checkMouse()</code> method
	 * was called.
	 */
    private int mouseX = Mouse.getX();

    /**
	 * The Y coordinate in pixels for the mouse the last time <code>checkMouse()</code> method
	 * was called.
	 */
    private int mouseY = Mouse.getY();

    /**
	 * The change (d) in the X direction in pixels from the last mouse event.
	 */
    private int dX = Mouse.getDX();

    /**
	 * The change (d) in the Y direction in pixels from the last mouse event.
	 */
    private int dY = Mouse.getDY();

    /**
	 * The last mouse button pressed when the <code>checkMouse()</code> method was called.
	 */
    private int lastMouseButtonDown = MouseEvent.NO_BUTTON;

    /**
	 * Creates a new <code>MouseManager</code> instance.
	 */
    public MouseManager() {
    }

    /**
	 * Adds a mouse listener.
	 * 
	 * @param listener The listener for mouse events.
	 */
    public void addListener(final MouseListener listener) {
        if (!listeners.contains(listener)) {
            listeners.add(listener);
        }
    }

    /**
	 * Removes a mouse listener.
	 * 
	 * @param listener The listener for mouse events.
	 */
    public void removeListener(final MouseListener listener) {
        listeners.remove(listener);
    }

    /**
	 * Checks if the mouse listener is already registered for mouse events.
	 * 
	 * @param listener The mouse listener.
	 * @return <code>true</code> if the listener is already registered; otherwise, <code>false</code>.
	 */
    public boolean hasListener(final MouseListener listener) {
        return listeners.contains(listener);
    }

    /**
	 * Checks for mouse events. This method should be called from within the main program loop in order
	 * for listeners to receive mouse events.
	 */
    public void update() {
        mouseX = Mouse.getX();
        mouseY = Mouse.getY();
        dX = Mouse.getDX();
        dY = Mouse.getDY();
        if (lastMouseButtonDown != -1 && Mouse.isButtonDown(lastMouseButtonDown)) {
            fireEvent(DRAGGED);
        } else {
            if (dX != 0 || dY != 0) {
                fireEvent(MOVED);
            }
            if (lastMouseButtonDown != -1) {
                fireEvent(RELEASED);
                lastMouseButtonDown = -1;
            }
            while (Mouse.next()) {
                if (Mouse.getEventButton() != -1 && Mouse.getEventButtonState()) {
                    lastMouseButtonDown = Mouse.getEventButton();
                    fireEvent(PRESSED);
                }
                int wheel = Mouse.getEventDWheel();
                if (wheel != 0) {
                    if (wheel > 0) {
                        fireEvent(WHEEL_UP);
                    } else {
                        fireEvent(WHEEL_DOWN);
                    }
                }
            }
        }
    }

    /**
	 * Calls the event method for all listeners based on the event ID.
	 * 
	 * @param eventID The event ID, either <code>DRAGGED</code>, <code>MOVED</code>, 
	 * 					<code>PRESSED</code>, <code>RELEASED</code>, <code>WHEEL_UP</code>, or 
	 * 					<code>WHEEL_DOWN</code>.
	 */
    private void fireEvent(final int eventID) {
        Iterator<MouseListener> i = listeners.iterator();
        MouseEvent event = new MouseEvent(mouseX, mouseY, dX, dY, lastMouseButtonDown);
        MouseListener listener = null;
        while (i.hasNext()) {
            listener = i.next();
            switch(eventID) {
                case DRAGGED:
                    listener.mouseDragged(event);
                    break;
                case MOVED:
                    listener.mouseMoved(event);
                    break;
                case RELEASED:
                    listener.mouseReleased(event);
                    break;
                case PRESSED:
                    listener.mousePressed(event);
                    break;
                case WHEEL_UP:
                    listener.mouseWheelUp(event);
                    break;
                case WHEEL_DOWN:
                    listener.mouseWheelDown(event);
                    break;
                default:
                    throw new IllegalArgumentException("The event ID muse be either DRAGGED, MOVED, PRESSED, RELEASED, WHEEL_UP, or WHEEL_DOWN");
            }
            if (event.isConsumed()) {
                break;
            }
        }
    }
}
