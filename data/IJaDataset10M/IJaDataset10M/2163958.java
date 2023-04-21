package jconch.testing;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import org.testng.annotations.Test;
import org.testng.annotations.BeforeTest;
import static org.testng.AssertJUnit.*;

/**
 * Test for TestCoordinator.
 * @author Hamlet D'Arcy (hamletdrc@gmail.com)
 */
public class TestCoordinatorTest {

    private ExecutorService executor;

    @BeforeTest
    public void setUp() throws Exception {
        executor = Executors.newSingleThreadExecutor();
    }

    @Test(timeOut = 1000)
    public void testWaitsToFinish() throws Exception {
        final TestCoordinator coord = new TestCoordinator();
        final AtomicBoolean wasCalled = new AtomicBoolean(false);
        executor.submit(new Runnable() {

            public void run() {
                wasCalled.set(true);
                coord.finishTest();
            }
        });
        coord.delayTestFinish();
        assertTrue("task not called!", wasCalled.get());
    }

    @Test(timeOut = 1000)
    public void testWaitsToFinish_WithTimeout() throws Exception {
        final TestCoordinator coord = new TestCoordinator();
        final AtomicBoolean wasCalled = new AtomicBoolean(false);
        executor.submit(new Runnable() {

            public void run() {
                wasCalled.set(true);
                coord.finishTest();
            }
        });
        final boolean result = coord.delayTestFinish(1000);
        assertTrue("task not called!", wasCalled.get());
        assertTrue("task should not have timed out", result);
    }

    @Test(timeOut = 5000)
    public void testErrorCondition_WithTimeout() throws Exception {
        final TestCoordinator coord = new TestCoordinator();
        boolean result = coord.delayTestFinish(2000);
        assertFalse("task should have timed out", result);
    }

    @Test(timeOut = 5000)
    public void testErrorCondition_WithTimeoutAndTimeUnit() throws Exception {
        final TestCoordinator coord = new TestCoordinator();
        final boolean result = coord.delayTestFinish(1, TimeUnit.SECONDS);
        assertFalse("task should have timed out", result);
    }

    @Test(timeOut = 1000)
    public void testWaitsToFinish_WithTimeoutAndTimeUnit() throws Exception {
        final TestCoordinator coord = new TestCoordinator();
        final AtomicBoolean wasCalled = new AtomicBoolean(false);
        executor.submit(new Runnable() {

            public void run() {
                wasCalled.set(true);
                coord.finishTest();
            }
        });
        final boolean result = coord.delayTestFinish(1, TimeUnit.SECONDS);
        assertTrue("task not called!", wasCalled.get());
        assertTrue("task should not have timed out", result);
    }

    @Test(timeOut = 1000)
    public void testWaitsToFinishCanBeCalledTwice() throws Exception {
        final TestCoordinator coord = new TestCoordinator();
        final AtomicBoolean wasCalled = new AtomicBoolean(false);
        final Runnable task = new Runnable() {

            public void run() {
                wasCalled.set(true);
                coord.finishTest();
            }
        };
        executor.submit(task);
        coord.delayTestFinish();
        assertTrue("1st task not called!", wasCalled.get());
        wasCalled.set(false);
        executor.submit(task);
        coord.delayTestFinish();
        assertTrue("2nd task not called!", wasCalled.get());
    }

    @Test(timeOut = 1000)
    public void testErrorCondition_FinishCalledTwice() throws Exception {
        final TestCoordinator coord = new TestCoordinator();
        coord.finishTest();
        coord.finishTest();
    }
}
