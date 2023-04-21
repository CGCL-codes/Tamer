package org.openscience.jmol;

import junit.framework.Test;
import junit.framework.TestSuite;

/**
 * Test suite of all tests in this package.
 *
 * @author Bradley A. Smith (bradley@baysmith.com)
 */
public class Tests {

    /**
   * Returns a suite containing all the tests in this package.
   *
   * @return a suite containing all the tests in this package.
   */
    public static Test suite() {
        TestSuite suite = new TestSuite();
        suite.addTest(BaseAtomTypeTest.suite());
        suite.addTest(AtomTypeTest.suite());
        suite.addTest(AtomTypeSetTest.suite());
        suite.addTest(AtomTypesModelTest.suite());
        suite.addTest(ReaderFactoryTest.suite());
        suite.addTest(Gaussian98ReaderTest.suite());
        return suite;
    }
}
