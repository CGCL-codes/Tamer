package com.org.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import com.org.beans.AdminUsersForm;
import com.org.daoImp.AdminLoginDao;

public class AdminLoginController extends Action {

    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        HttpSession session = request.getSession();
        AdminUsersForm adminForm = (AdminUsersForm) form;
        AdminLoginDao dao = new AdminLoginDao();
        String sessionUser = dao.adminLogin(adminForm.getAdminUserName(), adminForm.getAdminPassword());
        if (sessionUser != null) {
            session.setAttribute("adminlogin", sessionUser);
            return mapping.findForward("success");
        } else {
            request.setAttribute("msg", "Wrong Usernamr and Password");
            return mapping.findForward("fail");
        }
    }
}
