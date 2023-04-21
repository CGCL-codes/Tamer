package org.matsim.router;

import junit.framework.Test;
import junit.framework.TestSuite;

public class AllTests {

    public static Test suite() {
        TestSuite suite = new TestSuite("Test for org.matsim.router");
        suite.addTestSuite(RoutingTest.class);
        return suite;
    }
}
