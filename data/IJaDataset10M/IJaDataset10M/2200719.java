package com.hack23.cia.functionalintegrationtest.scenarios.service.application;

import java.util.HashMap;
import java.util.Map;
import junit.framework.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.test.context.transaction.AfterTransaction;
import org.springframework.test.context.transaction.BeforeTransaction;
import com.hack23.cia.functionalintegrationtest.scenarios.service.common.AbstractServiceRequestFunctionalIntegrationTest;
import com.hack23.cia.service.api.application.ApplicationManager;
import com.hack23.cia.service.api.application.ApplicationRequest;
import com.hack23.cia.service.api.common.ServiceResponse;
import com.hack23.cia.service.api.common.ServiceResponse.ServiceResult;

/**
 * The Class ApplicationRequestFailureFullTest.
 */
public class ApplicationRequestFailureFullTest extends AbstractServiceRequestFunctionalIntegrationTest {

    /** The application manager. */
    @Autowired
    @Qualifier("applicationManager")
    private ApplicationManager applicationManager;

    /** The service response. */
    private ServiceResponse serviceResponse;

    /**
	 * Instantiates a new application request failure full test.
	 */
    public ApplicationRequestFailureFullTest() {
        super();
        this.testScenarioName = this.getClass().getSimpleName();
    }

    /**
	 * Application request failure.
	 * 
	 * @throws Exception
	 *             the exception
	 */
    @Test
    public final void applicationRequestFailure() throws Exception {
        Map<String, String> userSettings = new HashMap<String, String>();
        ApplicationRequest incompleteApplicationRequest = new ApplicationRequest(userSettings);
        serviceResponse = this.applicationManager.service(incompleteApplicationRequest);
        Assert.assertEquals(ServiceResult.FAILURE, serviceResponse.getResult());
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
