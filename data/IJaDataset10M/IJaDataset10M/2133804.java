package com.flagstone.transform.test;

import junit.framework.Test;
import junit.framework.TestSuite;

public class MovieObjectTests {

    public static Test suite() {
        TestSuite suite = new TestSuite();
        suite.addTestSuite(FSProtectTest.class);
        suite.addTestSuite(FSSetBackgroundColorTest.class);
        suite.addTestSuite(FSShowFrameTest.class);
        return suite;
    }
}
