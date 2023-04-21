package org.jazzteam.shareideas.webapp.actions.idea.pages;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.jazzteam.shareideas.application.AppMapping;
import org.jazzteam.shareideas.application.Application;
import org.jazzteam.shareideas.model.User;
import org.jazzteam.shareideas.service.IAuthService;

/**
 * 
 * 
 * @author ADLeR
 * @version $Rev: $
 */
public class AddIdeaPageAction extends Action {

    @Override
    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        IAuthService authService = (IAuthService) Application.getBean("authService");
        authService.setSession(request.getSession());
        if (!authService.isUserLogged()) {
            return mapping.findForward(AppMapping.forbidden);
        }
        User user = authService.getCurrentUser();
        request.setAttribute("user", user);
        return mapping.findForward(AppMapping.show);
    }
}
