package org.gvsig.i18n;

import junit.framework.Test;
import junit.framework.TestSuite;

public class AllTests {

    public static Test suite() {
        TestSuite suite = new TestSuite("Test for org.gvsig.i18n");
        suite.addTestSuite(TestMessages.class);
        return suite;
    }
}
