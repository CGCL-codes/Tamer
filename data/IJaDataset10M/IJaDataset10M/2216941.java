package com.mtp.pounder.assrt;

import junit.framework.Test;
import junit.framework.TestSuite;

public class AllTests {

    public static Test suite() {
        TestSuite suite = new TestSuite("All com.mtp.pounder.assrt tests");
        suite.addTest(PackageTests.suite());
        return suite;
    }
}
