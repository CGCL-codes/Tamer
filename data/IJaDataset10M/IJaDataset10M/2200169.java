package tests.xnet;

import junit.framework.Test;
import junit.framework.TestSuite;

/**
 * Test suite that includes all tests for the Math project.
 */
public class AllTests {

    public static void main(String[] args) {
        junit.textui.TestRunner.run(AllTests.suite());
    }

    public static Test suite() {
        TestSuite suite = tests.TestSuiteFactory.createTestSuite("All javax.net and javax.net.ssl test suites");
        suite.addTest(tests.api.javax.net.AllTests.suite());
        suite.addTest(tests.api.javax.net.ssl.AllTests.suite());
        return suite;
    }
}
