package org.jazzteam.shareideas.webapp.actions.user;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.jazzteam.shareideas.application.AppMapping;
import org.jazzteam.shareideas.application.Application;
import org.jazzteam.shareideas.service.IAuthService;
import org.jazzteam.shareideas.service.IUserService;

/**
 * 
 * 
 * @author ADLeR
 * @version $Rev: $
 */
public class DeleteUserAction extends Action {

    @Override
    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        IUserService userService = (IUserService) Application.getBean("userService");
        IAuthService authService = (IAuthService) Application.getBean("authService");
        authService.setSession(request.getSession());
        if (!authService.isUserLogged()) {
            return mapping.findForward(AppMapping.forbidden);
        }
        userService.delete(authService.getCurrentUser());
        authService.logout();
        return mapping.findForward(AppMapping.success);
    }
}
