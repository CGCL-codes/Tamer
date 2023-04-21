package org.jaffa.metadata;

import junit.framework.Test;
import junit.framework.TestSuite;

/**
 *
 * @author  GautamJ
 */
public class AllTests {

    static {
        org.jaffa.config.InitLog4J.init();
    }

    /** Assembles and returns a test suite containing all known tests.
     * @return A test suite.
     */
    public static Test suite() {
        TestSuite suite = new TestSuite();
        return suite;
    }

    /** Runs the test suite.
     * @param args The input args are not used.
     */
    public static void main(String[] args) {
        junit.swingui.TestRunner.run(AllTests.class);
    }
}
