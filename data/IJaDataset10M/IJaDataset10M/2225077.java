package org.objectstyle.wolips.core.tests;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import org.objectstyle.wolips.core.resources.tests.CoreResourcesTestSuite;

/**
 * Run all compiler regression tests
 */
public class CoreTestSuite extends TestCase {

    /**
	 * @param testName
	 */
    public CoreTestSuite(String testName) {
        super(testName);
    }

    /**
	 * @return
	 * @throws Exception
	 */
    public static Test suite() throws Exception {
        TestSuite suite = new TestSuite();
        suite.addTest(CoreResourcesTestSuite.suite());
        return suite;
    }
}
