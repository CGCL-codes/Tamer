package sun.porting.windowsystem;

import java.awt.*;

/**
 * This is the public interface for a window object.  In this context it
 * is a relatively low-level object (not to be confused with java.awt.Window)
 * which underlies the implementation of a set of toolkit peers.
 * <code>Window</code> has only a small number of capabilities:
 * <ul>
 * <li> It is an opaque, clipped drawing surface (i.e. has a background color)
 * <li> Its size can be dynamically changed
 * <li> It is capable of being the target for input events
 * <li> It can contain other Windows in a strict hierarchical fashion
 * <li> It can exist without being visible (i.e. "mapped")
 * </ul>
 * Finally, <code>Window</code> encapsulates some additional state:
 * <ul>
 * <li>  X, Y position relative to its parent
 * <li>  Z order with respect to its siblings
 * <li>  keyboard and input focus
 * <li>  an associated cursor image
 * </ul>
 *
 * @version 1.18, 08/19/02
 */
public interface Window {

    /**
     * Get a graphics object for this window.  The <code>Graphics</code> object 
     * which is returned will be created with a <code>GeometryProvider</code> which 
     * corresponds to this window; as a result, all drawing operations
     * using this <code>Graphics</code> object will be located within, and clipped
     * to this window.
     */
    public Graphics getGraphics();

    /** 
     * Get the location and size of the window.  If global is true,
     * the position will be returned in root coordinates; otherwise
     * they will be relative to this window's parent.
     * @param global A flag indicating whether to return x and y in
     * global or parent-relative coordinates.
     * @return The position and size in pixels, as a <code>Rectangle</code> object.
     * @throws IllegalStateException if the window has been disposed.
     */
    Rectangle getBounds(boolean global) throws IllegalStateException;

    /**
     * Set the location and/or size of the window.  x and y are relative
     * to this window's parent.
     * @param x x coordinate of the upper left corner, relative to parent.
     * @param y y coordinate of the upper left corner, relative to parent.
     * @param w width in pixels.
     * @param h height in pixels.
     * @throws IllegalStateException if the window has been disposed.
     */
    void setBounds(int x, int y, int w, int h) throws IllegalStateException;

    /**
     * Move this window so that it is Nth in its list of siblings.
     * N = 0 moves the window to the front; N = -1 moves it to the back.
     * N = -2 moves it so it is above only the last window, etc.
     * @throws IllegalStateException if the window has been disposed.
     */
    void setStackingOrder(int n) throws IllegalStateException;

    /**
     * Set the background color for the window (i.e. the color which
     * will be used to erase it when damage occurs, etc.)
     */
    void setBackgroundColor(Color c);

    /**
     * Get the background color for the window (i.e. the color which
     * will be used to erase it when damage occurs, etc.)
     * @return The background color, as a <code>Color</code> object.
     */
    Color getBackgroundColor();

    /**
     * Query the map state of the window
     * @return true if the window is mapped (visible) false otherwise.
     * @throws IllegalStateException if the window has been disposed.
     */
    boolean isMapped() throws IllegalStateException;

    /**
     * Make this window visible.
     * @throws IllegalStateException if the window has been disposed.
     */
    void map() throws IllegalStateException;

    /**
     * Make this window invisible.
     * If the window has the grab or the input focus, implicitly releases them.
     * @throws IllegalStateException if the window has been disposed.
     */
    void unmap() throws IllegalStateException;

    /**
     * Dispose of any resources associated with the window, and mark it invalid.
     * If the window has the grab or the input focus, implicitly releases them.
     */
    void dispose();

    /**
     * Determine whether this window has the keyboard input focus.
     * @return true if this window has keyboard input focus, false otherwise.
     * @throws IllegalStateException if the window has been disposed.
     */
    boolean hasInputFocus() throws IllegalStateException;

    /**
     * Assign the keyboard input focus to this window.
     * @throws IllegalStateException if the window is disposed or not mapped.
     */
    void acquireInputFocus() throws IllegalStateException;

    /**
     * Determine whether this window (actually its top-level ancestor) 
     * owns the input grab.
     * @return true if this window has the grab, false otherwise.
     * @throws IllegalStateException if the window has been disposed.
     */
    boolean hasGrab() throws IllegalStateException;

    /**
     * Delivery mode for grab: deliver all events to the grab window.
     */
    static final int DELIVER_ALL = 1;

    /**
     * Delivery mode for grab: discard events not directed to the grabbing
     * window.  Some auditory and/or visual feedback should accompany each
     * discarded event.
     */
    static final int DISCARD_NOISY = 2;

    /**
     * Delivery mode for grab: discard events not directed to the grabbing
     * window, giving no feedback.
     */
    static final int DISCARD_SILENT = 3;

    /**
     * Make this window grab all input. <b>Does not change keyboard focus.</b>
     * If this window is not a top-level window, the grab will be given to
     * its top-level ancestor instead.  The mode argument indicates what 
     * should be done with events that are not directed at the grab window.
     * @param mode how to handle events directed at other windows.
     * @throws IllegalStateException if the window is disposed or not mapped,
     * or if the grab is owned by someone else.
     * <strong>Important note: the actual window that owns the explicit grab 
     * must always be a top-level window.  It is the window system's 
     * responsibility to find the top-level ancestor when grabbing, and to 
     * ensure that the event target is owned by (not necessarily equal to) 
     * the grab owner.</strong>
     */
    void grabInput(int mode) throws IllegalStateException;

    /**
     * Make this window (or its top-level ancestor) release the grab. 
     * <b>Does not change keyboard focus.</b>  Only explicitly-acquired
     * grabs can be released by this method.
     * @throws IllegalStateException if the window does not have the grab
     * or has been disposed.
     */
    void releaseGrab() throws IllegalStateException;

    /**
     * Set the cursor image to match the one supplied.
     * @param image The contents of the cursor image
     * @param hotX  The x coordinate of the hotspot
     * @param hotY  The y coordinate of the hotspot
     * @see sun.porting.graphicssystem.CursorImage
     */
    void setCursorImage(sun.porting.graphicssystem.CursorImage image);

    /**
     * Set the window's "user data" field to the given object.
     * This is a convenience for <code>Toolkit</code> implementors which allows
     * them to efficiently associate private data with each window.
     */
    void setUserData(java.lang.Object o);

    /**
     * Return the window's "user data" field.
     * This is a convenience for <code>Toolkit</code> implementors which allows
     * them to efficiently associate private data with each window.
     */
    java.lang.Object getUserData();
}
