package jmathlibtests.toolbox.jmathlib.graphics.graph3d;

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
        TestSuite suite = new TestSuite("3D graphics functions");
        suite.addTest(jmathlibtests.toolbox.jmathlib.graphics.graph3d.testMeshgrid.suite());
        return suite;
    }
}
