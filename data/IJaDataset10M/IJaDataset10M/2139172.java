package unbbayes.prs.mebn.test;

import junit.framework.Test;
import junit.framework.TestSuite;

public class AllTests {

    public static Test suite() {
        TestSuite suite = new TestSuite("Test for unbbayes.prs.mebn.test");
        suite.addTest(GenerativeInputNodeTest.suite());
        suite.addTest(MultiEntityBayesianNetworkTest.suite());
        suite.addTest(ResidentNodeTest.suite());
        suite.addTest(MultiEntityNodeTest.suite());
        suite.addTest(DomainMFragTest.suite());
        suite.addTest(BuiltInRVTest.suite());
        suite.addTest(ArgumentTest.suite());
        suite.addTest(MTheoryAlgorithmTest.suite());
        suite.addTest(ContextNodeTest.suite());
        suite.addTest(DomainResidentNodeTest.suite());
        suite.addTest(InputNodeTest.suite());
        suite.addTest(OrdinaryVariableTest.suite());
        suite.addTest(MFragTest.suite());
        suite.addTest(ResidentNodePointerTest.suite());
        return suite;
    }
}
