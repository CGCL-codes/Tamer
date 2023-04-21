package CH.ifa.draw.test.standard;

import CH.ifa.draw.standard.PeripheralLocator;
import junit.framework.TestCase;

public class PeripheralLocatorTest extends TestCase {

    private PeripheralLocator peripherallocator;

    /**
	 * Constructor PeripheralLocatorTest is
	 * basically calling the inherited constructor to
	 * initiate the TestCase for use by the Framework.
	 */
    public PeripheralLocatorTest(String name) {
        super(name);
    }

    /**
	 * Factory method for instances of the class to be tested.
	 */
    public CH.ifa.draw.standard.PeripheralLocator createInstance() throws Exception {
        return new CH.ifa.draw.standard.PeripheralLocator(5, 10);
    }

    /**
	 * Method setUp is overwriting the framework method to
	 * prepare an instance of this TestCase for a single test.
	 * It's called from the JUnit framework only.
	 */
    protected void setUp() throws Exception {
        super.setUp();
        peripherallocator = createInstance();
    }

    /**
	 * Method tearDown is overwriting the framework method to
	 * clean up after each single test of this TestCase.
	 * It's called from the JUnit framework only.
	 */
    protected void tearDown() throws Exception {
        peripherallocator = null;
        super.tearDown();
    }

    public void testLocate() throws Exception {
    }

    public void testVault() throws Exception {
    }
}
