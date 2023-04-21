package org.impalaframework.web.test;

import junit.framework.Test;
import junit.framework.TestSuite;
import org.impalaframework.web.StartJettyTest;

public class ManualTests {

    public static Test suite() {
        TestSuite suite = new TestSuite();
        suite.addTestSuite(StartJettyTest.class);
        return suite;
    }
}
