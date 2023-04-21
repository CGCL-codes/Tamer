package com.jeantessier.metrics;

import junit.framework.*;

public class TestMetricsComparator extends TestCase {

    public void testSortOn() {
        MetricsComparator c = new MetricsComparator("foo", StatisticalMeasurement.DISPOSE_IGNORE);
        assertEquals("c.Name()", "foo", c.getName());
        assertEquals("c.Direction()", MetricsComparator.ASCENDING, c.getDirection());
        c.sortOn("foo", StatisticalMeasurement.DISPOSE_IGNORE);
        assertEquals("c.Name()", "foo", c.getName());
        assertEquals("c.Direction()", MetricsComparator.DESCENDING, c.getDirection());
        c.sortOn("foo", StatisticalMeasurement.DISPOSE_IGNORE);
        assertEquals("c.Name()", "foo", c.getName());
        assertEquals("c.Direction()", MetricsComparator.ASCENDING, c.getDirection());
        c.sortOn("bar", StatisticalMeasurement.DISPOSE_IGNORE);
        assertEquals("c.Name()", "bar", c.getName());
        assertEquals("c.Direction()", MetricsComparator.ASCENDING, c.getDirection());
        c.sortOn("bar", StatisticalMeasurement.DISPOSE_IGNORE);
        assertEquals("c.Name()", "bar", c.getName());
        assertEquals("c.Direction()", MetricsComparator.DESCENDING, c.getDirection());
        c.sortOn("baz", StatisticalMeasurement.DISPOSE_IGNORE);
        assertEquals("c.Name()", "baz", c.getName());
        assertEquals("c.Direction()", MetricsComparator.ASCENDING, c.getDirection());
        c.sortOn("foobar", StatisticalMeasurement.DISPOSE_IGNORE);
        assertEquals("c.Name()", "foobar", c.getName());
        assertEquals("c.Direction()", MetricsComparator.ASCENDING, c.getDirection());
        c.sortOn("foobar", StatisticalMeasurement.DISPOSE_IGNORE);
        assertEquals("c.Name()", "foobar", c.getName());
        assertEquals("c.Direction()", MetricsComparator.DESCENDING, c.getDirection());
        c.sortOn("foobar", StatisticalMeasurement.DISPOSE_MINIMUM);
        assertEquals("c.Name()", "foobar", c.getName());
        assertEquals("c.Direction()", MetricsComparator.ASCENDING, c.getDirection());
    }

    public void testCompareTo() {
        Metrics m1 = new Metrics("m1");
        Metrics m2 = new Metrics("m2");
        m1.track("foo", new CounterMeasurement(null, null, null));
        m1.track("bar", new CounterMeasurement(null, null, null));
        m1.track("baz", new CounterMeasurement(null, null, null));
        m2.track("foo", new CounterMeasurement(null, null, null));
        m2.track("bar", new CounterMeasurement(null, null, null));
        m2.track("baz", new CounterMeasurement(null, null, null));
        m1.addToMeasurement("foo", 1);
        m1.addToMeasurement("bar", 2);
        m1.addToMeasurement("baz", 3);
        m2.addToMeasurement("foo", 3);
        m2.addToMeasurement("bar", 2);
        m2.addToMeasurement("baz", 1);
        MetricsComparator c1 = new MetricsComparator("foo");
        MetricsComparator c2 = new MetricsComparator("bar");
        MetricsComparator c3 = new MetricsComparator("baz");
        assertTrue(c1.compare(m1, m2) < 0);
        assertTrue(c2.compare(m1, m2) == 0);
        assertTrue(c3.compare(m1, m2) > 0);
        c1.reverse();
        c2.reverse();
        c3.reverse();
        assertTrue(c1.compare(m1, m2) > 0);
        assertTrue(c2.compare(m1, m2) == 0);
        assertTrue(c3.compare(m1, m2) < 0);
    }

    public void testCompareNaN() {
        Metrics m1 = new Metrics("m1");
        Metrics m2 = new Metrics("m2");
        m1.track("foo", new CounterMeasurement(null, null, null));
        m2.track("foo", new CounterMeasurement(null, null, null));
        m1.track("bar", new CounterMeasurement(null, null, null));
        m2.track("bar", new CounterMeasurement(null, null, null));
        m1.track("baz", new CounterMeasurement(null, null, null));
        m2.track("baz", new CounterMeasurement(null, null, null));
        m1.addToMeasurement("foo", Double.NaN);
        m2.addToMeasurement("foo", Double.NaN);
        m1.addToMeasurement("bar", Double.NaN);
        m2.addToMeasurement("bar", 1);
        m1.addToMeasurement("baz", 1);
        m2.addToMeasurement("baz", Double.NaN);
        MetricsComparator c1 = new MetricsComparator("foo");
        MetricsComparator c2 = new MetricsComparator("bar");
        MetricsComparator c3 = new MetricsComparator("baz");
        assertTrue(c1.compare(m1, m2) == 0);
        assertTrue(c2.compare(m1, m2) > 0);
        assertTrue(c3.compare(m1, m2) < 0);
        c1.reverse();
        c2.reverse();
        c3.reverse();
        assertTrue(c1.compare(m1, m2) == 0);
        assertTrue(c2.compare(m1, m2) > 0);
        assertTrue(c3.compare(m1, m2) < 0);
    }
}
