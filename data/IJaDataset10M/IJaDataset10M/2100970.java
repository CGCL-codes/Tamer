package org.njo.webapp.root.action.admin;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.njo.commons.lang.StringUtil;
import org.njo.webapp.root.model.activity.UseerAdminActivity;

/**
 * 更新用户信息.
 *
 * @author yu.peng
 * @version 0.01
 */
public class SaveUserAction extends Action {

    /**
     * Process the specified HTTP request, and create the corresponding HTTP
     * response (or forward to another web component that will create it).
     * Return an <code>ActionForward</code> instance describing where and how
     * control should be forwarded, or <code>null</code> if the response has
     * already been completed.
     *
     * @param mapping  The ActionMapping used to select this instance
     * @param form     The optional ActionForm bean for this request (if any)
     * @param request  The HTTP request we are processing
     * @param response The HTTP response we are creating
     * @throws Exception if the application business logic throws
     *                   an exception
     */
    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        UseerAdminActivity userAdmin = new UseerAdminActivity();
        String name = request.getParameter("name");
        String password = request.getParameter("password");
        String resetPassword = request.getParameter("resetPassword");
        String description = request.getParameter("description");
        String[] userGroups = request.getParameterValues("userGroups");
        String[] userRoles = request.getParameterValues("userRoles");
        if (StringUtil.isEmptyOrSpaces(resetPassword)) {
            userAdmin.updateUser(name, description, userGroups, userRoles);
        } else {
            userAdmin.updateUser(name, password, description, userGroups, userRoles);
        }
        return (new ActionForward("/processor.listuser.tiles", false));
    }
}
