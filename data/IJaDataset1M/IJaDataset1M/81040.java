package org.roomba.dao;

import org.apache.commons.logging.Log;
import org.springframework.test.AbstractTransactionalDataSourceSpringContextTests;

/**
 * Base class for DAO TestCases.
 *
 * @author Matt Raible
 */
public abstract class BaseDAOTestCase extends AbstractTransactionalDataSourceSpringContextTests {

    protected final Log log = logger;

    protected String[] getConfigLocations() {
        setAutowireMode(AUTOWIRE_BY_NAME);
        return new String[] { "/WEB-INF/applicationContext*.xml" };
    }
}
