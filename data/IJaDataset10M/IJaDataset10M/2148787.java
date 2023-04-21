package org.vizzini.ui.graphics;

import junit.framework.TestCase;
import junit.textui.TestRunner;
import org.vizzini.math.Quaternion;
import org.vizzini.math.Vector;
import org.vizzini.ui.graphics.shape.AbstractShape;
import java.awt.Color;
import java.awt.Rectangle;
import java.util.Iterator;

/**
 * Provides unit tests for the <code>ShapeGroup</code> class.
 *
 * @author   Jeffrey M. Thompson
 * @version  v0.4
 * @since    v0.2
 */
public class ShapeGroupTest extends TestCase {

    /** First color. */
    protected static final Color COLOR0 = Color.RED;

    /** Second color. */
    protected static final Color COLOR1 = Color.GREEN;

    /** First visible flag. */
    protected static final boolean VISIBLE0 = true;

    /** Second visible flag. */
    protected static final boolean VISIBLE1 = false;

    /** First wireframe flag. */
    protected static final boolean WIREFRAME0 = true;

    /** Second wireframe flag. */
    protected static final boolean WIREFRAME1 = false;

    /** First position. */
    protected static final Vector POSITION0 = new Vector(0.0, 10.0, -20.0);

    /** Second position. */
    protected static final Vector POSITION1 = new Vector(10.0, 20.0, -30.0);

    /** First group. */
    protected ShapeGroup _group0;

    /** First shape. */
    protected IShape _shape0;

    /** Second shape. */
    protected IShape _shape1;

    /**
     * Application method.
     *
     * @param  args  Application arguments.
     *
     * @since  v0.2
     */
    public static void main(String[] args) {
        TestRunner.run(ShapeGroupTest.class);
    }

    /**
     * Test the <code>add()</code> method.
     *
     * @since  v0.2
     */
    public void testAdd() {
        assertEquals(2, _group0.size());
        IShape shape = new TestShape();
        _group0.add(shape);
        assertEquals(3, _group0.size());
    }

    /**
     * Test the <code>clear()</code> method.
     *
     * @since  v0.2
     */
    public void testClear() {
        assertEquals(2, _group0.size());
        _group0.clear();
        assertEquals(0, _group0.size());
    }

    /**
     * Test the <code>getBounds()</code> method.
     *
     * @since  v0.2
     */
    public void testGetBounds() {
        Rectangle expected = new Rectangle(34, 69, 10, 40);
        Rectangle result = _group0.getBounds();
        assertEquals(expected, result);
    }

    /**
     * Test the <code>isEmpty()</code> method.
     *
     * @since  v0.2
     */
    public void testIsEmpty() {
        assertFalse(_group0.isEmpty());
        _group0.clear();
        assertTrue(_group0.isEmpty());
    }

    /**
     * Test the <code>iterator()</code> method.
     *
     * @since  v0.2
     */
    public void testIterator() {
        Iterator<IShape> iter = _group0.iterator();
        assertTrue(iter.hasNext());
        assertEquals(_shape0, iter.next());
        assertTrue(iter.hasNext());
        assertEquals(_shape1, iter.next());
        assertFalse(iter.hasNext());
    }

    /**
     * Test the <code>remove()</code> method.
     *
     * @since  v0.2
     */
    public void testRemove() {
        assertEquals(2, _group0.size());
        _group0.remove(_shape0);
        assertEquals(1, _group0.size());
    }

    /**
     * Test the <code>size()</code> method.
     *
     * @since  v0.2
     */
    public void testSize() {
        assertEquals(2, _group0.size());
    }

    /**
     * @see  junit.framework.TestCase#setUp()
     */
    @Override
    protected void setUp() {
        _shape0 = new TestShape();
        _shape0.setColor(COLOR0);
        _shape0.setVisible(VISIBLE0);
        _shape0.setWireframe(WIREFRAME0);
        _shape0.getState().setPosition(POSITION0);
        _shape1 = new TestShape();
        _shape1.setColor(COLOR1);
        _shape1.setVisible(VISIBLE1);
        _shape1.setWireframe(WIREFRAME1);
        _shape1.getState().setPosition(POSITION1);
        Quaternion q = Quaternion.ZERO;
        Vector v = new Vector(10.0, 20.0, -30.0);
        double magnify = 1.0;
        int offsetX = 0;
        int offsetY = 0;
        double d = 5000.0;
        _group0 = new ShapeGroup();
        _group0.add(_shape0);
        _group0.add(_shape1);
        _group0.computeScreenPoints(q, v, magnify, offsetX, offsetY, d);
    }

    /**
     * @see  junit.framework.TestCase#tearDown()
     */
    @Override
    protected void tearDown() {
        _shape0 = null;
        _shape1 = null;
    }

    /**
     * Provides a test shape.
     *
     * @author   Jeffrey M. Thompson
     * @version  v0.4
     * @since    v0.2
     */
    class TestShape extends AbstractShape {

        /**
         * Construct this object.
         */
        public TestShape() {
            super(10.0, 20.0, 30.0);
            int width = 10;
            int height = 20;
            int depth = 30;
            int halfW = width / 2;
            int halfH = height / 2;
            int halfD = depth / 2;
            _points = new Vector[8];
            _points[0] = new Vector(-halfW, -halfH, halfD);
            _points[1] = new Vector(-halfW, -halfH, -halfD);
            _points[2] = new Vector(halfW, -halfH, -halfD);
            _points[3] = new Vector(halfW, -halfH, halfD);
            _points[4] = new Vector(halfW, halfH, halfD);
            _points[5] = new Vector(halfW, halfH, -halfD);
            _points[6] = new Vector(-halfW, halfH, -halfD);
            _points[7] = new Vector(-halfW, halfH, halfD);
            _screenPoints = new Vector[_points.length];
        }
    }
}
