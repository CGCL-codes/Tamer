package com.googlecode.sparda.commons.performance;

import static org.junit.Assert.assertEquals;
import org.junit.Test;

public class CachedValueTest {

    @Test
    public void testInitialState() {
        CachedValue<Integer> cached = this.newCachedValue(180);
        assertEquals(true, cached.isExpired());
        assertEquals(-1, cached.getLastUpdateTime());
    }

    @Test
    public void testGetValue() {
        final Integer result = 2;
        CachedValue<Integer> cached = this.newCachedValue(result);
        assertEquals(result, cached.getValue());
    }

    @Test
    public void testIsInvalidate_BeforeFirstComputing() {
        final Integer result = 2;
        CachedValue<Integer> cached = this.newCachedValue(result);
        assertEquals(true, cached.isExpired());
    }

    @Test
    public void testIsInvalidate_AfterFirstComputing() {
        final Integer result = 2;
        CachedValue<Integer> cached = this.newCachedValue(result);
        cached.getValue();
        assertEquals(false, cached.isExpired());
    }

    @Test
    public void testIsInvalidate_AfterRecomputing() {
        final Integer result = 2;
        CachedValue<Integer> cached = this.newCachedValue(result);
        cached.getValue();
        cached.expireCache();
        cached.getValue();
        assertEquals(false, cached.isExpired());
    }

    @Test
    public void testRecomputingAfterInvalidate() {
        Integer startingValue = 2;
        CachedValue<Integer> cached = this.newCachedValueChanging(startingValue);
        cached.getValue();
        startingValue++;
        cached.expireCache();
        assertEquals(startingValue, cached.getValue());
    }

    @Test
    public void testRecomputingAfterInvalidate_More() {
        Integer startingValue = 11;
        CachedValue<Integer> cached = this.newCachedValueChanging(startingValue);
        for (int i = 0; i < 20; i++, startingValue++) {
            assertEquals(startingValue, cached.getValue());
            cached.expireCache();
        }
    }

    @Test
    public void testGetLastUpdatingTime_FirstCalculus_NoComputingDelay() {
        final Integer result = 2;
        CachedValue<Integer> cached = this.newCachedValue(result);
        cached.getValue();
        assertEquals(System.currentTimeMillis(), cached.getLastUpdateTime(), 1 * 1000);
    }

    @Test
    public void testGetLastUpdatingTime_FirstCalculus_ComputingDelay() {
        final Integer result = 2;
        CachedValue<Integer> cached = this.newCachedValue(result, 100);
        cached.getValue();
        assertEquals(System.currentTimeMillis(), cached.getLastUpdateTime(), 1 * 100);
    }

    private <T> CachedValue<T> newCachedValue(final T result) {
        return this.newCachedValue(result, 0);
    }

    private <T> CachedValue<T> newCachedValue(final T result, final long computingDelayInMillisecond) {
        return new CachedValue<T>() {

            @Override
            protected T computeValue() {
                try {
                    Thread.sleep(computingDelayInMillisecond);
                } catch (InterruptedException e) {
                }
                return result;
            }
        };
    }

    private CachedValue<Integer> newCachedValueChanging(final Integer startingValue) {
        return this.newCachedValueChanging(startingValue, 0);
    }

    private CachedValue<Integer> newCachedValueChanging(final Integer startingValue, final long computingDelayInMillisecond) {
        return new CachedValue<Integer>() {

            private int result = startingValue;

            @Override
            protected Integer computeValue() {
                try {
                    Thread.sleep(computingDelayInMillisecond);
                } catch (InterruptedException e) {
                }
                return result++;
            }
        };
    }
}
