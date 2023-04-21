package br.guj.chat.service.action;

import java.util.Iterator;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import br.guj.chat.ChatCheckedException;
import br.guj.chat.model.action.Action;
import br.guj.chat.model.action.ActionGroup;
import br.guj.chat.model.server.ChatServer;
import br.guj.chat.service.ManagementService;
import br.guj.chat.user.AdminUser;

/**
 * Edit all actions for an user group
 * @author Guilherme de Azevedo Silveira
 * @version $Revision: 1.5 $, $Date: 2003/07/18 11:19:03 $
 */
public class EditActions extends ManagementService {

    /**
	 * Performs an action
	 * @see br.guj.chat.service.Service#perform(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse, br.guj.chat.user.AdminUser)
	 */
    public void perform(HttpServletRequest request, HttpServletResponse response, AdminUser user) throws ChatCheckedException {
        ChatServer server = getServer(request.getParameter("instance"));
        if (user.isSuperuser() || server.getAdminUser().equals(user)) {
            ActionGroup actionGroup = getActionGroup(server, request.getParameter("actionGroup"));
            Iterator i = actionGroup.getActions().iterator();
            while (i.hasNext()) {
                Action action = (Action) i.next();
                action.setName(request.getParameter("action_" + action.getID() + "_name"));
                action.setMask(request.getParameter("action_" + action.getID() + "_mask"));
                action.setToMask(request.getParameter("action_" + action.getID() + "_tomask"));
            }
            getActionDAO(server).update(actionGroup);
            server.getLogger().info("edited actions for action group " + actionGroup.getID());
        }
        displayPage(request, response, request.getParameter("view"), user);
    }
}
