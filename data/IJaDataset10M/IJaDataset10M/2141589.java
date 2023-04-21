package com.jeantessier.metrics;

import junit.framework.*;

public class TestSumMeasurement extends TestCase implements MeasurementVisitor {

    private MeasurementDescriptor descriptor;

    private Metrics metrics;

    private SumMeasurement measurement;

    private Measurement visited;

    protected void setUp() {
        descriptor = new MeasurementDescriptor();
        descriptor.setShortName("foo");
        descriptor.setLongName("FOO");
        descriptor.setClassFor(SumMeasurement.class);
        descriptor.setCached(false);
        metrics = new Metrics("foobar");
    }

    public void testMeasurementDescriptor() throws Exception {
        measurement = (SumMeasurement) descriptor.createMeasurement();
        assertNotNull(measurement.getDescriptor());
        assertEquals(SumMeasurement.class, measurement.getDescriptor().getClassFor());
        assertEquals("foo", measurement.getShortName());
        assertEquals("FOO", measurement.getLongName());
    }

    public void testCreateFromMeasurementDescriptor() throws Exception {
        measurement = (SumMeasurement) descriptor.createMeasurement();
        assertNotNull(measurement);
        assertEquals(descriptor, measurement.getDescriptor());
        assertSame(descriptor, measurement.getDescriptor());
        assertEquals(SumMeasurement.class, measurement.getClass());
        assertEquals("foo", measurement.getShortName());
        assertEquals("FOO", measurement.getLongName());
    }

    public void testCreateDefault() {
        measurement = new SumMeasurement(descriptor, null, null);
        assertEquals(0, measurement.getValue().doubleValue(), 0.01);
    }

    public void testEmptyInitText() throws Exception {
        descriptor.setInitText("");
        measurement = (SumMeasurement) descriptor.createMeasurement();
        assertEquals(0, measurement.getValue().doubleValue(), 0.01);
    }

    public void testEmptyLineInitText() throws Exception {
        descriptor.setInitText("\n");
        measurement = (SumMeasurement) descriptor.createMeasurement();
        assertEquals(0, measurement.getValue().doubleValue(), 0.01);
    }

    public void testDashInitText() throws Exception {
        descriptor.setInitText("-");
        measurement = (SumMeasurement) descriptor.createMeasurement();
        assertEquals(0, measurement.getValue().doubleValue(), 0.01);
    }

    public void testConstant() throws Exception {
        descriptor.setInitText("2");
        measurement = (SumMeasurement) descriptor.createMeasurement();
        assertEquals(2, measurement.getValue().doubleValue(), 0.01);
    }

    public void testConstantAndEmptyLine() throws Exception {
        descriptor.setInitText("\n2\n");
        measurement = (SumMeasurement) descriptor.createMeasurement();
        assertEquals(2, measurement.getValue().doubleValue(), 0.01);
    }

    public void testAddition() throws Exception {
        descriptor.setInitText("1\n1");
        measurement = (SumMeasurement) descriptor.createMeasurement();
        assertEquals(2, measurement.getValue().doubleValue(), 0.01);
    }

    public void testNegative() throws Exception {
        descriptor.setInitText("-2");
        measurement = (SumMeasurement) descriptor.createMeasurement();
        assertEquals(-2, measurement.getValue().doubleValue(), 0.01);
    }

    public void testSubstraction() throws Exception {
        descriptor.setInitText("2\n-1");
        measurement = (SumMeasurement) descriptor.createMeasurement();
        assertEquals(1, measurement.getValue().doubleValue(), 0.01);
        descriptor.setInitText("1\n-2");
        measurement = (SumMeasurement) descriptor.createMeasurement();
        assertEquals(-1, measurement.getValue().doubleValue(), 0.01);
    }

    public void testSubMeasurement() throws Exception {
        descriptor.setInitText("bar");
        metrics.track("bar", new CounterMeasurement(null, metrics, "2"));
        measurement = (SumMeasurement) descriptor.createMeasurement(metrics);
        assertEquals(2, measurement.getValue().doubleValue(), 0.01);
    }

    public void testStatisticalMeasurement() throws Exception {
        descriptor.setInitText("bar DISPOSE_SUM");
        metrics.track("bar", new StatisticalMeasurement(null, metrics, "bar"));
        Metrics submetrics = new Metrics("submetrics");
        submetrics.track("bar", new CounterMeasurement(null, submetrics, "2"));
        metrics.addSubMetrics(submetrics);
        measurement = (SumMeasurement) descriptor.createMeasurement(metrics);
        assertEquals(2, measurement.getValue().doubleValue(), 0.01);
    }

    public void testAddMeasurements() throws Exception {
        descriptor.setInitText("bar\nbaz");
        metrics.track("bar", new CounterMeasurement(null, metrics, "1"));
        metrics.track("baz", new CounterMeasurement(null, metrics, "1"));
        measurement = (SumMeasurement) descriptor.createMeasurement(metrics);
        assertEquals(2, measurement.getValue().doubleValue(), 0.01);
    }

    public void testSubstractMeasurements() throws Exception {
        descriptor.setInitText("bar\n-baz");
        metrics.track("bar", new CounterMeasurement(null, metrics, "1"));
        metrics.track("baz", new CounterMeasurement(null, metrics, "2"));
        measurement = (SumMeasurement) descriptor.createMeasurement(metrics);
        assertEquals(-1, measurement.getValue().doubleValue(), 0.01);
    }

    public void testInUndefinedRange() throws Exception {
        descriptor.setInitText("bar");
        metrics.track("bar", new CounterMeasurement(null, null, null));
        measurement = (SumMeasurement) descriptor.createMeasurement(metrics);
        assertTrue(measurement.isInRange());
        metrics.addToMeasurement("bar", 1);
        assertTrue(measurement.isInRange());
        metrics.addToMeasurement("bar", 1);
        assertTrue(measurement.isInRange());
    }

    public void testInOpenRange() throws Exception {
        descriptor.setInitText("bar");
        metrics.track("bar", new CounterMeasurement(null, null, null));
        measurement = (SumMeasurement) descriptor.createMeasurement(metrics);
        assertTrue(measurement.isInRange());
        metrics.addToMeasurement("bar", 1);
        assertTrue(measurement.isInRange());
        metrics.addToMeasurement("bar", 1);
        assertTrue(measurement.isInRange());
    }

    public void testInLowerBoundRange() throws Exception {
        descriptor.setInitText("bar");
        descriptor.setLowerThreshold(new Integer(1));
        metrics.track("bar", new CounterMeasurement(null, null, null));
        measurement = (SumMeasurement) descriptor.createMeasurement(metrics);
        assertEquals(0, measurement.getValue().intValue());
        assertTrue(!measurement.isInRange());
        metrics.addToMeasurement("bar", 1);
        assertEquals(1, measurement.getValue().intValue());
        assertTrue(measurement.isInRange());
        metrics.addToMeasurement("bar", 1);
        assertEquals(2, measurement.getValue().intValue());
        assertTrue(measurement.isInRange());
    }

    public void testInUpperBoundRange() throws Exception {
        descriptor.setInitText("bar");
        descriptor.setUpperThreshold(new Float(1.5));
        metrics.track("bar", new CounterMeasurement(null, null, null));
        measurement = (SumMeasurement) descriptor.createMeasurement(metrics);
        assertTrue(measurement.isInRange());
        metrics.addToMeasurement("bar", 1);
        assertTrue(measurement.isInRange());
        metrics.addToMeasurement("bar", 1);
        assertTrue(!measurement.isInRange());
    }

    public void testInBoundRange() throws Exception {
        descriptor.setInitText("bar");
        descriptor.setLowerThreshold(new Integer(1));
        descriptor.setUpperThreshold(new Float(1.5));
        metrics.track("bar", new CounterMeasurement(null, null, null));
        measurement = (SumMeasurement) descriptor.createMeasurement(metrics);
        assertTrue(!measurement.isInRange());
        metrics.addToMeasurement("bar", 1);
        assertTrue(measurement.isInRange());
        metrics.addToMeasurement("bar", 1);
        assertTrue(!measurement.isInRange());
    }

    public void testCachedValue() throws Exception {
        descriptor.setInitText("bar");
        descriptor.setCached(true);
        metrics.track("bar", new CounterMeasurement(null, null, null));
        measurement = (SumMeasurement) descriptor.createMeasurement(metrics);
        assertEquals(0, measurement.getValue().doubleValue(), 0.01);
        metrics.addToMeasurement("bar", 1);
        assertEquals(0, measurement.getValue().doubleValue(), 0.01);
    }

    public void testAccept() {
        measurement = new SumMeasurement(null, null, null);
        visited = null;
        measurement.accept(this);
        assertSame(measurement, visited);
    }

    public void testEmptyWithOneMeasurement() throws Exception {
        descriptor.setInitText("bar");
        metrics.track("bar", new CounterMeasurement(null, null, null));
        measurement = (SumMeasurement) descriptor.createMeasurement(metrics);
        assertTrue("Before Add()", measurement.isEmpty());
        metrics.addToMeasurement("bar", 1);
        assertFalse("After Add(1)", measurement.isEmpty());
        metrics.addToMeasurement("bar", -1);
        assertFalse("After Add(-1)", measurement.isEmpty());
    }

    public void testEmptyWithTwoMeasurements() throws Exception {
        descriptor.setInitText("bar\nbaz");
        metrics.track("bar", new CounterMeasurement(null, null, null));
        metrics.track("baz", new CounterMeasurement(null, null, null));
        measurement = (SumMeasurement) descriptor.createMeasurement(metrics);
        assertTrue("bar is not empty", metrics.getMeasurement("bar").isEmpty());
        assertTrue("baz is not empty", metrics.getMeasurement("baz").isEmpty());
        assertTrue("Before Add()", measurement.isEmpty());
        metrics.addToMeasurement("bar", 1);
        assertFalse("bar is empty", metrics.getMeasurement("bar").isEmpty());
        assertTrue("baz is not empty", metrics.getMeasurement("baz").isEmpty());
        assertFalse("After Add(1)", measurement.isEmpty());
        metrics.addToMeasurement("bar", -1);
        assertFalse("bar is empty", metrics.getMeasurement("bar").isEmpty());
        assertTrue("baz is not empty", metrics.getMeasurement("baz").isEmpty());
        assertFalse("After Add(-1)", measurement.isEmpty());
    }

    public void testEmptyWithConstant() throws Exception {
        descriptor.setInitText("2");
        measurement = (SumMeasurement) descriptor.createMeasurement(metrics);
        assertTrue("with constants", measurement.isEmpty());
    }

    public void visitStatisticalMeasurement(StatisticalMeasurement measurement) {
    }

    public void visitRatioMeasurement(RatioMeasurement measurement) {
    }

    public void visitNbSubMetricsMeasurement(NbSubMetricsMeasurement measurement) {
    }

    public void visitCounterMeasurement(CounterMeasurement measurement) {
    }

    public void visitContextAccumulatorMeasurement(ContextAccumulatorMeasurement measurement) {
    }

    public void visitNameListMeasurement(NameListMeasurement measurement) {
    }

    public void visitSubMetricsAccumulatorMeasurement(SubMetricsAccumulatorMeasurement measurement) {
    }

    public void visitSumMeasurement(SumMeasurement measurement) {
        visited = measurement;
    }
}
