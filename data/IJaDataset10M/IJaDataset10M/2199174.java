package subethaclipse.net.command.test;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 * @author Vaughan
 */
public class AllTests extends TestCase {

    public static Test suite() {
        TestSuite suite = new TestSuite("Test for subethaclipse.net.command.test");
        suite.addTestSuite(CommandTest.class);
        suite.addTestSuite(QueueTest.class);
        suite.addTestSuite(FileRequestCommandTest.class);
        suite.addTestSuite(ResendCommandTest.class);
        suite.addTestSuite(AcknowledgeCommandTest.class);
        suite.addTestSuite(DriverViewUpdateCommandTest.class);
        suite.addTestSuite(DriverViewResponseCommandTest.class);
        suite.addTestSuite(DisconnectCommandTest.class);
        suite.addTestSuite(ViewChangedCommandTest.class);
        suite.addTestSuite(FactoryTest.class);
        suite.addTestSuite(SourceTreeRequestCommandTest.class);
        suite.addTestSuite(SourceTreeResponseCommandTest.class);
        return suite;
    }
}
