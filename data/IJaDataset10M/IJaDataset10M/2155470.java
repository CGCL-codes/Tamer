package kite.circuitbreaker;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.lessThan;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CyclicBarrier;
import kite.test.BarrierThread;
import kite.test.KiteObjectMother;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * <p>
 * Test case for {@link CircuitBreakerTemplate}.
 * </p>
 * 
 * @version $Id: CircuitBreakerTemplateTest.java 65 2010-03-25 06:37:30Z willie.wheeler $
 * @author Willie Wheeler
 * @since 1.0
 */
public class CircuitBreakerTemplateTest {

    private static KiteObjectMother mom = KiteObjectMother.instance();

    private CircuitBreakerTemplate breaker;

    private CircuitBreakerCallback<String> goodAction;

    private CircuitBreakerCallback<String> badAction;

    private CircuitBreakerCallback<String> actionThatAlwaysThrowsSqlException;

    @Before
    public void setUp() throws Exception {
        this.goodAction = mom.getGoodCircuitBreakerAction();
        this.badAction = mom.getBadCircuitBreakerAction();
        this.actionThatAlwaysThrowsSqlException = mom.getCircuitBreakerActionThatAlwaysThrowsSqlException();
        this.breaker = new CircuitBreakerTemplate();
        breaker.setBeanName("messageServiceBreaker");
        breaker.setTimeout(60000L);
    }

    @After
    public void tearDown() throws Exception {
        this.goodAction = null;
        this.badAction = null;
        this.breaker = null;
    }

    @Test
    public void testTripOpensBreakerAndSetsTimeout() {
        assertBreakerIsClosed();
        long now = System.currentTimeMillis();
        long expected = now + breaker.getTimeout();
        breaker.trip();
        assertBreakerIsOpen();
        long delta = breaker.getRetryTime() - expected;
        assertThat(delta, is(lessThan(100L)));
    }

    @Test
    public void testResetClosesBreakerAndClearsExceptionCount() {
        breaker.trip();
        breaker.setExceptionCount(5);
        assertBreakerIsOpen();
        assertThat(breaker.getExceptionCount(), is(5));
        breaker.reset();
        assertBreakerIsClosed();
        assertThat(breaker.getExceptionCount(), is(0));
    }

    @Test
    public void testCallPassesThroughClosedBreaker() {
        assertBreakerIsClosed();
        int exceptionCount = breaker.getExceptionCount();
        try {
            breaker.execute(goodAction);
        } catch (Exception e) {
            fail("Unexpected exception: " + e);
        }
        assertBreakerIsClosed();
        assertThat(breaker.getExceptionCount(), is(exceptionCount));
    }

    @Test
    public void testSuccessfulCallResetsClosedBreakerExceptionCount() {
        breaker.setExceptionThreshold(10);
        breaker.setExceptionCount(5);
        assertThat(breaker.getExceptionCount(), is(5));
        assertBreakerIsClosed();
        try {
            breaker.execute(goodAction);
        } catch (Exception e) {
            fail("Unexpected exception: " + e);
        }
        assertThat(breaker.getExceptionCount(), is(0));
    }

    @Test
    public void testFailedCallThrowsExceptionAndIncrementsClosedBreakerExceptionCount() {
        assertBreakerIsClosed();
        int exceptionCount = breaker.getExceptionCount();
        try {
            breaker.execute(badAction);
            fail("Expected exception");
        } catch (Exception e) {
            assertThat(breaker.getExceptionCount(), is(exceptionCount + 1));
        }
    }

    @Test
    public void testCallsPassThroughClosedBreakerTilThresholdReached() {
        final int exceptionThreshold = 3;
        breaker.setExceptionThreshold(exceptionThreshold);
        breaker.reset();
        assertThat(breaker.getExceptionThreshold(), is(exceptionThreshold));
        assertThat(breaker.getExceptionCount(), is(0));
        assertBreakerIsClosed();
        for (int i = 0; i < exceptionThreshold; i++) {
            try {
                breaker.execute(badAction);
            } catch (CircuitOpenException e) {
                fail("Unexpected exception: " + e);
            } catch (Exception e) {
                if (i < exceptionThreshold - 1) {
                    assertBreakerIsClosed();
                } else {
                    assertBreakerIsOpen();
                }
            }
        }
        try {
            breaker.execute(badAction);
            fail("Expected CircuitOpenException");
        } catch (CircuitOpenException e) {
            assertBreakerIsOpen();
        } catch (Exception e) {
            fail("Unexpected exception: " + e);
        }
    }

    @Test
    public void testExceptionCountIsThreadSafe() throws Exception {
        breaker.setExceptionThreshold(Integer.MAX_VALUE);
        final int numThreads = 10;
        final int callsPerThread = 1000000;
        CyclicBarrier entryBarrier = new CyclicBarrier(numThreads + 1);
        CyclicBarrier exitBarrier = new CyclicBarrier(numThreads + 1);
        Runnable runnable = new Runnable() {

            public void run() {
                for (int i = 0; i < callsPerThread; i++) {
                    try {
                        breaker.execute(badAction);
                    } catch (Exception e) {
                    }
                }
            }
        };
        for (int i = 0; i < numThreads; i++) {
            new BarrierThread(runnable, "BarrierThread-" + i, entryBarrier, exitBarrier).start();
        }
        assertThat(breaker.getExceptionCount(), is(0));
        entryBarrier.await();
        exitBarrier.await();
        assertThat(breaker.getExceptionCount(), is(numThreads * callsPerThread));
    }

    @Test
    public void testHandledExceptionsTripBreaker() {
        breaker.setExceptionCount(3);
        assertBreakerIsClosed();
        List<Class<? extends Exception>> handledExceptions = new ArrayList<Class<? extends Exception>>();
        handledExceptions.add(SQLException.class);
        breaker.setHandledExceptions(handledExceptions);
        for (int i = 0; i < 3; i++) {
            try {
                breaker.execute(actionThatAlwaysThrowsSqlException);
            } catch (Exception e) {
            }
        }
        try {
            breaker.execute(actionThatAlwaysThrowsSqlException);
            fail("Expected CircuitOpenException");
        } catch (CircuitOpenException e) {
        } catch (Exception e) {
            fail("Expected CircuitOpenException, got " + e);
        }
    }

    @Test
    public void testUnhandledExceptionsDontTripBreaker() {
        breaker.setExceptionCount(3);
        assertBreakerIsClosed();
        List<Class<? extends Exception>> handledExceptions = new ArrayList<Class<? extends Exception>>();
        handledExceptions.add(RuntimeException.class);
        breaker.setHandledExceptions(handledExceptions);
        for (int i = 0; i < 3; i++) {
            try {
                breaker.execute(actionThatAlwaysThrowsSqlException);
            } catch (Exception e) {
            }
        }
        try {
            breaker.execute(actionThatAlwaysThrowsSqlException);
            fail("Expected SQLException");
        } catch (SQLException e) {
        } catch (CircuitOpenException e) {
            fail("Breaker not supposed to handle the SQLExceptions");
        } catch (Exception e) {
            fail("Expected SQLException, got " + e);
        }
    }

    @Test
    public void testCallingOpenBreakerThrowsCircuitOpenException() {
        breaker.trip();
        assertBreakerIsOpen();
        try {
            breaker.execute(goodAction);
            fail("Expected CircuitOpenException, but no exception occurred at all");
        } catch (CircuitOpenException e) {
            return;
        } catch (Exception e) {
            fail("Expected CircuitOpenException, but got " + e);
        }
    }

    @Test
    public void testSuccessfulCallAgainstHalfOpenResetsBreaker() throws Exception {
        breaker.setTimeout(500L);
        assertBreakerIsClosed();
        breaker.trip();
        assertBreakerIsOpen();
        Thread.sleep(550L);
        assertBreakerIsHalfOpen();
        try {
            breaker.execute(goodAction);
        } catch (Exception e) {
            fail("Unexpected exception: " + e);
        }
        assertBreakerIsClosed();
    }

    @Test
    public void testFailedCallAgainstHalfOpenTripsBreaker() throws Exception {
        breaker.setTimeout(500L);
        assertBreakerIsClosed();
        breaker.trip();
        assertBreakerIsOpen();
        Thread.sleep(550L);
        assertBreakerIsHalfOpen();
        try {
            breaker.execute(badAction);
            fail("Expected exception");
        } catch (CircuitOpenException e) {
            fail("Unexpected CircuitOpenException");
        } catch (Exception e) {
        }
        assertBreakerIsOpen();
    }

    @Test
    public void testHalfOpenBreakerRemainsHalfOpenWhenUnhandledExceptionOccurs() throws Exception {
        breaker.setState(CircuitBreakerTemplate.State.HALF_OPEN);
        assertBreakerIsHalfOpen();
        List<Class<? extends Exception>> handledExceptions = new ArrayList<Class<? extends Exception>>();
        handledExceptions.add(RuntimeException.class);
        breaker.setHandledExceptions(handledExceptions);
        try {
            breaker.execute(actionThatAlwaysThrowsSqlException);
            fail("Expected SQLException");
        } catch (SQLException e) {
        } catch (Exception e) {
            fail("Expected SQLException, got " + e);
        }
        assertBreakerIsHalfOpen();
    }

    private void assertBreakerIsClosed() {
        assertBreakerState(CircuitBreakerTemplate.State.CLOSED);
    }

    private void assertBreakerIsOpen() {
        assertBreakerState(CircuitBreakerTemplate.State.OPEN);
    }

    private void assertBreakerIsHalfOpen() {
        assertBreakerState(CircuitBreakerTemplate.State.HALF_OPEN);
    }

    private void assertBreakerState(CircuitBreakerTemplate.State state) {
        assertThat(breaker.getState(), is(state));
    }
}
