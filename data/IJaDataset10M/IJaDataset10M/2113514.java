package com.sun.midp.util;

import javax.microedition.lcdui.Canvas;
import javax.microedition.lcdui.Display;
import javax.microedition.lcdui.Graphics;

/**
 * A simple test canvas. Adds the ability to wait for the Canvas to become 
 * visible for the first time.
 */
public class LcduiTestCanvas extends Canvas {

    static final long TIMEOUT = 1000L;

    boolean painted = false;

    boolean shown = false;

    boolean everShown = false;

    /**
     * Subclassers should override this to do actual painting.
     */
    public void paint1(Graphics g) {
    }

    /**
     * Subclassers should override this to do showNotify() processing.
     */
    public void showNotify1() {
    }

    /**
     * Subclassers should override this to do hideNotify() processing.
     */
    public void hideNotify1() {
    }

    /**
     * Notifies when painted for the first time. This is made final so that
     * subclassers can't accidentally break this if they override and forget
     * to call super(). Subclassers should override paint1() to do actual
     * painting.
     */
    public final void paint(Graphics g) {
        synchronized (this) {
            if (!painted) {
                painted = true;
                notifyAll();
            }
        }
        paint1(g);
    }

    /**
     * Tracks the shown state. If this is the first time the Canvas is being
     * shown, sets everShown and does a monitor notify. This is made final so
     * that subclassers can't accidentally break the protocol if they override
     * and forget to call super(). Subclassers may override showNotify1() if
     * they wish to do show processing.
     */
    public final void showNotify() {
        synchronized (this) {
            shown = true;
            if (!everShown) {
                everShown = true;
                notifyAll();
            }
        }
        showNotify1();
    }

    /**
     * Tracks the shown state. This is made final so that subclassers can't
     * accidentally break the protocol if they override and forget to call
     * super(). Subclassers may override hideNotify1() if they wish to do hide
     * processing.
     */
    public final void hideNotify() {
        synchronized (this) {
            shown = false;
        }
        hideNotify1();
    }

    /**
     * Waits until this canvas is painted for the first time. Proper operation 
     * relies on the caller to have arranged for this canvas to be shown, for 
     * example, with setCurrent(). Returns immediately if the canvas has been 
     * painted already, even if it's been hidden and shown again. Returns true 
     * if the canvas has been painted at least once. Returns false if the wait 
     * was interrupted or if it timed out. This method must not be called on 
     * the event thread. If it is, it will block paint processing and will 
     * eventually timeout.
     */
    public boolean awaitPaint() {
        long deadline = System.currentTimeMillis() + TIMEOUT;
        synchronized (this) {
            try {
                while (!painted && System.currentTimeMillis() < deadline) {
                    wait(TIMEOUT);
                }
            } catch (InterruptedException ie) {
                return false;
            }
            return painted;
        }
    }

    /**
     * Waits until this canvas is shown for the first time. Proper operation 
     * relies on the caller to have arranged for this canvas to be shown, for 
     * example, with setCurrent(). Returns immediately if the canvas has been 
     * shown already, even if it has subsequently been hidden. Returns true if 
     * the canvas has ever been shown. Returns false if the wait was 
     * interrupted or if it timed out. This method must not be called on the 
     * event thread. If it is, it will block show processing and will 
     * eventually timeout.
     */
    public boolean awaitShow() {
        long deadline = System.currentTimeMillis() + TIMEOUT;
        synchronized (this) {
            try {
                while (!everShown && System.currentTimeMillis() < deadline) {
                    wait(TIMEOUT);
                }
            } catch (InterruptedException ie) {
                return false;
            }
            return everShown;
        }
    }

    /**
     * Returns a boolean indicating whether showNotify has been called on this
     * canvas more recently than hideNotify.  This differs from the
     * Displayable.isShown() method, which checks the internal state of the
     * Displayable object.
     */
    public synchronized boolean showCalled() {
        return shown;
    }

    /**
     * Returns a boolean indicating whether showNotify() has ever been called 
     * at any time since the canvas was created.
     */
    public synchronized boolean wasShown() {
        return everShown;
    }
}
