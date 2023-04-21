package org.piccolo2d.jdk16.nodes;

import java.awt.BasicStroke;
import java.awt.Shape;
import java.awt.Stroke;
import java.awt.geom.Path2D;
import java.awt.geom.Rectangle2D;

/**
 * Unit test for PPath.Double.
 */
public class PPathDoubleTest extends AbstractPPathTest {

    /** {@inheritDoc} */
    protected PPath createPathNode() {
        return new PPath.Double();
    }

    public void testNoArgConstructor() {
        assertNotNull(new PPath.Double());
    }

    public void testStrokeConstructor() {
        assertNotNull(new PPath.Double((Stroke) null));
        assertNotNull(new PPath.Double(new BasicStroke(2.0f)));
    }

    public void testShapeConstructor() {
        assertNotNull(new PPath.Double(new Rectangle2D.Double(0.0d, 0.0d, 100.0d, 100.0d)));
    }

    public void testShapeStrokeConstructor() {
        assertNotNull(new PPath.Double(new Rectangle2D.Double(0.0d, 0.0d, 100.0d, 100.0d), null));
        assertNotNull(new PPath.Double(new Rectangle2D.Double(0.0d, 0.0d, 100.0d, 100.0d), new BasicStroke(2.0f)));
    }

    public void testShapeConstructorNullArgument() {
        try {
            new PPath.Double((Shape) null);
            fail("ctr((Shape) null) expected NullPointerException");
        } catch (NullPointerException e) {
        }
    }

    public void testShapeStrokeConstructorNullArgument() {
        try {
            new PPath.Double((Shape) null, null);
            fail("ctr((Shape) null, ) expected NullPointerException");
        } catch (NullPointerException e) {
        }
    }

    public void testPathConstructor() {
        assertNotNull(new PPath.Double(new Path2D.Double()));
    }

    public void testPathStrokeConstructor() {
        assertNotNull(new PPath.Double(new Path2D.Double(), null));
        assertNotNull(new PPath.Double(new Path2D.Double(), new BasicStroke(2.0f)));
    }

    public void testPathConstructorNullArgument() {
        try {
            new PPath.Double((Path2D) null);
            fail("ctr((Path2D) null) expected NullPointerException");
        } catch (NullPointerException e) {
        }
    }

    public void testPathStrokeConstructorNullArgument() {
        try {
            new PPath.Double((Path2D) null, null);
            fail("ctr((Path2D) null, ) expected NullPointerException");
        } catch (NullPointerException e) {
        }
    }
}
