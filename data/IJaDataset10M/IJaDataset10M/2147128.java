package jmri.jmrix.nce;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 * tests for the jmri.jmrix.nce package
 * @author			Bob Jacobsen
 * @version   $Revision: 17977 $
 */
public class NceTest extends TestCase {

    public NceTest(String s) {
        super(s);
    }

    public void testDemo() {
        assertTrue(true);
    }

    public static void main(String[] args) {
        String[] testCaseName = { NceTest.class.getName() };
        junit.swingui.TestRunner.main(testCaseName);
    }

    public static Test suite() {
        apps.tests.AllTest.initLogging();
        TestSuite suite = new TestSuite("jmri.jmrix.nce.NceTest");
        suite.addTest(jmri.jmrix.nce.NceTurnoutTest.suite());
        suite.addTest(jmri.jmrix.nce.NceTurnoutManagerTest.suite());
        suite.addTest(jmri.jmrix.nce.NceSensorManagerTest.suite());
        suite.addTest(jmri.jmrix.nce.NceAIUTest.suite());
        suite.addTest(jmri.jmrix.nce.NceProgrammerTest.suite());
        suite.addTest(jmri.jmrix.nce.NceTrafficControllerTest.suite());
        suite.addTest(jmri.jmrix.nce.NceMessageTest.suite());
        suite.addTest(jmri.jmrix.nce.NceReplyTest.suite());
        suite.addTest(jmri.jmrix.nce.NcePowerManagerTest.suite());
        if (!System.getProperty("jmri.headlesstest", "false").equals("true")) {
            suite.addTest(jmri.jmrix.nce.ncemon.NceMonPanelTest.suite());
            suite.addTest(jmri.jmrix.nce.packetgen.NcePacketGenPanelTest.suite());
        }
        return suite;
    }
}
