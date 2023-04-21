package com.thesett.common.util.concurrent;

import java.util.concurrent.TimeUnit;

/**
 * BaseUnaryCondition provides a base implementation of {@link UnaryCondition}. It provides methods to wait on condition
 * evaluations using the {@link #evaluateWithWaitTimeNanos(Object)} method and a mechanism to signal waiting threads as
 * the condition favourable changes. It can also accept favourable condition change signals from an external resource
 * and propagates them to any {@link Signalable} resource that is dependant on this one.
 *
 * <pre><p/><table id="crc"><caption>CRC Card</caption>
 * <tr><th>Responsibilities<th>Collaborations
 * <tr><td>Wait for a condition to evaluate to true.
 * <td> {@link UnaryCondition}
 * <tr><td>Notify dependant signalable resources when the condition favourably changes. <td> {@link Signalable}
 * <tr><td>Allow this resource to be registered with an external notifying resource. <td> {@link Notifying}
 * <tr><td>Wake up threads blocking on this condition when conditions favourably change to re-evaluate the condition.
 * </table></pre>
 *
 * @author Rupert Smith
 */
public abstract class BaseUnaryCondition<T> implements UnaryCondition<T> {

    /** Used as the thread monitor for threads to wait on this condition. */
    private Object monitor = new Object();

    /** Holds the signalable resource to notify when conditions change. */
    Signalable signalable;

    /**
     * Wait for the predicate to become true on the specified object.
     *
     * @param  t The object to test for predicate membership.
     *
     * @throws InterruptedException If interrupted whilst waiting.
     */
    public void await(T t) throws InterruptedException {
        synchronized (monitor) {
            long waitNanos = evaluateWithWaitTimeNanos(t);
            while (waitNanos > 0) {
                long milliPause = waitNanos / 1000000;
                int nanoPause = (int) (waitNanos % 1000000);
                monitor.wait(milliPause, nanoPause);
                waitNanos = evaluateWithWaitTimeNanos(t);
            }
        }
    }

    /**
     * Wait for up to a timeout limit for the predicate to become true on the specified object.
     *
     * @param  t       The object to test for predicate membership.
     * @param  timeout The amount of time to wait.
     * @param  unit    The units of time used.
     *
     * @return <tt>true</tt> if the predicate became true before the timeout expired, <tt>false</tt> otherwise.
     *
     * @throws InterruptedException If interrupted whilst waiting.
     */
    public boolean await(T t, long timeout, TimeUnit unit) throws InterruptedException {
        synchronized (monitor) {
            long expiryTimeNanos = System.nanoTime() + unit.toNanos(timeout);
            long waitNanos = evaluateWithWaitTimeNanos(t);
            while (waitNanos > 0) {
                long remainingTimeNanos = expiryTimeNanos - System.nanoTime();
                if (remainingTimeNanos <= 0) {
                    return false;
                }
                long timeToPauseNanos = (waitNanos < remainingTimeNanos) ? waitNanos : remainingTimeNanos;
                long milliPause = timeToPauseNanos / 1000000;
                int nanoPause = (int) (timeToPauseNanos % 1000000);
                monitor.wait(milliPause, nanoPause);
                waitNanos = evaluateWithWaitTimeNanos(t);
            }
        }
        return true;
    }

    /**
     * Signals to one waiting thread on this predicate, that conditions have changed such that it may now be possible
     * for the thread to proceed. This wakes up one waiting thread, in order for it to re-check the predicate.
     */
    public void signal() {
        synchronized (monitor) {
            monitor.notify();
        }
        if (signalable != null) {
            signalable.signal();
        }
    }

    /**
     * Signals to all waiting threads on this predicate, that conditions have changed such that it may now be possible
     * for the thread to proceed. This wakes up all waiting threads, in order for them to re-check the predicate.
     */
    public void signalAll() {
        synchronized (monitor) {
            monitor.notifyAll();
        }
        if (signalable != null) {
            signalable.signalAll();
        }
    }

    /**
     * Sets a signallable resource on this condition that the condition can use to notify threads waiting on this
     * condition that they should wake up and recheck the condition in order to see if they can proceed.
     *
     * @param signalable The signallable resource to notify when threads may be able to proceed.
     */
    public void setSignalableResource(Signalable signalable) {
        this.signalable = signalable;
    }
}
