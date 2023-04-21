package com.liferay.portlet.polls.action;

import javax.portlet.PortletConfig;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;
import javax.servlet.jsp.PageContext;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import com.liferay.portal.struts.PortletAction;
import com.liferay.portal.util.Constants;
import com.liferay.portlet.polls.NoSuchQuestionException;
import com.liferay.util.servlet.SessionErrors;

/**
 * <a href="ViewResultsAction.java.html"><b><i>View Source</i></b></a>
 *
 * @author  Brian Wing Shun Chan
 * @version $Revision: 1.3 $
 *
 */
public class ViewResultsAction extends PortletAction {

    public ActionForward render(ActionMapping mapping, ActionForm form, PortletConfig config, RenderRequest req, RenderResponse res) throws Exception {
        try {
            ActionUtil.getQuestion(req);
            return mapping.findForward("portlet.polls.view_results");
        } catch (Exception e) {
            if (e != null && e instanceof NoSuchQuestionException) {
                SessionErrors.add(req, e.getClass().getName());
                return mapping.findForward("portlet.polls.error");
            } else {
                req.setAttribute(PageContext.EXCEPTION, e);
                return mapping.findForward(Constants.COMMON_ERROR);
            }
        }
    }
}
