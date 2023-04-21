package com.notuvy.gui;

import org.apache.log4j.Logger;
import java.awt.event.MouseEvent;
import java.awt.event.KeyEvent;
import java.awt.*;
import com.notuvy.thread.IdlenessMonitorable;

/**
 * An EventQueue that keeps track of last activity, so it can be determined
 * how long the user is idle.
 *
 * http://www.javakb.com/Uwe/Forum.aspx/java-gui/2628/Inactivity-detect
 *
 * User: murali
 * Date: Aug 29, 2008
 * Time: 1:02:12 PM
 */
public class SwingActivityEventQueue extends EventQueue implements IdlenessMonitorable {

    @SuppressWarnings({ "UnusedDeclaration" })
    private static final Logger LOG = Logger.getLogger("com.notuvy.gui");

    private static final SwingActivityEventQueue INSTANCE = new SwingActivityEventQueue();

    static {
        EventQueue current = Toolkit.getDefaultToolkit().getSystemEventQueue();
        if (current != INSTANCE) {
            current.push(INSTANCE);
        }
    }

    public static SwingActivityEventQueue getInstance() {
        return INSTANCE;
    }

    private final UIActivityMonitor fActivityMonitor = new UIActivityMonitor();

    private SwingActivityEventQueue() {
    }

    @Override
    public String toString() {
        return String.format("Swing activity [%s].", fActivityMonitor);
    }

    public long lastActivity() {
        return fActivityMonitor.lastActivity();
    }

    public void markActionTakenOnIdle() {
        fActivityMonitor.touchActionTaken();
    }

    protected void dispatchEvent(AWTEvent event) {
        try {
            super.dispatchEvent(event);
        } catch (RuntimeException re) {
            LOG.error(String.format("UI error dispatching [%s].", event.toString()), re);
            throw re;
        }
        switch(event.getID()) {
            case MouseEvent.MOUSE_RELEASED:
            case KeyEvent.KEY_RELEASED:
                fActivityMonitor.touchUIActivity();
                break;
        }
    }
}
