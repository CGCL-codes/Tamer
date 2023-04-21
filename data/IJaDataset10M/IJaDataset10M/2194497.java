package com.technoetic.xplanner.actions;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.log4j.Logger;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import com.technoetic.xplanner.security.SecurityHelper;
import com.technoetic.xplanner.security.auth.Authorizer;

/**
 * Generic dispatcher to ActionForwards.
 * Original Author: Ted Husted
 * Source: http://husted.com/about/scaffolding/catalog.htm
 */
public final class DispatchForward extends Action {

    private Logger log = Logger.getLogger(getClass());

    private boolean isAuthorizationRequired = true;

    private Authorizer authorizer;

    /**
     * Forward request to "cancel", {forward}, or "error" mapping, where {forward}
     * is an action path given in the parameter mapping or in the request as
     * "forward=actionPath".
     *
     * @param mapping The ActionMapping used to select this instance
     * @param form The optional ActionForm bean for this request (if any)
     * @param request The HTTP request we are processing
     * @param response The HTTP response we are creating
     *
     * @exception IOException if an input/output error occurs
     * @exception ServletException if a servlet exception occurs
     */
    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        if (isSecure(mapping)) {
            int projectId = 0;
            String projectIdParameter = request.getParameter("projectId");
            if (projectIdParameter != null) {
                projectId = Integer.parseInt(request.getParameter("projectId"));
            }
            if (projectId == 0) {
                log.error("no project identifier supplied for secure access");
                return mapping.findForward("security/notAuthorized");
            }
            if (!authorizer.hasPermission(projectId, SecurityHelper.getRemoteUserId(request), "system.project", projectId, "read")) {
                return mapping.findForward("security/notAuthorized");
            }
        }
        ActionForward thisForward = null;
        String wantForward = null;
        wantForward = mapping.getParameter();
        if (wantForward != null) thisForward = mapping.findForward(wantForward);
        if (thisForward == null) {
            thisForward = mapping.findForward("error");
            ActionErrors errors = new ActionErrors();
            errors.add(ActionErrors.GLOBAL_ERROR, new ActionError("action.missing.parameter"));
            saveErrors(request, errors);
        }
        return thisForward;
    }

    public void setAuthorizationRequired(boolean authorizationRequired) {
        isAuthorizationRequired = authorizationRequired;
    }

    public void setAuthorizer(Authorizer authorizer) {
        this.authorizer = authorizer;
    }

    private boolean isSecure(ActionMapping mapping) {
        return mapping.findForward("@secure") != null ? Boolean.valueOf(mapping.findForward("@secure").getPath()) != Boolean.FALSE : isAuthorizationRequired;
    }
}
