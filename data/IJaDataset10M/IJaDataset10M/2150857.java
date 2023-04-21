package base;

import junit.framework.Test;
import junit.framework.TestSuite;

/**
 * Prior to running tests, 
 *
 * @author grahen
 *
 */
public class RunAllStandardTests {

    public static void main(String[] args) {
        junit.textui.TestRunner.run(suite());
    }

    public static Test suite() {
        TestSuite suite = new TestSuite("Test suite for wsml2reasoner (all tests)");
        suite.addTest(RunFacadeTests.suite());
        suite.addTest(RunFrameworkTests.suite());
        suite.addTest(RunEngineTests.suite());
        suite.addTest(RunVariantTests.suite());
        return suite;
    }
}
