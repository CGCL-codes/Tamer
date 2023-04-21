package com.liferay.portlet.mail.action;

import com.liferay.portal.struts.PortletAction;
import com.liferay.portal.util.Constants;
import javax.portlet.PortletConfig;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;
import javax.servlet.jsp.PageContext;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

/**
 * <a href="ViewMessageAction.java.html"><b><i>View Source</i></b></a>
 *
 * @author  Brian Wing Shun Chan
 *
 */
public class ViewMessageAction extends PortletAction {

    public ActionForward render(ActionMapping mapping, ActionForm form, PortletConfig config, RenderRequest req, RenderResponse res) throws Exception {
        try {
            ActionUtil.getMessage(req);
            ActionUtil.getAttachments(req);
            return mapping.findForward("portlet.mail.view_message");
        } catch (Exception e) {
            req.setAttribute(PageContext.EXCEPTION, e);
            return mapping.findForward(Constants.COMMON_ERROR);
        }
    }
}
