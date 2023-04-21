package org.jazzteam.shareideas.webapp.actions.idea;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.jazzteam.shareideas.application.AppMapping;
import org.jazzteam.shareideas.application.Application;
import org.jazzteam.shareideas.model.Idea;
import org.jazzteam.shareideas.service.IAuthService;
import org.jazzteam.shareideas.service.IIdeaService;

/**
 * Adds post to idea.
 * 
 * @author ADLeR
 * @version $Rev: $
 */
public class DeleteIdeaAction extends Action {

    @Override
    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        IIdeaService ideaService = (IIdeaService) Application.getBean("ideaService");
        IAuthService authService = (IAuthService) Application.getBean("authService");
        authService.setSession(request.getSession());
        if (!authService.isUserLogged()) {
            return mapping.findForward(AppMapping.forbidden);
        }
        long ideaId = 0;
        try {
            ideaId = Integer.parseInt(request.getParameter("id"));
        } catch (Exception e) {
            return mapping.findForward(AppMapping.error);
        }
        Idea idea = ideaService.get(ideaId);
        if (idea == null) {
            return mapping.findForward(AppMapping.error);
        }
        if (!ideaService.isOwner(authService.getCurrentUser(), idea)) {
            return mapping.findForward(AppMapping.forbidden);
        }
        ideaService.delete(idea);
        return mapping.findForward(AppMapping.success);
    }
}
