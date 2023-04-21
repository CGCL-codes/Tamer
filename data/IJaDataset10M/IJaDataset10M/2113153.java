package net.sf.ideoreport.engines.chartengine;

import junit.framework.Test;
import junit.framework.TestSuite;

/**
 * Lance tous les tests unitaires du Chart Engine
 */
public class ChartEngineAllTests {

    public static Test suite() {
        TestSuite suite = new TestSuite("Test ChartEngine");
        suite.addTestSuite(ChartEngineAreaTest.class);
        suite.addTestSuite(ChartEngineLineTest.class);
        suite.addTestSuite(ChartEngineConfigTest.class);
        suite.addTestSuite(ChartEngineSimpleTest.class);
        suite.addTestSuite(ChartEnginePieTest.class);
        suite.addTestSuite(ChartEngineMultiPieTest.class);
        suite.addTestSuite(ChartEngineOverlaidTest.class);
        suite.addTestSuite(ChartEngineCombinedTest.class);
        suite.addTestSuite(ChartEngineLegendTest.class);
        suite.addTest(XYTests.suite());
        return suite;
    }
}
