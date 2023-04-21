package javax.microedition.sensor;

import com.sun.midp.i3test.TestCase;

public class TestMeasurementRange extends TestCase {

    private static final double smallest = 1.0;

    private static final double largest = 10.0;

    private static final double resolution = 0.1;

    private MeasurementRange range;

    /** Creates a new instance of TestMeasurementRange */
    public TestMeasurementRange() {
    }

    private void testCreation() {
        range = new MeasurementRange(smallest, largest, resolution);
        assertTrue(true);
    }

    private void testValues() {
        assertTrue(smallest == range.getSmallestValue());
        assertTrue(largest == range.getLargestValue());
        assertTrue(resolution == range.getResolution());
    }

    private void testRanges() {
        try {
            MeasurementRange r = new MeasurementRange(largest, smallest, resolution);
            assertTrue(false);
        } catch (IllegalArgumentException e) {
            assertTrue(true);
        }
    }

    private void testResolution() {
        try {
            MeasurementRange r = new MeasurementRange(smallest, largest, -resolution);
            assertTrue(false);
        } catch (IllegalArgumentException e) {
            assertTrue(true);
        }
    }

    public void runTests() {
        try {
            declare("testCreation");
            testCreation();
            declare("testValues");
            testValues();
            declare("testRanges");
            testRanges();
            declare("testResolution");
            testResolution();
        } catch (Throwable t) {
            fail("" + t);
        }
    }
}
