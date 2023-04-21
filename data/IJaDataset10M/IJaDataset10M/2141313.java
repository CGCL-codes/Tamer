package org.vramework.vow.test;

import junit.framework.Test;
import org.vramework.commons.junit.VTestSuite;
import org.vramework.vow.collections.test.TransactionalCollectionTest;
import org.vramework.vow.collections.test.TransactionalListTest;
import org.vramework.vow.test.aspects.VOWAspectsTestSuite;

public class VOWTestSuite extends VTestSuite {

    public static Test suite() {
        log("Start " + VOWTestSuite.class.getName());
        VTestSuite suite = new VTestSuite(VOWTestSuite.class.getName());
        suite.addTestSuite(VOWReadTest.class);
        suite.addTestSuite(TransactionalBeanTest.class);
        suite.addTestSuite(CopyingTransactionalBeanTest.class);
        suite.addTestSuite(VOWTest.class);
        suite.addTestSuite(VOWSetAndGetPropertiesTest.class);
        suite.addTestSuite(VOWAddTest.class);
        suite.addTestSuite(VOWChangeTest.class);
        suite.addTestSuite(VOWDeleteTest.class);
        suite.addTestSuite(VOWVersioningTest.class);
        suite.addTestSuite(VOWLockingTest.class);
        suite.addTestSuite(TransactionalBeanFactoryTest.class);
        suite.addTestSuite(TransactionalCollectionTest.class);
        suite.addTestSuite(TransactionalListTest.class);
        suite.addTestSuite(LockAdapterTest.class);
        suite.addTestSuite(VOWAspectsTestSuite.class);
        suite.addTestSuite(VOWIdentityHandlingTest.class);
        suite.addTestSuite(RegisteredIteratorTest.class);
        return suite;
    }
}
