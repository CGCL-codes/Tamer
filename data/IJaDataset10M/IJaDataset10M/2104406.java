package jmri.jmrix.tmcc;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 * Tests for the jmri.jmrix.tmcc package.
 * @author      Bob Jacobsen  Copyright 2003
 * @version   $Revision: 17977 $
 */
public class SerialTest extends TestCase {

    public SerialTest(String s) {
        super(s);
    }

    public static void main(String[] args) {
        String[] testCaseName = { "-noloading", SerialTest.class.getName() };
        junit.swingui.TestRunner.main(testCaseName);
    }

    public static Test suite() {
        apps.tests.AllTest.initLogging();
        TestSuite suite = new TestSuite("jmri.jmrix.tmcc.SerialTest");
        suite.addTest(SerialTurnoutTest.suite());
        suite.addTest(SerialTurnoutManagerTest.suite());
        suite.addTest(SerialMessageTest.suite());
        suite.addTest(SerialReplyTest.suite());
        suite.addTest(SerialTrafficControllerTest.suite());
        suite.addTest(SerialAddressTest.suite());
        if (!System.getProperty("jmri.headlesstest", "false").equals("true")) {
            suite.addTest(jmri.jmrix.tmcc.serialmon.SerialMonFrameTest.suite());
        }
        return suite;
    }

    protected void setUp() {
        apps.tests.Log4JFixture.setUp();
    }

    protected void tearDown() {
        apps.tests.Log4JFixture.tearDown();
    }
}
