package java.awt;

import java.awt.event.*;

/** The base class for all top level windows. Top level X windows
 have the X root window as their parent.
 @version 1.17, 08/19/02
 @author Nicholas Allen*/
class WindowXWindow extends HeavyweightComponentXWindow {

    public WindowXWindow(Window window) {
        super(window);
        topBorder = defaultTopBorder;
        leftBorder = defaultLeftBorder;
        bottomBorder = defaultBottomBorder;
        rightBorder = defaultRightBorder;
    }

    void create() {
        this.windowID = create((component.parent != null && component.parent.xwindow != null) ? component.parent.xwindow.windowID : 0);
    }

    native int create(int owner);

    final native void setTitle(String title);

    final void setIconImage(Image image) {
        if (image != null) setIconImageNative(((X11Image) image).Xpixmap);
    }

    final native void setIconImageNative(int pixmap);

    final native void toFront();

    final native void toBack();

    final synchronized void setResizable(boolean b) {
        resizable = b;
        if (!resizable) {
            int width = Math.max(1, component.width - leftBorder - rightBorder);
            int height = Math.max(1, component.height - topBorder - bottomBorder);
            setSizeHintsNative(width, height, width, height);
        } else setSizeHints(component.getMinimumSize(), component.getMaximumSize());
    }

    /** Sets the event mask for the X window using the supplied event mask (see AWTEvent).
     This lets the X server know what kinds of events we are interested in. */
    final native void setMouseEventMask(long mask);

    final synchronized void setBounds(int x, int y, int width, int height) {
        width = Math.max(1, width - leftBorder - rightBorder);
        height = Math.max(1, height - topBorder - bottomBorder);
        if (!resizable) setSizeHintsNative(width, height, width, height);
        setBoundsNative(x, y, width, height);
    }

    native void setBoundsNative(int x, int y, int width, int height);

    /** Sets the hints for the window manager for the minimum/maximum size of the window.
     The window manager may or may not respect this. */
    final synchronized void setSizeHints(Dimension minSize, Dimension maxSize) {
        if (resizable) {
            minSize.width = Math.max(minSize.width - leftBorder - rightBorder, 1);
            minSize.height = Math.max(minSize.height - topBorder - bottomBorder, 1);
            maxSize.width = Math.max(Math.min(maxSize.width - leftBorder - rightBorder, Short.MAX_VALUE), minSize.width);
            maxSize.height = Math.max(Math.min(maxSize.height - topBorder - bottomBorder, Short.MAX_VALUE), minSize.height);
            setSizeHintsNative(minSize.width, minSize.height, maxSize.width, maxSize.height);
        }
    }

    private final native void setSizeHintsNative(int minWidth, int minHeight, int maxWidth, int maxHeight);

    void map() {
        mapNative();
    }

    /** Called by the native code when an area of the X Window is exposed.
     This posts a paint event into Java's event queue for processing by Java
     so that the exposed area is redrawn. */
    final void postPaintEvent(int x, int y, int width, int height) {
        super.postPaintEvent(x + leftBorder, y + topBorder, width, height);
    }

    /** Called when a mouse event is received from the X server for this X window. We post a
     mouse event into Java's event queue for processing. This is overriden to translate the coordinates
     to the outer window's coordinate system. */
    void postMouseButtonEvent(int id, long when, int modifiers, int x, int y) {
        super.postMouseButtonEvent(id, when, modifiers, x + leftBorder, y + topBorder);
    }

    void postMouseEvent(int id, long when, int modifiers, int x, int y) {
        super.postMouseEvent(id, when, modifiers, x + leftBorder, y + topBorder);
    }

    private void postWindowEvent(int id) {
        Toolkit.getEventQueue().postEvent(new WindowEvent((Window) component, id));
    }

    private void postKeyEvent(int id, long when, int modifiers, int keyCode, char keyChar) {
        Toolkit.getEventQueue().postEvent(new KeyEvent(component, id, when, modifiers, keyCode, keyChar));
    }

    private synchronized void setBorders(int top, int left, int bottom, int right) {
        if (top != topBorder || left != leftBorder || bottom != bottomBorder || right != rightBorder) {
            topBorder = top;
            leftBorder = left;
            bottomBorder = bottom;
            rightBorder = right;
            EventQueue.invokeLater(new Runnable() {

                public void run() {
                    component.invalidate();
                    component.validate();
                    component.repaint();
                }
            });
        }
        setDefaultBorders(top, left, bottom, right);
    }

    void setDefaultBorders(int top, int left, int bottom, int right) {
        defaultTopBorder = top;
        defaultBottomBorder = bottom;
        defaultLeftBorder = left;
        defaultRightBorder = right;
    }

    void postMovedEvent() {
        Toolkit.getEventQueue().postEvent(new ComponentEvent(component, ComponentEvent.COMPONENT_MOVED));
    }

    void postResizedEvent() {
        Toolkit.getEventQueue().postEvent(new ComponentEvent(component, ComponentEvent.COMPONENT_RESIZED));
    }

    private int outerWindow;

    int topBorder, leftBorder, bottomBorder, rightBorder;

    private boolean resizable = true;

    private static int defaultTopBorder = 0;

    private static int defaultLeftBorder = 0;

    private static int defaultBottomBorder = 0;

    private static int defaultRightBorder = 0;
}
