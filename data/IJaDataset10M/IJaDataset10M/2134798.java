package com.fddtool.services.email;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

public class AllTests extends TestCase {

    public AllTests(String s) {
        super(s);
    }

    public static Test suite() {
        TestSuite suite = new TestSuite();
        suite.addTestSuite(com.fddtool.services.email.TestEmailProperties.class);
        suite.addTestSuite(com.fddtool.services.email.TestEmailProcessor.class);
        suite.addTestSuite(com.fddtool.services.email.TestEmailMessage.class);
        return suite;
    }
}
