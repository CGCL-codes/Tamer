package com.struts.action.user;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.actions.DispatchAction;
import com.spring.service.user.AdminUidService;
import com.struts.form.AdminUidForm;

public class AdminUidAction extends DispatchAction {

    private AdminUidService adminUidService = null;

    /**
	 * manage user login
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 */
    public ActionForward login(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
        HttpSession session = request.getSession(true);
        AdminUidForm adminuidform = (AdminUidForm) form;
        boolean bool = adminUidService.checkAdminUid(adminuidform);
        if (bool) {
            session.setAttribute("uid", adminuidform.getUid());
            return mapping.findForward("success");
        } else return mapping.findForward("fail");
    }

    public AdminUidService getAdminUidService() {
        return adminUidService;
    }

    public void setAdminUidService(AdminUidService adminUidService) {
        this.adminUidService = adminUidService;
    }
}
