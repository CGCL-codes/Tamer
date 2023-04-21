package br.usp.ime.origami.model;

import junit.framework.TestCase;

public class TestMovingTriangulation extends TestCase {

    public void testSimple() {
        MovingPoint p1 = new MovingPoint(new SimplePoint(0, 0), Math.PI / 4, 0.2);
        MovingPoint p2 = new MovingPoint(new SimplePoint(0, 1), 7 * Math.PI / 4, 0.1);
        MovingPoint p3 = new MovingPoint(new SimplePoint(5, 0), 7 * Math.PI / 4, -0.1);
        MovingPoint p4 = new MovingPoint(new SimplePoint(5, 1), Math.PI / 4, -0.1);
        MovingTriangle t1 = new MovingTriangle(p2, p3, p4);
        MovingTriangle t2 = new MovingTriangle(p1, p2, p3);
        MovingTriangulation mt = new MovingTriangulation();
        mt.add(t1);
        mt.add(t2);
        assertTrue(mt.hasMoreCollapsingTriangles());
        assertEquals(mt.nextCollapsingTriangle(), t2);
        assertTrue(mt.hasMoreCollapsingTriangles());
        assertEquals(mt.nextCollapsingTriangle(), t1);
        assertFalse(mt.hasMoreCollapsingTriangles());
    }

    public void testOneTriangleWillNeverColapse() {
        MovingPoint p1 = new MovingPoint(new SimplePoint(0, 0), Math.PI / 4, 0.2);
        MovingPoint p2 = new MovingPoint(new SimplePoint(0, 1), 7 * Math.PI / 4, -0.1);
        MovingPoint p3 = new MovingPoint(new SimplePoint(5, 0), 7 * Math.PI / 4, 0.1);
        MovingPoint p4 = new MovingPoint(new SimplePoint(5, 1), Math.PI / 4, 0.1);
        MovingTriangle t1 = new MovingTriangle(p2, p3, p4);
        MovingTriangle t2 = new MovingTriangle(p1, p2, p3);
        MovingTriangulation mt = new MovingTriangulation();
        mt.add(t1);
        mt.add(t2);
        assertTrue(mt.hasMoreCollapsingTriangles());
        assertEquals(mt.nextCollapsingTriangle(), t2);
        assertFalse(mt.hasMoreCollapsingTriangles());
    }
}
