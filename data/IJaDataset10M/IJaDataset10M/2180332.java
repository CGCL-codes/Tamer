package playground.marcel.pt.transitSchedule.modules;

import junit.framework.Test;
import junit.framework.TestSuite;

public class AllTests {

    public static Test suite() {
        TestSuite suite = new TestSuite("Tests for " + AllTests.class.getPackage().getName());
        suite.addTestSuite(CreateTimetableForStopTest.class);
        return suite;
    }
}
