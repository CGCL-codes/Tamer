package org.matsim.integration.events;

import junit.framework.Test;
import junit.framework.TestSuite;

public class AllTests {

    public static Test suite() {
        TestSuite suite = new TestSuite("Integration Tests for MATSim simulation events");
        suite.addTestSuite(AgentMoneyEventIntegrationTest.class);
        return suite;
    }
}
