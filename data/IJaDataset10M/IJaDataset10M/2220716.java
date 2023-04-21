package jmri.jmrix.lenz.swing.liusb;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 * Tests for the jmri.jmrix.lenz.swing.liusb package
 * @author                      Paul Bender  
 * @version                     $Revision: 17977 $
 */
public class LIUSBTest extends TestCase {

    public LIUSBTest(String s) {
        super(s);
    }

    public static void main(String[] args) {
        String[] testCaseName = { LIUSBTest.class.getName() };
        junit.swingui.TestRunner.main(testCaseName);
    }

    public static Test suite() {
        TestSuite suite = new TestSuite("jmri.jmrix.lenz.liusb.swing.LIUSBTest");
        suite.addTest(new TestSuite(LIUSBConfigFrameTest.class));
        return suite;
    }

    static org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(LIUSBTest.class.getName());
}
