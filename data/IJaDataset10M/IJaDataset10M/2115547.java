package com.easyblog.web.action;

import java.util.Date;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import com.easyblog.core.dto.User;
import com.easyblog.core.service.UserService;
import com.easyblog.core.util.MD5Utils;
import com.easyblog.core.util.UserSession;

public class LoginAction extends BaseAction {

    @Override
    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        super.execute(mapping, form, request, response);
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        UserService userService = (UserService) locateService("userService", UserService.class);
        User foundUser = userService.loginUser(username, MD5Utils.getAsMD5(password));
        if (foundUser != null && foundUser.getId() > 0 && foundUser.getActivated() > 0) {
            foundUser.setLoginTime(new Date());
            userService.updateUser(foundUser);
            foundUser.setFirstName(MD5Utils.decodeUTF8String(foundUser.getFirstName()));
            foundUser.setLastName(MD5Utils.decodeUTF8String(foundUser.getLastName()));
            ((UserSession) request.getSession().getAttribute(UserSession.USER_SESSION_KEY)).setCurrentUser(foundUser);
        } else {
            request.setAttribute("loginFailure", "Error");
            return mapping.findForward("failure");
        }
        String url = null;
        if (request.getSession().getAttribute("ACT_URL") != null) {
            url = (String) request.getSession().getAttribute("ACT_URL");
        } else {
            url = (String) request.getSession().getAttribute("PRE_URL");
        }
        return new ActionForward(url.substring(url.lastIndexOf("/")), true);
    }
}
