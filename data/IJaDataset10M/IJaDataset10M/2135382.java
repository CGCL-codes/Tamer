package jmri.jmrix.lenz.hornbyelite;

import junit.framework.Assert;
import junit.framework.Test;
import junit.framework.TestSuite;
import jmri.jmrix.lenz.XNetInterfaceScaffold;
import jmri.jmrix.lenz.XNetReply;

/**
 * Tests for the {@link jmri.jmrix.lenz.hornbyelite.EliteXNetTurnout} class.
 * @author	    Bob Jacobsen
 * @version         $Revision: 17977 $
 */
public class EliteXNetTurnoutTest extends jmri.implementation.AbstractTurnoutTest {

    public int numListeners() {
        return lnis.numListeners();
    }

    XNetInterfaceScaffold lnis;

    public void checkClosedMsgSent() {
        Assert.assertEquals("closed message", "52 05 8A DD", lnis.outbound.elementAt(lnis.outbound.size() - 1).toString());
        Assert.assertEquals("CLOSED state", jmri.Turnout.CLOSED, t.getCommandedState());
    }

    public void checkThrownMsgSent() {
        Assert.assertEquals("thrown message", "52 05 8B DC", lnis.outbound.elementAt(lnis.outbound.size() - 1).toString());
        Assert.assertEquals("THROWN state", jmri.Turnout.THROWN, t.getCommandedState());
    }

    public void checkIncoming() {
        t.setFeedbackMode(jmri.Turnout.MONITORING);
        XNetReply m = new XNetReply();
        m.setElement(0, 0x42);
        m.setElement(1, 0x05);
        m.setElement(2, 0x04);
        m.setElement(3, 0x43);
        lnis.sendTestMessage(m);
        Assert.assertTrue(t.getKnownState() == jmri.Turnout.CLOSED);
        m = new XNetReply();
        m.setElement(0, 0x42);
        m.setElement(1, 0x05);
        m.setElement(2, 0x08);
        m.setElement(3, 0x4F);
        lnis.sendTestMessage(m);
        Assert.assertTrue(t.getKnownState() == jmri.Turnout.THROWN);
    }

    public void testEliteXNetTurnoutPropertyChange() {
        try {
            t.setCommandedState(jmri.Turnout.THROWN);
        } catch (Exception e) {
            log.error("TO exception: " + e);
        }
        Assert.assertTrue(t.getCommandedState() == jmri.Turnout.THROWN);
        t.setFeedbackMode(jmri.Turnout.ONESENSOR);
        jmri.Sensor s = jmri.InstanceManager.sensorManagerInstance().provideSensor("IS1");
        try {
            t.provideFirstFeedbackSensor("IS1");
        } catch (Exception x1) {
            log.error("TO exception: " + x1);
        }
        try {
            s.setState(jmri.Sensor.ACTIVE);
        } catch (Exception x) {
            log.error("TO exception: " + x);
        }
        Assert.assertTrue(t.getKnownState() == jmri.Turnout.THROWN);
    }

    public EliteXNetTurnoutTest(String s) {
        super(s);
    }

    public static void main(String[] args) {
        String[] testCaseName = { "-noloading", EliteXNetTurnoutTest.class.getName() };
        junit.swingui.TestRunner.main(testCaseName);
    }

    public static Test suite() {
        TestSuite suite = new TestSuite(EliteXNetTurnoutTest.class);
        return suite;
    }

    protected void setUp() {
        apps.tests.Log4JFixture.setUp();
        lnis = new XNetInterfaceScaffold(new HornbyEliteCommandStation());
        t = new EliteXNetTurnout("XT", 21, lnis);
        jmri.InstanceManager.store(new jmri.NamedBeanHandleManager(), jmri.NamedBeanHandleManager.class);
    }

    protected void tearDown() {
        apps.tests.Log4JFixture.tearDown();
    }

    static org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(EliteXNetTurnoutTest.class.getName());
}
