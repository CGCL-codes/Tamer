package org.jscsi.scsi.tasks.management;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import org.apache.log4j.Logger;
import org.jscsi.core.scsi.Status;
import org.jscsi.scsi.protocol.sense.KCQ;
import org.jscsi.scsi.protocol.sense.SenseData;
import org.jscsi.scsi.protocol.sense.SenseDataFactory;
import org.jscsi.scsi.tasks.Task;
import org.jscsi.scsi.transport.Nexus;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class DefaultTaskSetTest extends AbstractTaskSetTest {

    private static Logger _logger = Logger.getLogger(DefaultTaskSetTest.class);

    public static final int MANAGER_THREAD_COUNT = 10;

    public static final int SET_QUEUE_DEPTH = 10;

    public static final int LIMITING_SET_QUEUE_DEPTH = 5;

    @Before
    public void setUp() throws Exception {
    }

    @After
    public void tearDown() throws Exception {
    }

    private static void waitForTask(TestTask task, int loginterval) throws InterruptedException {
        synchronized (task) {
            _logger.debug("Testing framework waiting on task: " + task);
            synchronized (task) {
                while (!task.isDone()) {
                    _logger.debug("testing framework waiting... (" + task + ")");
                    task.wait(loginterval);
                }
            }
            _logger.debug("Testing framework found task is now complete: " + task);
        }
    }

    private static void executeTaskSet(List<TestTask> taskSet) throws InterruptedException {
        TaskSet set = new DefaultTaskSet(SET_QUEUE_DEPTH);
        DefaultTaskManager manager = new DefaultTaskManager(MANAGER_THREAD_COUNT, set);
        for (Task t : taskSet) {
            set.offer(t);
        }
        Thread thread = new Thread(manager);
        _logger.debug("Starting task manager: " + manager);
        thread.start();
        for (TestTask task : taskSet) {
            waitForTask(task, 100);
        }
        manager.shutdown();
        thread.join();
    }

    private static void checkTaskSet(List<TestTask> taskSet) {
        for (int i = 0; i < taskSet.size(); i++) {
            TestTask t = taskSet.get(i);
            if (!t.isDone()) {
                fail("Task " + i + " not executed: " + t.getClass().getName());
            } else {
                assertTrue("Task " + i + " failed (" + t.isProper() + "): " + t.reason() + ": " + t.getClass().getName(), t.isProper());
            }
        }
    }

    @Test
    public void testStaticInsertion_HSO() throws InterruptedException {
        List<TestTask> taskSet = new ArrayList<TestTask>();
        Nexus nexus = new Nexus("initiator", "target", 0);
        new HeadOfQueueTask(new Nexus(nexus, 0), taskSet, 0);
        new SimpleTask(new Nexus(nexus, 1), taskSet, 0);
        new OrderedTask(new Nexus(nexus, 2), taskSet, 100);
        executeTaskSet(taskSet);
        checkTaskSet(taskSet);
    }

    @Test
    public void testStaticInsertion_HSSO() throws InterruptedException {
        List<TestTask> taskSet = new ArrayList<TestTask>();
        Nexus nexus = new Nexus("initiator", "target", 0);
        new HeadOfQueueTask(new Nexus(nexus, 0), taskSet, 0);
        new SimpleTask(new Nexus(nexus, 1), taskSet, 0);
        new SimpleTask(new Nexus(nexus, 2), taskSet, 0);
        new OrderedTask(new Nexus(nexus, 3), taskSet, 100);
        executeTaskSet(taskSet);
        checkTaskSet(taskSet);
    }

    @Test
    public void testStaticInsertion_HOOS() throws InterruptedException {
        List<TestTask> taskSet = new ArrayList<TestTask>();
        Nexus nexus = new Nexus("initiator", "target", 0);
        new HeadOfQueueTask(new Nexus(nexus, 0), taskSet, 0);
        new OrderedTask(new Nexus(nexus, 1), taskSet, 0);
        new OrderedTask(new Nexus(nexus, 2), taskSet, 0);
        new SimpleTask(new Nexus(nexus, 3), taskSet, 100);
        executeTaskSet(taskSet);
        checkTaskSet(taskSet);
    }

    @Test
    public void testStaticInsertion_HHSS() throws InterruptedException {
        List<TestTask> taskSet = new ArrayList<TestTask>();
        Nexus nexus = new Nexus("initiator", "target", 0);
        new HeadOfQueueTask(new Nexus(nexus, 0), taskSet, 0);
        new HeadOfQueueTask(new Nexus(nexus, 1), taskSet, 0);
        new SimpleTask(new Nexus(nexus, 2), taskSet, 0);
        new SimpleTask(new Nexus(nexus, 3), taskSet, 100);
        executeTaskSet(taskSet);
        checkTaskSet(taskSet);
    }

    @Test
    public void testStaticInsertion_SOSSOHH() throws InterruptedException {
        List<TestTask> taskSet = new ArrayList<TestTask>();
        Nexus nexus = new Nexus("initiator", "target", 0);
        new SimpleTask(new Nexus(nexus, 0), taskSet, 0);
        new OrderedTask(new Nexus(nexus, 1), taskSet, 0);
        new SimpleTask(new Nexus(nexus, 2), taskSet, 100);
        new SimpleTask(new Nexus(nexus, 3), taskSet, 50);
        new OrderedTask(new Nexus(nexus, 4), taskSet, 0);
        new HeadOfQueueTask(new Nexus(nexus, 5), taskSet, 0);
        new HeadOfQueueTask(new Nexus(nexus, 6), taskSet, 100);
        executeTaskSet(taskSet);
        checkTaskSet(taskSet);
    }

    @Test
    public void testStaticInsertion_OSOOHH() throws InterruptedException {
        List<TestTask> taskSet = new ArrayList<TestTask>();
        Nexus nexus = new Nexus("initiator", "target", 0);
        new OrderedTask(new Nexus(nexus, 0), taskSet, 0);
        new SimpleTask(new Nexus(nexus, 1), taskSet, 0);
        new OrderedTask(new Nexus(nexus, 2), taskSet, 0);
        new OrderedTask(new Nexus(nexus, 3), taskSet, 0);
        new HeadOfQueueTask(new Nexus(nexus, 4), taskSet, 0);
        new HeadOfQueueTask(new Nexus(nexus, 5), taskSet, 100);
        executeTaskSet(taskSet);
        checkTaskSet(taskSet);
    }

    @Test
    public void testStaticInsertion_SOSSO() throws InterruptedException {
        List<TestTask> taskSet = new ArrayList<TestTask>();
        Nexus nexus = new Nexus("initiator", "target", 0);
        new SimpleTask(new Nexus(nexus, 0), taskSet, 0);
        new OrderedTask(new Nexus(nexus, 1), taskSet, 0);
        new SimpleTask(new Nexus(nexus, 2), taskSet, 0);
        new SimpleTask(new Nexus(nexus, 3), taskSet, 0);
        new OrderedTask(new Nexus(nexus, 4), taskSet, 100);
        executeTaskSet(taskSet);
        checkTaskSet(taskSet);
    }

    @Test
    public void testStaticInsertion_OSSO() throws InterruptedException {
        List<TestTask> taskSet = new ArrayList<TestTask>();
        Nexus nexus = new Nexus("initiator", "target", 0);
        new OrderedTask(new Nexus(nexus, 0), taskSet, 0);
        new SimpleTask(new Nexus(nexus, 1), taskSet, 0);
        new SimpleTask(new Nexus(nexus, 2), taskSet, 0);
        new OrderedTask(new Nexus(nexus, 3), taskSet, 100);
        executeTaskSet(taskSet);
        checkTaskSet(taskSet);
    }

    @Test
    public void testStaticInsertion_SSO() throws InterruptedException {
        List<TestTask> taskSet = new ArrayList<TestTask>();
        Nexus nexus = new Nexus("initiator", "target", 0);
        new SimpleTask(new Nexus(nexus, 0), taskSet, 0);
        new SimpleTask(new Nexus(nexus, 1), taskSet, 0);
        new OrderedTask(new Nexus(nexus, 2), taskSet, 100);
        executeTaskSet(taskSet);
        checkTaskSet(taskSet);
    }

    @Test
    public void testDynamicInsertion_H1S2H3S4_34() throws InterruptedException {
        List<TestTask> taskSet = new ArrayList<TestTask>();
        Nexus nexus = new Nexus("initiator", "target", 0);
        TestTask h1 = new HeadOfQueueTask(new Nexus(nexus, 1), taskSet, 4000);
        TestTask s2 = new SimpleTask(new Nexus(nexus, 2), taskSet, 4000);
        TestTask h3 = new HeadOfQueueTask(new Nexus(nexus, 3), taskSet, 500);
        TestTask s4 = new SimpleTask(new Nexus(nexus, 4), taskSet, 100);
        TaskSet set = new DefaultTaskSet(SET_QUEUE_DEPTH);
        TaskManager manager = new DefaultTaskManager(MANAGER_THREAD_COUNT, set);
        Thread thread = new Thread(manager);
        _logger.debug("Starting task manager with empty task set: " + manager);
        thread.start();
        Thread.sleep(1000);
        set.offer(h1);
        set.offer(s2);
        Thread.sleep(100);
        assertTrue("H1 finished too quickly", !h1.isDone());
        assertTrue("S2 finished too quickly", !s2.isDone());
        set.offer(h3);
        set.offer(s4);
        waitForTask(h3, 500);
        assertTrue("H1 finished too quickly", !h1.isDone());
        assertTrue("S2 finished too quickly", !s2.isDone());
        assertTrue("H3 finished improperly", h3.isProper());
        assertTrue("S4 finished too quickly", !s4.isDone());
        waitForTask(h1, 500);
        assertTrue("H1 finished improperly", h1.isProper());
        assertTrue("S2 finished too quickly", !s2.isDone());
        assertTrue("S4 finished too quickly", !s4.isDone());
        waitForTask(s4, 500);
        assertTrue("S2 finished too quickly", !s2.isDone());
        assertTrue("S4 finished improperly", s4.isProper());
        waitForTask(s2, 500);
        assertTrue("S2 finished improperly", s2.isProper());
        thread.interrupt();
        thread.join();
    }

    @Test
    public void testDynamicInsertion_H1S2H3S4_35() throws InterruptedException {
        List<TestTask> taskSet = new ArrayList<TestTask>();
        Nexus nexus = new Nexus("initiator", "target", 0);
        TestTask h1 = new HeadOfQueueTask(new Nexus(nexus, 1), taskSet, 2000);
        TestTask s2 = new SimpleTask(new Nexus(nexus, 2), taskSet, 1000);
        TestTask h3 = new HeadOfQueueTask(new Nexus(nexus, 3), taskSet, 4000);
        TestTask s4 = new SimpleTask(new Nexus(nexus, 4), taskSet, 100);
        TaskSet set = new DefaultTaskSet(SET_QUEUE_DEPTH);
        TaskManager manager = new DefaultTaskManager(MANAGER_THREAD_COUNT, set);
        Thread thread = new Thread(manager);
        _logger.debug("Starting task manager with empty task set: " + manager);
        thread.start();
        Thread.sleep(1000);
        set.offer(h1);
        set.offer(s2);
        Thread.sleep(100);
        assertTrue("H1 finished too quickly", !h1.isDone());
        assertTrue("S2 finished too quickly", !s2.isDone());
        set.offer(h3);
        set.offer(s4);
        waitForTask(h1, 500);
        assertTrue("H1 finished improperly", h1.isProper());
        assertTrue("S2 finished too quickly", !s2.isDone());
        assertTrue("H3 finished too quickly", !h3.isDone());
        assertTrue("S4 finished too quickly", !s4.isDone());
        waitForTask(s2, 500);
        assertTrue("S2 finished improperly", s2.isProper());
        assertTrue("H3 finished too quickly", !h3.isDone());
        assertTrue("S4 finished too quickly", !s4.isDone());
        waitForTask(h3, 500);
        assertTrue("H3 finished improperly", h3.isProper());
        assertTrue("S4 finished too quickly", !s4.isDone());
        waitForTask(s4, 500);
        assertTrue("S4 finished improperly", s4.isProper());
        thread.interrupt();
        thread.join();
    }

    @Test
    public void testDynamicInsertion_S1O2S3S4O5_36() throws InterruptedException {
        List<TestTask> taskSet = new ArrayList<TestTask>();
        Nexus nexus = new Nexus("initiator", "target", 0);
        TestTask s1 = new SimpleTask(new Nexus(nexus, 1), taskSet, 1000);
        TestTask o2 = new OrderedTask(new Nexus(nexus, 2), taskSet, 1000);
        TestTask s3 = new SimpleTask(new Nexus(nexus, 3), taskSet, 1000);
        TestTask s4 = new SimpleTask(new Nexus(nexus, 4), taskSet, 500);
        TestTask o5 = new OrderedTask(new Nexus(nexus, 5), taskSet, 500);
        TaskSet set = new DefaultTaskSet(SET_QUEUE_DEPTH);
        TaskManager manager = new DefaultTaskManager(MANAGER_THREAD_COUNT, set);
        Thread thread = new Thread(manager);
        _logger.debug("Starting task manager with empty task set: " + manager);
        thread.start();
        Thread.sleep(1000);
        set.offer(s1);
        set.offer(o2);
        Thread.sleep(100);
        assertTrue("S1 finished too quickly", !s1.isDone());
        assertTrue("O2 finished too quickly", !o2.isDone());
        set.offer(s3);
        set.offer(s4);
        set.offer(o5);
        waitForTask(s1, 500);
        assertTrue("S1 finished improperly", s1.isProper());
        assertTrue("O2 finished too quickly", !o2.isDone());
        assertTrue("S3 finished too quickly", !s3.isDone());
        assertTrue("S4 finished too quickly", !s4.isDone());
        assertTrue("O5 finished too quickly", !o5.isDone());
        waitForTask(o2, 500);
        assertTrue("O2 finished improperly", o2.isProper());
        assertTrue("S3 finished too quickly", !s3.isDone());
        assertTrue("S4 finished too quickly", !s4.isDone());
        assertTrue("O5 finished too quickly", !o5.isDone());
        waitForTask(s4, 500);
        assertTrue("S3 finished too quickly", !s3.isDone());
        assertTrue("S4 finished improperly", s4.isProper());
        assertTrue("O5 finished too quickly", !o5.isDone());
        waitForTask(s3, 500);
        assertTrue("S3 finished improperly", s3.isProper());
        assertTrue("O5 finished too quickly", !o5.isDone());
        waitForTask(o5, 500);
        assertTrue("O5 finished improperly", o5.isProper());
        thread.interrupt();
        thread.join();
    }

    @Test
    public void testQueueOverflow() throws InterruptedException {
        TestTargetTransportPort port = new TestTargetTransportPort(null, true);
        List<TestTask> taskSet = new ArrayList<TestTask>();
        Nexus nexus = new Nexus("initiator", "target", 0);
        TaskSet set = new DefaultTaskSet(LIMITING_SET_QUEUE_DEPTH);
        for (int i = 0; i < LIMITING_SET_QUEUE_DEPTH; i++) {
            set.offer(new SimpleTask(port, new Nexus(nexus, i), taskSet, 0));
        }
        boolean result = set.offer(new SimpleTask(port, new Nexus(nexus, LIMITING_SET_QUEUE_DEPTH + 1), taskSet, 0));
        assertTrue("Task set accepted too many tasks", !result);
        assertEquals("Task set did not report TASK SET FULL condition to transport", Status.TASK_SET_FULL, port.getLastStatus());
        assertEquals("Task set sent invalid sense data to transport", null, port.getSenseData());
    }

    private void testDuplicateTasks(long tag1, long tag2, boolean expectFailure) {
        TestTargetTransportPort port = new TestTargetTransportPort(null, true);
        TaskSet set = new DefaultTaskSet(LIMITING_SET_QUEUE_DEPTH);
        List<TestTask> taskSet = new ArrayList<TestTask>();
        Nexus nexus = new Nexus("initiator", "target", 0);
        Task one = new SimpleTask(port, new Nexus(nexus, tag1), taskSet, 0);
        Task two = new SimpleTask(port, new Nexus(nexus, tag2), taskSet, 0);
        boolean result = set.offer(one);
        assertTrue("Added first task failed", result);
        result = set.offer(two);
        if (result) {
            if (expectFailure) fail("Adding second task suceeded unexpectedly");
        } else {
            if (!expectFailure) fail("Adding second task failed unexpectedly");
            assertEquals("Task set did not report CHECK CONDITION to transport", Status.CHECK_CONDITION, port.getLastStatus());
            try {
                SenseData sense = (new SenseDataFactory()).decode(port.getSenseData());
                assertEquals("Task set did not return correct KCQ", KCQ.OVERLAPPED_COMMANDS_ATTEMPTED, sense.getKCQ());
            } catch (IOException e) {
                e.printStackTrace();
                fail("Could not decode sense data returned from task set");
            }
        }
    }

    @Test
    public void testDuplicateTaggedTasks() {
        Random rand = new Random();
        long tag = Math.abs(rand.nextLong());
        testDuplicateTasks(tag, tag, true);
    }

    @Test
    public void testNonDuplicateTaggedTasks() {
        Random rand = new Random();
        long tag1 = Math.abs(rand.nextLong());
        long tag2 = Math.abs(rand.nextLong());
        testDuplicateTasks(tag1, tag2, false);
    }

    @Test
    public void testDuplicateUntaggedTasks() {
        testDuplicateTasks(-1, -1, true);
    }
}
