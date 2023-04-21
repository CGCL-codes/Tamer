package org.apache.harmony.awt.wtk.windows;

import org.apache.harmony.awt.wtk.NativeCursor;

public class WinCursor implements NativeCursor {

    final long hCursor;

    final WinEventQueue eventQueue;

    final boolean system;

    WinCursor(WinEventQueue eventQueue, final long handle, final boolean system) {
        hCursor = handle;
        this.system = system;
        this.eventQueue = eventQueue;
    }

    WinCursor(WinEventQueue eventQueue, final long handle) {
        this(eventQueue, handle, true);
    }

    /**
     * @see org.apache.harmony.awt.wtk.NativeCursor#setCursor()
     */
    public void setCursor(long winID) {
        WinEventQueue.Task task = new WinEventQueue.Task() {

            @Override
            public void perform() {
                WinEventQueue.win32.SetCursor(hCursor);
            }
        };
        eventQueue.performTask(task);
    }

    /**
     * @see org.apache.harmony.awt.wtk.NativeCursor#destroyCursor()
     */
    public void destroyCursor() {
        if (!system) {
            WinEventQueue.win32.DestroyCursor(hCursor);
        }
    }

    /**
     * @see org.apache.harmony.awt.wtk.NativeCursor#getId()
     */
    public long getId() {
        return hCursor;
    }
}
