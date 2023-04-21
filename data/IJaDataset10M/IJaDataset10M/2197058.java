package org.pixory.pxfoundation;

import junit.framework.TestCase;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class PXJobTestCase extends TestCase {

    private static final Log LOG = LogFactory.getLog(PXJobTestCase.class);

    public PXJobTestCase(String name) {
        super(name);
    }

    public void testValidation() throws NoSuchMethodException {
        Tasks aTarget = new Tasks();
        try {
            PXJob aJob = new PXJob(aTarget, "task0", null, Thread.NORM_PRIORITY);
            fail("should have thrown NoSuchMethodException");
        } catch (NoSuchMethodException e) {
        }
        PXJob aJob = new PXJob(aTarget, "task1", null, Thread.NORM_PRIORITY);
        Object[] someArgs = new Object[2];
        someArgs[0] = "string0";
        someArgs[1] = "string1";
        aJob = new PXJob(aTarget, "spinCheck", someArgs, Thread.NORM_PRIORITY);
    }

    public void testSpinCheck() throws NoSuchMethodException, InterruptedException {
        Tasks aTarget = new Tasks();
        Object[] someArgs = new Object[2];
        someArgs[0] = "string0";
        someArgs[1] = "string1";
        PXJob aJob = new PXJob(aTarget, "spinCheck", someArgs, Thread.NORM_PRIORITY);
        assertTrue(aJob.getStatus() == PXJob.NOT_STARTED);
        aJob.start();
        while (aJob.getStatus() != PXJob.DONE) {
            Thread.sleep(10);
        }
        assertTrue(aJob.getStatus() == PXJob.DONE);
    }

    public void testStates() throws NoSuchMethodException, InterruptedException {
        Tasks aTarget = new Tasks();
        Object[] someArgs = new Object[2];
        someArgs[0] = "string0";
        someArgs[1] = "string1";
        PXJob aJob = new PXJob(aTarget, "spinCheck", someArgs, Thread.NORM_PRIORITY);
        assertTrue(aJob.getStatus() == PXJob.NOT_STARTED);
        aJob.start();
        while (aJob.getStatus() != PXJob.DONE) {
            Thread.sleep(10);
        }
        assertTrue(aJob.getStatus() == PXJob.DONE);
    }

    public void testSuspend() throws NoSuchMethodException, InterruptedException {
        Tasks aTarget = new Tasks();
        Object[] someArgs = new Object[1];
        someArgs[0] = new Integer(100);
        PXJob aJob = new PXJob(aTarget, "log", someArgs, Thread.NORM_PRIORITY);
        assertTrue(aJob.getStatus() == PXJob.NOT_STARTED);
        aJob.start();
        while (aJob.getStatus() != PXJob.RUNNING) {
            Thread.sleep(10);
        }
        assertTrue(aJob.getStatus() == PXJob.RUNNING);
        Thread.sleep(200);
        aJob.suspend();
        assertTrue(aJob.getStatus() == PXJob.SUSPENDED);
        Thread.sleep(500);
        aJob.resume();
        while (aJob.getStatus() == PXJob.RUNNING) {
            Thread.sleep(10);
        }
        assertTrue(aJob.getStatus() == PXJob.DONE);
    }

    public void testTimedSuspend() throws NoSuchMethodException, InterruptedException {
        Tasks aTarget = new Tasks();
        Object[] someArgs = new Object[1];
        someArgs[0] = new Integer(50);
        PXJob aJob = new PXJob(aTarget, "log", someArgs, Thread.NORM_PRIORITY);
        assertTrue(aJob.getStatus() == PXJob.NOT_STARTED);
        aJob.start();
        while (aJob.getStatus() != PXJob.RUNNING) {
            Thread.sleep(10);
        }
        aJob.suspend(400);
        assertTrue(aJob.getStatus() == PXJob.SUSPENDED);
        while (aJob.getStatus() == PXJob.SUSPENDED) {
            Thread.sleep(10);
        }
        while (aJob.getStatus() == PXJob.RUNNING) {
            Thread.sleep(10);
        }
        PXJob.Status aStatus = aJob.getStatus();
        assertTrue(aJob.getStatus() == PXJob.DONE);
    }

    public void testTimedResume() throws NoSuchMethodException, InterruptedException {
        Tasks aTarget = new Tasks();
        Object[] someArgs = new Object[1];
        someArgs[0] = new Integer(50);
        PXJob aJob = new PXJob(aTarget, "log", someArgs, Thread.NORM_PRIORITY);
        assertTrue(aJob.getStatus() == PXJob.NOT_STARTED);
        aJob.start();
        aJob.suspend(100000);
        assertTrue(aJob.getStatus() == PXJob.SUSPENDED);
        Thread.sleep(300);
        assertTrue(aJob.getStatus() == PXJob.SUSPENDED);
        aJob.resume();
        while (aJob.getStatus() == PXJob.RUNNING) {
            Thread.sleep(10);
        }
        assertTrue(aJob.getStatus() == PXJob.DONE);
    }

    public void testStop() throws NoSuchMethodException, InterruptedException {
        Tasks aTarget = new Tasks();
        PXJob aJob = new PXJob(aTarget, "infiniteLoop", null, Thread.NORM_PRIORITY);
        assertTrue(aJob.getStatus() == PXJob.NOT_STARTED);
        aJob.start();
        while (aJob.getStatus() != PXJob.RUNNING) {
            Thread.sleep(10);
        }
        assertTrue(aJob.getStatus() == PXJob.RUNNING);
        Thread.sleep(200);
        aJob.suspend();
        Thread.sleep(100);
        aJob.stop();
        assertTrue(aJob.getStatus() == PXJob.STOPPED);
        aJob = new PXJob(aTarget, "infiniteLoop", null, Thread.NORM_PRIORITY);
        assertTrue(aJob.getStatus() == PXJob.NOT_STARTED);
        aJob.start();
        while (aJob.getStatus() != PXJob.RUNNING) {
            Thread.sleep(10);
        }
        assertTrue(aJob.getStatus() == PXJob.RUNNING);
        Thread.sleep(200);
        aJob.stop();
        assertTrue(aJob.getStatus() == PXJob.STOPPED);
    }

    public void testAbort() throws NoSuchMethodException, InterruptedException {
        Tasks aTarget = new Tasks();
        PXJob aJob = new PXJob(aTarget, "abortTest", null, Thread.NORM_PRIORITY);
        assertTrue(aJob.getStatus() == PXJob.NOT_STARTED);
        aJob.start();
        while (aJob.getStatus() != PXJob.RUNNING) {
            Thread.sleep(10);
        }
        while (aJob.getStatus() == PXJob.RUNNING) {
            Thread.sleep(10);
        }
        assertTrue(aJob.getStatus() == PXJob.ABORTED);
        Throwable anException = aJob.getException();
        assertTrue(anException instanceof NullPointerException);
    }

    public void testReturn() throws NoSuchMethodException, InterruptedException {
        Tasks aTarget = new Tasks();
        PXJob aJob = new PXJob(aTarget, "returnTest", null, Thread.NORM_PRIORITY);
        assertTrue(aJob.getStatus() == PXJob.NOT_STARTED);
        aJob.start();
        while (aJob.getStatus() == PXJob.RUNNING) {
            Thread.sleep(10);
        }
        assertTrue(aJob.getStatus() == PXJob.DONE);
        assertEquals("hello", aJob.getReturnValue());
    }

    private static class Tasks {

        public void task0() {
        }

        public void task1(PXJob.StatusCheck statusCheck) {
        }

        public void spinCheck(PXJob.StatusCheck statusCheck, String arg0, String arg1) throws InterruptedException {
            for (int i = 0; i < 100; i++) {
                statusCheck.check();
            }
        }

        public void log(PXJob.StatusCheck statusCheck, Integer logCount) throws InterruptedException {
            for (int i = 0; i < logCount.intValue(); i++) {
                statusCheck.check();
                LOG.debug("count: " + i);
                Thread.sleep(10);
            }
        }

        public void infiniteLoop(PXJob.StatusCheck statusCheck) throws InterruptedException {
            while (true) {
                statusCheck.check();
                LOG.debug("running...");
                Thread.sleep(10);
            }
        }

        public void abortTest(PXJob.StatusCheck statusCheck) throws InterruptedException {
            for (int i = 0; i < 10; i++) {
                statusCheck.check();
                LOG.debug("running...");
                Thread.sleep(10);
                if (i == 5) {
                    Object var = new Object();
                    var = null;
                    var.toString();
                }
            }
        }

        public String returnTest(PXJob.StatusCheck statusCheck) throws InterruptedException {
            for (int i = 0; i < 10; i++) {
                statusCheck.check();
                LOG.debug("running...");
                Thread.sleep(10);
            }
            return "hello";
        }
    }
}
