package playground.marcel.pt.queuesim;

import junit.framework.Test;
import junit.framework.TestSuite;

public abstract class AllTests {

    public static Test suite() {
        TestSuite suite = new TestSuite("Tests for " + AllTests.class.getPackage().getName());
        suite.addTestSuite(TransitAgentTest.class);
        suite.addTestSuite(TransitDriverTest.class);
        suite.addTestSuite(TransitQueueVehicleTest.class);
        suite.addTestSuite(TransitQueueNetworkTest.class);
        suite.addTestSuite(TransitQueueSimulationTest.class);
        suite.addTestSuite(TransitStopAgentTrackerTest.class);
        return suite;
    }
}
