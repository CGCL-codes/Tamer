package net.wgbv.photov.action;

import java.sql.Connection;
import java.sql.SQLException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import net.wgbv.photov.dam.ConnectionFactory;
import net.wgbv.photov.dam.EmailFactory;
import net.wgbv.photov.dam.PhotoFactory;
import net.wgbv.photov.dam.UserFactory;
import net.wgbv.photov.form.UserForm;
import net.wgbv.photov.objects.User;
import org.apache.log4j.Logger;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

/**
 * MyEclipse Struts Creation date: 07-21-2004
 * 
 * XDoclet definition:
 * 
 * @struts:action path="/user" name="userForm" input="/form/user.jsp"
 *                validate="true"
 * @struts:action-forward name="/form/user.jsp" path="/form/user.jsp"
 */
public class UserAction extends Action {

    /**
	 * Method execute
	 * 
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return ActionForward
	 */
    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
        Logger l = Logger.getLogger(UserAction.class);
        UserForm userForm = (UserForm) form;
        String strForward = new String(Constants.USER_EDIT_FORWARD);
        Connection conn = null;
        User user = null;
        try {
            conn = ConnectionFactory.getConnection();
            if (request.getSession().getAttribute(Constants.USER_KEY) != null) {
                user = (User) request.getSession().getAttribute(Constants.USER_KEY);
            }
            String action = null;
            if (request.getParameter(Constants.ACTION) != null) {
                action = request.getParameter(Constants.ACTION);
            }
            if ((user != null) && (conn != null) && ((user.getCanUpdate() || (user.getUserId() == userForm.getUserId())))) {
                if ((action != null) && action.equalsIgnoreCase(Constants.ACTION_ACTIVE)) {
                    UserFactory.populateUserForm(userForm, user, conn);
                    UserFactory.toggleActive(userForm, conn);
                } else if ((action != null) && action.equalsIgnoreCase(Constants.ACTION_UPDATE)) {
                    UserFactory.populateUserForm(userForm, user, conn);
                    UserFactory.toggleUpdate(userForm, conn);
                } else if ((action != null) && action.equalsIgnoreCase(Constants.ACTION_EMAIL_ACTIVATE)) {
                    EmailFactory.sendActivateEmail(UserFactory.getUserFromUserForm(userForm), conn);
                    UserFactory.populateUserForm(userForm, user, conn);
                    strForward = Constants.NO_USER_FORWARD;
                } else if ((action != null) && (action.equalsIgnoreCase(Constants.ACTION_GRP_ADD))) {
                    UserFactory.addUserGroup(userForm, user, conn);
                } else if ((action != null) && (action.equalsIgnoreCase(Constants.ACTION_GRP_REMOVE))) {
                    UserFactory.removeUserGroup(userForm, user, conn);
                } else if ((userForm.getUsername() == null)) {
                    UserFactory.populateUserForm(userForm, user, conn);
                } else if ((request.getParameter(Constants.ACTION) != null) && (request.getParameter(Constants.ACTION).equalsIgnoreCase(Constants.ACTION_CREATE))) {
                    userForm.reset(mapping, request);
                } else {
                    UserFactory.setUserForm(userForm, user, conn);
                }
            } else {
                strForward = Constants.NO_USER_FORWARD;
            }
            request.getSession().setAttribute(Constants.USER_KEY, user);
        } catch (SQLException sqle) {
            l.error(" SQL Exception in UserAction opening Connection ");
            l.error(sqle);
        } finally {
            PhotoFactory.closeConn(conn);
        }
        return (mapping.findForward(strForward));
    }
}
