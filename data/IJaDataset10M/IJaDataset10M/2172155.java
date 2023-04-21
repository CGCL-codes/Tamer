package com.lc.util;

import javax.swing.SwingUtilities;

/**
 * 3<sup>rd</sup> version of the <code>SwingWorker</code>, an abstract class
 * to override for GUI-related operation, in a dedicated thread.<br>
 * This class has been imported from Sun's site.
 * For more information, have a look at&nbsp;:<br>
 * <a href="http://java.sun.com/docs/books/tutorial/uiswing/misc/threads.html">http://java.sun.com/docs/books/tutorial/uiswing/misc/threads.html</a>
 * @author Laurent Caillette
 * @author Sun Microsystems
 * @version $Revision: 1.1.1.1 $  $Date: 2002/02/19 22:12:03 $
 */
public abstract class SwingWorker {

    private Object value;

    private Thread thread;

    /**
 * Holds a reference to the worker thread, in a distinct synchronization
 * context.
 */
    private static class ThreadVar {

        private Thread thread;

        ThreadVar(Thread t) {
            thread = t;
        }

        synchronized Thread get() {
            return thread;
        }

        synchronized void clear() {
            thread = null;
        }
    }

    private ThreadVar threadVar;

    /**
 * Gets the value produced by the worker thread, or <tt>null</tt> if
 * not produced yet.
 */
    protected synchronized Object getValue() {
        return value;
    }

    /**
 * Sets the value produced by the worker thread.
 */
    private synchronized void setValue(Object x) {
        value = x;
    }

    /**
 * Calculates the value to be returned by the <tt>get</tt> method.
 */
    public abstract Object construct();

    /**
 * Called by the dispath thread, after the <tt>construct</tt> completed.
 */
    public void finished() {
    }

    /**
 * Forces the worker thread to interrupt.
 */
    public void interrupt() {
        Thread t = threadVar.get();
        if (t != null) {
            t.interrupt();
        }
        threadVar.clear();
    }

    /**
 * Returns the value created by the <tt>construct</tt> method.
 * @return <tt>null</tt> if the the worker thread, or the current thread,
 *     were interrupted before producing a value. The created value
 *     otherwise.
 */
    public Object get() {
        while (true) {
            Thread t = threadVar.get();
            if (t == null) {
                return getValue();
            }
            try {
                t.join();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                return null;
            }
        }
    }

    /**
 * Creates a thread which will call the <tt>construct</tt> method before
 * ending.
 */
    public SwingWorker() {
        final Runnable doFinished = new Runnable() {

            public void run() {
                finished();
            }
        };
        Runnable doConstruct = new Runnable() {

            public void run() {
                try {
                    setValue(construct());
                } finally {
                    threadVar.clear();
                }
                SwingUtilities.invokeLater(doFinished);
            }
        };
        Thread t = new Thread(doConstruct);
        threadVar = new ThreadVar(t);
    }

    /**
 * Starts the worker thread.
 */
    public void start() {
        Thread t = threadVar.get();
        if (t != null) {
            t.start();
        }
    }
}
