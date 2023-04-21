package com.volantis.synergetics.reporting.impl;

import java.util.Map;
import java.util.HashMap;

/**
 * Metric enumeration.
 */
public class Metric {

    private static final Map METRICS = new HashMap();

    /**
     * The Event substitution parameter
     */
    public static final Metric EVENT = new Metric(":EVENT");

    /**
     * The Message substitution parameter
     */
    public static final Metric MESSAGE = new Metric(":MESSAGE");

    /**
     * The Transaction id substitution parameter
     */
    public static final Metric TRANSID = new Metric(":TRANSID");

    /**
     * The Parents transaction id substitution parameter
     */
    public static final Metric PTRANSID = new Metric(":PTRANSID");

    /**
     * The Timestamp substitution parameter
     */
    public static final Metric TIMESTAMP = new Metric(":TIMESTAMP");

    /**
     * Returns the metric with the specified name or return null, if the metric
     * cannot be found.
     *
     * @param name the name of the metric
     * @return the metric or null
     */
    public static Metric lookup(final String name) {
        return (Metric) METRICS.get(name);
    }

    /**
     * The name of the metric.
     */
    private final String name;

    /**
     * Creates a new metric with the given name.
     *
     * @param name the name of the metric to be created
     */
    private Metric(final String name) {
        this.name = name;
        METRICS.put(name, this);
    }

    /**
     * Returns the name of the metric
     * @return the name of the metric
     */
    public String getName() {
        return name;
    }

    public String toString() {
        return name;
    }
}
