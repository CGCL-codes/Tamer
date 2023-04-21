package com.reeltwo.jumble.util;

import java.util.Collection;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 * Tests the RTSI class. Extremely low com.reeltwo.jumble score so this needs more work.
 * 
 * @author Tin Pavlinic
 * @version $Revision: 500 $
 */
public class RTSITest extends TestCase {

    public void testRTSI() throws Exception {
        Collection c = RTSI.find("com.reeltwo.jumble.util", "com.reeltwo.jumble.util.Command");
        assertEquals(2, c.size());
        assertTrue(c.contains("com.reeltwo.jumble.util.LightOff"));
        assertTrue(c.contains("com.reeltwo.jumble.util.DoorClose"));
    }

    public void testJarRTSI() throws Exception {
        Collection c = RTSI.find("org.apache.bcel.generic", "org.apache.bcel.generic.Instruction");
        assertTrue(c.contains("org.apache.bcel.generic.IADD"));
    }

    public void testGetAllVisiblePackages() {
        Collection c = RTSI.getAllVisiblePackages();
        assertTrue(c.contains("com.reeltwo.jumble"));
        assertTrue(c.contains("com.reeltwo.jumble.dependency"));
        assertTrue(c.contains("com.reeltwo.jumble.fast"));
        assertTrue(c.contains("com.reeltwo.jumble.util"));
        assertTrue(c.contains("org.apache.bcel"));
        assertTrue(c.contains("junit"));
        assertTrue(c.contains("org.apache"));
        assertTrue(c.contains("junit.framework"));
        assertTrue(c.contains("experiments"));
    }

    public static Test suite() {
        TestSuite suite = new TestSuite(RTSITest.class);
        return suite;
    }

    public static void main(String[] args) {
        junit.textui.TestRunner.run(suite());
    }
}
