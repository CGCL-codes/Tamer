package org.mili.jmibs.impl;

import org.mili.jmibs.api.Interval;
import org.mili.jmibs.api.IntervalBenchmark;

/**
 * This class defines an interval thats based on long values.
 *
 * @author Michael Lieshoff
 * @version 1.0 04.06.2010
 * @since 1.1
 */
public class LongInterval extends AbstractInterval<Long> {

    /**
     * creates a new long interval.
     */
    protected LongInterval() {
        super();
    }

    /**
     * creates a new long interval.
     *
     * @param min minimum.
     * @param max maximum.
     */
    protected LongInterval(long min, long max) {
        super();
        this.setMin(min);
        this.setMax(max);
    }

    /**
     * creates a new long interval.
     *
     * @param min minimum.
     * @param max maximum.
     * @return a new long interval.
     */
    public static LongInterval create(long min, long max) {
        return new LongInterval(min, max);
    }

    @Override
    public int compareTo(Interval<Long> i) {
        return (int) (this.getDifference() - i.getDifference());
    }

    @Override
    public void traverse(IntervalBenchmark<?> olb) {
        for (long i = this.getMin(); i < this.getMax(); i++) {
            this.setValue(i);
            olb.execute();
        }
    }

    @Override
    public long getDifference() {
        return Math.abs(this.getMax() - this.getMin());
    }

    @Override
    public LongInterval getMinimumInterval() {
        return LongInterval.create(0, 1);
    }
}
