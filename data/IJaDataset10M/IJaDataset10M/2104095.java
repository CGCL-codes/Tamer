package ucalgary.ebe.ci.mice.cursor;

import icculus.manymouse.jni.ManyMouseEvent;
import java.util.HashMap;
import java.util.LinkedList;
import org.eclipse.swt.graphics.Point;
import ucalgary.ebe.ci.mice.utils.MouseUtils;

/**
 * Data object to save all important data for the spezified cursor.
 * It keeps the location and the event queue of the fake cursor.
 * 
 * @author herbiga
 *
 */
public class MouseCursorData {

    private Point hideSystemMouseLocation;

    private int mouseID;

    private HashMap<Integer, Integer> buttons;

    private HashMap<Integer, Integer> lastHandle;

    private int displayWidth;

    private int displayHeight;

    private Point mouseShellLocation;

    public Point rotateDelta;

    private LinkedList<ManyMouseEvent> eventQueue;

    /**
	 * Standard constructor to create the mouse data object
	 * @param mouseId
	 * @param location
	 * @param rotateDelta
	 */
    public MouseCursorData(int mouseId, Point location, Point rotateDelta) {
        this.mouseID = mouseId;
        this.rotateDelta = rotateDelta;
        this.mouseShellLocation = location;
        this.hideSystemMouseLocation = new Point(0, 0);
        this.eventQueue = new LinkedList<ManyMouseEvent>();
        this.buttons = new HashMap<Integer, Integer>();
        this.lastHandle = new HashMap<Integer, Integer>();
        this.displayHeight = MouseUtils.getDisplayHeight();
        this.displayWidth = MouseUtils.getDisplayWidth();
    }

    /**
	 * Returns the mouse location
	 * @return
	 */
    public Point getMouseShellLocation() {
        return mouseShellLocation;
    }

    /**
	 * Returns the mouse pointer location (including the rotation delta)
	 * @return
	 */
    public Point getMouseLocation() {
        Point mouseLocation = new Point(this.mouseShellLocation.x + this.rotateDelta.x, this.mouseShellLocation.y + this.rotateDelta.y);
        return mouseLocation;
    }

    /**
	 * Sets the mouse location to a spezified (x,y) coordinate
	 * @param mouseLocation
	 */
    public void setMouseLocation(Point mouseLocation) {
        this.mouseShellLocation = mouseLocation;
    }

    /**
	 * Changes the X coordinate (relative)
	 * @param x
	 */
    public void setXLocationRelative(int x) {
        int mouse_x = this.mouseShellLocation.x + x;
        if (mouse_x + rotateDelta.x < MouseUtils.DISPLAY_WIDTH_START) {
            this.mouseShellLocation.x = MouseUtils.DISPLAY_WIDTH_START - rotateDelta.x;
        } else if (mouse_x > displayWidth) {
            this.mouseShellLocation.x = displayWidth;
        } else {
            this.mouseShellLocation.x = mouse_x;
        }
    }

    /**
	 * Changes the Y coordinate (relative)
	 * @param y
	 */
    public void setYLocationRelative(int y) {
        int mouse_y = this.mouseShellLocation.y + y;
        if (mouse_y + rotateDelta.y < MouseUtils.DISPLAY_HEIGHT_START) {
            this.mouseShellLocation.y = MouseUtils.DISPLAY_HEIGHT_START - rotateDelta.y;
        } else if (mouse_y > displayHeight) {
            this.mouseShellLocation.y = displayHeight;
        } else {
            this.mouseShellLocation.y = mouse_y;
        }
    }

    /**
	 * Returns the mouse ID
	 * @return
	 */
    public int getMouseID() {
        return mouseID;
    }

    /**
	 * Returns the set mouse location of the system mouse
	 * @return
	 */
    public Point getHideSystemMouseLocation() {
        return hideSystemMouseLocation;
    }

    /**
	 * Sets the mouse location of the system mouse
	 * @param hideSystemMouseLocation
	 */
    public void setHideSystemMouseLocation(Point hideSystemMouseLocation) {
        this.hideSystemMouseLocation = hideSystemMouseLocation;
    }

    /**
	 * Adds an event on the end of the queue
	 * @param event
	 */
    public synchronized void addEvent(ManyMouseEvent event) {
        this.eventQueue.addLast(event);
    }

    /**
	 * Returns the first element and deletes it in the queue
	 * @return Returns the event or null if there is no event in the queue
	 */
    public synchronized ManyMouseEvent getNextEvent() {
        ManyMouseEvent temp;
        if (this.eventQueue.size() > 0) {
            temp = this.eventQueue.getFirst();
            this.eventQueue.removeFirst();
        } else {
            temp = null;
        }
        return temp;
    }

    /**
	 * Sets the state of the pressed/released mouse button
	 * @param buttonId
	 * @param value
	 */
    public synchronized void setButtonsState(int buttonId, int value) {
        buttons.put(new Integer(buttonId), new Integer(value));
    }

    /**
	 * Returns the state of the button (if it is pressed or released)
	 * @param buttonId
	 * @return
	 */
    public synchronized int getButtonValue(int buttonId) {
        Integer button = buttons.get(new Integer(buttonId));
        if (button == null) return -1; else return button.intValue();
    }

    /**
	 * Sets the handle which was clicked
	 * @param buttonId
	 * @param value
	 */
    public synchronized void setLastHandle(int buttonId, int value) {
        lastHandle.put(new Integer(buttonId), new Integer(value));
    }

    /**
	 * Returns the handle
	 * @param buttonId
	 * @return
	 */
    public synchronized int getLastHandle(int buttonId) {
        Integer handle = lastHandle.get(new Integer(buttonId));
        if (handle == null) return -1; else return handle.intValue();
    }

    /**
	 * Sets the rotate delta
	 * @param rotateDelta
	 */
    public void setRotateDelta(Point rotateDelta) {
        this.rotateDelta = rotateDelta;
    }
}
