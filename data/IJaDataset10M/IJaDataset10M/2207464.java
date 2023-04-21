package evolve.genes;

import junit.framework.Test;
import junit.framework.TestSuite;

public class GenesTestSuite {

    public static Test suite() {
        TestSuite suite = new TestSuite("Test for evolve.genes");
        suite.addTestSuite(BoolGeneratorTest.class);
        suite.addTestSuite(SymbolGeneratorTest.class);
        suite.addTestSuite(FunctionGeneTest.class);
        suite.addTestSuite(LookupGeneTest.class);
        suite.addTestSuite(ListGeneTest.class);
        suite.addTestSuite(ApplicationGeneTest.class);
        suite.addTestSuite(NullGeneTest.class);
        suite.addTestSuite(IfGeneTest.class);
        suite.addTestSuite(FixNumGeneratorTest.class);
        suite.addTestSuite(PassMaybeGeneTest.class);
        suite.addTestSuite(DemaybeGeneTest.class);
        suite.addTestSuite(ConsGeneTest.class);
        suite.addTestSuite(RealGeneratorTest.class);
        suite.addTestSuite(TrueGeneTest.class);
        suite.addTestSuite(StringGeneratorTest.class);
        return suite;
    }
}
