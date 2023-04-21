package org.jikesrvm.scheduler.greenthreads;

import org.jikesrvm.VM;
import org.jikesrvm.runtime.Time;
import org.jikesrvm.scheduler.RVMThread;
import org.vmmagic.pragma.Interruptible;
import org.vmmagic.pragma.Uninterruptible;
import org.vmmagic.pragma.Unpreemptible;

/**
 * Queue of threads waiting for a specific kind of event to occur.
 * This class contains the high level functionality of enqueueing
 * and dequeueing threads and implementing timeouts.
 * Subclasses implement methods which determine when events
 * have occurred. Subclasses <em>must</em> directly implement the
 * {@link Uninterruptible} interface.
 *
 * <p>This class was adapted from the original
 * <code>ThreadIOQueue</code>, which is now a subclass.
 *
 *
 * @see ThreadIOQueue
 * @see ThreadProcessWaitQueue
 * @see ThreadEventConstants
 */
@Uninterruptible
abstract class ThreadEventWaitQueue extends AbstractThreadQueue implements ThreadEventConstants {

    protected GreenThread head, tail;

    /** Number of queued threads. */
    private int length;

    /**
   * Number of threads ready to run because their events occurred,or their
   * timeout expired.
   */
    private int ready;

    /**
   * Is queue empty?
   */
    @Override
    public boolean isEmpty() {
        return length == 0;
    }

    /**
   * Number of threads on queue.
   */
    @Override
    public int length() {
        return length;
    }

    /**
   * Check to see if any threads are ready to run, either because
   * their events occurred or their waits timed out.
   */
    boolean isReady() {
        if (length == 0) {
            return false;
        }
        if (VM.VerifyAssertions) VM._assert(ready >= 0);
        if (ready == 0) {
            if (!pollForEvents()) {
                return false;
            }
            GreenThread thread = head;
            long currentNano = Time.nanoTime();
            while (thread != null) {
                ThreadEventWaitData waitData = thread.waitData;
                long maxWaitNano = waitData.getMaxWaitNano();
                if (maxWaitNano > 0 && maxWaitNano < currentNano) {
                    waitData.setFinishedAndTimeout();
                    ++ready;
                } else if (isReady(thread)) {
                    if (VM.VerifyAssertions) {
                        VM._assert(waitData.isFinished());
                    }
                    ++ready;
                } else {
                    waitData.clearFinished();
                }
                thread = thread.getNext();
            }
        }
        return ready != 0;
    }

    /**
   * Check to see if any events occurred.
   * Called prior to calling {@link #isReady(GreenThread)} on
   * queued threads.
   * @return whether or not polling was successful
   */
    abstract boolean pollForEvents();

    /**
   * Check to see if the event the given thread is waiting for
   * has occurred, or if it should be woken up for any other reason
   * (such as being interrupted).
   */
    abstract boolean isReady(GreenThread thread);

    /**
   * Place a thread on this queue.
   * Its {@link org.jikesrvm.scheduler.greenthreads.GreenThread#waitData waitData} field should
   * be set to indicate the event that the thread is waiting for.
   * @param thread the thread to put on the queue
   */
    @Override
    public void enqueue(GreenThread thread) {
        if (VM.VerifyAssertions) {
            VM._assert(thread.waitData.isPending() || thread.waitData.isNative());
            VM._assert(thread.getNext() == null);
        }
        if (head == null) {
            head = thread;
        } else {
            tail.setNext(thread);
        }
        tail = thread;
        ++length;
    }

    /**
   * Get a thread that has become ready to run.
   * @return the thread, or null if no threads from this queue are ready
   */
    @Override
    public GreenThread dequeue() {
        GreenThread prev = null;
        GreenThread thread = head;
        if (VM.VerifyAssertions) VM._assert(ready >= 0);
        while (thread != null) {
            if (thread.waitData.isFinished()) {
                break;
            }
            prev = thread;
            thread = thread.getNext();
        }
        if (thread != null) {
            if (prev == null) {
                head = thread.getNext();
            } else {
                prev.setNext(thread.getNext());
            }
            if (tail == thread) {
                tail = prev;
            }
            thread.setNext(null);
            --length;
            --ready;
        } else {
            if (VM.VerifyAssertions) VM._assert(ready == 0);
        }
        return thread;
    }

    /** Does the queue contain the given thread */
    final boolean contains(RVMThread x) {
        for (GreenThread t = head; t != null; t = t.getNext()) {
            if (t == x) return true;
        }
        return false;
    }

    /**
   * Dump state for debugging.
   */
    @Unpreemptible
    void dump() {
        dump(" ");
    }

    /**
   * Dump state for debugging.
   */
    @Unpreemptible
    void dump(String prefix) {
        VM.sysWrite(prefix);
        for (GreenThread t = head; t != null; t = t.getNext()) {
            VM.sysWrite(t.getIndex());
            dumpWaitDescription(t);
        }
        VM.sysWrite("\n");
    }

    /**
   * Dump description of what given thread is waiting for.
   * For debugging.
   */
    @Unpreemptible
    abstract void dumpWaitDescription(GreenThread thread);

    /**
   * Get string describing what given thread is waiting for.
   * This method must be interruptible!
   */
    @Interruptible
    abstract String getWaitDescription(GreenThread thread);
}
