package org.apache.harmony.awt.wtk.linux;

import org.apache.harmony.awt.wtk.NativeCursor;
import org.apache.harmony.awt.nativebridge.linux.X11;

/**
 * Implementation of NativeCursor for Linux(X11) platform.
 */
public class LinuxCursor implements NativeCursor {

    private static X11 x11 = X11.getInstance();

    private final long display;

    private final long cursorId;

    private boolean system;

    private boolean valid = true;

    LinuxCursor(final long id, final boolean system, long display) {
        cursorId = id;
        this.system = system;
        this.display = display;
    }

    LinuxCursor(final long id, long display) {
        this(id, true, display);
    }

    /**
     * @see org.apache.harmony.awt.wtk.NativeCursor#setCursor(long)
     */
    public void setCursor(long winID) {
        if (valid) {
            x11.XDefineCursor(display, winID, cursorId);
        }
    }

    /**
     * Destroys any non-system(user-defined cursor)
     * @see org.apache.harmony.awt.wtk.NativeCursor#destroyCursor()
     */
    public void destroyCursor() {
        if (!system) {
            destroy();
        }
    }

    /**
     * Destroys any cursor
     */
    void destroy() {
        if (valid) {
            x11.XFreeCursor(display, cursorId);
            valid = false;
        }
    }

    /**
     * @see org.apache.harmony.awt.wtk.NativeCursor#getId()
     */
    public long getId() {
        return cursorId;
    }
}
