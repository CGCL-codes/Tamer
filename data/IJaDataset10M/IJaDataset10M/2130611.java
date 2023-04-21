package org.iqual.chaplin.example.variableInPath;

import junit.framework.AssertionFailedError;
import junit.framework.TestCase;
import org.iqual.chaplin.Role;
import java.util.Arrays;
import java.util.Random;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import static org.iqual.chaplin.DynaCastUtils.$;

/**
 * Created by IntelliJ IDEA.
 * User: zslajchrt
 * Date: Jan 4, 2010
 * Time: 7:42:34 PM
 * To change this template use File | Settings | File Templates.
 */
public class VariableInPathTest extends TestCase {

    private static final int NUM_THREADS = 10;

    public static class MockBase implements Protocol {

        int called;

        public void sendMessage(String message, String data) {
            called++;
        }
    }

    @Role(value = LeftHandler.class)
    public static class LeftMock extends MockBase {
    }

    @Role(value = RightHandler.class)
    public static class RightMock extends MockBase {
    }

    public void testRouting() {
        LeftMock left = new LeftMock();
        RightMock right = new RightMock();
        Processor proc = $(left, right, new Core());
        proc.processData("A");
        assertEquals(1, right.called);
        assertEquals(0, left.called);
        proc.processData("ABCDEF");
        assertEquals(1, right.called);
        assertEquals(1, left.called);
    }

    @Role(value = LeftHandler.class)
    public static class LeftMockForMT implements Protocol {

        public void sendMessage(String message, String data) {
            assertTrue(data.length() > 3);
        }
    }

    @Role(value = RightHandler.class)
    public static class RightMockForMT implements Protocol {

        public void sendMessage(String message, String data) {
            assertTrue(data.length() <= 3);
        }
    }

    private final Random rand = new Random();

    private int nextInt(int upperBound) {
        return rand.nextInt(upperBound);
    }

    public void testMultiThreads() throws Throwable {
        final AtomicBoolean loopCondition = new AtomicBoolean();
        final AtomicBoolean assertionFailure = new AtomicBoolean();
        final AtomicReference<Throwable> error = new AtomicReference<Throwable>();
        final CountDownLatch latch = new CountDownLatch(NUM_THREADS);
        final Lock lock = new ReentrantLock();
        final Condition startCondition = lock.newCondition();
        final Condition breakCondition = lock.newCondition();
        LeftMockForMT left = new LeftMockForMT();
        RightMockForMT right = new RightMockForMT();
        final Processor proc = $(left, right, new Core());
        lock.lock();
        try {
            for (int i = 0; i < NUM_THREADS; i++) {
                new Thread("T" + i) {

                    @Override
                    public void run() {
                        lock.lock();
                        try {
                        } finally {
                            lock.unlock();
                        }
                        try {
                            while (!loopCondition.get()) {
                                int length = nextInt(6);
                                char[] chars = new char[length];
                                Arrays.fill(chars, (char) (32 + nextInt(30)));
                                proc.processData(new String(chars));
                            }
                        } catch (AssertionFailedError e) {
                            assertionFailure.set(true);
                            loopCondition.set(true);
                        } catch (Throwable t) {
                            error.set(t);
                            loopCondition.set(true);
                        } finally {
                            lock.lock();
                            try {
                                breakCondition.signalAll();
                            } finally {
                                lock.unlock();
                            }
                        }
                        latch.countDown();
                    }
                }.start();
            }
            startCondition.signalAll();
            breakCondition.await(3, TimeUnit.SECONDS);
        } finally {
            lock.unlock();
        }
        loopCondition.set(true);
        latch.await();
        assertFalse(assertionFailure.get());
        if (error.get() != null) {
            throw error.get();
        }
    }
}
