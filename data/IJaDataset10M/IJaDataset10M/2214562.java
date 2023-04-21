package pipes.root;

import junit.framework.Test;
import junit.framework.TestSuite;

public class RootTestSuite {

    public static Test suite() {
        TestSuite suite = new TestSuite("Pipes Root Tests");
        suite.addTestSuite(SimplePieceTest.class);
        suite.addTestSuite(DualPieceTest.class);
        suite.addTestSuite(SourcePieceTest.class);
        suite.addTestSuite(PieceFactoryTest.class);
        suite.addTestSuite(PieceQueueTest.class);
        suite.addTestSuite(SubstrateTest.class);
        return suite;
    }
}
