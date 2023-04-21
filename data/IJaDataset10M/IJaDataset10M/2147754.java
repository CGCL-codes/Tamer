package jmri.jmrix.grapevine;

import junit.framework.Test;
import junit.framework.TestSuite;
import jmri.*;
import jmri.jmrix.AbstractMRMessage;
import jmri.jmrix.AbstractMRListener;

/**
 * SerialTurnoutManagerTest.java
 *
 * Description:	    tests for the SerialLightManager class
 * @author			Bob Jacobsen Copyright 2004, 2007, 2008
 * @version  $Revision: 17977 $
 */
public class SerialLightManagerTest extends jmri.managers.AbstractLightMgrTest {

    public void setUp() {
        apps.tests.Log4JFixture.setUp();
        SerialTrafficController t = new SerialTrafficController() {

            SerialTrafficController test() {
                setInstance();
                return this;
            }

            protected synchronized void sendMessage(AbstractMRMessage m, AbstractMRListener reply) {
            }
        }.test();
        t.registerNode(new SerialNode(1, SerialNode.NODE2002V6));
        l = new SerialLightManager();
        jmri.InstanceManager.setLightManager(l);
    }

    public String getSystemName(int n) {
        return "GL" + n;
    }

    public void testAsAbstractFactory() {
        Light o = l.newLight("GL1105", "my name");
        if (log.isDebugEnabled()) log.debug("received light value " + o);
        assertTrue(null != (SerialLight) o);
        if (log.isDebugEnabled()) log.debug("by system name: " + l.getBySystemName("GL1105"));
        if (log.isDebugEnabled()) log.debug("by user name:   " + l.getByUserName("my name"));
        assertTrue(null != l.getBySystemName("GL1105"));
        assertTrue(null != l.getByUserName("my name"));
    }

    /**
	 * Number of light to test.  
	 * Use 9th output on node 1.
	 */
    protected int getNumToTest1() {
        return 1109;
    }

    protected int getNumToTest2() {
        return 1107;
    }

    public SerialLightManagerTest(String s) {
        super(s);
    }

    public static void main(String[] args) {
        String[] testCaseName = { SerialLightManagerTest.class.getName() };
        junit.swingui.TestRunner.main(testCaseName);
    }

    public static Test suite() {
        apps.tests.AllTest.initLogging();
        TestSuite suite = new TestSuite(SerialLightManagerTest.class);
        return suite;
    }

    protected void tearDown() {
        apps.tests.Log4JFixture.tearDown();
    }

    static org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(SerialLightManagerTest.class.getName());
}
