package org.acs.elated.test;

import junit.framework.Test;
import junit.framework.TestSuite;
import org.acs.elated.test.app.AllAppTests;
import org.acs.elated.test.commons.AllCommonsTests;
import org.acs.elated.test.commons.parser.AllParserTests;
import org.acs.elated.test.database.AllDatabaseTests;

/**
 * @author ACS Tech Center
 *
 */
public class AllTests {

    public static Test suite() {
        System.setProperty("fed.FedoraInterface", "org.acs.elated.test.mockObjects.MockFedoraInterface");
        System.setProperty("database.DatabaseConnectorInterface", "org.acs.elated.test.mockObjects.MockDatabaseConnectorInterface");
        TestSuite suite = new TestSuite("Test for org.acs.elated");
        suite.addTest(AllAppTests.suite());
        suite.addTest(AllParserTests.suite());
        suite.addTest(AllCommonsTests.suite());
        suite.addTest(AllDatabaseTests.suite());
        return suite;
    }
}
