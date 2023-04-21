package com.hack23.cia.functionalintegrationtest.scenarios.service.user;

import org.junit.Test;
import org.springframework.test.context.transaction.AfterTransaction;
import org.springframework.test.context.transaction.BeforeTransaction;

/**
 * The Class SearchRequestSuccessFullTest.
 */
public class SearchRequestSuccessFullTest extends AbstractUserRequestTest {

    /**
     * Instantiates a new search request success full test.
     */
    public SearchRequestSuccessFullTest() {
        super();
        this.testScenarioName = this.getClass().getSimpleName();
    }

    /**
     * Search request succeeds.
     * 
     * @throws Exception the exception
     */
    @Test
    public final void searchRequestSucceeds() throws Exception {
    }

    @Override
    @BeforeTransaction
    public void setupDatabasePreCondition() throws Exception {
    }

    @Override
    @AfterTransaction
    public void verifyDatabasePostCondition() throws Exception {
    }
}
