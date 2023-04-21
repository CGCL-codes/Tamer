package com.hack23.cia.functionalintegrationtest.scenarios.service.admin;

import junit.framework.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.test.context.transaction.AfterTransaction;
import org.springframework.test.context.transaction.BeforeTransaction;
import com.hack23.cia.model.application.impl.common.Agency;
import com.hack23.cia.model.application.impl.common.Language;
import com.hack23.cia.model.application.impl.common.ParliamentChart;
import com.hack23.cia.model.sweden.impl.ParliamentMember;
import com.hack23.cia.service.dao.AgencyDAO;
import com.hack23.cia.service.dao.BallotDAO;
import com.hack23.cia.service.impl.admin.LoaderService;
import com.hack23.cia.service.impl.common.ParliamentService;
import com.hack23.cia.testfoundation.AbstractFunctionalIntegrationTransactionalTest;

/**
 * The Class LoaderServiceImplGenerateChartsSuccessFullTest.
 */
public class LoaderServiceImplGenerateChartsSuccessFullTest extends AbstractFunctionalIntegrationTransactionalTest {

    /** The agency dao. */
    @Autowired
    @Qualifier("agencyDAO")
    private AgencyDAO agencyDAO;

    /** The ballot dao. */
    @Autowired
    @Qualifier("ballotDAO")
    private BallotDAO ballotDAO;

    /** The loader service. */
    @Autowired
    @Qualifier("loaderService")
    private LoaderService loaderService;

    /** The parliament service. */
    @Autowired
    @Qualifier("parliamentService")
    private ParliamentService parliamentService;

    /**
	 * Instantiates a new loader service impl generate charts success full test.
	 */
    public LoaderServiceImplGenerateChartsSuccessFullTest() {
        super();
        this.testScenarioName = this.getClass().getSimpleName();
    }

    /**
	 * Generate charts succeeds.
	 * 
	 * @throws Exception
	 *             the exception
	 */
    @Test
    public final void generateChartsSucceeds() throws Exception {
        loaderService.generateParliamentCharts();
        ParliamentMember parliamentMember = parliamentService.findParliamentMembersByName("Sahlin").get(0);
        this.loaderService.generateParliamentMemberCharts(parliamentMember);
    }

    /**
	 * Setup database pre condition.
	 * 
	 * @throws Exception
	 *             the exception
	 */
    @BeforeTransaction
    public void setupDatabasePreCondition() throws Exception {
    }

    /**
	 * Verify database post condition.
	 * 
	 * @throws Exception
	 *             the exception
	 */
    @AfterTransaction
    public void verifyDatabasePostCondition() throws Exception {
        Agency agency = agencyDAO.load(1L);
        for (Language language : agency.getLanguages()) {
            Assert.assertNotNull("Chart Missing:", parliamentService.getGraph(ParliamentChart.Outcome, language));
            Assert.assertNotNull("Chart Missing:", parliamentService.getGraph(ParliamentChart.ProffessionalBehavior, language));
            Assert.assertNotNull("Chart Missing:", parliamentService.getGraph(ParliamentChart.PoliticalPartyBehavior, language));
            Assert.assertNotNull("Chart Missing:", parliamentService.getGraph(ParliamentChart.Summary, language));
            ParliamentMember parliamentMember = parliamentService.findParliamentMembersByName("Sahlin").get(0);
            Assert.assertNotNull("Chart Missing:", parliamentService.getGraph(ParliamentChart.Summary, parliamentMember, language));
            Assert.assertNotNull("Chart Missing:", parliamentService.getGraph(ParliamentChart.Outcome, parliamentMember, language));
            Assert.assertNotNull("Chart Missing:", parliamentService.getGraph(ParliamentChart.ProffessionalBehavior, parliamentMember, language));
            Assert.assertNotNull("Chart Missing:", parliamentService.getGraph(ParliamentChart.PoliticalPartyBehavior, parliamentMember, language));
        }
    }
}
