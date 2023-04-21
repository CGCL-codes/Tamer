package com.hack23.cia.functionalintegrationtest.scenarios.service.admin;

import junit.framework.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.test.context.transaction.AfterTransaction;
import org.springframework.test.context.transaction.BeforeTransaction;
import com.hack23.cia.model.impl.application.common.Agency;
import com.hack23.cia.model.impl.application.common.Language;
import com.hack23.cia.model.impl.application.common.ParliamentChart;
import com.hack23.cia.service.impl.admin.agent.sweden.api.ParliamentAgentSupportService;
import com.hack23.cia.service.impl.common.ParliamentService;
import com.hack23.cia.service.impl.commondao.api.AgencyDAO;
import com.hack23.cia.testfoundation.AbstractFunctionalIntegrationTransactionalTest;

/**
 * The Class LoaderServiceImplGenerateParliamentChartsSuccessFullTest.
 */
public class LoaderServiceImplGenerateParliamentChartsSuccessFullTest extends AbstractFunctionalIntegrationTransactionalTest {

    /** The agency dao. */
    @Autowired
    @Qualifier("agencyDAO")
    private AgencyDAO agencyDAO;

    /** The agent support service. */
    @Autowired
    @Qualifier("parliamentAgentSupportService")
    private ParliamentAgentSupportService agentSupportService;

    /** The parliament service. */
    @Autowired
    @Qualifier("parliamentService")
    private ParliamentService parliamentService;

    /**
     * Instantiates a new loader service impl generate parliament charts success
     * full test.
     */
    public LoaderServiceImplGenerateParliamentChartsSuccessFullTest() {
        super();
        this.testScenarioName = this.getClass().getSimpleName();
    }

    /**
     * Generate charts succeeds.
     * 
     * @throws Exception the exception
     */
    @Test
    public final void generateChartsSucceeds() throws Exception {
        agentSupportService.generateParliamentCharts();
    }

    /**
     * Setup database pre condition.
     * 
     * @throws Exception the exception
     */
    @BeforeTransaction
    public void setupDatabasePreCondition() throws Exception {
        this.cleanOutOldTestData();
    }

    /**
     * Verify database post condition.
     * 
     * @throws Exception the exception
     */
    @AfterTransaction
    public void verifyDatabasePostCondition() throws Exception {
        Agency agency = agencyDAO.load(1L);
        Language language = agency.getLanguages().iterator().next();
        Assert.assertNotNull("Chart Missing:", parliamentService.getGraph(ParliamentChart.Outcome, language));
        Assert.assertNotNull("Chart Missing:", parliamentService.getGraph(ParliamentChart.ProffessionalBehavior, language));
        Assert.assertNotNull("Chart Missing:", parliamentService.getGraph(ParliamentChart.PoliticalPartyBehavior, language));
    }
}
