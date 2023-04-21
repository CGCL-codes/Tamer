package org.expasy.jpl.commons.collection.stat;

import org.expasy.jpl.commons.collection.Interval;

/**
 * A statistical category or class is an interval of values where data lie.
 * 
 * @author nikitin
 * 
 * @version 1.0
 * 
 */
public final class StatisticalCategory extends Number {

    private static final long serialVersionUID = 1006653730774220432L;

    private double from;

    private double to;

    private double center;

    private HistogramDataSet histogram;

    private double frequency;

    public StatisticalCategory(HistogramDataSet histo, Interval interval) {
        this(histo, interval.getLowerBound(), interval.getUpperBound());
    }

    public StatisticalCategory(HistogramDataSet histo, double from, double to) {
        this.histogram = histo;
        this.from = from;
        this.to = to;
        this.center = (to + from) / 2;
    }

    public final void updateFrequency(double weight) {
        frequency += weight;
    }

    /**
	 * @return the center of this category.
	 */
    public final double getCenter() {
        return center;
    }

    /**
	 * @return the mean number of observations in this interval.
	 */
    public final double getFrequency() {
        return frequency;
    }

    /**
	 * @return the observations of this interval.
	 */
    public final double[] getValues() {
        return histogram.getValuesAtInterval(new Interval.Builder(from, to).build());
    }

    @Override
    public double doubleValue() {
        return center;
    }

    @Override
    public float floatValue() {
        return (float) center;
    }

    @Override
    public int intValue() {
        return (int) center;
    }

    @Override
    public long longValue() {
        return (long) center;
    }

    /**
	 * @return the class interval.
	 */
    public Interval getInterval() {
        return new Interval.Builder(from, to).build();
    }

    public String toString() {
        return "[" + from + ", " + to + "[" + ": " + getFrequency();
    }
}
