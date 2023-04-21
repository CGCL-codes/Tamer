package jopt.js.test.arc;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

public class ArcTestSuite extends TestCase {

    public ArcTestSuite(java.lang.String testName) {
        super(testName);
    }

    public static Test suite() {
        TestSuite suite = new TestSuite();
        suite.addTestSuite(ForwardCheckArcTest.class);
        suite.addTestSuite(ForwardCheckReflexArcTest.class);
        suite.addTestSuite(TemporalArcTest.class);
        return suite;
    }
}
