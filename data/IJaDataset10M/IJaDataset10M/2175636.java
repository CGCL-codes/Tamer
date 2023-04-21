package org.fcrepo.server.search;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith(Suite.class)
@Suite.SuiteClasses({ FieldSearchSQLImplIntegrationTest.class })
public class AllIntegrationTests {

    public static junit.framework.Test suite() throws Exception {
        junit.framework.TestSuite suite = new junit.framework.TestSuite(AllIntegrationTests.class.getName());
        suite.addTest(FieldSearchSQLImplIntegrationTest.suite());
        return suite;
    }
}
