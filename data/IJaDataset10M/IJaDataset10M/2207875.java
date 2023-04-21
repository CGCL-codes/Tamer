package test.unit_sim.peripheral;

import junit.framework.Test;
import junit.framework.TestSuite;

/**
 * @author timk
 *
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public class AllTests {

    public static void main(String[] args) {
        junit.textui.TestRunner.run(AllTests.class);
    }

    public static Test suite() {
        TestSuite suite = new TestSuite("Test for test.unit_sim.peripheral");
        suite.addTest(new TestSuite(ByteRAMTest.class));
        suite.addTest(new TestSuite(ByteROMTest.class));
        suite.addTest(new TestSuite(ByteInvalidMemoryTest.class));
        return suite;
    }
}
