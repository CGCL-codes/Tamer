package org.curjent.impl.agent;

import static java.lang.Long.*;
import java.util.ArrayList;
import java.util.concurrent.Executor;
import java.util.concurrent.PriorityBlockingQueue;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Background task for expiring messages. Uses the controller's executor
 * ({@link Messengers#getExecutor()}) to run the task. The task only runs when
 * there are timers to process.
 */
final class ExpirationsTask implements Runnable {

    /**
	 * Owning controller. Foreground operations, such as enqueueing a new timer,
	 * assume the controller is locked. Background tasks, such as polling for
	 * enqueued timers, explicitly lock the controller.
	 */
    private final Controller controller;

    /**
	 * Synchronization monitor for timer feeds.
	 * 
	 * @see #feed
	 */
    private final Condition monitor;

    /**
	 * Starting, stopping, and restarting the background task is coordinated
	 * using {@link AtomicBoolean#compareAndSet(boolean, boolean)}.
	 */
    private final AtomicBoolean running = new AtomicBoolean();

    /**
	 * New timers from the controller are queued in a simple list and processed
	 * in the background. This defers the overhead of priority ordering to the
	 * background thread outside of the controller's lock. The controller is
	 * locked to add and pull new timers to the feed via {@link #timers}, but
	 * those timers are added to {@link #queue} in the background without
	 * locking the controller.
	 */
    private final ArrayList<ExpirationsTimer> feed = new ArrayList<ExpirationsTimer>();

    /**
	 * Enqueued timers from {@link #feed} are copied to this buffer while the
	 * controller is locked so that they can be processed outside of the lock.
	 * The {@link #awaitFeed(long)} expands this buffer as needed.
	 * <p>
	 * Thread safety is ensured by updating the buffer while the controller is
	 * locked and only reading from it when unlocked. Reading is safe since only
	 * one background executor thread accesses the buffer at a time. The JVM
	 * memory model ensures consistency across threads used by the controller's
	 * executor.
	 */
    private ExpirationsTimer[] timers = new ExpirationsTimer[10];

    /**
	 * Ordered queue of timers. A synchronized queue implementation is used to
	 * ensure consistency across executor threads. Only the background task
	 * accesses the queue. New timers are fed to the task via {@link #feed}.
	 */
    private final PriorityBlockingQueue<ExpirationsTimer> queue = new PriorityBlockingQueue<ExpirationsTimer>();

    /**
	 * Saves the controller and its lock for internal use.
	 */
    ExpirationsTask(Controller controller, ReentrantLock lock) {
        this.controller = controller;
        monitor = lock.newCondition();
    }

    /**
	 * Enqueues a new timer for expiration. Starts the background task if
	 * needed.
	 * <p>
	 * Called while the controller is locked.
	 */
    void enqueue(ExpirationsTimer timer) {
        feed.add(timer);
        if (running.compareAndSet(false, true)) {
            Executor executor = controller.messengers.getExecutor();
            executor.execute(this);
        } else {
            monitor.signal();
        }
    }

    /**
	 * Notifies the background task that a message has finished.
	 * <p>
	 * Called while the controller is locked.
	 */
    void signal() {
        monitor.signal();
    }

    /**
	 * Runs the background task for as long as there are more timers to process.
	 * Exits once all timers are processed. Waits on a condition of the
	 * controller's lock for timing out the next message.
	 */
    @Override
    public void run() {
        final Controller controller = this.controller;
        boolean running = true;
        while (running) {
            try {
                long timeout = processQueue();
                running = awaitFeed(timeout);
            } catch (Throwable e) {
                controller.handle(e);
            }
        }
    }

    /**
	 * Dequeues and expires messages. Also cleans up finished messages. Returns
	 * the amount of time the background task should wait before timing out the
	 * next message. Returns <code>Long.MAX_VALUE</code> if the queue is empty.
	 */
    private long processQueue() {
        final PriorityBlockingQueue<ExpirationsTimer> queue = this.queue;
        ExpirationsTimer timer = queue.peek();
        while (timer != null) {
            Message message = timer.message;
            if (message == null) {
                queue.remove();
            } else {
                long timeout = timer.timeout();
                if (timeout > 0) {
                    return timeout;
                }
                queue.remove();
                message.expire();
            }
            timer = queue.peek();
        }
        return MAX_VALUE;
    }

    /**
	 * Waits for new timers to queue, or for the given timeout period.
	 * Notifications for new timers are signaled via {@link #monitor}. The
	 * timeout is specified by {@link #processQueue()} and denotes how long the
	 * task should wait to expire the next message. A timeout value of
	 * <code>Long.MAX_VALUE</code> means the priority queue is empty.
	 * <p>
	 * Returns <code>true</code> if there are more timers in the queue to
	 * process. Sets {@link #running} to <code>false</code> and returns the same
	 * if there are no more timers, and the task should exit.
	 */
    private boolean awaitFeed(long timeout) throws InterruptedException {
        final ReentrantLock lock = controller.lock;
        final Condition monitor = this.monitor;
        final ArrayList<ExpirationsTimer> feed = this.feed;
        final AtomicBoolean running = this.running;
        final PriorityBlockingQueue<ExpirationsTimer> queue = this.queue;
        int count = 0;
        ExpirationsTimer[] timers = null;
        lock.lock();
        try {
            if (feed.size() == 0) {
                if (timeout == MAX_VALUE) {
                    running.set(false);
                    return false;
                }
                monitor.awaitNanos(timeout);
            }
            count = feed.size();
            if (count > 0) {
                timers = this.timers;
                if (count > timers.length) {
                    this.timers = timers = new ExpirationsTimer[count];
                }
                feed.toArray(timers);
                feed.clear();
            }
        } finally {
            lock.unlock();
        }
        if (timers != null) {
            for (int i = 0; i < count; i++) {
                ExpirationsTimer timer = timers[i];
                timers[i] = null;
                queue.add(timer);
            }
        }
        return true;
    }
}
