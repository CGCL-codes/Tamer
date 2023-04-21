package com.fddtool.si.system;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import com.fddtool.pd.bug.IBugSystem;

public class TestSystemIntegrationFactory extends TestCase {

    public void testGetBugSystem() {
        IBugSystem bs = SystemIntegrationFactory.getBugSystem();
        assertTrue("Could bot create bug system", bs != null);
        assertTrue("Found dummy bug system", !(bs instanceof BugSystemDummy));
    }

    /**
     * Provides a way to run just this test by itself.
     */
    public static void main(String args[]) {
        junit.textui.TestRunner.run(suite());
    }

    /**
     * Provides a way to include this test into test suite.
     */
    public static Test suite() {
        return new TestSuite(TestSystemIntegrationFactory.class);
    }
}
