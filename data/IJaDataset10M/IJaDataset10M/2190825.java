package com.sun.j2me.content;

import javax.microedition.content.RequestListener;

/**
 * Thread to monitor pending invocations and notify a listener
 * when a matching one is present.
 */
class RequestListenerImpl implements Runnable {

    /** ContenHandlerImpl for which this is listening. */
    private final ContentHandlerImpl handler;

    /** The active thread processing the run method. */
    private Thread thread;

    /**
     * Create a new listener for pending invocations.
     *
     * @param handler the ContentHandlerImpl to listen for
     * @param listener the listener to notify when present
     */
    RequestListenerImpl(ContentHandlerImpl handler, RequestListener listener) {
        this.handler = handler;
        setListener(listener);
    }

    /**
     * Set the listener to be notified and start/stop the monitoring
     * thread as necessary.
     * If the listener is non-null make sure there is a thread active
     * to monitor it.
     * If there is no listener, then stop the monitor thread.
     * Unblock any blocked threads so they can get the updated listener.
     * @param listener the listener to update
     */
    void setListener(RequestListener listener) {
        if (listener != null) {
            if (thread == null || !thread.isAlive()) {
                thread = new Thread(this);
                thread.start();
            }
        } else {
            thread = null;
        }
        InvocationStore.setListenNotify(handler.storageId, handler.classname, true);
        InvocationStore.cancel();
    }

    /**
     * The run method checks for pending invocations for a
     * desired ContentHandler or application.
     * When an invocation is available the listener is
     * notified.
     */
    public void run() {
        Thread mythread = Thread.currentThread();
        while (mythread == thread) {
            boolean pending = InvocationStore.listen(handler.storageId, handler.classname, true, true);
            if (pending) {
                handler.requestNotify();
            }
        }
    }
}
