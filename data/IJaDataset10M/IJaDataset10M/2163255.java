package com.liferay.portlet.pollsdisplay.action;

import com.liferay.portal.struts.PortletAction;
import com.liferay.portal.util.Constants;
import com.liferay.portlet.PortletPreferencesFactory;
import com.liferay.portlet.polls.NoSuchQuestionException;
import com.liferay.portlet.polls.model.PollsQuestion;
import com.liferay.portlet.polls.service.spring.PollsQuestionServiceUtil;
import com.liferay.util.ParamUtil;
import com.liferay.util.servlet.SessionErrors;
import com.liferay.util.servlet.SessionMessages;
import javax.portlet.ActionRequest;
import javax.portlet.ActionResponse;
import javax.portlet.PortletConfig;
import javax.portlet.PortletPreferences;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

/**
 * <a href="EditConfigurationAction.java.html"><b><i>View Source</i></b></a>
 *
 * @author  Brian Wing Shun Chan
 *
 */
public class EditConfigurationAction extends PortletAction {

    public void processAction(ActionMapping mapping, ActionForm form, PortletConfig config, ActionRequest req, ActionResponse res) throws Exception {
        try {
            String cmd = ParamUtil.getString(req, Constants.CMD);
            if (!cmd.equals(Constants.UPDATE)) {
                return;
            }
            String questionId = ParamUtil.getString(req, "questionId");
            PollsQuestion question = PollsQuestionServiceUtil.getQuestion(questionId);
            String portletResource = ParamUtil.getString(req, "portletResource");
            PortletPreferences prefs = PortletPreferencesFactory.getPortletSetup(req, portletResource, true, true);
            prefs.setValue("question-id", questionId);
            prefs.store();
            SessionMessages.add(req, config.getPortletName() + ".doConfigure");
        } catch (NoSuchQuestionException nsqe) {
            SessionErrors.add(req, nsqe.getClass().getName());
        }
    }

    public ActionForward render(ActionMapping mapping, ActionForm form, PortletConfig config, RenderRequest req, RenderResponse res) throws Exception {
        return mapping.findForward("portlet.polls_display.edit_configuration");
    }
}
