package org.webcastellum;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * This counter tracks each increment with a timestamp to have overaged trackings timeout.
 * But to conserve time and memory it aggregates over a certain period (a few seconds) trackings
 * and counts them against a single timestamp.
 */
public final class TrackingCounter extends AbstractCounter {

    private static final long serialVersionUID = 1L;

    private static final int DEFAULT_AGGREGATION_PERIOD_SECONDS = 10;

    private final List counter = new ArrayList();

    private final long aggregationPeriodMillis;

    private AggregatedTrackedValue current;

    public TrackingCounter(final long resetPeriodMillis) {
        super(resetPeriodMillis);
        this.aggregationPeriodMillis = Math.min(resetPeriodMillis, DEFAULT_AGGREGATION_PERIOD_SECONDS * 1000);
        increment();
    }

    public final synchronized void increment() {
        if (this.current == null) {
            createAndAddNewAggregation();
        } else {
            if (this.current.timestamp >= System.currentTimeMillis()) {
                this.current.size++;
            } else {
                createAndAddNewAggregation();
            }
        }
    }

    private void createAndAddNewAggregation() {
        final AggregatedTrackedValue newAggregation = new AggregatedTrackedValue(this.aggregationPeriodMillis);
        this.counter.add(newAggregation);
        this.current = newAggregation;
    }

    public final synchronized boolean isOveraged() {
        cutoffOldTrackings();
        return this.counter.isEmpty();
    }

    public final synchronized int getCounter() {
        cutoffOldTrackings();
        int result = 0;
        for (final Iterator iter = this.counter.iterator(); iter.hasNext(); ) {
            final AggregatedTrackedValue trackedValue = (AggregatedTrackedValue) iter.next();
            result += trackedValue.size;
        }
        return result;
    }

    private final void cutoffOldTrackings() {
        if (this.counter.isEmpty()) return;
        final long cutoffTimestamp = System.currentTimeMillis() - this.getResetPeriodMillis();
        for (final Iterator iter = this.counter.iterator(); iter.hasNext(); ) {
            final AggregatedTrackedValue trackedValue = (AggregatedTrackedValue) iter.next();
            if (trackedValue.timestamp < cutoffTimestamp) iter.remove(); else break;
        }
    }

    public final String toString() {
        return "counter with reset period: " + getResetPeriodMillis();
    }

    private static final class AggregatedTrackedValue implements Serializable {

        final long timestamp;

        int size = 1;

        public AggregatedTrackedValue(final long aggregationPeriodMillis) {
            this.timestamp = System.currentTimeMillis() + aggregationPeriodMillis;
        }
    }
}
