package org.springframework.util;

import junit.framework.TestCase;

/**
 * @author Rod Johnson
 */
public class StopWatchTests extends TestCase {

    /**
	 * Are timings off in JUnit?
	 */
    public void testValidUsage() throws Exception {
        StopWatch sw = new StopWatch();
        long int1 = 166L;
        long int2 = 45L;
        String name1 = "Task 1";
        String name2 = "Task 2";
        long fudgeFactor = 5L;
        assertFalse(sw.isRunning());
        sw.start(name1);
        Thread.sleep(int1);
        assertTrue(sw.isRunning());
        sw.stop();
        sw.start(name2);
        Thread.sleep(int2);
        sw.stop();
        assertTrue(sw.getTaskCount() == 2);
        String pp = sw.prettyPrint();
        assertTrue(pp.indexOf(name1) != -1);
        assertTrue(pp.indexOf(name2) != -1);
        StopWatch.TaskInfo[] tasks = sw.getTaskInfo();
        assertTrue(tasks.length == 2);
        assertTrue(tasks[0].getTaskName().equals(name1));
        assertTrue(tasks[1].getTaskName().equals(name2));
        sw.toString();
    }

    public void testValidUsageNotKeepingTaskList() throws Exception {
        StopWatch sw = new StopWatch();
        sw.setKeepTaskList(false);
        long int1 = 166L;
        long int2 = 45L;
        String name1 = "Task 1";
        String name2 = "Task 2";
        long fudgeFactor = 5L;
        assertFalse(sw.isRunning());
        sw.start(name1);
        Thread.sleep(int1);
        assertTrue(sw.isRunning());
        sw.stop();
        sw.start(name2);
        Thread.sleep(int2);
        sw.stop();
        assertTrue(sw.getTaskCount() == 2);
        String pp = sw.prettyPrint();
        assertTrue(pp.indexOf("kept") != -1);
        sw.toString();
        try {
            sw.getTaskInfo();
            fail();
        } catch (UnsupportedOperationException ex) {
        }
    }

    public void testFailureToStartBeforeGettingTimings() {
        StopWatch sw = new StopWatch();
        try {
            sw.getLastTaskTimeMillis();
            fail("Can't get last interval if no tests run");
        } catch (IllegalStateException ex) {
        }
    }

    public void testFailureToStartBeforeStop() {
        StopWatch sw = new StopWatch();
        try {
            sw.stop();
            fail("Can't stop without starting");
        } catch (IllegalStateException ex) {
        }
    }

    public void testRejectsStartTwice() {
        StopWatch sw = new StopWatch();
        try {
            sw.start("");
            sw.stop();
            sw.start("");
            assertTrue(sw.isRunning());
            sw.start("");
            fail("Can't start twice");
        } catch (IllegalStateException ex) {
        }
    }
}
