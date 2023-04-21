package org.matsim.replanning.modules;

import junit.framework.Test;
import junit.framework.TestSuite;

public class AllTests {

    public static Test suite() {
        TestSuite suite = new TestSuite("Test for org.matsim.replanning.modules");
        suite.addTestSuite(TimeAllocationMutatorTest.class);
        return suite;
    }
}
