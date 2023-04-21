package org.hip.vif.groupadmin.tasks;

import org.hip.kernel.exc.VException;
import org.hip.kernel.servlet.HtmlView;
import org.hip.kernel.servlet.impl.AbstractHtmlPage;
import org.hip.kernel.servlet.impl.DefaultHtmlPage;
import org.hip.vif.bom.Question;
import org.hip.vif.bom.QuestionHome;
import org.hip.vif.bom.VIFWorkflowAware;
import org.hip.vif.groupadmin.Activator;
import org.hip.vif.groupadmin.Constants;
import org.hip.vif.groupadmin.views.QuestionView;
import org.hip.vif.registry.IPluggableTask;
import org.hip.vif.servlets.VIFContext;

/**
 * Task to search questions.
 * 
 * @author Benno Luthiger
 */
public class AdminQuestionEditTask extends AbstractQuestionShowTask implements IPluggableTask {

    /**
	 * @see org.hip.kernel.servlet.Task#run()
	 */
    public void runChecked() throws VException {
        VIFContext lContext = (VIFContext) getContext();
        try {
            String lQuestionID = lContext.getParameterValue(VIFContext.KEY_QUESTION_ID);
            Question lQuestion = getQuestionHome().getQuestion(lQuestionID);
            if (((VIFWorkflowAware) lQuestion).isPrivate()) {
                editQuestion(lContext, lQuestionID, lQuestion);
            } else {
                redisplayQuestion(lContext, lQuestion);
            }
        } catch (Exception exc) {
            throw createContactAdminException(exc, lContext.getLocale());
        }
    }

    private void editQuestion(VIFContext inContext, String inQuestionID, Question inQuestion) throws Exception {
        DefaultHtmlPage lPage = new DefaultHtmlPage();
        HtmlView lView = new QuestionView(inContext, inQuestion, getQuestionHierarchyHome().getParentQuestion(inQuestionID), getChildren(inQuestionID), getAuthors(inQuestionID), getReviewers(inQuestionID), getGroupHome().getGroup(inContext.getParameterValue(VIFContext.KEY_GROUP_ID)), getContributions(inQuestionID), false);
        prepareVIFPage(lPage, Activator.getPartletHelper().renderOnLoad(Constants.TASK_SET_ID_LIST_PUBLISH));
        lPage.add(lView);
        inContext.setView(lPage);
    }

    private void redisplayQuestion(VIFContext inContext, Question inQuestion) throws VException, ClassNotFoundException, InstantiationException, IllegalAccessException {
        String lGroupID = inQuestion.get(QuestionHome.KEY_GROUP_ID).toString();
        inContext.setParameter(VIFContext.KEY_GROUP_ID, lGroupID);
        runForwarded(Activator.getContext().getBundle().getSymbolicName() + ".showQuestion", inContext);
        ((AbstractHtmlPage) inContext.getView()).setErrorMessage(Activator.getMessages().getMessage("org.hip.vif.admin.errmsg.published", inContext.getLocale()));
    }
}
