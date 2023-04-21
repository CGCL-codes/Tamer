package blms.tests.selenium;

import junit.framework.Test;
import junit.framework.TestSuite;

/**
 * This class is responsible for running all Selenium tests for the BLMS system. Be sure to have
 * initialized the application and the Selenium server.
 *
 */
public class AllSeleniumTests extends SeleniumSuperTest {

    /**
     * Create a test suite for running all unit tests of the system.
     * @return The test suite.
     */
    public static Test suite() {
        TestSuite suite = new TestSuite("Selenium tests for blms");
        suite.addTestSuite(IndexTest.class);
        suite.addTestSuite(JoinLeagueTest.class);
        suite.addTestSuite(LeagueMembershipTest.class);
        suite.addTestSuite(LoginTest.class);
        suite.addTestSuite(OperatorMainTest.class);
        suite.addTestSuite(PlayerTest.class);
        return suite;
    }
}
