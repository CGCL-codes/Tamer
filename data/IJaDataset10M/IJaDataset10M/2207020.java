package uk.org.brindy.jwebdoc.actions;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

/**
 * Forwards to the main tile def.
 * 
 * @author brindy
 */
public class MainAction extends BaseAction {

    /** Execute this action. 
     * @param mapping the mapping
     * @param form the form
     * @param request the request
     * @param response the response
     * @return the action to forward to
     */
    protected ActionForward doExecute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
        return mapping.findForward("success");
    }

    /** Not logged in. 
     * @return false
     */
    protected boolean checkLogon() {
        return false;
    }
}
