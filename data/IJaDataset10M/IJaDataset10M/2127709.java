package org.jfree.chart.axis.junit;

import java.awt.geom.Rectangle2D;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInput;
import java.io.ObjectInputStream;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import org.jfree.chart.axis.DateAxis;
import org.jfree.chart.axis.DateTickMarkPosition;
import org.jfree.chart.axis.DateTickUnit;
import org.jfree.chart.axis.SegmentedTimeline;
import org.jfree.data.time.DateRange;
import org.jfree.ui.RectangleEdge;

/**
 * Tests for the {@link DateAxis} class.
 */
public class DateAxisTests extends TestCase {

    /**
     * Returns the tests as a test suite.
     *
     * @return The test suite.
     */
    public static Test suite() {
        return new TestSuite(DateAxisTests.class);
    }

    /**
     * Constructs a new set of tests.
     *
     * @param name  the name of the tests.
     */
    public DateAxisTests(String name) {
        super(name);
    }

    /**
     * Confirm that the equals method can distinguish all the required fields.
     */
    public void testEquals() {
        DateAxis a1 = new DateAxis("Test");
        DateAxis a2 = new DateAxis("Test");
        assertTrue(a1.equals(a2));
        a1.setTickUnit(new DateTickUnit(DateTickUnit.DAY, 7));
        assertFalse(a1.equals(a2));
        a2.setTickUnit(new DateTickUnit(DateTickUnit.DAY, 7));
        assertTrue(a1.equals(a2));
        a1.setDateFormatOverride(new SimpleDateFormat("yyyy"));
        assertFalse(a1.equals(a2));
        a2.setDateFormatOverride(new SimpleDateFormat("yyyy"));
        assertTrue(a1.equals(a2));
        a1.setTickMarkPosition(DateTickMarkPosition.END);
        assertFalse(a1.equals(a2));
        a2.setTickMarkPosition(DateTickMarkPosition.END);
        assertTrue(a1.equals(a2));
        a1.setTimeline(SegmentedTimeline.newMondayThroughFridayTimeline());
        assertFalse(a1.equals(a2));
        a2.setTimeline(SegmentedTimeline.newMondayThroughFridayTimeline());
        assertTrue(a1.equals(a2));
    }

    /**
     * Two objects that are equal are required to return the same hashCode. 
     */
    public void testHashCode() {
        DateAxis a1 = new DateAxis("Test");
        DateAxis a2 = new DateAxis("Test");
        assertTrue(a1.equals(a2));
        int h1 = a1.hashCode();
        int h2 = a2.hashCode();
        assertEquals(h1, h2);
    }

    /**
     * Confirm that cloning works.
     */
    public void testCloning() {
        DateAxis a1 = new DateAxis("Test");
        DateAxis a2 = null;
        try {
            a2 = (DateAxis) a1.clone();
        } catch (CloneNotSupportedException e) {
            System.err.println("Failed to clone.");
        }
        assertTrue(a1 != a2);
        assertTrue(a1.getClass() == a2.getClass());
        assertTrue(a1.equals(a2));
    }

    /**
     * Test that the setRange() method works.
     */
    public void testSetRange() {
        DateAxis axis = new DateAxis("Test Axis");
        Calendar calendar = Calendar.getInstance();
        calendar.set(1999, Calendar.JANUARY, 3);
        Date d1 = calendar.getTime();
        calendar.set(1999, Calendar.JANUARY, 31);
        Date d2 = calendar.getTime();
        axis.setRange(d1, d2);
        DateRange range = (DateRange) axis.getRange();
        assertEquals(d1, range.getLowerDate());
        assertEquals(d2, range.getUpperDate());
    }

    /**
     * Test that the setMaximumDate() method works.
     */
    public void testSetMaximumDate() {
        DateAxis axis = new DateAxis("Test Axis");
        Date date = new Date();
        axis.setMaximumDate(date);
        assertEquals(date, axis.getMaximumDate());
    }

    /**
     * Test that the setMinimumDate() method works.
     */
    public void testSetMinimumDate() {
        DateAxis axis = new DateAxis("Test Axis");
        Date d1 = new Date();
        Date d2 = new Date(d1.getTime() + 1);
        axis.setMaximumDate(d2);
        axis.setMinimumDate(d1);
        assertEquals(d1, axis.getMinimumDate());
    }

    /**
     * Tests two doubles for 'near enough' equality.
     * 
     * @param d1  number 1.
     * @param d2  number 2.
     * @param tolerance  maximum tolerance.
     * 
     * @return A boolean.
     */
    private boolean same(double d1, double d2, double tolerance) {
        return (Math.abs(d1 - d2) < tolerance);
    }

    /**
     * Test the translation of Java2D values to data values.
     */
    public void testJava2DToValue() {
        DateAxis axis = new DateAxis();
        axis.setRange(50.0, 100.0);
        Rectangle2D dataArea = new Rectangle2D.Double(10.0, 50.0, 400.0, 300.0);
        double y1 = axis.java2DToValue(75.0, dataArea, RectangleEdge.LEFT);
        assertTrue(same(y1, 95.8333333, 1.0));
        double y2 = axis.java2DToValue(75.0, dataArea, RectangleEdge.RIGHT);
        assertTrue(same(y2, 95.8333333, 1.0));
        double x1 = axis.java2DToValue(75.0, dataArea, RectangleEdge.TOP);
        assertTrue(same(x1, 58.125, 1.0));
        double x2 = axis.java2DToValue(75.0, dataArea, RectangleEdge.BOTTOM);
        assertTrue(same(x2, 58.125, 1.0));
        axis.setInverted(true);
        double y3 = axis.java2DToValue(75.0, dataArea, RectangleEdge.LEFT);
        assertTrue(same(y3, 54.1666667, 1.0));
        double y4 = axis.java2DToValue(75.0, dataArea, RectangleEdge.RIGHT);
        assertTrue(same(y4, 54.1666667, 1.0));
        double x3 = axis.java2DToValue(75.0, dataArea, RectangleEdge.TOP);
        assertTrue(same(x3, 91.875, 1.0));
        double x4 = axis.java2DToValue(75.0, dataArea, RectangleEdge.BOTTOM);
        assertTrue(same(x4, 91.875, 1.0));
    }

    /**
     * Serialize an instance, restore it, and check for equality.
     */
    public void testSerialization() {
        DateAxis a1 = new DateAxis("Test Axis");
        DateAxis a2 = null;
        try {
            ByteArrayOutputStream buffer = new ByteArrayOutputStream();
            ObjectOutput out = new ObjectOutputStream(buffer);
            out.writeObject(a1);
            out.close();
            ObjectInput in = new ObjectInputStream(new ByteArrayInputStream(buffer.toByteArray()));
            a2 = (DateAxis) in.readObject();
            in.close();
        } catch (Exception e) {
            System.out.println(e.toString());
        }
        boolean b = a1.equals(a2);
        assertTrue(b);
    }
}
