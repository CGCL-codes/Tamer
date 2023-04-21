package com.icesoft.faces.async.render;

import edu.emory.mathcs.backport.java.util.concurrent.ThreadFactory;

/**
 * The Rendering API uses it's own ThreadFactory so that we can set the name,
 * daemon status, and context class loader.
 *
 * @author ICEsoft Technologies, Inc.
 */
class RenderThreadFactory implements ThreadFactory {

    public static final String PREFIX = "Render Thread - ";

    private static int threadCounter = 0;

    /**
     * Return a new Thread using the supplied Runnable.  Each thread is named
     * using the PREFIX and a counter.  The daemon status is set to true. We
     * also set the context class loader for the thread as some J2EE containers
     * don't do this properly.
     *
     * @param runnable
     * @return A new Thread to use in the {@link RenderHub}s thread pools.
     */
    public Thread newThread(Runnable runnable) {
        Thread thread = new Thread(runnable, PREFIX + (threadCounter++));
        thread.setDaemon(true);
        return thread;
    }
}
