package com.mat.http.actions;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import com.be.bo.GlobalParameter;
import com.be.bo.UserObject;
import com.mat.http.forms.ItemForm;

public class ItemAction extends Action {

    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        HttpSession session = request.getSession();
        ItemForm fForm = (ItemForm) form;
        UserObject uo = (UserObject) session.getAttribute("userObject");
        if (this.isCancelled(request)) {
        } else if ((fForm == null) || (uo.getFacade() == null)) {
            return (mapping.findForward("failure"));
        } else if ("insert".equals(fForm.getAction())) {
            ((com.mat.bo.Facade) uo.getFacade(GlobalParameter.facadeMat)).insertItemVO(fForm);
        } else if ("update".equals(fForm.getAction())) {
            if (request.getParameter("buttonCopy") != null) {
                ((com.mat.bo.Facade) uo.getFacade(GlobalParameter.facadeMat)).insertNewItemVO(fForm);
            } else {
                ((com.mat.bo.Facade) uo.getFacade(GlobalParameter.facadeMat)).updateItemVO(fForm);
            }
        } else if ("delete".equals(fForm.getAction())) {
            ((com.mat.bo.Facade) uo.getFacade(GlobalParameter.facadeMat)).deleteItemVO(fForm);
        }
        if (request.getParameter("forward") != null) {
            ActionForward forward = new ActionForward();
            forward.setPath(request.getParameter("forward"));
            return (forward);
        }
        return (mapping.findForward("success"));
    }
}
