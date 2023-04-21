package org.matsim.facilities;

import junit.framework.Test;
import junit.framework.TestSuite;

public class AllTests {

    public static Test suite() {
        TestSuite suite = new TestSuite("Tests for org.matsim.facilities");
        suite.addTest(org.matsim.facilities.algorithms.AllTests.suite());
        suite.addTestSuite(FacilitiesParserWriterTest.class);
        return suite;
    }
}
