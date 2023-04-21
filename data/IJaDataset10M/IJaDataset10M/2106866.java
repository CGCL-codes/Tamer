package tests.com.scholardesk.filtering;

import junit.framework.Test;
import junit.framework.TestSuite;

public class AllTests {

    public static Test suite() {
        TestSuite suite = new TestSuite("(Bayesian) Filtering Tests");
        suite.addTestSuite(GenericBayesianFilterStrategyTestCase.class);
        suite.addTestSuite(GenericTrainingSourceTestCase.class);
        suite.addTestSuite(BayesianFilterTestCase.class);
        return suite;
    }

    public static void main(String args[]) {
        junit.textui.TestRunner.run(suite());
    }
}
