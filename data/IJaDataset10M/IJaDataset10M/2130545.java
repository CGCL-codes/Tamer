package com.xiyou.cms.struts.login.action;

import java.util.Iterator;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import com.xiyou.cms.util.Md5;
import com.xiyou.cms.hibernate.dao.XyUserDAO;
import com.xiyou.cms.hibernate.mapping.XyUser;
import com.xiyou.cms.struts.login.form.UserLoginForm;

/** 
 * MyEclipse Struts
 * Creation date: 07-26-2008
 * 
 * XDoclet definition:
 * @struts.action path="/userLogin" name="userLoginForm" input="/login/userLogin.jsp" scope="request" validate="true"
 * @struts.action-forward name="success" path="/index.jsp"
 * @struts.action-forward name="fail" path="login/userLogin.jsp"
 */
public class UserLoginAction extends Action {

    /** 
	 * Method execute
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return ActionForward
	 */
    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
        UserLoginForm userLoginForm = (UserLoginForm) form;
        List list;
        HttpSession session = request.getSession();
        XyUserDAO userDao = new XyUserDAO();
        String username = userLoginForm.getUserName();
        String password = userLoginForm.getPassword();
        list = userDao.findByUserName(username);
        Iterator it = list.iterator();
        while (it.hasNext()) {
            XyUser user = (XyUser) it.next();
            String changedPassword = Md5.str2MD5(userLoginForm.getPassword());
            if (user.getUserPassword().trim().equals(changedPassword)) {
                session.setAttribute("user", user);
                return mapping.findForward("success");
            }
        }
        System.out.println("fial");
        userLoginForm.setCheckNum("");
        request.setAttribute("error", "�û�����������!");
        return mapping.findForward("fail");
    }
}
