package com.google.gwt.junit;

import com.google.gwt.junit.client.DevModeOnCompiledScriptTest;
import com.google.gwt.junit.client.GWTTestCaseTest;
import com.google.gwt.junit.client.PropertyDefiningGWTTest;
import com.google.gwt.junit.tools.GWTTestSuite;
import junit.framework.Test;

/**
 * Tests of the junit package.
 */
public class JUnitSuite {

    public static Test suite() {
        GWTTestSuite suite = new GWTTestSuite("Test for suite for com.google.gwt.junit");
        suite.addTestSuite(GWTTestCaseTest.class);
        suite.addTestSuite(DevModeOnCompiledScriptTest.class);
        suite.addTestSuite(BatchingStrategyTest.class);
        suite.addTestSuite(CompileStrategyTest.class);
        suite.addTestSuite(FakeMessagesMakerTest.class);
        suite.addTestSuite(GWTMockUtilitiesTest.class);
        suite.addTestSuite(JUnitMessageQueueTest.class);
        suite.addTestSuite(GWTTestCaseNoClientTest.class);
        suite.addTestSuite(PropertyDefiningStrategyTest.class);
        suite.addTestSuite(PropertyDefiningGWTTest.class);
        return suite;
    }
}
