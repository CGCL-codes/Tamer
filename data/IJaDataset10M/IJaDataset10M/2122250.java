package de.fuh.xpairtise.tests.common.network.model;

import junit.framework.Test;
import junit.framework.TestSuite;

public class NetworkModelTestsuite {

    public static Test suite() {
        TestSuite suite = new TestSuite("Test for de.fuh.xpairtise.tests.common.network.model");
        suite.addTestSuite(NetworkExceptionTest.class);
        suite.addTestSuite(SessionParametersTest.class);
        return suite;
    }
}
