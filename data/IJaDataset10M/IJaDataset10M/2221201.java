package org.hip.vif.groupadmin.tasks;

import org.hip.kernel.bom.QueryResult;
import org.hip.kernel.exc.VException;
import org.hip.kernel.servlet.HtmlView;
import org.hip.kernel.servlet.impl.DefaultHtmlPage;
import org.hip.vif.bom.Completion;
import org.hip.vif.bom.Question;
import org.hip.vif.groupadmin.Activator;
import org.hip.vif.groupadmin.Constants;
import org.hip.vif.groupadmin.views.CompletionView;
import org.hip.vif.registry.IPluggableTask;
import org.hip.vif.servlets.AbstractVIFAdminTask;
import org.hip.vif.servlets.VIFContext;

/**
 * Task to edit a contribution.
 * 
 * @author Benno Luthiger
 */
public class AdminCompletionEditTask extends AbstractVIFAdminTask implements IPluggableTask {

    /**
	 * @see org.hip.vif.servlets.AbstractVIFTask#needsPermission()
	 */
    protected String needsPermission() {
        return "newCompletionAdmin";
    }

    /**
	 * @see org.hip.kernel.servlet.Task#run()
	 */
    public void runChecked() throws VException {
        VIFContext lContext = (VIFContext) getContext();
        try {
            String lCompletionID = lContext.getParameterValue(VIFContext.KEY_COMPLETION_ID);
            String lQuestionID = lContext.getParameterValue(VIFContext.KEY_QUESTION_ID);
            Completion lCompletion = getCompletionHome().getCompletion(lCompletionID);
            QueryResult lSiblings = getCompletionHome().getSiblingCompletions(lQuestionID, lCompletionID);
            Question lQuestion = getQuestionHome().getQuestion(lQuestionID);
            DefaultHtmlPage lPage = new DefaultHtmlPage();
            HtmlView lView = new CompletionView(lContext, lCompletion, lSiblings, lQuestion);
            prepareVIFPage(lPage, Activator.getPartletHelper().renderOnLoad(Constants.TASK_SET_ID_AMDIN));
            lPage.add(lView);
            lContext.setView(lPage);
        } catch (Exception exc) {
            throw createContactAdminException(exc, lContext.getLocale());
        }
    }
}
