package tests.api.java.lang.ref;

import dalvik.annotation.TestTargets;
import dalvik.annotation.TestLevel;
import dalvik.annotation.TestTargetNew;
import dalvik.annotation.TestTargetClass;
import java.lang.ref.PhantomReference;
import java.lang.ref.Reference;
import java.lang.ref.ReferenceQueue;
import java.lang.ref.SoftReference;
import java.lang.ref.WeakReference;

@TestTargetClass(ReferenceQueue.class)
public class ReferenceQueueTest extends junit.framework.TestCase {

    static Boolean b;

    static Integer integer;

    boolean isThrown = false;

    protected void doneSuite() {
        b = null;
        integer = null;
    }

    public class ChildThread implements Runnable {

        public ChildThread() {
        }

        public void run() {
            try {
                rq.wait(1000);
            } catch (Exception e) {
            }
            synchronized (rq) {
                integer = new Integer(667);
                SoftReference sr = new SoftReference(integer, rq);
                sr.enqueue();
                rq.notify();
            }
        }
    }

    ReferenceQueue rq;

    /**
     * @tests java.lang.ref.ReferenceQueue#poll()
     */
    @TestTargetNew(level = TestLevel.COMPLETE, notes = "", method = "poll", args = {  })
    public void test_poll() {
        b = new Boolean(true);
        Object obj = new Object();
        String str = "Test";
        SoftReference sr = new SoftReference(b, rq);
        WeakReference wr = new WeakReference(obj, rq);
        PhantomReference pr = new PhantomReference(str, rq);
        assertNull(rq.poll());
        sr.enqueue();
        wr.enqueue();
        pr.enqueue();
        try {
            assertNull("Remove failed.", rq.poll().get());
        } catch (Exception e) {
            fail("Exception during the test : " + e.getMessage());
        }
        try {
            assertEquals("Remove failed.", obj, (rq.poll().get()));
        } catch (Exception e) {
            fail("Exception during the test : " + e.getMessage());
        }
        try {
            assertTrue("Remove failed.", ((Boolean) rq.poll().get()).booleanValue());
        } catch (Exception e) {
            fail("Exception during the test : " + e.getMessage());
        }
        assertNull(rq.poll());
        sr.enqueue();
        wr.enqueue();
        System.gc();
        System.runFinalization();
        assertNull(rq.poll());
    }

    /**
     * @tests java.lang.ref.ReferenceQueue#remove()
     */
    @TestTargetNew(level = TestLevel.COMPLETE, notes = "", method = "remove", args = {  })
    public void test_remove() {
        b = new Boolean(true);
        SoftReference sr = new SoftReference(b, rq);
        sr.enqueue();
        try {
            assertTrue("Remove failed.", ((Boolean) rq.remove().get()).booleanValue());
        } catch (Exception e) {
            fail("Exception during the test : " + e.getMessage());
        }
        assertNull(rq.poll());
        sr.enqueue();
        class RemoveThread extends Thread {

            public void run() {
                try {
                    rq.remove();
                } catch (InterruptedException ie) {
                    isThrown = true;
                }
            }
        }
        RemoveThread rt = new RemoveThread();
        rt.start();
        try {
            Thread.sleep(100);
        } catch (InterruptedException ie) {
        }
        rt.interrupt();
        try {
            Thread.sleep(100);
        } catch (InterruptedException ie) {
        }
        assertTrue(isThrown);
        assertNull(rq.poll());
    }

    /**
     * @tests java.lang.ref.ReferenceQueue#remove(long)
     */
    @TestTargetNew(level = TestLevel.COMPLETE, notes = "", method = "remove", args = { long.class })
    public void test_removeJ() {
        try {
            assertNull("Queue should be empty. (poll)", rq.poll());
            assertNull("Queue should be empty. (remove(1))", rq.remove((long) 1));
            Thread ct = new Thread(new ChildThread());
            ct.start();
            Reference ret = rq.remove(0L);
            assertNotNull("Delayed remove failed.", ret);
        } catch (InterruptedException e) {
            fail("InterruptedExeException during test : " + e.getMessage());
        } catch (Exception e) {
            fail("Exception during test : " + e.getMessage());
        }
        Object obj = new Object();
        WeakReference wr = new WeakReference(obj, rq);
        Boolean b = new Boolean(true);
        SoftReference sr = new SoftReference(b, rq);
        String str = "Test";
        PhantomReference pr = new PhantomReference(str, rq);
        pr.enqueue();
        wr.enqueue();
        sr.enqueue();
        try {
            Reference result = rq.remove(1L);
            assertTrue((Boolean) result.get());
            result = rq.remove(1L);
            assertEquals(obj, result.get());
            result = rq.remove(1L);
            assertNull(result.get());
        } catch (IllegalArgumentException e1) {
            fail("IllegalArgumentException was thrown.");
        } catch (InterruptedException e1) {
            fail("InterruptedException was thrown.");
        }
        rq = new ReferenceQueue();
        isThrown = false;
        assertNull(rq.poll());
        class RemoveThread extends Thread {

            public void run() {
                try {
                    rq.remove(1000L);
                } catch (InterruptedException ie) {
                    isThrown = true;
                }
            }
        }
        RemoveThread rt = new RemoveThread();
        rt.start();
        try {
            Thread.sleep(10);
        } catch (InterruptedException ie) {
        }
        rt.interrupt();
        try {
            Thread.sleep(10);
        } catch (InterruptedException ie) {
        }
        assertTrue(isThrown);
        assertNull(rq.poll());
        try {
            rq.remove(-1);
            fail("IllegalArgumentException expected.");
        } catch (IllegalArgumentException iae) {
        } catch (InterruptedException e) {
            fail("Unexpected InterruptedException.");
        }
    }

    /**
     * @tests java.lang.ref.ReferenceQueue#ReferenceQueue()
     */
    @TestTargetNew(level = TestLevel.COMPLETE, notes = "", method = "ReferenceQueue", args = {  })
    public void test_Constructor() {
        ReferenceQueue rq = new ReferenceQueue();
        assertNull(rq.poll());
        try {
            rq.remove(100L);
        } catch (InterruptedException e) {
            fail("InterruptedException was thrown.");
        }
    }

    protected void setUp() {
        rq = new ReferenceQueue();
    }

    protected void tearDown() {
    }
}
