package org.matsim.utils.geometry.geotools;

import junit.framework.Test;
import junit.framework.TestSuite;

public class AllTests {

    public static Test suite() {
        TestSuite suite = new TestSuite("Test for org.matsim.utils.geometry.geotools");
        suite.addTestSuite(MGCTest.class);
        return suite;
    }
}
