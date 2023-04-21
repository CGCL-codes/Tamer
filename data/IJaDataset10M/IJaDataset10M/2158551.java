package org.argouml.util;

import java.lang.reflect.InvocationTargetException;
import javax.swing.SwingUtilities;
import org.argouml.model.Model;

/**
 * Helper methods for tests which need to deal with threads.  Not intended for
 * use in applications.
 * 
 * @author Tom Morris <tfmorris@gmail.com>
 */
public class ThreadHelper {

    /**
     * Wait for all events to be delivered on the MDR event thread and on the
     * AWT/Swing event thread.
     * 
     * @throws InterruptedException if we were interrupted while waiting for the
     *             AWT thread to sync
     * @throws InvocationTargetException should never happen. Indicates an
     *             internal error.
     */
    public static void synchronize() throws InterruptedException, InvocationTargetException {
        Model.getPump().flushModelEvents();
        Runnable doWorkRunnable = new Runnable() {

            public void run() {
            }
        };
        SwingUtilities.invokeAndWait(doWorkRunnable);
    }
}
