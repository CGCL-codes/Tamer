package com.sri.emo.test;

import junit.framework.Test;
import junit.framework.TestSuite;
import com.sri.emo.test.selenium.NodeManipulationTestCase;
import com.sri.emo.test.selenium.SeleniumTestSetup;

public class SeleniumTestSuite extends TestSuite {

    public SeleniumTestSuite() throws Exception {
        super();
    }

    public static Test suite() throws Exception {
        SeleniumTestSuite seleniumSuite = new SeleniumTestSuite();
        seleniumSuite.setName("Selenium TestSuite");
        TestSuite ts = new TestSuite();
        ts.addTestSuite(NodeManipulationTestCase.class);
        seleniumSuite.addTest(new SeleniumTestSetup(ts));
        return seleniumSuite;
    }

    public static void main(String[] args) throws Exception {
        junit.textui.TestRunner.run(suite());
        System.exit(0);
    }
}
