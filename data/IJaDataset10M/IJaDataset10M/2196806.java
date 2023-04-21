package org.bionote.webapp.action;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

/**
 * @author mbreese
 *
 */
public class RSSPrivateLink extends BaseStrutsAction {

    public ActionForward bionoteExecute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        Long pageId = getParameterLong(request, "pageId");
        request.setAttribute("pageId", pageId);
        if (container.getUser() != null) {
            return mapping.findForward("View");
        } else {
            return mapping.findForward("Signin");
        }
    }
}
