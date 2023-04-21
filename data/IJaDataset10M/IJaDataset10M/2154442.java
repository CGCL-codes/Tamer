package com.young.struts.action;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import DAOIMPL.CARDimpl;
import Factory.DAOimplFactory;
import VO.EMPBEAN;
import com.young.struts.form.LoginForm;

/** 
 * MyEclipse Struts
 * Creation date: 08-10-2007
 * 
 * XDoclet definition:
 * @struts.action path="/login" name="loginForm" input="/login.jsp" scope="request" validate="true"
 */
public class LoginAction extends Action {

    /** 
	 * Method execute
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return ActionForward
	 */
    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
        LoginForm loginForm = (LoginForm) form;
        boolean isUser = false;
        String id = loginForm.getID();
        EMPBEAN user = new EMPBEAN();
        CARDimpl beanimpl = DAOimplFactory.getCARDEANInstance();
        try {
            isUser = beanimpl.user_check(id);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (isUser) {
            return mapping.findForward("loginSuccess");
        } else {
            return mapping.findForward("loginFail");
        }
    }
}
