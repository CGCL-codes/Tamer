package com.creditors.http.actions;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import com.be.bo.UserObject;
import com.creditors.http.forms.PF24Form;

public class PF24Action extends Action {

    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        HttpSession session = request.getSession();
        PF24Form fForm = (PF24Form) form;
        UserObject uo = (UserObject) session.getAttribute("userObject");
        if ((fForm == null) || (uo.getFacade() == null)) {
            return (mapping.findForward("failure"));
        } else if ("insert".equals(fForm.getAction())) {
            ((com.creditors.bo.Facade) uo.getFacade("com.creditors")).insertPF24VO(fForm);
        } else if ("update".equals(fForm.getAction())) {
            ((com.creditors.bo.Facade) uo.getFacade("com.creditors")).updatePF24VO(fForm);
        } else if ("delete".equals(fForm.getAction())) {
            ((com.creditors.bo.Facade) uo.getFacade("com.creditors")).deletePF24VO(fForm);
        }
        return (mapping.findForward("success"));
    }
}
