package com.hack23.cia.functionalintegrationtest.scenarios.service.user;

import org.junit.Test;
import org.springframework.test.context.transaction.AfterTransaction;
import org.springframework.test.context.transaction.BeforeTransaction;

/**
 * The Class RegisterUserRequestSuccessFullTest.
 */
public class RegisterUserRequestSuccessFullTest extends AbstractUserRequestTest {

    /**
     * Instantiates a new register user request success full test.
     */
    public RegisterUserRequestSuccessFullTest() {
        super();
        this.testScenarioName = this.getClass().getSimpleName();
    }

    /**
     * Register user request succeeds.
     * 
     * @throws Exception the exception
     */
    @Test
    public final void registerUserRequestSucceeds() throws Exception {
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
