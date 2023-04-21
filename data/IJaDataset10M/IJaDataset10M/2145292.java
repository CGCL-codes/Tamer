package sw_emulator.util;

import java.lang.InterruptedException;

/**
 * Implement a Monitor for sinchronizing threads
 * The available operation to the monitor are <code>opSignal</code> and
 * <code>opWait</code>. A internal counter is used for know when all the
 * given threads that use this monitor have finish to make their body.
 * This is needed as the caller of <code>opSignal</code> has to know
 * when the other has finish before going away. In the real system,
 * this is not needed as the operations terminate in the clock period 
 * of time. <code>opNotify</code> is to use for notify the thread that
 * call <code>opSignal</code> that this thread will do a <code>opWait</code>
 * in his body. Be carefull: if one thred use <code>opNotify</code> and will
 * not do an <code>opWait</code>, a deadlook will occurs.
 *
 * @author Ice
 * @version 1.00 19/09/1999
 */
public class Monitor {

    /** Contains the name of the monitor (used as debug info) */
    protected String name;

    /** The actual threads counter */
    protected int counter;

    /** The max threads counter value to use */
    protected int maxCounter = 0;

    /**
   * Build a named monitor
   *
   * @param name the monitor debug name
   */
    public Monitor(String name) {
        this.name = name;
    }

    /**
   * Notify the this thread will do an <code>opWait</code> to this monitor
   */
    public synchronized void opNotify() {
        maxCounter++;
    }

    /**
   * Subspend the thread until a <code>opSignal</code> operation is made
   */
    public synchronized void opWait() {
        try {
            if (counter > 0) counter--;
            wait();
        } catch (InterruptedException e) {
            System.err.println("Thread error for monitor " + name + ": " + e);
        }
    }

    /**
   * Resume all the thread that was subspended by <code>opWait</code> operation
   */
    public synchronized void opSignal() {
        counter = maxCounter;
        notifyAll();
    }

    /**
   * Return true if all threads have finish
   * 
   * @return true if all threads have finish
   */
    public synchronized boolean isFinish() {
        return (counter == 0);
    }

    /**
   * Return the name of the monitor
   *
   * @return the name of the monitor
   */
    public String getName() {
        return name;
    }
}
