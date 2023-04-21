package org.bionote.webapp.action.setup.space;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;
import org.bionote.om.IUser;
import org.bionote.om.service.SpaceService;
import org.bionote.om.service.UserService;
import org.bionote.webapp.action.BaseStrutsAction;

/**
 * @author mbreese
 *
 */
public class AccessAdd extends BaseStrutsAction {

    public ActionForward bionoteExecute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        if (isCancelled(request)) {
            return mapping.findForward("Cancel");
        }
        if (container.getUser() == null || (!container.getUser().isAdmin() && !container.isUserSpaceAdmin())) {
            return mapping.findForward("NotAllowed");
        }
        String username = getParameterString(request, "username");
        Boolean admin = getParameterBoolean(request, "admin");
        if (admin == null) {
            admin = Boolean.FALSE;
        }
        if (username != null) {
            log.debug(username + ":" + admin);
            SpaceService spaceService = (SpaceService) springContext.getBean("spaceService");
            UserService userService = (UserService) springContext.getBean("userService");
            IUser user = userService.findUser(username);
            if (user != null) {
                log.debug("granting");
                spaceService.grantUserAccess(container.getSpace(), user, admin.booleanValue());
                log.debug("refreshing space");
                container.refreshSpace();
                log.debug("done");
                if (user.equals(container.getUser())) {
                    log.debug("refreshing user");
                    container.refreshUser();
                    log.debug("done");
                }
            } else {
                ActionMessages errors = new ActionMessages();
                errors.add("", new ActionMessage("error.user.not.found", username));
                saveErrors(request, errors);
            }
        }
        return mapping.findForward("Success");
    }
}
