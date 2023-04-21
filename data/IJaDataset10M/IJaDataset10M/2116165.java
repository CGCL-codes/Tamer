package com.hack23.cia.fit.scenarios.service.user;

import org.junit.Test;
import org.springframework.test.context.transaction.AfterTransaction;
import org.springframework.test.context.transaction.BeforeTransaction;

/**
 * The Class CommitteeReportRequestSuccessFullTest.
 */
public class CommitteeReportRequestSuccessFullTest extends AbstractUserRequestTest {

    /**
	 * Instantiates a new committee report request success full test.
	 */
    public CommitteeReportRequestSuccessFullTest() {
        super();
        testScenarioName = this.getClass().getSimpleName();
    }

    /**
	 * Committee report request succeeds.
	 * 
	 * @throws Exception
	 *             the exception
	 */
    @Test(timeout = 2000)
    public void committeeReportRequestSucceeds() throws Exception {
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
