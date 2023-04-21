package org.curjent.impl.agent;

import java.util.concurrent.TimeUnit;
import org.curjent.agent.CallState;

/**
 * Common await implementations.
 * 
 * @see Awaiter
 */
final class Awaiters {

    /**
	 * Common await implementation. Waits until the <code>condition</code> is
	 * <code>satisfied</code>. Waits up to <code>nanos</code> nanoseconds before
	 * timing out. This call simply returns once the condition has been
	 * satisfied or the wait period has timed out; callers either recheck the
	 * condition or don't care.
	 * <p>
	 * <code>synchronous</code> is <code>true</code> for synchronous calls. More
	 * specifically, it is only <code>true</code> when this method is called by
	 * the {@link Message#await()} method. <code>EXECUTING</code> synchronous
	 * calls are special-cased; the <code>Message.await()</code> method
	 * simulates direct method calls. Support for this execution model is
	 * partially implemented here and in {@link Deadlocks}. Specifically, a
	 * <code>DeadlockException</code> is never thrown to the caller making the
	 * synchronous call while the message is running. In deadlock scenarios
	 * detected by curjent, at least one of the threads is waiting on a message
	 * that is not running, and it is the caller on one of those other threads
	 * that receives the deadlock exception.
	 * <p>
	 * Deadlock is defined only for infinite waits; i.e., where
	 * <code>nanos == MAX_VALUE</code>.
	 * <p>
	 * Agents can be configured to <i>not</i> check for deadlock by configuring
	 * the deadlock check interval to <code>MAX_VALUE</code> via
	 * {@link org.curjent.agent.AgentConfig#setDeadcheckTimeout(long, TimeUnit)}.
	 * <p>
	 * The await is either timed or not, where <code>timed</code> is
	 * <code>true</code> if <code>nanos < MAX_VALUE</code>. If timed, we wait at
	 * most <code>nanos</code> nanoseconds and never check for deadlock.
	 * <p>
	 * If not timed, we set <code>check</code> to
	 * <code>AgentConfig.getDeadcheckTimeout()</code>. This is the number of
	 * nanoseconds (if any) to wait before checking for deadlock the first time.
	 * <p>
	 * <code>Object.wait</code> may return before the requested
	 * <code>duration</code> has elapsed, so an inner loop is used to repeatedly
	 * wait until the intended <code>duration</code> has truly elapsed.
	 * <p>
	 * The first time the inner loop is entered, the wait <code>duration</code>
	 * is either the initial deadlock <code>check</code> delay for an infinite
	 * await, or equal to <code>nanos</code> for a <code>timed</code> await. For
	 * a timed await, the inner loop never exits until this method returns
	 * either because the <code>condition</code> is <code>satisfied</code> or
	 * <code>nanos</code> nanoseconds have elapsed.
	 * <p>
	 * The second and subsequent times into the inner loop, the inner loop waits
	 * forever until the message receives a notification via
	 * <code>Object.notifyAll()</code>. When notified, either the
	 * <code>condition</code> has potentially been <code>satisfied</code> and we
	 * return, or <code>deadcheck</code> has been set and we check for deadlock
	 * if <code>eligible</code>.
	 * <p>
	 * We are never <code>eligible</code> to check for deadlock the first time
	 * the inner loop is entered. In other words, requests for checking deadlock
	 * are ignored until at least <code>check</code> nanoseconds have elapsed
	 * first (which is initially set to the configured
	 * <code>getDeadcheckTimeout</code> value).
	 * <p>
	 * The second and subsequent times into the inner loop are always
	 * <code>eligible</code> to check for deadlock <i>except</i> for
	 * <code>EXECUTING</code> synchronous calls.
	 */
    static boolean await(Awaiter awaiter, boolean synchronous, CallState state, long nanos) throws InterruptedException {
        final Controller controller = awaiter.getController();
        final boolean timed = nanos < Long.MAX_VALUE;
        long start = System.nanoTime();
        boolean eligible = false;
        boolean blocked = false;
        long check;
        if (timed) {
            check = -1;
        } else {
            check = controller.getDeadcheckNanos();
            if (check == Long.MAX_VALUE) {
                check = -1;
            }
        }
        try {
            for (; ; ) {
                awaiter.lock();
                try {
                    synchronized (awaiter) {
                        for (; ; ) {
                            if (awaiter.isSatisfied(state)) {
                                return true;
                            }
                            if (nanos <= 0) {
                                return false;
                            }
                            if (check == 0 || (eligible && awaiter.isDeadcheckRequested())) {
                                break;
                            }
                            long duration = (check > 0) ? check : nanos;
                            awaiter.await(duration);
                            long current = System.nanoTime();
                            long elapsed = current - start;
                            start = current;
                            if (timed) {
                                nanos -= elapsed;
                            } else if (check > 0) {
                                check -= elapsed;
                                if (check <= 0) {
                                    break;
                                }
                            }
                        }
                        eligible = !synchronous || !awaiter.isExecuting();
                        awaiter.clearDeadcheckRequest();
                    }
                } finally {
                    awaiter.unlock();
                }
                if (!blocked) {
                    controller.blocked(awaiter, synchronous);
                    blocked = true;
                }
                Deadlocks.INSTANCE.deadcheck();
                check = -1;
            }
        } finally {
            if (blocked) {
                controller.unblocked(awaiter);
            }
        }
    }
}
