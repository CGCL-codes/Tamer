package org.matsim.core.replanning.selectors;

import junit.framework.Test;
import junit.framework.TestSuite;

public class AllTests {

    public static Test suite() {
        TestSuite suite = new TestSuite("Tests for " + AllTests.class.getPackage().getName());
        suite.addTestSuite(BestPlanSelectorTest.class);
        suite.addTestSuite(ExpBetaPlanChangerTest.class);
        suite.addTestSuite(ExpBetaPlanSelectorTest.class);
        suite.addTestSuite(ExpBetaPlanForRemovalSelectorTest.class);
        suite.addTestSuite(KeepSelectedTest.class);
        suite.addTestSuite(PathSizeLogitSelectorTest.class);
        suite.addTestSuite(RandomPlanSelectorTest.class);
        suite.addTestSuite(WorstPlanForRemovalSelectorTest.class);
        return suite;
    }
}
