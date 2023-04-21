package com.xiyou.cms.struts.user.action;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import com.xiyou.cms.hibernate.dao.XyRoleDAO;
import com.xiyou.cms.hibernate.dao.XyUserDAO;
import com.xiyou.cms.hibernate.mapping.XyUser;
import com.xiyou.cms.struts.user.form.UserAddForm;
import com.xiyou.cms.util.Md5;

/** 
 * MyEclipse Struts
 * Creation date: 08-10-2008
 * 
 * XDoclet definition:
 * @struts.action path="/user/userAdd" name="userAddForm" input="/form/userAdd.jsp" scope="request" validate="true"
 * @struts.action-forward name="goto" path="/user/showUserList.do"
 */
public class UserAddAction extends Action {

    /** 
	 * Method execute
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return ActionForward
	 */
    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
        UserAddForm userAddForm = (UserAddForm) form;
        XyUser user = new XyUser();
        XyUserDAO userDAO = new XyUserDAO();
        user.setXyRole(new XyRoleDAO().findById(Integer.valueOf(userAddForm.getUserRole())));
        user.setUserName(userAddForm.getUserName());
        user.setUserPassword(Md5.str2MD5(userAddForm.getUserPassword()));
        user.setUserRealName(userAddForm.getUserRealName());
        user.setUserSex(userAddForm.getUserSex());
        user.setUserTel(userAddForm.getUserTel());
        userDAO.save(user);
        return mapping.findForward("goto");
    }
}
