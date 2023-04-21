package com.hack23.cia.functionalintegrationtest.scenarios.web.controller.user;

import junit.framework.Assert;
import org.jmock.Expectations;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.test.context.transaction.BeforeTransaction;
import com.hack23.cia.model.sweden.impl.CommitteeReport;
import com.hack23.cia.service.dao.CommitteeReportDAO;
import com.hack23.cia.web.action.user.CommitteeReportAction;
import com.hack23.cia.web.viewfactory.api.user.CommitteeReportModelAndView;

/**
 * The Class CommitteReportActionSuccessTest.
 */
public class CommitteReportActionSuccessTest extends AbstractFunctionalUserActionTest {

    /** The commitee report dao. */
    @Autowired
    @Qualifier("committeeReportDAO")
    protected CommitteeReportDAO commiteeReportDAO;

    /** The model and view matcher. */
    private ModelAndViewMatcher modelAndViewMatcher = new ModelAndViewMatcher(CommitteeReportModelAndView.class);

    /**
	 * Instantiates a new committe report action success test.
	 */
    public CommitteReportActionSuccessTest() {
        super();
        this.testScenarioName = this.getClass().getSimpleName();
    }

    /**
	 * Committee report action succeeds.
	 * 
	 * @throws Exception
	 *             the exception
	 */
    @Test
    public final void committeeReportActionSucceeds() throws Exception {
        CommitteeReport committeeReport = commiteeReportDAO.getAllLastDecided().get(0);
        CommitteeReportAction action = new CommitteeReportAction(committeeReport.getId());
        this.frontController.handleAction(action);
    }

    @Override
    @BeforeTransaction
    public void setupDatabasePreCondition() throws Exception {
        createInitialUserSession();
    }

    @Override
    @Before
    public void setupExpectedModelAndView() throws Exception {
        context.checking(new Expectations() {

            {
                one(viewFactoryService).processView(with(modelAndViewMatcher));
            }
        });
    }

    @Override
    @After
    public void verifyModelAndView() throws Exception {
        CommitteeReportModelAndView modelAndView = (CommitteeReportModelAndView) modelAndViewMatcher.getModelAndView();
        Assert.assertNotNull(modelAndView.getControllerAction());
        Assert.assertNotNull(modelAndView.getUserSessionDTO());
        Assert.assertEquals(CommitteeReportModelAndView.CommitteeReportViewSpecification.CommitteeReportView, modelAndView.getCommitteeReportViewSpecification());
        Assert.assertEquals(CommitteeReportModelAndView.CommitteeReportViewSpecification.CommitteeReportView.toString(), modelAndView.getViewSpecificationDescription());
        Assert.assertNotNull(modelAndView.getCommitteeReport());
    }
}
