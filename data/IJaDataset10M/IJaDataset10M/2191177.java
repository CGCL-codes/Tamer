package org.doframework.sample.component;

import org.junit.*;
import org.doframework.sample.global.*;
import org.doframework.sample.persistence.jdbc_persistence.*;

/**
 * This class demonstrates using the database for persistence with the same tests that passed with the mock objects
 */
public class JdbcAccountingTest extends AccountingTest {

    @Before
    public void setUp() {
        GlobalContext.setPersistanceFactory(new JdbcPersistenceFactory());
    }
}
