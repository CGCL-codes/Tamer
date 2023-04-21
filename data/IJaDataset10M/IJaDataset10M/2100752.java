package jmri.jmrix.loconet;

import jmri.Sensor;
import jmri.SensorManager;
import junit.framework.Assert;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 * Tests for the jmri.jmrix.loconet.LnSensorManagerTurnout class.
 * @author	Bob Jacobsen  Copyright 2001
 * @version     $Revision: 17977 $
 */
public class LnSensorManagerTest extends TestCase {

    public void testLnSensorCreate() {
        LocoNetInterfaceScaffold lnis = new LocoNetInterfaceScaffold();
        Assert.assertNotNull("exists", lnis);
        LnSensorManager l = new LnSensorManager(lnis, "L");
        jmri.InstanceManager.setSensorManager(l);
    }

    public void testByAddress() {
        LocoNetInterfaceScaffold lnis = new LocoNetInterfaceScaffold();
        Assert.assertNotNull("exists", lnis);
        new jmri.InstanceManager() {

            protected void init() {
                super.init();
                root = null;
            }
        };
        LnSensorManager l = new LnSensorManager(lnis, "L");
        Sensor t = l.newSensor("LS22", "test");
        Assert.assertTrue(t == l.getByUserName("test"));
        Assert.assertTrue(t == l.getBySystemName("LS22"));
    }

    public void testMisses() {
        LocoNetInterfaceScaffold lnis = new LocoNetInterfaceScaffold();
        Assert.assertNotNull("exists", lnis);
        LnSensorManager l = new LnSensorManager(lnis, "L");
        Sensor t = l.newSensor("LS22", "test");
        Assert.assertNotNull("exists", t);
        Assert.assertTrue(null == l.getByUserName("foo"));
        Assert.assertTrue(null == l.getBySystemName("bar"));
    }

    public void testLocoNetMessages() {
        LocoNetInterfaceScaffold lnis = new LocoNetInterfaceScaffold();
        LnSensorManager l = new LnSensorManager(lnis, "L");
        LocoNetMessage m1 = new LocoNetMessage(4);
        m1.setOpCode(0xb2);
        m1.setElement(1, 0x15);
        m1.setElement(2, 0x60);
        m1.setElement(3, 0x38);
        lnis.sendTestMessage(m1);
        Assert.assertTrue(null != l.getBySystemName("LS44"));
    }

    public void testAsAbstractFactory() {
        LocoNetInterfaceScaffold lnis = new LocoNetInterfaceScaffold();
        new jmri.InstanceManager() {

            protected void init() {
                super.init();
                root = null;
            }
        };
        LnSensorManager l = new LnSensorManager(lnis, "L");
        jmri.InstanceManager.setSensorManager(l);
        SensorManager t = jmri.InstanceManager.sensorManagerInstance();
        Sensor o = t.newSensor("LS21", "my name");
        if (log.isDebugEnabled()) log.debug("received sensor value " + o);
        Assert.assertTrue(null != (LnSensor) o);
        if (log.isDebugEnabled()) log.debug("by system name: " + t.getBySystemName("LS21"));
        if (log.isDebugEnabled()) log.debug("by user name:   " + t.getByUserName("my name"));
        Assert.assertTrue(null != t.getBySystemName("LS21"));
        Assert.assertTrue(null != t.getByUserName("my name"));
    }

    public LnSensorManagerTest(String s) {
        super(s);
    }

    public static void main(String[] args) {
        String[] testCaseName = { LnSensorManagerTest.class.getName() };
        junit.swingui.TestRunner.main(testCaseName);
    }

    public static Test suite() {
        TestSuite suite = new TestSuite(LnSensorManagerTest.class);
        return suite;
    }

    static org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(LnSensorManagerTest.class.getName());

    protected void setUp() {
        apps.tests.Log4JFixture.setUp();
    }

    protected void tearDown() {
        apps.tests.Log4JFixture.tearDown();
    }
}
