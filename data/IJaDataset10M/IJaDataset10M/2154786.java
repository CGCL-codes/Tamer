package net.sf.ideoreport.common.config;

import junit.framework.Test;
import junit.framework.TestSuite;
import net.sf.ideoreport.engines.DefaultEnginePremierTest;

public class DefaultEngineAllTests {

    public static Test suite() {
        TestSuite suite = new TestSuite("Test for net.sf.ideoreport.common.config");
        suite.addTest(new TestSuite(DefaultEngineConfigTest.class));
        suite.addTest(new TestSuite(DefaultEnginePremierTest.class));
        suite.addTest(new TestSuite(DefaultColorCodingConfigTest.class));
        suite.addTest(new TestSuite(DefaultColorCodingConfigKOTest.class));
        return suite;
    }
}
