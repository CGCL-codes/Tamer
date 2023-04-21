package com.germinus.portlet.groupware.community_admin.action;

import java.util.Collection;
import javax.portlet.ActionRequest;
import javax.portlet.ActionResponse;
import javax.portlet.PortletConfig;
import javax.portlet.PortletRequest;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.Globals;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;
import com.germinus.xpression.groupware.action.GroupwareHelper;
import com.germinus.xpression.groupware.CommunityManager;
import com.germinus.xpression.groupware.GroupwareRuntimeException;
import com.germinus.xpression.groupware.GroupwareUser;
import com.germinus.xpression.groupware.NotAuthorizedException;
import com.germinus.xpression.groupware.communities.CommunityPersister;
import com.germinus.xpression.groupware.util.GroupwareKeys;
import com.germinus.xpression.groupware.util.GroupwareManagerRegistry;
import com.liferay.portal.struts.PortletAction;

/**
 * Deletes a group of users, including its world and all its webs.
 * @author Acheca
 */
public class DeleteCommunityAction extends PortletAction {

    private static final Log log = LogFactory.getLog(DeleteCommunityAction.class);

    public void processAction(ActionMapping mapping, ActionForm form, PortletConfig config, ActionRequest req, ActionResponse res) {
        ActionMessages errors = new ActionMessages();
        ActionMessages messages = new ActionMessages();
        String errorKey = "error.could-not-delete-community";
        String communityId = req.getParameter("communityId");
        CommunityManager communityManager = GroupwareManagerRegistry.getCommunityManager();
        GroupwareUser gwUser = GroupwareHelper.getUser(req);
        try {
            communityManager.deleteCommunityById(communityId, gwUser);
            messages.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("community_admin.community-successfully-deleted"));
        } catch (GroupwareRuntimeException ex) {
            log.warn("Error deleting community " + communityId, ex);
            errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage(errorKey));
        } catch (NotAuthorizedException ex) {
            log.warn("User " + gwUser.getId() + "is not authorized to delete the community with id " + communityId, ex);
            errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("error.you-re-not-authorized-to-delete-the-community"));
        } catch (Exception ex) {
            log.warn("Error deleting community " + communityId, ex);
            errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage(errorKey));
        }
        saveMessages(req, messages);
        saveErrors(req, errors);
        String forward = (errors.isEmpty()) ? GroupwareKeys.SUCCESS : GroupwareKeys.ERROR;
        setForward(req, forward);
    }

    protected void saveMessages(PortletRequest request, ActionMessages messages) {
        request.getPortletSession().setAttribute(Globals.MESSAGE_KEY, messages);
    }

    protected void saveErrors(PortletRequest request, ActionMessages errors) {
        request.getPortletSession().setAttribute(Globals.ERROR_KEY, errors);
    }
}
