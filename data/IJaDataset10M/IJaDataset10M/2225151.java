package com.quikj.application.communicator.applications.webtalk.controller;

import java.sql.Connection;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.DynaActionForm;
import com.quikj.application.communicator.admin.model.AccountElement;

/**
 * 
 * @author bhm
 */
public class GroupWizardFinishedAction extends Action {

    /** Creates a new instance of GroupWizardViewLogAction */
    public GroupWizardFinishedAction() {
    }

    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
        ActionErrors errors = new ActionErrors();
        Connection c = (Connection) request.getSession().getAttribute("connection");
        if (c == null) {
            errors.add(ActionErrors.GLOBAL_ERROR, new ActionError("error.not.logged.in"));
            saveErrors(request, errors);
            return mapping.findForward("logon");
        }
        AccountElement element = (AccountElement) request.getSession().getAttribute("userInfo");
        if (element.isAdminLevel() == false) {
            errors.add(ActionErrors.GLOBAL_ERROR, new ActionError("error.insufficient.privilege"));
            saveErrors(request, errors);
            return mapping.findForward("main_menu");
        }
        String submit = (String) ((DynaActionForm) form).get("submit");
        if (submit.equals("Exit") == true) {
            request.getSession().setAttribute("groupWizardLog", null);
            request.getSession().setAttribute("groupWizardDomain", null);
            request.getSession().setAttribute("groupWizardCompanyName", null);
            request.getSession().setAttribute("groupWizardCompanyUrl", null);
        }
        return mapping.findForward(submit);
    }
}
