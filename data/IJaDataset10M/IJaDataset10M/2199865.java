package br.guj.chat.service.instance;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import br.guj.chat.ChatCheckedException;
import br.guj.chat.model.server.ChatServer;
import br.guj.chat.model.server.Login;
import br.guj.chat.service.ManagementService;
import br.guj.chat.user.AdminUser;

/**
 * Changes the login properties<br>
 * @author Guilherme de Azevedo Silveira
 * @version $Revision: 1.6 $, $Date: 2003/07/18 11:21:20 $
 */
public class EditLogin extends ManagementService {

    /**
	 * Performs the action
	 * @see br.guj.chat.service.Service#perform(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse, br.guj.chat.user.AdminUser)
	 */
    public void perform(HttpServletRequest request, HttpServletResponse response, AdminUser user) throws ChatCheckedException {
        ChatServer server = getServer(request.getParameter("instance"));
        if (user.isSuperuser() || server.getAdminUser().equals(user)) {
            Login l = server.getLogin();
            l.setCenter(request.getParameter("Logo.Center").equals("true"));
            l.setLink(request.getParameter("Logo.Link"));
            l.setPicture(request.getParameter("Logo.Picture"));
            l.setTitle(request.getParameter("Logo.Text"));
            l.setValidUsernameCharacters(request.getParameter("Misc.ValidCharacters"));
            getServerDAO().update(server);
            server.getLogger().info("updated the login options");
        }
        displayPage(request, response, user);
    }
}
