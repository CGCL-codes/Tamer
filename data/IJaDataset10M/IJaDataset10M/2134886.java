package com.google.code.jetm.reporting.xml;

import java.util.Collections;
import java.util.Map;
import etm.core.aggregation.Aggregate;

/**
 * An implementation of {@link Aggregate} to be used to marshal and unmarshal to
 * and from XML.
 * 
 * @author jrh3k5
 * 
 */
public class XmlAggregate implements Aggregate {

    private final double min;

    private final double max;

    private final double total;

    private final long measurements;

    private final String name;

    /**
     * Create an aggregate.
     * 
     * @param min
     *            The minimum amount of time that occurred within a measurement
     *            collection.
     * @param max
     *            The maximum amount of time that occurred within a measurement
     *            collection.
     * @param total
     *            The total amount of time collected.
     * @param measurements
     *            The number of times the measurement was collected.
     * @param name
     *            The name of the measurement.
     */
    public XmlAggregate(double min, double max, double total, long measurements, String name) {
        this.min = min;
        this.max = max;
        this.total = total;
        this.name = name;
        this.measurements = measurements;
    }

    /**
     * {@inheritDoc}
     */
    public double getAverage() {
        return measurements == 0 ? 0 : total / (double) measurements;
    }

    /**
     * {@inheritDoc}
     */
    public Map<?, ?> getChilds() {
        return Collections.emptyMap();
    }

    /**
     * {@inheritDoc}
     */
    public double getMax() {
        return max;
    }

    /**
     * {@inheritDoc}
     */
    public long getMeasurements() {
        return measurements;
    }

    /**
     * {@inheritDoc}
     */
    public double getMin() {
        return min;
    }

    /**
     * {@inheritDoc}
     */
    public String getName() {
        return name;
    }

    /**
     * {@inheritDoc}
     */
    public double getTotal() {
        return total;
    }

    /**
     * {@inheritDoc}
     */
    public boolean hasChilds() {
        return false;
    }
}
