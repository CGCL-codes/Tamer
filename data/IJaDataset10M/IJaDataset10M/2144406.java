package org.gromurph.util.junitextensions;

import junit.framework.Test;
import junit.framework.TestSuite;

/**
 * The <code>LoadTestSuite</code> is the topmost
 * test suite used to run all known load tests.
 *
 * @author <a href="mailto:mike@clarkware.com">Mike Clark</a>
 * @author <a href="http://www.clarkware.com">Clarkware Consulting</a>
 *
 * @see BaseTestCase
 */
public class LoadTestSuite extends BaseTestCase {

    /**
	 * Constructs a <code>LoadTestSuite</code>
	 * with the specified name.
	 *
	 * @param name Test suite name.
	 */
    public LoadTestSuite(String name) {
        super(name);
    }

    /**
	 * Assembles and returns a test suite
	 * containing all known load tests.
	 * <p>
	 * New load tests should be added here.
	 *
	 * @return A non-null test suite.
	 */
    public static Test suite() {
        TestSuite suite = new TestSuite();
        suite.addTest(ExampleLoadTest.suite());
        suite.addTest(UrlLoadTest.suite());
        return suite;
    }

    /**
	 * Test main.
	 */
    public static void main(String[] args) {
        instanceMain(LoadTestSuite.class.getName(), args);
    }
}
