package tests.prefs;

import junit.framework.Test;
import junit.framework.TestSuite;

/**
 * Test suite that includes all tests for the Prefs project.
 *
 */
public class AllTests {

    public static void main(String[] args) {
        junit.textui.TestRunner.run(AllTests.suite());
    }

    public static Test suite() {
        TestSuite suite = tests.TestSuiteFactory.createTestSuite("All Prefs test suites");
        suite.addTest(org.apache.harmony.prefs.tests.java.util.prefs.AllTests.suite());
        return suite;
    }
}
