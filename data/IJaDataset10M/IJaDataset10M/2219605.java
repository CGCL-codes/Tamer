package net.kano.joscar.ratelim;

import junit.framework.TestCase;

public class QueueRunnerTest extends TestCase {

    public void testDequeueOnce() {
        assertDequeued(1);
    }

    public void testDequeueMultiple() {
        assertDequeued(100);
    }

    public void testTimeout() throws InterruptedException {
        QueueRunner<MockEventQueue> runner = createAndClearQueue();
        assertTimesOut(runner);
    }

    public void testRestartAfterTimeout() throws InterruptedException {
        QueueRunner<MockEventQueue> runner = createAndClearQueue();
        assertTimesOut(runner);
        runner.setTimeout(50000);
        runner.getQueue().addEvents(1);
        assertTrue(runner.isRunning());
    }

    public void testTimesOutImmediately() throws InterruptedException {
        QueueRunner<MockEventQueue> runner = QueueRunner.create(new MockEventQueue(0));
        assertTimesOut(runner);
    }

    private void assertTimesOut(QueueRunner<MockEventQueue> runner) throws InterruptedException {
        assertTrue(runner.isRunning());
        forceTimeout(runner);
        assertFalse(runner.isRunning());
    }

    private QueueRunner<MockEventQueue> createAndClearQueue() {
        MockEventQueue queue = new MockEventQueue(1);
        QueueRunner<MockEventQueue> runner = QueueRunner.create(queue);
        runner.setTimeout(50000);
        queue.waitForEmpty(5000);
        return runner;
    }

    private void forceTimeout(QueueRunner<MockEventQueue> runner) throws InterruptedException {
        runner.setTimeout(0);
        for (int i = 0; i < 500 && runner.isRunning(); i++) {
            Thread.sleep(10);
        }
    }

    private void assertDequeued(int events) {
        MockEventQueue queue = new MockEventQueue(events);
        QueueRunner.create(queue);
        assertTrue(queue.waitForEmpty(5000));
    }

    private static class MockEventQueue extends AbstractFutureEventQueue {

        private final Object lock = new Object();

        private int toFlush;

        public MockEventQueue(int events) {
            toFlush = events;
        }

        public long flushQueues() {
            synchronized (lock) {
                if (toFlush > 0) {
                    toFlush--;
                }
                boolean empty = isEmpty();
                if (empty) {
                    lock.notifyAll();
                    return -1;
                } else {
                    return 1;
                }
            }
        }

        private synchronized boolean isEmpty() {
            return toFlush == 0;
        }

        public boolean waitForEmpty(long time) {
            synchronized (lock) {
                try {
                    lock.wait(time);
                } catch (InterruptedException e) {
                    throw new IllegalStateException(e);
                }
                return isEmpty();
            }
        }

        public boolean hasQueues() {
            return true;
        }

        public void addEvents(int events) {
            synchronized (this) {
                toFlush += events;
            }
            updateQueueRunners();
        }
    }
}
