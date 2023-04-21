package test.longhall.service;

import junit.framework.*;

public class AllTests {

    /**
   *  The main program for the AllTests class
   *
   *  @param  args  The command line arguments
   */
    public static void main(String[] args) {
        junit.textui.TestRunner.run(suite());
    }

    /**
   *  A unit test suite for JUnit
   *
   *  @return    The test suite
   */
    public static Test suite() {
        TestSuite suite = new TestSuite();
        suite.addTest(ResourcePoolTests.suite());
        return suite;
    }
}
