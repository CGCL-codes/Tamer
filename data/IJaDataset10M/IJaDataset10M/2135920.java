package org.jhotdraw.test.standard;

import java.awt.Point;
import org.jhotdraw.figures.RectangleFigure;
import org.jhotdraw.standard.LocatorHandle;
import org.jhotdraw.standard.RelativeLocator;
import org.jhotdraw.test.JHDTestCase;

public class LocatorHandleTest extends JHDTestCase {

    private LocatorHandle locatorhandle;

    /**
	 * Constructor LocatorHandleTest is
	 * basically calling the inherited constructor to
	 * initiate the TestCase for use by the Framework.
	 */
    public LocatorHandleTest(String name) {
        super(name);
    }

    /**
	 * Factory method for instances of the class to be tested.
	 */
    public LocatorHandle createInstance() throws Exception {
        return new LocatorHandle(new RectangleFigure(new Point(), new Point()), new RelativeLocator(1.0, 2.0));
    }

    /**
	 * Method setUp is overwriting the framework method to
	 * prepare an instance of this TestCase for a single test.
	 * It's called from the JUnit framework only.
	 */
    protected void setUp() throws Exception {
        super.setUp();
        locatorhandle = createInstance();
    }

    /**
	 * Method tearDown is overwriting the framework method to
	 * clean up after each single test of this TestCase.
	 * It's called from the JUnit framework only.
	 */
    protected void tearDown() throws Exception {
        locatorhandle = null;
        super.tearDown();
    }

    public void testGetLocator() throws Exception {
    }

    public void testLocate() throws Exception {
    }

    public void testVault() throws Exception {
    }
}
