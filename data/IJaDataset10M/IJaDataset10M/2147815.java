package org.jhotdraw.test.samples.minimap;

import org.jhotdraw.samples.minimap.MiniMapApplication;
import junit.framework.TestCase;

public class MiniMapApplicationTest extends TestCase {

    private MiniMapApplication minimapapplication;

    /**
	 * Constructor MiniMapApplicationTest is
	 * basically calling the inherited constructor to
	 * initiate the TestCase for use by the Framework.
	 */
    public MiniMapApplicationTest(String name) {
        super(name);
    }

    /**
	 * Factory method for instances of the class to be tested.
	 */
    public MiniMapApplication createInstance() throws Exception {
        return new MiniMapApplication();
    }

    /**
	 * Method setUp is overwriting the framework method to
	 * prepare an instance of this TestCase for a single test.
	 * It's called from the JUnit framework only.
	 */
    protected void setUp() throws Exception {
        super.setUp();
        minimapapplication = createInstance();
    }

    /**
	 * Method tearDown is overwriting the framework method to
	 * clean up after each single test of this TestCase.
	 * It's called from the JUnit framework only.
	 */
    protected void tearDown() throws Exception {
        minimapapplication = null;
        super.tearDown();
    }

    public void testMain() throws Exception {
    }

    public void testVault() throws Exception {
    }
}
