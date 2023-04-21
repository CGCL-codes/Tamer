package org.jhotdraw.test.samples.pert;

import org.jhotdraw.samples.pert.PertApplication;
import junit.framework.TestCase;

public class PertApplicationTest extends TestCase {

    private PertApplication pertapplication;

    /**
	 * Constructor PertApplicationTest is
	 * basically calling the inherited constructor to
	 * initiate the TestCase for use by the Framework.
	 */
    public PertApplicationTest(String name) {
        super(name);
    }

    /**
	 * Factory method for instances of the class to be tested.
	 */
    public PertApplication createInstance() throws Exception {
        return new PertApplication();
    }

    /**
	 * Method setUp is overwriting the framework method to
	 * prepare an instance of this TestCase for a single test.
	 * It's called from the JUnit framework only.
	 */
    protected void setUp() throws Exception {
        super.setUp();
        pertapplication = createInstance();
    }

    /**
	 * Method tearDown is overwriting the framework method to
	 * clean up after each single test of this TestCase.
	 * It's called from the JUnit framework only.
	 */
    protected void tearDown() throws Exception {
        pertapplication = null;
        super.tearDown();
    }

    public void testMain() throws Exception {
    }

    public void testVault() throws Exception {
    }
}
