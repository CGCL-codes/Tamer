package game.engine;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;

/**
 * GameInputEventManager provides a simple mouse and keyboard event handler
 * intended to capture game input.
 * <P>
 * The keyboard handlers provides a means of determining which keys are currently
 * depressed and also provides simple key type event handling (currently, at
 * most one 'unprocessed' key typed event per key can be 'stored'. This can be
 * improved by changing the key typed array from a boolean array to an integer
 * array.
 *
 * @author <A HREF="mailto:P.Hanna@qub.ac.uk">Philip Hanna</A>
 * @version $Revision: 1 $ $Date: 2006/08 $
 */
public class GameInputEventManager implements KeyListener, MouseListener, MouseMotionListener, MouseWheelListener {

    /**
     * Static final variable used to determine the size of the Unicode key arrays.
     */
    private static final int NUM_MAPPED_UNICODE_KEYS = 1026;

    /**
     * Static keyPressed array with the array index corresponding to the 
     * Unicode value of a given input key. A value of true signifies that the 
     * key is currently pressed.
     */
    public static boolean[] keyPressed;

    /**
     * Static keyType array with the array index corresponding to the Unicode
     * value of a given input key. A value of true signifies that the key has
     * been typed (i.e. pressed and released). This array is not public as the
     * reset of a typed key from true to false (i.e. representing that the 
     * key press event has been consumed is contained within keyTyped).
     */
    private static boolean[] keyTyped;

    /**
     * Boolean flag signifying if an unprocessed key press is awaiting 
     * collection, alongside an integer containing the key code and a
     * char contained the pressed character.
     */
    private boolean newKeyTyped;

    private int newKeyTypedID;

    private char newCharTyped;

    /**
     * Static class data specifying the maximum distance a mouse button press
     * can differ from a mouse button release and be considered as a mouse
     * button click.
     */
    public static int mouseClickTolerance = 10;

    /**
     * Boolean flag which determines if mouse button 1 is currently pressed
     */
    public static boolean mouseButton1Pressed;

    /**
     * Boolean flag which determines if mouse button 2 is currently pressed
     */
    public static boolean mouseButton2Pressed;

    /**
     * Boolean flag which determines if mouse button 3 is currently pressed
     */
    public static boolean mouseButton3Pressed;

    /**
     * int value containing the current mouse x coordinate
     */
    public static int mouseXCoordinate;

    /**
     * int value containing the current mouse y coordinate
     */
    public static int mouseYCoordinate;

    /**
     * Boolean flags tracking if mouse button 1, 2 or 3 has been clicked.
     * A flag will be reset to false once the button click has been 
     * consumed (i.e. the mouseClicked method returns true to a caller.
     */
    private static boolean mouseButton1Clicked;

    private static boolean mouseButton2Clicked;

    private static boolean mouseButton3Clicked;

    /**
     * Screen x and y location of the last mouse click
     * (i.e. a mouse button release that is within the permitted
     * distance of the last mouse button press)
     */
    private static int mouseClickXLocation;

    private static int mouseClickYLocation;

    /**
     * Screen x and y location of the last mouse button press 
     */
    private static int mousePressXLocation;

    private static int mousePressYLocation;

    /**
     * Current mouse scroll wheel rotation
     */
    private static int mouseWheelRotation;

    /**
     * Create a new game input event manager
     */
    public GameInputEventManager() {
        keyPressed = new boolean[NUM_MAPPED_UNICODE_KEYS];
        keyTyped = new boolean[NUM_MAPPED_UNICODE_KEYS];
        for (int idx = 0; idx < NUM_MAPPED_UNICODE_KEYS; idx++) {
            keyPressed[idx] = false;
            keyTyped[idx] = false;
        }
        newKeyTyped = false;
        newKeyTypedID = 0;
        newCharTyped = ' ';
        mouseButton1Pressed = false;
        mouseButton2Pressed = false;
        mouseButton3Pressed = false;
        mouseXCoordinate = 0;
        mouseYCoordinate = 0;
        mouseButton1Clicked = false;
        mouseButton2Clicked = false;
        mouseButton3Clicked = false;
        mouseClickXLocation = 0;
        mouseClickYLocation = 0;
        mouseWheelRotation = 0;
    }

    /**
     * Update the key pressed array based on the specified key event
     *
     * @param  event Key event detailing the key press
     */
    public final void keyPressed(KeyEvent event) {
        keyPressed[event.getKeyCode()] = true;
    }

    /**
     * Update the key pressed and typed arrys based on the specified
     * key event
     *
     * @param  event Key event detailing the key release
     */
    public final void keyReleased(KeyEvent event) {
        newKeyTyped = true;
        newKeyTypedID = event.getKeyCode();
        newCharTyped = event.getKeyChar();
        keyTyped[newKeyTypedID] = true;
        keyPressed[newKeyTypedID] = false;
    }

    /**
     * Null keyTyped method as the keyReleased event handler deals with
     * key type updates.
     *
     * @param  event Key event detailing the key release
     */
    public final void keyTyped(KeyEvent event) {
    }

    /**
     * Determine if the specified key has been typed
     *
     * @param  keyCode Unicode key value of the queried button
     * @return boolean true if the key has been typed, false otherwise
     */
    public final boolean keyTyped(int keyCode) {
        if (keyTyped[keyCode]) {
            newKeyTyped = false;
            keyTyped[keyCode] = false;
            return true;
        } else {
            return false;
        }
    }

    /**
     *  Determine if any new key press has been received
     */
    public final boolean newKeyTyped() {
        if (newKeyTyped) {
            newKeyTyped = false;
            return true;
        } else {
            return false;
        }
    }

    /**
     *  Return the key code of the last key press
     */
    public final int getNewKeyTyped() {
        return newKeyTypedID;
    }

    /**
     *  Return the character (if any) that corresponds to the last key press
     */
    public final char getNewCharTyped() {
        return newCharTyped;
    }

    /**
     * Set the mouse click tolerance to that specified
     *
     * @param  integer mouse click tolerance to use
     */
    public static final void setMouseClickTolerance(int mouseClickTolerance) {
        GameInputEventManager.mouseClickTolerance = mouseClickTolerance;
    }

    /**
     * mouseClicked event handler.
     *
     * @param  event corresponding Mouse event
     */
    public final void mouseClicked(MouseEvent event) {
        int eventButton = event.getButton();
        if (eventButton == MouseEvent.BUTTON1) {
            mouseButton1Clicked = true;
        } else if (eventButton == MouseEvent.BUTTON2) {
            mouseButton2Clicked = true;
        } else if (eventButton == MouseEvent.BUTTON3) {
            mouseButton3Clicked = true;
        }
        mouseClickXLocation = event.getX();
        mouseClickYLocation = event.getY();
    }

    /**
     * Determine if the specified mouse button has been pressed.
     * <P>
     * Note: If the specified mouse button has been clicked then calling this
     * method will effectively consume the mouse click
     *
     * @param  buttonCode int mouse button code
     * @return Boolean true if the specified mouse button has been clicked,
     *         otherwise false
     */
    public final boolean mouseClicked(int buttonCode) {
        if (buttonCode == MouseEvent.BUTTON1) {
            if (mouseButton1Clicked) {
                mouseButton1Clicked = false;
                return true;
            }
        }
        if (buttonCode == MouseEvent.BUTTON2) {
            if (mouseButton2Clicked) {
                mouseButton2Clicked = false;
                return true;
            }
        }
        if (buttonCode == MouseEvent.BUTTON3) {
            if (mouseButton3Clicked) {
                mouseButton3Clicked = false;
                return true;
            }
        }
        return false;
    }

    /**
     * Return the location of the last mouse click.
     *
     * @return int[] integer array of size two containing the x and y screen
     *         location of the last mouse click
     */
    public final int[] getMouseClickLocation() {
        return new int[] { mouseClickXLocation, mouseClickYLocation };
    }

    /**
     * Null mouseEntered event handler.
     * <P>
     * Note: This event handler does not currently deal with mouseEntered events
     *
     * @param  event corresponding Mouse event
     */
    public final void mouseEntered(MouseEvent event) {
    }

    /**
     * Null mouseExited event handler.
     * <P>
     * Note: This event handler does not currently deal with mouseExited events
     *
     * @param  event corresponding Mouse event
     */
    public final void mouseExited(MouseEvent event) {
    }

    /**
     * mouseDragged event handler.
     * <P>
     * Note: Updates the mouse x and y location variables
     *
     * @param  event corresponding Mouse event
     */
    public final void mouseDragged(MouseEvent event) {
        mouseXCoordinate = event.getX();
        mouseYCoordinate = event.getY();
    }

    /**
     * mousePressed event handler
     *
     * @param  event corresponding mouse pressed MouseEvent
     */
    public final void mousePressed(MouseEvent event) {
        int eventButton = event.getButton();
        if (eventButton == MouseEvent.BUTTON1) {
            mouseButton1Pressed = true;
        } else if (eventButton == MouseEvent.BUTTON2) {
            mouseButton2Pressed = true;
        } else if (eventButton == MouseEvent.BUTTON3) {
            mouseButton3Pressed = true;
        }
        mousePressXLocation = event.getX();
        mousePressYLocation = event.getY();
    }

    /**
     * mouseReleased event handler
     *
     * @param  event corresponding mouse released MouseEvent
     */
    public final void mouseReleased(MouseEvent event) {
        int eventButton = event.getButton();
        if (eventButton == MouseEvent.BUTTON1) {
            mouseButton1Pressed = false;
        } else if (eventButton == MouseEvent.BUTTON2) {
            mouseButton2Pressed = false;
        } else if (eventButton == MouseEvent.BUTTON3) {
            mouseButton3Pressed = false;
        }
        if (Math.abs(event.getX() - mousePressXLocation) < mouseClickTolerance && Math.abs(event.getY() - mousePressYLocation) < mouseClickTolerance) {
            mouseClicked(event);
        }
    }

    /**
     * mouseMoved event handler
     *
     * @param  event corresponding MouseEvent
     */
    public final void mouseMoved(MouseEvent event) {
        mouseXCoordinate = event.getX();
        mouseYCoordinate = event.getY();
    }

    /**
     * mouseWheelMoved event handler
     *
     * @param  event corresponding MouseWheelEvent
     */
    public final void mouseWheelMoved(MouseWheelEvent event) {
        mouseWheelRotation += event.getWheelRotation();
    }

    /**
     * Return the current mouse wheel rotation.
     * <P>
     * Note: the value returned by this method is reflective of the amount
     * and direction of mouse wheel rotation since the last call to the method
     *
     * @return int value of the current mouse wheel rotation
     */
    public final int mouseWheelRotation() {
        int rotation = mouseWheelRotation;
        mouseWheelRotation = 0;
        return rotation;
    }

    /**
     * Reset all input events.
     * <P>
     * Note: This method can be used whenever the game wishes to clear out any
     * unprocessed prior input events.
     */
    public final void resetInputEvents() {
        for (int idx = 0; idx < keyPressed.length; idx++) {
            keyPressed[idx] = false;
        }
        for (int idx = 0; idx < keyTyped.length; idx++) {
            keyTyped[idx] = false;
        }
        newKeyTyped = false;
        newKeyTypedID = 0;
        newCharTyped = ' ';
        mouseButton1Pressed = false;
        mouseButton2Pressed = false;
        mouseButton3Pressed = false;
        mouseButton1Clicked = false;
        mouseButton2Clicked = false;
        mouseButton3Clicked = false;
    }
}
