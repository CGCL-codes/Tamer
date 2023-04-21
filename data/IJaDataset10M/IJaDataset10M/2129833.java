package com.debitors.http.actions;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import com.be.bo.UserObject;
import com.debitors.http.forms.PFT4Form;

public class PFT4Action extends Action {

    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        HttpSession session = request.getSession();
        PFT4Form fForm = (PFT4Form) form;
        UserObject uo = (UserObject) session.getAttribute("userObject");
        if ((fForm == null) || (uo.getFacade() == null)) {
            return (mapping.findForward("failure"));
        } else if ("insert".equals(fForm.getAction())) {
            ((com.debitors.bo.Facade) uo.getFacade("com.debitors")).insertPFT4VO(fForm);
        } else if ("update".equals(fForm.getAction())) {
            ((com.debitors.bo.Facade) uo.getFacade("com.debitors")).updatePFT4VO(fForm);
        } else if ("delete".equals(fForm.getAction())) {
            ((com.debitors.bo.Facade) uo.getFacade("com.debitors")).deletePFT4VO(fForm);
        }
        return (mapping.findForward("success"));
    }
}
