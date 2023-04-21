package org.matsim.replanning;

import junit.framework.Test;
import junit.framework.TestSuite;
import org.matsim.testcases.TestDepth;

public class AllTests {

    public static Test suite() {
        TestSuite suite = new TestSuite("Test for org.matsim.replanning");
        if (TestDepth.getDepth() == TestDepth.extended) {
            suite.addTestSuite(ReRoutingTest.class);
        }
        suite.addTestSuite(StrategyManagerTest.class);
        suite.addTest(org.matsim.replanning.modules.AllTests.suite());
        suite.addTest(org.matsim.replanning.selectors.AllTests.suite());
        return suite;
    }
}
