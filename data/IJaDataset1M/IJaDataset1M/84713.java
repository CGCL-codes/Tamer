package com.jeantessier.metrics;

import junit.framework.*;
import org.apache.log4j.*;

public class TestStatisticalMeasurementEmpty extends TestCase {

    private StatisticalMeasurement measurement;

    private Metrics metrics;

    protected void setUp() throws Exception {
        Logger.getLogger(getClass()).info("Starting test: " + getName());
        metrics = new Metrics("foo");
        measurement = new StatisticalMeasurement(null, metrics, "bar");
    }

    protected void tearDown() throws Exception {
        Logger.getLogger(getClass()).info("End of " + getName());
    }

    public void testDirect() {
        Metrics m = new Metrics("m");
        m.track("bar", new CounterMeasurement(null, null, null));
        metrics.addSubMetrics(m);
        assertTrue("Before AddToMeasurement()", measurement.isEmpty());
        m.addToMeasurement("bar", 1);
        assertFalse("After AddToMeasurement()", !measurement.isEmpty());
    }

    public void testIndirect() {
        Metrics m = new Metrics("m");
        metrics.addSubMetrics(m);
        Metrics sm = new Metrics("sm");
        sm.track("bar", new CounterMeasurement(null, null, null));
        m.addSubMetrics(sm);
        assertTrue("Before AddToMeasurement()", measurement.isEmpty());
        sm.addToMeasurement("bar", 1);
        assertFalse("After AddToMeasurement()", !measurement.isEmpty());
    }

    public void testViaStatisticalMeasurement() {
        Metrics m = new Metrics("m");
        m.track("bar", new StatisticalMeasurement(null, m, "bar"));
        metrics.addSubMetrics(m);
        Metrics sm = new Metrics("sm");
        sm.track("bar", new CounterMeasurement(null, null, null));
        m.addSubMetrics(sm);
        assertTrue("Before AddToMeasurement()", measurement.isEmpty());
        sm.addToMeasurement("bar", 1);
        assertFalse("After AddToMeasurement()", !measurement.isEmpty());
    }
}
