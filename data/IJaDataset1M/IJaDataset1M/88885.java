package net.sf.mercator.web.action;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

/**
* Generic dispatcher to ActionForwards.
* @author Ted Husted
* @version $Revision: 1.1 $ $Date: 2003/06/18 11:02:42 $
*/
public class DispatchForward extends Action {

    /**
	* Forward request to "cancel", {forward}, or "error" mapping, where {forward}
	* is an action path given in the parameter mapping or in the request as
	* "forward=actionPath".
	*
	* @param mapping The ActionMapping used to select this instance
	* @param actionForm The optional ActionForm bean for this request (if any)
	* @param request The HTTP request we are processing
	* @param response The HTTP response we are creating
	*
	* @exception IOException if an input/output error occurs
	* @exception ServletException if a servlet exception occurs
	*/
    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        if (isCancelled(request)) {
            form.reset(mapping, request);
            return (mapping.findForward("cancel"));
        }
        ActionForward thisForward = null;
        String wantForward = null;
        wantForward = mapping.getParameter();
        if (wantForward == null) wantForward = request.getParameter("forward");
        if (wantForward != null) thisForward = mapping.findForward(wantForward);
        if (thisForward == null) {
            thisForward = mapping.findForward("error");
            ActionErrors errors = new ActionErrors();
            errors.add(ActionErrors.GLOBAL_ERROR, new ActionError("action.missing.parameter"));
            saveErrors(request, errors);
        }
        return thisForward;
    }
}
