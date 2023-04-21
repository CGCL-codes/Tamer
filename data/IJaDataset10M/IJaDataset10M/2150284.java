package com.xinex.siteminder.struts;

import java.util.HashMap;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;
import com.netegrity.sdk.apiutil.SmApiResult;
import com.netegrity.sdk.policyapi.SmPolicy;

public final class ListUserPolicyAction extends Action {

    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        HttpSession session = request.getSession();
        ActionForward myActionFwd = new ActionForward();
        myActionFwd = mapping.findForward("loginTarget");
        if (!SessionCheck.strutsCheckSession(request)) {
            ActionMessages errors = new ActionMessages();
            ActionMessage error = new ActionMessage("error.siteminder.session");
            errors.add(ActionMessages.GLOBAL_MESSAGE, error);
            saveErrors(request, errors);
            return myActionFwd;
        }
        SiteminderService smService = (SiteminderService) session.getAttribute("SiteMinderSession");
        SmPolicy smPolicy = new SmPolicy();
        HashMap userPolicyMap = new HashMap();
        SmApiResult result = smService.smapi.getPolicyApi().getPolicy(request.getParameter("policyOid"), request.getParameter("domainOid"), smPolicy);
        userPolicyMap = smService.smapi.userPolicyApiObj.getUserPolicies(smPolicy.getOid().getOidString(), smPolicy.getDomain().getOidString());
        System.out.println("ListUserPolicyAction --> Result ? = " + result);
        UserPolicyListForm userPolicyListForm = (UserPolicyListForm) form;
        userPolicyListForm.setUserPolicyMap(userPolicyMap);
        userPolicyListForm.setDomainName(request.getParameter("domainName"));
        userPolicyListForm.setSmPolicy(smPolicy);
        return (mapping.findForward("continue"));
    }
}
