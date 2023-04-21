package net.sourceforge.fraglets.swing;

import java.awt.Toolkit;
import java.awt.event.InvocationEvent;
import java.util.Iterator;
import java.util.PriorityQueue;

/** A queue of runnables, to be run in the swing event loop at a designated time. */
public class RunnableQueue {

    /**
     * Queue entries, which are made as ready-to-post events. Sorting order is
     * run time, ascending.
     */
    private static class QueueEntry extends InvocationEvent implements Comparable<QueueEntry> {

        private static final long serialVersionUID = -1406463426385873379L;

        private final long timestamp;

        private final Toolkit toolkit;

        public QueueEntry(final Toolkit source, final Runnable runnable, final long timestamp) {
            super(source, runnable);
            this.toolkit = source;
            this.timestamp = timestamp;
        }

        public int compareTo(final QueueEntry other) {
            final long delta = this.timestamp - other.timestamp;
            if (delta < 0) {
                return -1;
            } else if (delta > 0) {
                return 1;
            } else {
                return 0;
            }
        }

        protected void postEvent() {
            toolkit.getSystemEventQueue().postEvent(this);
        }

        public long getTimestamp() {
            return timestamp;
        }
    }

    /**
     * The queue of pre-created events, sorted by run time.
     */
    private static PriorityQueue<QueueEntry> eventQueue = new PriorityQueue<QueueEntry>();

    /**
     * Inter-thread synchronization lock.
     */
    private static Object lock = new Object();

    /**
     * The dequeue thread when running.
     */
    private static Thread dequeueThread;

    /**
     * Invoke the given runnable from the swing event queue, delay milliseconds
     * after the latest current entry in the queue.
     * 
     * @param runnable
     *            Runnable to run.
     * @param delay
     *            delay in milliseconds to add to latest entry.
     */
    public static void invokeAfter(final Runnable runnable, final int delay) {
        synchronized (lock) {
            long last = System.currentTimeMillis();
            for (final Iterator<QueueEntry> i = eventQueue.iterator(); i.hasNext(); ) {
                final long timestamp = i.next().getTimestamp();
                if (timestamp > last) {
                    last = timestamp;
                }
            }
            invokeLater(runnable, last + delay);
        }
    }

    /**
     * Invoke the given runnable from the swing event queue, after delay
     * milliseconds.
     * 
     * @param runnable
     *            Runnable to run.
     * @param delay
     *            delay in milliseconds from now.
     */
    public static void invokeDelayed(final Runnable runnable, final int delay) {
        invokeLater(runnable, System.currentTimeMillis() + delay);
    }

    /**
     * Invoke the given runnable from the swing event queue, at the specified
     * time. If the time has already passed, the runnable will be run as soon as
     * the swing event queue is processed.
     * 
     * @param runnable
     *            Runnable to run.
     * @param time
     *            the time when to run <var>runnable</var>.
     */
    public static void invokeLater(final Runnable runnable, final long time) {
        enqueue(new QueueEntry(Toolkit.getDefaultToolkit(), runnable, time));
    }

    /**
     * Dequeue loop, wait for next event due and then post it.
     */
    private static void dequeue() {
        try {
            synchronized (lock) {
                while (!Thread.interrupted()) {
                    final long delay = getDelay();
                    if (delay >= 0) {
                        lock.wait(delay);
                    } else {
                        eventQueue.remove().postEvent();
                    }
                }
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    /**
     * Enqueue a new event, and start the dequeue thread if necessary.
     * 
     * @param entry
     *            the event to enqueue.
     */
    private static void enqueue(final QueueEntry entry) {
        synchronized (lock) {
            eventQueue.add(entry);
            if (dequeueThread == null || !dequeueThread.isAlive()) {
                dequeueThread = new Thread() {

                    public void run() {
                        dequeue();
                    }
                };
                dequeueThread.start();
            } else {
                lock.notify();
            }
        }
    }

    /**
     * Get the delay until the next event is due to be posted.
     * 
     * @return milliseconds until next event due, or 0 if no event pending, or
     *         -1 if the next event is due now.
     */
    private static long getDelay() {
        final QueueEntry nextEvent = eventQueue.peek();
        if (nextEvent == null) {
            return 0;
        } else {
            final long now = System.currentTimeMillis();
            final long timestamp = nextEvent.getTimestamp();
            if (timestamp <= now) {
                return -1;
            } else {
                return timestamp - now;
            }
        }
    }
}
