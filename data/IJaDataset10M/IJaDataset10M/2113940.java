package org.kineticsystem.commons.threads;

import java.awt.EventQueue;

/**
 * <p>This class is used to construct an <i>heavy</i> object in background
 * using a thread.</p>
 * <p>If you try to access the object by the <code>get</code> method before the
 * the end of the construction, your code will wait until the construction ends.
 * </p>
 * <p>After the construction of the heavy object, the
 * <code>ObjectWorker</code> invokes the <code>finished</code> method inside the
 * AWT event-dispatching thread. You can implement this method to correctly
 * catch the event inside your code.</p>
 * <p>You can define the construction activity implementing the 
 * <code>construct</code> method. The object construction thread is
 * interruptable only if the above method is interruptable. In the last case you
 * must provide a right implementation of the returned object.</p>
 * @author Giovanni Remigi
 * $Revision: 38 $
 */
public abstract class ObjectWorker {

    /** The object to be constructed. */
    private Object value;

    /** The thread used to construct the object. */
    private Thread t;

    /** Default constructor. */
    public ObjectWorker() {
        final Runnable doFinished = new Runnable() {

            public void run() {
                finished();
            }
        };
        Runnable doConstruct = new Runnable() {

            public void run() {
                value = construct();
                EventQueue.invokeLater(doFinished);
            }
        };
        t = new Thread(doConstruct);
        t.setPriority(Thread.NORM_PRIORITY);
    }

    /**
     * Return the heavy object. If the object is still being constructed, the 
     * calling code waits inside the method until the construction ends.
     * @return The heavy object as defined inside the <code>construct</code>
     *     method.
     */
    public Object get() {
        try {
            t.join();
        } catch (InterruptedException e) {
        }
        return value;
    }

    /** Start the construction of the heavy object. */
    public void start() {
        t.start();
    }

    /** Interrupt the heavy object construction (if possible). */
    public void interrupt() {
        t.interrupt();
    }

    /**
     * Implement this method to define the construction <i>heavy</i> of your
     * object. A construction can be interrupted only if the implemented
     * method is interruptable. In the last case remember to return a valid
     * object or a null value.
     * @return The constructed object.
     */
    protected abstract Object construct();

    /**
     * This method is invoked inside the AWT event-dispatching thread after the
     * construction process has terminated.
     */
    public void finished() {
    }
}
