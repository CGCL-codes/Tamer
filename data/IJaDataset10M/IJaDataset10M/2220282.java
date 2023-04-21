package orbe.test;

import static orbe.hex.HXGeom.*;
import java.util.List;
import java.util.Set;
import orbe.hex.*;
import junit.framework.TestCase;

public class TestHXGeom extends TestCase {

    public void testOffsetHex() {
        HXPoint a = new HXPoint(5, 5);
        offset(new HXPoint(5, 4), a, 0);
        offset(new HXPoint(6, 4), a, 1);
        offset(new HXPoint(6, 5), a, 2);
        offset(new HXPoint(6, 6), a, 3);
        offset(new HXPoint(5, 6), a, 4);
        offset(new HXPoint(4, 5), a, 5);
        a = new HXPoint(5, 4);
        offset(new HXPoint(4, 3), a, 0);
        offset(new HXPoint(5, 3), a, 1);
        offset(new HXPoint(6, 4), a, 2);
        offset(new HXPoint(5, 5), a, 3);
        offset(new HXPoint(4, 5), a, 4);
        offset(new HXPoint(4, 4), a, 5);
    }

    private void offset(HXPoint ex, HXPoint a, int dir) {
        a = new HXPoint(a);
        assertEquals(ex, offsetHex(a, dir));
    }

    public void testGetHexAround() {
        HXPoint a = new HXPoint(5, 5);
        Set<HXPoint> set = getHexAround(a, 1);
        assertTrue(set.contains(a));
        assertTrue(set.contains(new HXPoint(5, 4)));
        assertTrue(set.contains(new HXPoint(5, 4)));
        assertTrue(set.contains(new HXPoint(6, 4)));
        assertTrue(set.contains(new HXPoint(6, 5)));
        assertTrue(set.contains(new HXPoint(6, 6)));
        assertTrue(set.contains(new HXPoint(5, 6)));
        assertTrue(set.contains(new HXPoint(4, 5)));
        a = new HXPoint(5, 4);
        set = getHexAround(a, 1);
        assertTrue(set.contains(a));
        assertTrue(set.contains(new HXPoint(4, 3)));
        assertTrue(set.contains(new HXPoint(5, 3)));
        assertTrue(set.contains(new HXPoint(6, 4)));
        assertTrue(set.contains(new HXPoint(5, 5)));
        assertTrue(set.contains(new HXPoint(4, 5)));
        assertTrue(set.contains(new HXPoint(4, 4)));
    }

    public void testTo3DIntInt() {
        HX3D a = to3D(5, 5);
        assertEquals(5, a.x);
        assertEquals(3, a.y);
        assertEquals(8, a.z);
        a = to3D(5, 4);
        assertEquals(4, a.x);
        assertEquals(3, a.y);
        assertEquals(7, a.z);
    }

    public void testTo3DHXPoint() {
        HX3D a = to3D(new HXPoint(5, 5));
        assertEquals(5, a.x);
        assertEquals(3, a.y);
        assertEquals(8, a.z);
        a = to3D(new HXPoint(5, 4));
        assertEquals(4, a.x);
        assertEquals(3, a.y);
        assertEquals(7, a.z);
    }

    public void testTo2DIntIntInt() {
        HXPoint a = to2D(5, 3, 8);
        assertEquals(new HXPoint(5, 5), a);
        a = to2D(4, 3, 7);
        assertEquals(new HXPoint(5, 4), a);
    }

    public void testTo2DHX3D() {
        HXPoint a = to2D(new HX3D(5, 3, 8));
        assertEquals(new HXPoint(5, 5), a);
        a = to2D(new HX3D(4, 3, 7));
        assertEquals(new HXPoint(5, 4), a);
    }

    public void testDistanceHXPointHXPoint() {
        HXPoint a = new HXPoint(2, 9);
        assertEquals(0, distance(new HXPoint(2, 9), a));
        assertEquals(1, distance(new HXPoint(2, 8), a));
        assertEquals(1, distance(new HXPoint(3, 8), a));
        assertEquals(1, distance(new HXPoint(1, 9), a));
        assertEquals(1, distance(new HXPoint(3, 9), a));
        assertEquals(1, distance(new HXPoint(2, 10), a));
        assertEquals(1, distance(new HXPoint(3, 10), a));
        assertEquals(2, distance(new HXPoint(1, 7), a));
        assertEquals(2, distance(new HXPoint(2, 7), a));
        assertEquals(2, distance(new HXPoint(3, 7), a));
        assertEquals(2, distance(new HXPoint(1, 8), a));
        assertEquals(2, distance(new HXPoint(4, 8), a));
        assertEquals(2, distance(new HXPoint(0, 9), a));
        assertEquals(2, distance(new HXPoint(4, 9), a));
        assertEquals(2, distance(new HXPoint(1, 10), a));
        assertEquals(2, distance(new HXPoint(4, 10), a));
        assertEquals(2, distance(new HXPoint(1, 11), a));
        assertEquals(2, distance(new HXPoint(2, 11), a));
        assertEquals(2, distance(new HXPoint(3, 11), a));
        a = new HXPoint(3, 10);
        assertEquals(0, distance(new HXPoint(3, 10), a));
        assertEquals(1, distance(new HXPoint(2, 9), a));
        assertEquals(1, distance(new HXPoint(3, 9), a));
        assertEquals(1, distance(new HXPoint(2, 10), a));
        assertEquals(1, distance(new HXPoint(4, 10), a));
        assertEquals(1, distance(new HXPoint(2, 11), a));
        assertEquals(1, distance(new HXPoint(3, 11), a));
        assertEquals(2, distance(new HXPoint(2, 8), a));
        assertEquals(2, distance(new HXPoint(3, 8), a));
        assertEquals(2, distance(new HXPoint(4, 8), a));
        assertEquals(2, distance(new HXPoint(1, 9), a));
        assertEquals(2, distance(new HXPoint(4, 9), a));
        assertEquals(2, distance(new HXPoint(1, 10), a));
        assertEquals(2, distance(new HXPoint(5, 10), a));
        assertEquals(2, distance(new HXPoint(1, 11), a));
        assertEquals(2, distance(new HXPoint(4, 11), a));
        assertEquals(2, distance(new HXPoint(2, 12), a));
        assertEquals(2, distance(new HXPoint(3, 12), a));
        assertEquals(2, distance(new HXPoint(4, 12), a));
    }

    public void testLine() {
        doLine(new HXPoint(1, 6), new HXPoint(6, 6), 6);
        doLine(new HXPoint(1, 6), new HXPoint(6, 7), 7);
    }

    private void doLine(HXPoint a, HXPoint b, int expectedLength) {
        HXPointCollector collector = new HXPointCollector();
        collector.setControlDuplicates(true);
        line(a, b, true, true, collector);
        List<HXPoint> pts = collector.getPoints();
        System.out.println("Line from " + a + " to " + b + " is " + pts);
        assertEquals(expectedLength, pts.size());
    }
}
