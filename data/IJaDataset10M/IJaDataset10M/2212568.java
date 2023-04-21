package playground.wrashid.customTestSuites;

import junit.framework.Test;
import junit.framework.TestSuite;

public class AllPDES2Tests {

    public static Test suite() {
        TestSuite suite = new TestSuite("Test for playground.wrashid.PDES2");
        suite.addTest(playground.wrashid.PDES2.AllTests.suite());
        suite.addTest(playground.wrashid.deqsim.AllPDES2Tests.suite());
        return suite;
    }
}
