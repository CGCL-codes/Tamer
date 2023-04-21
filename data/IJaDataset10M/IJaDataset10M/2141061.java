package CH.ifa.draw.test.contrib;

import CH.ifa.draw.contrib.SVGDrawApp;
import junit.framework.TestCase;

public class SVGDrawAppTest extends TestCase {

    private SVGDrawApp svgdrawapp;

    /**
	 * Constructor SVGDrawAppTest is
	 * basically calling the inherited constructor to
	 * initiate the TestCase for use by the Framework.
	 */
    public SVGDrawAppTest(String name) {
        super(name);
    }

    /**
	 * Factory method for instances of the class to be tested.
	 */
    public CH.ifa.draw.contrib.SVGDrawApp createInstance() throws Exception {
        return new CH.ifa.draw.contrib.SVGDrawApp();
    }

    /**
	 * Method setUp is overwriting the framework method to
	 * prepare an instance of this TestCase for a single test.
	 * It's called from the JUnit framework only.
	 */
    protected void setUp() throws Exception {
        super.setUp();
        svgdrawapp = createInstance();
    }

    /**
	 * Method tearDown is overwriting the framework method to
	 * clean up after each single test of this TestCase.
	 * It's called from the JUnit framework only.
	 */
    protected void tearDown() throws Exception {
        svgdrawapp = null;
        super.tearDown();
    }

    public void testMain() throws Exception {
    }

    public void testCreateStorageFormatManager() throws Exception {
    }

    public void testVault() throws Exception {
    }
}
