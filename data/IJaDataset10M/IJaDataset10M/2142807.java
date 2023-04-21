package jmathlibtests.toolbox.finance;

import jmathlib.tools.junit.framework.*;

/**
 * TestSuite that runs all the tests
 *
 */
public class AllTests {

    public static void main(String[] args) {
        jmathlib.tools.junit.textui.TestRunner.run(suite());
    }

    public static Test suite() {
        TestSuite suite = new TestSuite("finance functions");
        suite.addTest(jmathlibtests.toolbox.finance.testFvl.suite());
        return suite;
    }
}
