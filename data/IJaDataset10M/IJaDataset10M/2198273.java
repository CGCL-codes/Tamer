package jmri.jmrix.can.cbus.swing.configtool;

import javax.swing.JFrame;
import jmri.jmrix.can.TestTrafficController;
import junit.framework.Assert;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import jmri.jmrix.can.*;

/**
 * Tests for the jmri.jmrix.can.cbus.swing.configtool package.
 * @author      Bob Jacobsen  Copyright 2008
 * @version   $Revision: 19698 $
 */
public class ConfigToolActionTest extends TestCase {

    public ConfigToolActionTest(String s) {
        super(s);
    }

    public void testAction() {
        TrafficControllerScaffold tcs = new TrafficControllerScaffold();
        CanSystemConnectionMemo memo = new CanSystemConnectionMemo();
        memo.setTrafficController(tcs);
        new ConfigToolPane();
        Assert.assertNotNull("exists", tcs);
    }

    public static void main(String[] args) {
        String[] testCaseName = { "-noloading", ConfigToolActionTest.class.getName() };
        junit.swingui.TestRunner.main(testCaseName);
    }

    public static Test suite() {
        apps.tests.AllTest.initLogging();
        TestSuite suite = new TestSuite(ConfigToolActionTest.class);
        return suite;
    }
}
