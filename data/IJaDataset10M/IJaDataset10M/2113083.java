package org.nakedobjects.nos.client.dnd.drawing;

import junit.framework.TestCase;

public class ShapeTest2 extends TestCase {

    private Shape shape;

    public static void main(final String[] args) {
        junit.textui.TestRunner.run(ShapeTest2.class);
    }

    protected void setUp() throws Exception {
        shape = new Shape(10, 20);
    }

    public void testNew() {
        assertEquals(1, shape.count());
        assertEquals(10, shape.getX()[0]);
        assertEquals(20, shape.getY()[0]);
    }

    public void testAddLine() {
        shape.extendsLine(5, 10);
        assertEquals(2, shape.count());
        assertEquals(15, shape.getX()[1]);
        assertEquals(30, shape.getY()[1]);
    }

    public void testAddTwoLines() {
        shape.extendsLine(5, 10);
        shape.extendsLine(-8, -5);
        assertEquals(3, shape.count());
        assertEquals(7, shape.getX()[2]);
        assertEquals(25, shape.getY()[2]);
    }
}
