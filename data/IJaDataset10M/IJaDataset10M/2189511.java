package com.be.http.actions;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import com.be.http.forms.VATCodeForm;
import com.be.bo.*;

public class VATCodeAction extends Action {

    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        HttpSession session = request.getSession();
        VATCodeForm fForm = (VATCodeForm) form;
        UserObject uo = (UserObject) session.getAttribute("userObject");
        if ((fForm == null) || (uo.getFacade() == null)) {
            return (mapping.findForward("failure"));
        } else if ("insert".equals(fForm.getAction())) {
            uo.getFacade().insertVATCodeVO(fForm);
        } else if ("update".equals(fForm.getAction())) {
            uo.getFacade().updateVATCodeVO(fForm);
        } else if ("delete".equals(fForm.getAction())) {
            uo.getFacade().deleteVATCodeVO(fForm);
        }
        return (mapping.findForward("success"));
    }
}
