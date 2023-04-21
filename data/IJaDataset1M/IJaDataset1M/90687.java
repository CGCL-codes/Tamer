package org.nodal.util;

import java.util.Vector;
import org.nodal.model.*;

/**
 * <p>Standard implementation of the Monitored interface.</p>
 *
 * <p>This class is designed to be extended by any class that wishes to
 * implement the Monitored interface.  It implements all of the
 * methods to manage Monitors and provides two methods
 * (signalBeginChange and signalEndChange) that the ancestor classes
 * should call whenever a modification is processed.  These methods
 * will the call beginChange and endChange for all registered
 * Monitors.</p>
 */
public class MonitoredUtil implements Monitored {

    /**
   * A list of all current Monitors of this Object.
   */
    private Vector monitors;

    protected MonitoredUtil() {
        monitors = null;
    }

    /**
   * Add a monitor that will track changes to this value.
   */
    public boolean addMonitor(Monitor m) {
        if (monitors == null) {
            monitors = new Vector();
        }
        monitors.addElement(m);
        return true;
    }

    /**
   * Remove a previously added monitor.
   */
    public boolean removeMonitor(Monitor m) {
        if (monitors != null) {
            if (monitors.removeElement(m)) {
                if (monitors.size() == 0) {
                    monitors = null;
                }
                return true;
            }
        }
        return false;
    }

    /**
   * Remove all previously added monitors.
   */
    public void clearMonitors() {
        monitors = null;
    }

    /**
   * Signal the start of a change to the Monitored resource.
   */
    protected void signalBeginChange(Getter g) {
        if (monitors != null) {
            int size = monitors.size();
            for (int i = size - 1; i >= 0; --i) {
                Monitor m = (Monitor) monitors.elementAt(i);
                m.beginChange(g);
            }
        }
    }

    /**
   * Signal the end of a change to the Monitored resource.
   */
    protected void signalEndChange(Getter g) {
        if (monitors != null) {
            int size = monitors.size();
            for (int i = 0; i < size; ++i) {
                Monitor m = (Monitor) monitors.elementAt(i);
                m.endChange(g);
            }
        }
    }
}
