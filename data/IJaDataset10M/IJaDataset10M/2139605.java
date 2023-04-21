package client;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

public class Client_testall extends TestCase {

    public static Test suite() {
        TestSuite suite = new TestSuite("Test for client");
        suite.addTestSuite(LocationParser_test.class);
        suite.addTestSuite(Waypoint_test.class);
        suite.addTestSuite(DeviceControl_test.class);
        return suite;
    }
}
