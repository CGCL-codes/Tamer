package org.fishwife.jrugged;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import org.junit.Before;
import org.junit.Test;

public final class TestWindowedEventCounter {

    private WindowedEventCounter impl;

    private StoppedClock clock = new StoppedClock();

    private static int CAPACITY = 3;

    private static long WINDOW_MILLIS = 5L;

    @Before
    public void setUp() {
        impl = new WindowedEventCounter(CAPACITY, WINDOW_MILLIS);
        clock.currentTimeMillis = System.currentTimeMillis();
        impl.setClock(clock);
        assertTrue(WINDOW_MILLIS > CAPACITY);
    }

    @Test
    public void testConstructor() {
        assertEquals(CAPACITY, impl.getCapacity());
        assertEquals(WINDOW_MILLIS, impl.getWindowMillis());
    }

    @Test
    public void testConstructorThrowsExceptionOnBadCapacity() {
        try {
            new WindowedEventCounter(0, WINDOW_MILLIS);
            fail("constructor should have thrown IllegalArgumentException");
        } catch (IllegalArgumentException iae) {
        }
    }

    @Test
    public void testConstructorThrowsExceptionOnBadWindowMillis() {
        try {
            new WindowedEventCounter(CAPACITY, 0);
            fail("constructor should have thrown IllegalArgumentException");
        } catch (IllegalArgumentException iae) {
        }
    }

    @Test
    public void testStartsEmpty() {
        assertEquals(0, impl.tally());
    }

    @Test
    public void testCountsToCapacity() {
        for (int i = 1; i <= CAPACITY; i++) {
            impl.mark();
            clock.currentTimeMillis = clock.currentTimeMillis + 1;
            assertEquals(i, impl.tally());
        }
    }

    @Test
    public void testCountsToCapacityOnOverflow() {
        for (int i = 0; i < (CAPACITY * 2); i++) {
            clock.currentTimeMillis = clock.currentTimeMillis + 1;
            impl.mark();
        }
        assertEquals(CAPACITY, impl.tally());
    }

    @Test
    public void testRollingExpiration() {
        long t0 = clock.currentTimeMillis;
        for (int i = 1; i <= CAPACITY; i++) {
            clock.currentTimeMillis = t0 + i;
            impl.mark();
        }
        clock.currentTimeMillis = t0 + 1 + WINDOW_MILLIS;
        int expectedCount = CAPACITY;
        assertEquals(CAPACITY, impl.tally());
        for (int j = 1; j <= CAPACITY; j++) {
            clock.currentTimeMillis++;
            expectedCount--;
            assertEquals(expectedCount, impl.tally());
        }
        clock.currentTimeMillis++;
        assertEquals(0, impl.tally());
    }

    @Test
    public void testReducingWindowDecreasesTally() throws Exception {
        long t0 = clock.currentTimeMillis;
        for (int i = 1; i <= CAPACITY; i++) {
            clock.currentTimeMillis = t0 + i;
            impl.mark();
        }
        clock.currentTimeMillis = clock.currentTimeMillis + 1;
        impl.setWindowMillis(1);
        assertEquals(1, impl.tally());
    }

    @Test
    public void testReducingCapacityDecreasesTally() {
        for (int i = 1; i <= CAPACITY; i++) {
            impl.mark();
        }
        assertEquals(CAPACITY, impl.tally());
        impl.setCapacity(CAPACITY - 1);
        assertEquals(CAPACITY - 1, impl.tally());
    }

    @Test
    public void testIncreasingCapacity() {
        for (int i = 1; i <= CAPACITY; i++) {
            impl.mark();
        }
        assertEquals(CAPACITY, impl.tally());
        impl.setCapacity(CAPACITY + 1);
        assertEquals(CAPACITY, impl.tally());
    }

    @Test
    public void testSettingBadCapacityThrowsException() {
        try {
            impl.setCapacity(0);
            fail("should have thrown IllegalArgumentException");
        } catch (IllegalArgumentException iae) {
        }
    }

    @Test
    public void testSettingBadWindowMillisThrowsException() {
        try {
            impl.setWindowMillis(0);
            fail("should have thrown IllegalArgumentException");
        } catch (IllegalArgumentException iae) {
        }
    }

    public class StoppedClock implements Clock {

        public long currentTimeMillis;

        public long currentTimeMillis() {
            return currentTimeMillis;
        }
    }
}
