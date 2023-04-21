package com.objectwave.appArch;

import com.objectwave.event.*;
import java.lang.reflect.*;

/**
 * Set the default status manager to be an instance of this class if you wish to support
 * asynchronous events.
 * @author Dave Hoag
 * @version 1.0
 */
public class AsynchStatusManager extends StatusManager {

    Queue threadedQueue;

    protected void finalize() throws Throwable {
        if (threadedQueue == null) return;
        Thread queue = threadedQueue.getThread();
        System.out.println(queue.isAlive());
        while (!queue.isInterrupted()) {
        }
        queue.stop();
    }

    /**
	 */
    public void fireStatusEvent(final StatusEvent evt, boolean asynch) {
        if (!asynch) super.fireStatusEvent(evt); else {
            Runnable r = new Runnable() {

                public void run() {
                    try {
                        fireStatusEvent(evt, false);
                    } catch (Throwable t) {
                        t.printStackTrace();
                    }
                }
            };
            new Thread(r).start();
        }
    }

    /**
	* Convience method to fire an informational status event.
	*/
    public void fireStatusEvent(Object source, String text, boolean asynch) {
        fireStatusEvent(new StatusEvent(source, text), asynch);
    }
}
