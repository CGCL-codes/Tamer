package org.jhotdraw.test.contrib;

import org.jhotdraw.contrib.PolygonFigure;
import org.jhotdraw.contrib.PolygonHandle;
import org.jhotdraw.test.JHDTestCase;

public class PolygonHandleTest extends JHDTestCase {

    private PolygonHandle polygonhandle;

    /**
	 * Constructor PolygonHandleTest is
	 * basically calling the inherited constructor to
	 * initiate the TestCase for use by the Framework.
	 */
    public PolygonHandleTest(String name) {
        super(name);
    }

    /**
	 * Factory method for instances of the class to be tested.
	 */
    public PolygonHandle createInstance() throws Exception {
        PolygonFigure figure = new PolygonFigure(20, 20);
        figure.addPoint(30, 30);
        figure.addPoint(40, 40);
        return new PolygonHandle(figure, PolygonFigure.locator(2), 2);
    }

    /**
	 * Method setUp is overwriting the framework method to
	 * prepare an instance of this TestCase for a single test.
	 * It's called from the JUnit framework only.
	 */
    protected void setUp() throws Exception {
        super.setUp();
        polygonhandle = createInstance();
    }

    /**
	 * Method tearDown is overwriting the framework method to
	 * clean up after each single test of this TestCase.
	 * It's called from the JUnit framework only.
	 */
    protected void tearDown() throws Exception {
        polygonhandle = null;
        super.tearDown();
    }

    public void testInvokeStart() throws Exception {
    }

    public void testInvokeStep() throws Exception {
    }

    public void testInvokeEnd() throws Exception {
    }

    public void testLocate() throws Exception {
    }

    public void testVault() throws Exception {
    }
}
