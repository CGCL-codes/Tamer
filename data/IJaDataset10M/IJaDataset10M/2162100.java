package com.fddtool.si.database;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

public class AllTests extends TestCase {

    public AllTests(String s) {
        super(s);
    }

    public static Test suite() {
        TestSuite suite = new TestSuite();
        suite.addTestSuite(com.fddtool.si.database.TestDataAccessObject.class);
        suite.addTestSuite(com.fddtool.si.database.TestDbProperties.class);
        return suite;
    }
}
