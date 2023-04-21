package com.debitors.http.actions;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import com.be.bo.UserObject;
import com.debitors.http.forms.CustomerItemDiscountForm;

public class CustomerItemDiscountAction extends Action {

    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        HttpSession session = request.getSession();
        CustomerItemDiscountForm fForm = (CustomerItemDiscountForm) form;
        UserObject uo = (UserObject) session.getAttribute("userObject");
        if ((fForm == null) || (uo.getFacade() == null)) {
            return (mapping.findForward("failure"));
        } else if ("insert".equals(fForm.getAction())) {
            ((com.debitors.bo.Facade) uo.getFacade("com.debitors")).insertCustomerItemDiscountVO(fForm);
        } else if ("update".equals(fForm.getAction())) {
            ((com.debitors.bo.Facade) uo.getFacade("com.debitors")).updateCustomerItemDiscountVO(fForm);
        } else if ("delete".equals(fForm.getAction())) {
            ((com.debitors.bo.Facade) uo.getFacade("com.debitors")).deleteCustomerItemDiscountVO(fForm);
        }
        return (mapping.findForward("success"));
    }
}
