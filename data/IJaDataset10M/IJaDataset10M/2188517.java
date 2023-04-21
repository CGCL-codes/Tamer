package jopt.csp.test.util;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

public class UtilTestSuite extends TestCase {

    public UtilTestSuite(java.lang.String testName) {
        super(testName);
    }

    public static Test suite() {
        TestSuite suite = new TestSuite();
        suite.addTestSuite(DoubleIntervalSetTest.class);
        suite.addTestSuite(FloatIntervalSetTest.class);
        suite.addTestSuite(GenericIndexTest.class);
        suite.addTestSuite(IndexIteratorTest.class);
        suite.addTestSuite(IntIntervalSetTest.class);
        suite.addTestSuite(IntMapTest.class);
        suite.addTestSuite(LongIntervalSetTest.class);
        suite.addTestSuite(NumberMathTest.class);
        suite.addTestSuite(NumUtilsTest.class);
        suite.addTestSuite(DoubleUtilTest.class);
        suite.addTestSuite(SortableListTest.class);
        return suite;
    }
}
