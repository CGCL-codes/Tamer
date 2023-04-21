package com.hack23.cia.functionalintegrationtest.scenarios.web.controller.application;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.transaction.PlatformTransactionManager;
import com.hack23.cia.functionalintegrationtest.scenarios.web.controller.common.AbstractFunctionalActionWithoutViewFactoryTest;
import com.hack23.cia.service.impl.admin.AdminService;
import com.hack23.cia.web.impl.ui.viewfactory.api.application.ApplicationModelAndView;

/**
 * The Class ApplicationActionNorwegianUserSuccessTest.
 */
@SuppressWarnings("unused")
public class ApplicationActionNorwegianUserSuccessTest extends AbstractFunctionalActionWithoutViewFactoryTest {

    /** The admin service. */
    @Autowired
    @Qualifier("adminService")
    protected AdminService adminService;

    /** The model and view matcher. */
    private ModelAndViewMatcher modelAndViewMatcher = new ModelAndViewMatcher(ApplicationModelAndView.class);

    /** The transaction manager. */
    @Autowired
    @Qualifier("transactionManager")
    protected PlatformTransactionManager transactionManager;

    /**
     * Instantiates a new application action norwegian user success test.
     */
    public ApplicationActionNorwegianUserSuccessTest() {
        super();
        this.testScenarioName = this.getClass().getSimpleName();
    }

    /**
     * Application action norwegian user succeeds.
     * 
     * @throws Exception the exception
     */
    @Test
    public final void applicationActionNorwegianUserSucceeds() throws Exception {
    }

    @Override
    @Before
    public void setupDatabasePreCondition() throws Exception {
    }

    @Override
    @Before
    public void setupExpectedModelAndView() throws Exception {
    }

    @Override
    @After
    public void verifyModelAndView() throws Exception {
    }
}
