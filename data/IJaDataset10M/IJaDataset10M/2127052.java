package net.sf.refactorit.common.util.graph;

import junit.framework.Test;
import junit.framework.TestSuite;

public class AllTests {

    /** Hidden constructor. */
    private AllTests() {
    }

    public static Test suite() {
        final TestSuite suite = new TestSuite("Graph");
        suite.addTest(WeightedGraphTest.suite());
        return suite;
    }
}
