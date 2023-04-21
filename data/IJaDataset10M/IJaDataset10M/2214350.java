package com.gwtext.client.widgets;

/**
 * The default global window group that is available automatically. To have more than one group of windows with separate z-order stacks, create additional instances of WindowGroup as needed.
 *
 * @author Sanjiv Jivan
 */
public class WindowMgr {

    /**
     * Brings the specified window to the front of any other active windows.
     *
     * @param windowID the window ID
     */
    public static native void bringToFront(String windowID);

    /**
     * Brings the specified window to the front of any other active windows.
     *
     * @param window the window
     */
    public static native void bringToFront(Window window);

    /**
     * Executes the specified function once for every window in the group, passing each window as the only parameter. Returning false from the function will stop the iteration.
     *
     * @param cb the traversal callback function
     */
    public static native void each(ComponentTraversalCallback cb);

    /**
     * Gets a registered window by id.
     *
     * @param id the window ID
     * @return the window or null if not found
     */
    public static native Window get(String id);

    /**
     * Gets the currently-active window in the group.
     *
     * @return the window or null if not found
     */
    public static native Window getActive();

    /**
     * Returns zero or more windows in the group using the custom search function passed to this method. The function
     * should accept a single Ext.Window reference as its only argument and should return true if the window matches
     * the search criteria, otherwise it should return false.
     *
     * @param cb the callback
     * @return an array of windows
     */
    public native Window[] findBy(ComponentTraversalCallback cb);

    /**
     * Hides all windows in the group.
     */
    public static native void hideAll();

    /**
     * Sends the specified window to the back of other active windows.
     *
     * @param windowID the window ID
     */
    public static native void sendToBack(String windowID);

    /**
     * Sends the specified window to the back of other active windows.
     *
     * @param window the window
     */
    public static native void sendToBack(Window window);

    /**
     * The starting z-index for windows (defaults to 9000).
     *
     * @param zseed the zseed
     */
    public static native void setZseed(int zseed);
}
