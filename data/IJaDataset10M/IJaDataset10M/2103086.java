package com.debitors.http.actions;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import com.be.bo.UserObject;
import com.debitors.http.forms.BillInpayForm;

public class BillInpayAction extends Action {

    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        HttpSession session = request.getSession();
        BillInpayForm fForm = (BillInpayForm) form;
        UserObject uo = (UserObject) session.getAttribute("userObject");
        if ((fForm == null) || (uo.getFacade() == null)) {
            return (mapping.findForward("failure"));
        } else if ("insert".equals(fForm.getAction())) {
            ((com.debitors.bo.Facade) uo.getFacade("com.debitors")).insertBillInpayVO(fForm);
        } else if ("update".equals(fForm.getAction())) {
            ((com.debitors.bo.Facade) uo.getFacade("com.debitors")).updateBillInpayVO(fForm);
        } else if ("delete".equals(fForm.getAction())) {
            ((com.debitors.bo.Facade) uo.getFacade("com.debitors")).deleteBillInpayVO(fForm);
        }
        return (mapping.findForward("success"));
    }
}
