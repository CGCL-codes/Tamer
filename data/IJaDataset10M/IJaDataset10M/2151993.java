package org.jhotdraw.test.util;

import java.awt.Dimension;
import java.awt.geom.Point2D;
import org.jhotdraw.util.Bounds;
import junit.framework.TestCase;

public class BoundsTest extends TestCase {

    private Bounds bounds;

    /**
	 * Constructor BoundsTest is
	 * basically calling the inherited constructor to
	 * initiate the TestCase for use by the Framework.
	 */
    public BoundsTest(String name) {
        super(name);
    }

    /**
	 * Factory method for instances of the class to be tested.
	 */
    public Bounds createInstance() throws Exception {
        return new Bounds(new Dimension(100, 100));
    }

    /**
	 * Method setUp is overwriting the framework method to
	 * prepare an instance of this TestCase for a single test.
	 * It's called from the JUnit framework only.
	 */
    protected void setUp() throws Exception {
        super.setUp();
        bounds = createInstance();
    }

    /**
	 * Method tearDown is overwriting the framework method to
	 * clean up after each single test of this TestCase.
	 * It's called from the JUnit framework only.
	 */
    protected void tearDown() throws Exception {
        bounds = null;
        super.tearDown();
    }

    public void testGetLesserX() throws Exception {
    }

    public void testGetGreaterX() throws Exception {
    }

    public void testGetLesserY() throws Exception {
    }

    public void testGetGreaterY() throws Exception {
    }

    public void testGetWest() throws Exception {
    }

    public void testGetEast() throws Exception {
    }

    public void testGetSouth() throws Exception {
    }

    public void testGetNorth() throws Exception {
    }

    public void testGetWidth() throws Exception {
    }

    public void testGetHeight() throws Exception {
    }

    public void testAsRectangle2D() throws Exception {
    }

    public void testSetGetCenter() throws Exception {
        Point2D[] tests = { new Point2D.Double(2.0, 3.0) };
        for (int i = 0; i < tests.length; i++) {
            bounds.setCenter(tests[i]);
            assertEquals(tests[i], bounds.getCenter());
        }
    }

    /**
	  * Test a null argument to setCenter.  Expect an IllegalArgumentException
	  * 
	  * @see org.jhotdraw.util.Bounds#setCenter(java.awt.geom.Point2D)
	  */
    public void testSetNullCenter() throws Exception {
        Point2D original = bounds.getCenter();
        try {
            bounds.setCenter(null);
            fail("IllegalArgumentException expected");
        } catch (IllegalArgumentException ok) {
            assertEquals("setCenter(null) altered property value", original, bounds.getCenter());
        }
    }

    public void testZoomBy() throws Exception {
    }

    public void testShiftBy() throws Exception {
    }

    public void testOffset() throws Exception {
    }

    public void testExpandToRatio() throws Exception {
    }

    public void testIncludeXCoordinate() throws Exception {
    }

    public void testIncludeYCoordinate() throws Exception {
    }

    public void testIncludePoint() throws Exception {
    }

    public void testIncludeLine() throws Exception {
    }

    public void testIncludeBounds() throws Exception {
    }

    public void testIncludeRectangle2D() throws Exception {
    }

    public void testIntersect() throws Exception {
    }

    public void testIntersectsPoint() throws Exception {
    }

    public void testIntersectsLine() throws Exception {
    }

    public void testIntersectsBounds() throws Exception {
    }

    public void testCompletelyContainsLine() throws Exception {
    }

    public void testIsCompletelyInside() throws Exception {
    }

    public void testCropLine() throws Exception {
    }

    public void testEquals() throws Exception {
    }

    public void testHashCode() throws Exception {
    }

    public void testToString() throws Exception {
    }

    public void testVault() throws Exception {
    }
}
