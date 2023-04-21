package br.usp.ime.origami.foldcut.geometry;

import junit.framework.TestCase;
import br.usp.ime.origami.foldcut.structures.simplegraph.Vertex;
import br.usp.ime.origami.model.SimplePoint;

public class LineTest extends TestCase {

    public void testEquals() {
        Line l1 = new Line(new Vertex(0, 0), Math.PI / 4);
        Line l2 = new Line(new Vertex(1, 1), Math.PI / 4);
        assertEquals(l1, l2);
    }

    public void testEquals2() {
        Line l1 = new Line(new Vertex(-1, -1), Math.PI / 4);
        Line l2 = new Line(new Vertex(1, 1), Math.PI / 4);
        assertEquals(l1, l2);
    }

    public void testEquals3() {
        Line l1 = new Line(new Vertex(-1, -1), 0);
        Line l2 = new Line(new Vertex(1, 2), 0);
        assertFalse(l1.equals(l2));
    }

    public void testEquals4() {
        Line l1 = new Line(new Vertex(-1, -1), 0);
        Line l2 = new Line(new Vertex(1, -1), 0);
        assertEquals(l1, l2);
    }

    public void testEquals5() {
        Line l1 = new Line(new Vertex(0, 0), 5 * Math.PI / 4);
        Line l2 = new Line(new Vertex(1, 1), Math.PI / 4);
        assertEquals(l1, l2);
    }

    public void testEquals6() {
        Line l1 = new Line(new Vertex(1, 0), 0);
        Line l2 = new Line(new Vertex(1, 0.00000000000000006), 0);
        assertEquals(l1, l2);
    }

    public void testVertical() {
        Line l1 = new Line(new Vertex(0, 0), Math.PI / 2);
        Line l2 = new Line(new Vertex(0, 1), Math.PI / 2);
        assertEquals(l1, l2);
    }

    public void testNew() {
        Line l1 = new Line(new Vertex(0, 0), 5 * Math.PI / 4 + 10 * Math.PI);
        assertEquals(5 * Math.PI / 4, l1.getAngle(), 0.00001);
    }

    public void testNew2() {
        Line l1 = new Line(new Vertex(0, 0), 5 * Math.PI / 4 - 10 * Math.PI);
        assertEquals(5 * Math.PI / 4, l1.getAngle(), 0.00001);
    }

    public void testNew3() {
        Line l1 = new Line(new Vertex(0, 0), -Math.PI / 4 - 10 * Math.PI);
        assertEquals(7 * Math.PI / 4, l1.getAngle(), 0.00001);
    }

    public void testPerpendicular() {
        Line l1 = new Line(new Vertex(0, 0), Math.PI / 4);
        Line l2 = new Line(new Vertex(1, 1), 3 * Math.PI / 4);
        assertEquals(l2, l1.perpendicularAt(new SimplePoint(1, 1)));
    }

    public void testPerpendicular2() {
        Line l1 = new Line(new Vertex(0, 0), 0);
        Line l2 = new Line(new Vertex(0, 0), Math.PI / 2);
        assertEquals(l2, l1.perpendicularAt(new SimplePoint(0, 0)));
    }

    public void testIntersection() {
        Line l1 = new Line(new Vertex(0, 0), Math.PI / 4);
        Line l2 = new Line(new Vertex(0, 2), 3 * Math.PI / 4);
        assertEquals(new SimplePoint(1, 1), l1.intersectionWith(l2));
    }

    public void testIntersection2() {
        Line l1 = new Line(new Vertex(0, 0), Math.PI / 6);
        Line l2 = new Line(new Vertex(2, 0), 5 * Math.PI / 6);
        assertEquals(new SimplePoint(1, Math.sqrt(3) / 3), l1.intersectionWith(l2));
    }

    public void testIntersectionVertical() {
        Line l1 = new Line(new Vertex(1, 1), Math.PI / 2);
        Line l2 = new Line(new Vertex(0, 2), 0);
        assertEquals(new SimplePoint(1, 2), l1.intersectionWith(l2));
    }

    public void testPointAt() {
        Line l1 = new Line(new Vertex(0, 0), Math.PI / 4);
        assertEquals(1d, l1.pointAt(1), 0.001);
        assertEquals(2d, l1.pointAt(2), 0.001);
    }

    public void testPointAt2() {
        Line l1 = new Line(new Vertex(0, 0), Math.PI / 6);
        assertEquals(Math.sqrt(3) / 3, l1.pointAt(1), 0.001);
        assertEquals(2 * Math.sqrt(3) / 3, l1.pointAt(2), 0.001);
    }

    public void testPointAtVertical() {
        Line l1 = new Line(new Vertex(0, 0), Math.PI / 2);
        assertEquals(0, l1.pointAt(0), 0.001);
        try {
            assertEquals(0, l1.pointAt(10), 0.001);
            fail();
        } catch (IllegalArgumentException e) {
        }
    }

    public void testBissector1() {
        Line l1 = new Line(new SimplePoint(1, 1), Math.PI / 6);
        Line l2 = new Line(new SimplePoint(1, 1), Math.PI / 3);
        assertEquals(new Line(new SimplePoint(0, 0), Math.PI / 4), l1.getBissector(l2));
    }

    public void testBissector2() {
        Line l1 = new Line(new SimplePoint(2, 1 + Math.sqrt(3) / 3), Math.PI / 6);
        Line l2 = new Line(new SimplePoint(1, 1), Math.PI / 3);
        assertEquals(new Line(new SimplePoint(0, 0), Math.PI / 4), l1.getBissector(l2));
    }

    public void testBissector3() {
        Line l1 = new Line(new SimplePoint(1, -1), 11 * Math.PI / 6);
        Line l2 = new Line(new SimplePoint(1, -1), 5 * Math.PI / 3);
        assertEquals(new Line(new SimplePoint(0, 0), 7 * Math.PI / 4), l1.getBissector(l2));
    }

    public void testBissector4() {
        Line l1 = new Line(new SimplePoint(1, -1), -Math.PI / 6);
        Line l2 = new Line(new SimplePoint(1, -1), -Math.PI / 3);
        assertEquals(new Line(new SimplePoint(0, 0), -Math.PI / 4), l1.getBissector(l2));
    }

    public void testBissector5() {
        Line l1 = new Line(new SimplePoint(1, -1), -Math.PI - Math.PI / 6);
        Line l2 = new Line(new SimplePoint(1, -1), -Math.PI * 5 - Math.PI / 3);
        assertEquals(new Line(new SimplePoint(0, 0), -Math.PI * 7 - Math.PI / 4), l1.getBissector(l2));
    }

    public void testBissector6() {
        Line l2 = new Line(new SimplePoint(1, 1), Math.PI / 6);
        Line l1 = new Line(new SimplePoint(1, 1), Math.PI / 3);
        assertEquals(new Line(new SimplePoint(0, 0), Math.PI / 4), l1.getBissector(l2));
    }

    public void testBissector7() {
        Line l1 = new Line(new SimplePoint(1, 1), Math.PI / 3);
        assertEquals(new Line(new SimplePoint(0, 0), Math.PI / 4), l1.getBissector(l1));
    }
}
