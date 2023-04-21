package org.joda.time.chrono;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import junit.textui.TestRunner;

/**
 * Entry point for all tests in this package.
 * 
 * @version $Revision$ $Date$
 * 
 * @author Stephen Colebourne
 */
public class TestAll extends TestCase {

    public static boolean FAST = false;

    public TestAll(String testName) {
        super(testName);
    }

    public static Test suite() {
        TestSuite suite = new TestSuite();
        suite.addTest(TestBuddhistChronology.suite());
        suite.addTest(TestCopticChronology.suite());
        suite.addTest(TestEthiopicChronology.suite());
        suite.addTest(TestGJChronology.suite());
        suite.addTest(TestGregorianChronology.suite());
        suite.addTest(TestIslamicChronology.suite());
        suite.addTest(TestJulianChronology.suite());
        suite.addTest(TestISOChronology.suite());
        suite.addTest(TestLenientChronology.suite());
        return suite;
    }

    public static void main(String args[]) {
        FAST = false;
        TestRunner.run(TestAll.suite());
    }
}
