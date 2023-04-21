package nl.alterra.openmi.sdk.spatial;

import junit.framework.TestCase;
import java.util.ArrayList;

/**
 * Unit test for the XYPolygon class.
 */
public class TestXYPolygon extends TestCase {

    public void testGetArea() {
        XYPolygon xypolygon = new XYPolygon();
        xypolygon.getPoints().add(new XYPoint(1, 1));
        xypolygon.getPoints().add(new XYPoint(9, 1));
        xypolygon.getPoints().add(new XYPoint(9, 6));
        xypolygon.getPoints().add(new XYPoint(1, 6));
        assertEquals((double) 40, xypolygon.getArea());
        XYPolygon xypolygon2 = new XYPolygon();
        xypolygon2.getPoints().add(new XYPoint(1, 1));
        xypolygon2.getPoints().add(new XYPoint(9, 1));
        xypolygon2.getPoints().add(new XYPoint(9, 6));
        assertEquals((double) 20, xypolygon2.getArea());
        XYPolygon xypolygon3 = new XYPolygon();
        xypolygon3.getPoints().add(new XYPoint(1, 1));
        xypolygon3.getPoints().add(new XYPoint(5, 3));
        xypolygon3.getPoints().add(new XYPoint(9, 1));
        xypolygon3.getPoints().add(new XYPoint(9, 6));
        xypolygon3.getPoints().add(new XYPoint(1, 6));
        assertEquals((double) 32, xypolygon3.getArea());
        XYPolygon xypolygon4 = new XYPolygon();
        xypolygon4.getPoints().add(new XYPoint(1, 1));
        xypolygon4.getPoints().add(new XYPoint(9, 1));
        xypolygon4.getPoints().add(new XYPoint(5, 5));
        xypolygon4.getPoints().add(new XYPoint(5, 3));
        xypolygon4.getPoints().add(new XYPoint(3, 3));
        xypolygon4.getPoints().add(new XYPoint(3, 8));
        xypolygon4.getPoints().add(new XYPoint(9, 8));
        xypolygon4.getPoints().add(new XYPoint(9, 11));
        xypolygon4.getPoints().add(new XYPoint(1, 11));
        assertEquals((double) 50, xypolygon4.getArea());
    }

    public void testGetLine() {
        XYPolygon xypolygon = new XYPolygon();
        xypolygon.getPoints().add(new XYPoint(1, 2));
        xypolygon.getPoints().add(new XYPoint(4, 3));
        xypolygon.getPoints().add(new XYPoint(2, 5));
        assertEquals(new XYLine(1, 2, 4, 3), xypolygon.getLine(0));
        assertEquals(new XYLine(4, 3, 2, 5), xypolygon.getLine(1));
        assertEquals(new XYLine(2, 5, 1, 2), xypolygon.getLine(2));
        XYPolygon xypolygon4 = new XYPolygon();
        xypolygon4.getPoints().add(new XYPoint(1, 1));
        xypolygon4.getPoints().add(new XYPoint(9, 1));
        xypolygon4.getPoints().add(new XYPoint(5, 5));
        xypolygon4.getPoints().add(new XYPoint(5, 3));
        xypolygon4.getPoints().add(new XYPoint(3, 3));
        xypolygon4.getPoints().add(new XYPoint(3, 8));
        xypolygon4.getPoints().add(new XYPoint(9, 8));
        xypolygon4.getPoints().add(new XYPoint(9, 11));
        xypolygon4.getPoints().add(new XYPoint(1, 11));
        assertEquals(new XYLine(1, 1, 9, 1), xypolygon4.getLine(0));
        assertEquals(new XYLine(9, 1, 5, 5), xypolygon4.getLine(1));
        assertEquals(new XYLine(5, 5, 5, 3), xypolygon4.getLine(2));
        assertEquals(new XYLine(5, 3, 3, 3), xypolygon4.getLine(3));
        assertEquals(new XYLine(3, 3, 3, 8), xypolygon4.getLine(4));
        assertEquals(new XYLine(3, 8, 9, 8), xypolygon4.getLine(5));
        assertEquals(new XYLine(9, 8, 9, 11), xypolygon4.getLine(6));
        assertEquals(new XYLine(9, 11, 1, 11), xypolygon4.getLine(7));
        assertEquals(new XYLine(1, 11, 1, 1), xypolygon4.getLine(8));
    }

    public void testFindEar() {
        XYPolygon p1 = new XYPolygon();
        p1.getPoints().add(new XYPoint(0, 3));
        p1.getPoints().add(new XYPoint(3, 0));
        p1.getPoints().add(new XYPoint(8, 0));
        p1.getPoints().add(new XYPoint(8, 2));
        p1.getPoints().add(new XYPoint(3, 1));
        p1.getPoints().add(new XYPoint(3, 3));
        p1.getPoints().add(new XYPoint(8, 3));
        p1.getPoints().add(new XYPoint(4, 7));
        assertEquals(2, p1.findEar());
    }

    public void testIsIntersected() {
        XYPolygon p1 = new XYPolygon();
        p1.getPoints().add(new XYPoint(0, 3));
        p1.getPoints().add(new XYPoint(3, 0));
        p1.getPoints().add(new XYPoint(8, 0));
        p1.getPoints().add(new XYPoint(8, 2));
        p1.getPoints().add(new XYPoint(3, 1));
        p1.getPoints().add(new XYPoint(3, 3));
        p1.getPoints().add(new XYPoint(8, 3));
        p1.getPoints().add(new XYPoint(4, 7));
        assertEquals(true, p1.isIntersected(0));
        assertEquals(true, p1.isIntersected(1));
        assertEquals(false, p1.isIntersected(2));
    }

    public void testIsConvex() {
        XYPolygon xypolygon4 = new XYPolygon();
        xypolygon4.getPoints().add(new XYPoint(1, 1));
        xypolygon4.getPoints().add(new XYPoint(9, 1));
        xypolygon4.getPoints().add(new XYPoint(5, 5));
        xypolygon4.getPoints().add(new XYPoint(5, 3));
        xypolygon4.getPoints().add(new XYPoint(3, 3));
        xypolygon4.getPoints().add(new XYPoint(3, 8));
        xypolygon4.getPoints().add(new XYPoint(9, 8));
        xypolygon4.getPoints().add(new XYPoint(9, 11));
        xypolygon4.getPoints().add(new XYPoint(1, 11));
        assertEquals(true, xypolygon4.isConvex(0));
        assertEquals(true, xypolygon4.isConvex(1));
        assertEquals(true, xypolygon4.isConvex(2));
        assertEquals(false, xypolygon4.isConvex(3));
        assertEquals(false, xypolygon4.isConvex(4));
        assertEquals(false, xypolygon4.isConvex(5));
        assertEquals(true, xypolygon4.isConvex(6));
        assertEquals(true, xypolygon4.isConvex(7));
        assertEquals(true, xypolygon4.isConvex(8));
    }

    public void testGetTriangulation() {
        XYPolygon p1 = new XYPolygon();
        p1.getPoints().add(new XYPoint(0, 3));
        p1.getPoints().add(new XYPoint(3, 0));
        p1.getPoints().add(new XYPoint(8, 0));
        p1.getPoints().add(new XYPoint(8, 2));
        p1.getPoints().add(new XYPoint(3, 1));
        p1.getPoints().add(new XYPoint(3, 3));
        p1.getPoints().add(new XYPoint(8, 3));
        p1.getPoints().add(new XYPoint(4, 7));
        ArrayList triangleList = p1.getTriangulation();
        XYPolygon refTriangle1 = new XYPolygon();
        refTriangle1.getPoints().add(new XYPoint(3, 0));
        refTriangle1.getPoints().add(new XYPoint(8, 0));
        refTriangle1.getPoints().add(new XYPoint(8, 2));
        XYPolygon refTriangle2 = new XYPolygon();
        refTriangle2.getPoints().add(new XYPoint(3, 0));
        refTriangle2.getPoints().add(new XYPoint(8, 2));
        refTriangle2.getPoints().add(new XYPoint(3, 1));
        XYPolygon refTriangle3 = new XYPolygon();
        refTriangle3.getPoints().add(new XYPoint(0, 3));
        refTriangle3.getPoints().add(new XYPoint(3, 0));
        refTriangle3.getPoints().add(new XYPoint(3, 1));
        XYPolygon refTriangle4 = new XYPolygon();
        refTriangle4.getPoints().add(new XYPoint(0, 3));
        refTriangle4.getPoints().add(new XYPoint(3, 1));
        refTriangle4.getPoints().add(new XYPoint(3, 3));
        XYPolygon refTriangle5 = new XYPolygon();
        refTriangle5.getPoints().add(new XYPoint(4, 7));
        refTriangle5.getPoints().add(new XYPoint(0, 3));
        refTriangle5.getPoints().add(new XYPoint(3, 3));
        XYPolygon refTriangle6 = new XYPolygon();
        refTriangle6.getPoints().add(new XYPoint(3, 3));
        refTriangle6.getPoints().add(new XYPoint(8, 3));
        refTriangle6.getPoints().add(new XYPoint(4, 7));
        assertEquals(refTriangle1, triangleList.get(0));
        assertEquals(refTriangle2, triangleList.get(1));
        assertEquals(refTriangle3, triangleList.get(2));
        assertEquals(refTriangle4, triangleList.get(3));
        assertEquals(refTriangle5, triangleList.get(4));
        assertEquals(refTriangle6, triangleList.get(5));
    }

    public void testEquals() {
        XYPolygon p1 = new XYPolygon();
        p1.getPoints().add(new XYPoint(0, 3));
        p1.getPoints().add(new XYPoint(3, 0));
        p1.getPoints().add(new XYPoint(8, 0));
        p1.getPoints().add(new XYPoint(8, 2));
        p1.getPoints().add(new XYPoint(3, 1));
        p1.getPoints().add(new XYPoint(3, 3));
        p1.getPoints().add(new XYPoint(8, 3));
        p1.getPoints().add(new XYPoint(4, 7));
        XYPolygon p2 = new XYPolygon();
        p2.getPoints().add(new XYPoint(0, 3));
        p2.getPoints().add(new XYPoint(3, 0));
        p2.getPoints().add(new XYPoint(8, 0));
        p2.getPoints().add(new XYPoint(8, 2));
        p2.getPoints().add(new XYPoint(3, 1));
        p2.getPoints().add(new XYPoint(3, 3));
        p2.getPoints().add(new XYPoint(8, 3));
        p2.getPoints().add(new XYPoint(4, 7));
        XYPolygon p3 = new XYPolygon();
        p3.getPoints().add(new XYPoint(0, 3));
        p3.getPoints().add(new XYPoint(3, 0));
        p3.getPoints().add(new XYPoint(8, 0));
        p3.getPoints().add(new XYPoint(8, 2));
        p3.getPoints().add(new XYPoint(3, 1.1));
        p3.getPoints().add(new XYPoint(3, 3));
        p3.getPoints().add(new XYPoint(8, 3));
        p3.getPoints().add(new XYPoint(4, 7));
        XYPolygon p4 = new XYPolygon();
        p4.getPoints().add(new XYPoint(0, 3));
        p4.getPoints().add(new XYPoint(3, 0));
        p4.getPoints().add(new XYPoint(8, 0));
        p4.getPoints().add(new XYPoint(8, 2));
        p4.getPoints().add(new XYPoint(3, 1));
        p4.getPoints().add(new XYPoint(3, 3));
        p4.getPoints().add(new XYPoint(8, 3));
        XYPolyline p5 = new XYPolyline();
        p5.getPoints().add(new XYPoint(0, 3));
        p5.getPoints().add(new XYPoint(3, 0));
        p5.getPoints().add(new XYPoint(8, 0));
        p5.getPoints().add(new XYPoint(8, 2));
        p5.getPoints().add(new XYPoint(3, 1.1));
        p5.getPoints().add(new XYPoint(3, 3));
        p5.getPoints().add(new XYPoint(8, 3));
        p5.getPoints().add(new XYPoint(4, 7));
        assertEquals(true, p1.equals(p1));
        assertEquals(true, p1.equals(p2));
        assertEquals(false, p1.equals(p3));
        assertEquals(false, p1.equals(p4));
        assertEquals(false, p1.equals(p5));
    }
}
