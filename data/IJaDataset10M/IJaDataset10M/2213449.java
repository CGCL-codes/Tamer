package org.mobicents.media.server.scheduler;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author kulikov
 */
public class TaskChainTest implements TaskChainListener {

    private Clock clock = new DefaultClock();

    private Scheduler scheduler;

    private MyTestTask task1, task2, task3;

    private TaskChain taskChain = new TaskChain(3);

    private int count;

    private long t[] = new long[3];

    private boolean done = false;

    private boolean failure = false;

    private boolean throwsException = false;

    public TaskChainTest() {
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }

    @Before
    public void setUp() {
        scheduler = new Scheduler(4);
        scheduler.setClock(clock);
        scheduler.start();
        task1 = new MyTestTask(scheduler, clock.getTime() + 1000000000L);
        task2 = new MyTestTask(scheduler, clock.getTime() + 1000000000L);
        task3 = new MyTestTask(scheduler, clock.getTime() + 1000000000L);
        count = 0;
    }

    @After
    public void tearDown() {
        scheduler.stop();
    }

    /**
     * Test of add method, of class TaskChain.
     */
    @Test
    public void testNormalExexcution() throws InterruptedException {
        taskChain.setListener(this);
        taskChain.add(task1, 0);
        taskChain.add(task2, 1000000000L);
        taskChain.add(task3, 1000000000L);
        scheduler.submit(taskChain);
        Thread.sleep(4000);
        assertEquals(3, count);
        assertEquals(1000000000L, t[1] - t[0], 100000000L);
        assertEquals(1000000000L, t[2] - t[1], 100000000L);
        assertTrue("Done", done);
    }

    @Test
    public void testException() throws InterruptedException {
        this.throwsException = true;
        taskChain.setListener(this);
        taskChain.add(task1, 0);
        taskChain.add(task2, 1000000000L);
        taskChain.add(task3, 1000000000L);
        scheduler.submit(taskChain);
        Thread.sleep(4000);
        assertFalse("Done", done);
        assertTrue("Failure", failure);
    }

    public void onTermination() {
        done = true;
    }

    public void onException(Exception e) {
        this.failure = true;
    }

    private class MyTestTask extends Task {

        private long priority;

        private long duration;

        private volatile boolean isActive = true;

        private volatile boolean isFailed = false;

        public MyTestTask(Scheduler scheduler, long d) {
            super(scheduler);
            setDeadLine(d);
        }

        public long getPriority() {
            return this.priority;
        }

        public void setPriority(long priority) {
            this.priority = priority;
        }

        public boolean isActive() {
            return this.isActive;
        }

        public long getDuration() {
            return this.duration;
        }

        public long perform() {
            if (throwsException) {
                throw new IllegalStateException();
            }
            t[count] = clock.getTime();
            count++;
            return 0;
        }

        public boolean isFailed() {
            return this.isFailed;
        }
    }
}
