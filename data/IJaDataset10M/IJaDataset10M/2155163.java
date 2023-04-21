package net.toften.jlips2.test;

import junit.framework.Test;
import junit.framework.TestSuite;

public class AllTests {

    public static Test suite() {
        TestSuite suite = new TestSuite("Test for net.toften.jlips2.test");
        suite.addTestSuite(TableInfoTest.class);
        suite.addTestSuite(IdentTest.class);
        suite.addTestSuite(DbPrimarykeyManagerTest.class);
        suite.addTestSuite(DbPrimarykeyFactoryTest.class);
        suite.addTestSuite(Sql92StatementFactoryTest.class);
        suite.addTestSuite(Sql92StatementManagerTest.class);
        suite.addTestSuite(DbGeneratorFactoryTest.class);
        suite.addTestSuite(DbGeneratorManagerTest.class);
        suite.addTestSuite(DbConnectionFactoryTest.class);
        suite.addTestSuite(DbConnectionManagerTest.class);
        suite.addTestSuite(DbTableFactoryTest.class);
        suite.addTestSuite(DbTableManagerTest.class);
        suite.addTestSuite(DefaultRecordFactoryTest.class);
        suite.addTestSuite(DefaultRecordManagerTest.class);
        return suite;
    }
}
