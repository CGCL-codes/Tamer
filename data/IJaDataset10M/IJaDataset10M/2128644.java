package com.plus.fcentre.jobfilter.dao.jpa;

import junit.framework.Test;
import junit.framework.TestSuite;

/**
 * JUnit @{link TestSuite} for JPA DAO class tests.
 * 
 * @author steve
 */
public class JpaDAOTestSuite {

    /**
	 * Create test suite.
	 * 
	 * @return test suite.
	 */
    public static Test suite() {
        TestSuite suite = new TestSuite();
        suite.addTestSuite(JpaAgencyDAOTest.class);
        suite.addTestSuite(JpaAgentDAOTest.class);
        suite.addTestSuite(JpaCVDAOTest.class);
        suite.addTestSuite(JpaVacancyDAOTest.class);
        return suite;
    }
}
