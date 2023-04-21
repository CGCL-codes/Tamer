package net.narusas.game.pushpush;

import junit.framework.Test;
import junit.framework.TestSuite;

public class AllTests {

    public static Test suite() {
        TestSuite suite = new TestSuite("Test for net.narusas.game.pushpush");
        suite.addTestSuite(WorkerTest.class);
        suite.addTestSuite(MoveStrategyTest.class);
        suite.addTestSuite(MapTest.class);
        return suite;
    }
}
