package junit.extensions.xtestsuite.abtests.subpackage;

import junit.framework.*;

/**
 *
 * @author daniel
 */
public class TestC extends TestCase {

    public TestC() {
    }

    public TestC(java.lang.String testName) {
        super(testName);
    }

    public static Test suite() {
        TestSuite suite = new TestSuite(TestC.class);
        return suite;
    }

    public void testNothing() {
    }
}
