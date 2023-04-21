package nl.flotsam.preon.util;

import java.util.concurrent.ExecutionException;
import nl.flotsam.preon.util.LazyLoadingReference;
import nl.flotsam.preon.util.LazyLoadingReference.Loader;
import edu.emory.mathcs.backport.java.util.concurrent.atomic.AtomicInteger;
import edu.umd.cs.mtc.MultithreadedTest;
import edu.umd.cs.mtc.TestFramework;
import junit.framework.TestCase;

public class LazyLoadingReferenceTest extends TestCase {

    public void testLoading() throws InterruptedException, ExecutionException {
        LazyLoadingReference<String> reference = new LazyLoadingReference<String>(new LazyLoadingReference.Loader<String>() {

            public String load() {
                return "abc";
            }
        });
        assertEquals("abc", reference.get());
    }

    public void testConcurrentAccess() throws Throwable {
        TestFramework.runManyTimes(new ConcurrentTest(), 100);
    }

    public static class ConcurrentTest extends MultithreadedTest {

        private LazyLoadingReference<Integer> reference;

        private AtomicInteger counter = new AtomicInteger();

        private int i = 0;

        public void initialize() {
            reference = new LazyLoadingReference<Integer>(new IntegerLoader());
            counter.set(0);
        }

        public void thread1() throws InterruptedException, ExecutionException {
            assertEquals(1, reference.get().intValue());
        }

        public void thread2() throws InterruptedException, ExecutionException {
            assertEquals(1, reference.get().intValue());
        }

        public void thread3() throws InterruptedException, ExecutionException {
            assertEquals(1, reference.get().intValue());
        }

        private class IntegerLoader implements Loader<Integer> {

            public Integer load() {
                return counter.addAndGet(1);
            }
        }
    }
}
