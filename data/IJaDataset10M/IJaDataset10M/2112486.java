package com.jy.bookshop;

import org.springframework.test.AbstractTransactionalDataSourceSpringContextTests;

public class AbstractSpringDaoTest extends AbstractTransactionalDataSourceSpringContextTests {

    protected String[] getConfigLocations() {
        return new String[] { "test-config.xml" };
    }
}
