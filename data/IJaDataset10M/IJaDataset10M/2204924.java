package com.fddtool;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

public class AllTests extends TestCase {

    public AllTests(String s) {
        super(s);
    }

    public static Test suite() {
        TestSuite suite = new TestSuite();
        suite.addTest(com.fddtool.pd.administration.AllTests.suite());
        suite.addTest(com.fddtool.pd.fddproject.AllTests.suite());
        suite.addTest(com.fddtool.pd.task.AllTests.suite());
        suite.addTest(com.fddtool.pd.account.AllTests.suite());
        suite.addTest(com.fddtool.pd.document.AllTests.suite());
        suite.addTest(com.fddtool.pd.property.AllTests.suite());
        suite.addTest(com.fddtool.pd.bug.AllTests.suite());
        suite.addTest(com.fddtool.resource.AllTests.suite());
        suite.addTest(com.fddtool.si.AllTests.suite());
        suite.addTest(com.fddtool.services.email.AllTests.suite());
        suite.addTest(com.fddtool.ui.AllTests.suite());
        suite.addTest(com.fddtool.util.AllTests.suite());
        return suite;
    }

    /**
     *  Provide a way to run just this test by itself.
     */
    public static void main(String args[]) {
        junit.textui.TestRunner.run(suite());
    }
}
