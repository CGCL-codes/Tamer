package jconch.testing;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;
import static org.testng.Assert.fail;
import org.testng.annotations.Test;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Vector;
import java.util.concurrent.Callable;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Unit test for the {@link jconch.testing.Assert} class.
 *
 * @author Hamlet D'Arcy
 */
public class AssertTest {

    /**
	 * Tests that each task in the taskFactory is called 1000 times and
	 * no to tasks within the same iteration executes on the same thread. 
	 * @throws Exception
	 * 		on error
	 */
    @Test
    public void testAssertSynchronized_SuccessfulIteration() throws Exception {
        final AtomicInteger calledFactoryCount = new AtomicInteger(0);
        final Map<Integer, Long> iterationToThreadId1 = new ConcurrentHashMap<Integer, Long>();
        final Map<Integer, Long> iterationToThreadId2 = new ConcurrentHashMap<Integer, Long>();
        final AtomicInteger calledClosure1Count = new AtomicInteger(0);
        final AtomicInteger calledClosure2Count = new AtomicInteger(0);
        Assert.assertSynchronized(new Callable<List<Callable<Void>>>() {

            public List<Callable<Void>> call() throws Exception {
                calledFactoryCount.incrementAndGet();
                return Arrays.asList(new Callable<Void>() {

                    public Void call() throws Exception {
                        iterationToThreadId1.put(calledClosure1Count.incrementAndGet(), Thread.currentThread().getId());
                        return null;
                    }
                }, new Callable<Void>() {

                    public Void call() throws Exception {
                        iterationToThreadId2.put(calledClosure2Count.incrementAndGet(), Thread.currentThread().getId());
                        return null;
                    }
                });
            }
        });
        assertEquals(1000, calledFactoryCount.get(), "Default should have been 1000 taskFactory invocations");
        assertEquals(1000, calledFactoryCount.get(), "Default should have been 1000 task 1 invocations");
        assertEquals(1000, calledFactoryCount.get(), "Default should have been 1000 task 2 invocations");
        for (int x = 1; x <= 1000; x++) {
            final Long firstThreadID = iterationToThreadId1.get(x);
            final Long secondThreadID = iterationToThreadId2.get(x);
            assertFalse(firstThreadID.equals(secondThreadID), String.format("Two closures in same run were executed on thread %d", firstThreadID));
        }
    }

    @Test
    public void testAssertSynchronized_Failure() throws Exception {
        try {
            Assert.assertSynchronized(new Callable<List<Callable<Void>>>() {

                public List<Callable<Void>> call() throws Exception {
                    final ArrayList<Object> unsafeObject = new ArrayList<Object>();
                    final Callable<Void> unsafeInvocation = new Callable<Void>() {

                        public Void call() throws Exception {
                            for (int x = 0; x < 1000; x++) {
                                unsafeObject.add(new Object());
                            }
                            return null;
                        }
                    };
                    return Arrays.asList(unsafeInvocation, unsafeInvocation);
                }
            });
            fail("Unsafe ArrayList usage should have failed.");
        } catch (AssertionError e) {
            assertTrue(e.getMessage().contains("An exception was raised running the synchronization test"));
            assertTrue(e.getMessage().contains("ArrayIndexOutOfBoundsException"));
            assertTrue(e.getMessage().contains("at java.util.ArrayList.add"));
        }
    }

    @Test
    public void testAssertSynchronized_Success() throws Exception {
        Assert.assertSynchronized(new Callable<List<Callable<Void>>>() {

            public List<Callable<Void>> call() throws Exception {
                final Vector<Object> safeObject = new Vector<Object>();
                final Callable<Void> safeInvocation = new Callable<Void>() {

                    public Void call() throws Exception {
                        for (int x = 0; x < 1000; x++) {
                            safeObject.add(new Object());
                        }
                        return null;
                    }
                };
                return Arrays.asList(safeInvocation, safeInvocation);
            }
        });
    }

    @Test
    public void testAssertSynchronized_TasksCountGreaterThanMaxThreads() throws Exception {
        Assert.assertSynchronized(new Callable<List<Callable<Void>>>() {

            public List<Callable<Void>> call() throws Exception {
                return new ArrayList<Callable<Void>>() {

                    {
                        add(new DummyCallable());
                        add(new DummyCallable());
                    }
                };
            }
        }, 1, 1);
    }
}
