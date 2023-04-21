package jimo.osgi.modules.testbundle.junit;

import junit.framework.Test;
import junit.framework.TestSuite;

public class AllTests {

    public static Test suite() {
        TestSuite suite = new TestSuite("Test for jimo.osgi.modules.testbundle.junit");
        suite.addTestSuite(ResourceTestCases.class);
        suite.addTestSuite(CoreTestCases.class);
        suite.addTestSuite(LoggerTestCases.class);
        suite.addTestSuite(SecurityTestCases.class);
        suite.addTestSuite(KnowledgeBaseTestCases.class);
        suite.addTestSuite(JDBCKBTestCases.class);
        suite.addTestSuite(EventAdminTestCases.class);
        suite.addTestSuite(ManagedServiceFactoryTestCases.class);
        suite.addTestSuite(ManagedServiceTestCases.class);
        return suite;
    }
}
