package tests.api.java.util.concurrent;

import dalvik.annotation.BrokenTest;
import junit.framework.*;

public class ThreadTest extends JSR166TestCase {

    public static void main(String[] args) {
        junit.textui.TestRunner.run(suite());
    }

    public static Test suite() {
        return new TestSuite(ThreadTest.class);
    }

    static class MyHandler implements Thread.UncaughtExceptionHandler {

        public void uncaughtException(Thread t, Throwable e) {
            e.printStackTrace();
        }
    }

    /**
     * getUncaughtExceptionHandler returns ThreadGroup unless set,
     * otherwise returning value of last setUncaughtExceptionHandler.
     */
    public void testGetAndSetUncaughtExceptionHandler() {
        Thread current = Thread.currentThread();
        ThreadGroup tg = current.getThreadGroup();
        MyHandler eh = new MyHandler();
        assertEquals(tg, current.getUncaughtExceptionHandler());
        current.setUncaughtExceptionHandler(eh);
        assertEquals(eh, current.getUncaughtExceptionHandler());
        current.setUncaughtExceptionHandler(null);
        assertEquals(tg, current.getUncaughtExceptionHandler());
    }

    @BrokenTest("Different behavior between run-core-tests and CTS")
    public void testGetAndSetDefaultUncaughtExceptionHandler() {
        assertEquals(null, Thread.getDefaultUncaughtExceptionHandler());
        try {
            Thread current = Thread.currentThread();
            ThreadGroup tg = current.getThreadGroup();
            MyHandler eh = new MyHandler();
            Thread.setDefaultUncaughtExceptionHandler(eh);
            assertEquals(eh, Thread.getDefaultUncaughtExceptionHandler());
            Thread.setDefaultUncaughtExceptionHandler(null);
        } catch (SecurityException ok) {
        }
        assertEquals(null, Thread.getDefaultUncaughtExceptionHandler());
    }
}
