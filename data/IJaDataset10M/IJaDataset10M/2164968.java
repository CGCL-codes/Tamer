package br.guj.chat.service.instance;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import br.guj.chat.ChatCheckedException;
import br.guj.chat.model.server.ChatServer;
import br.guj.chat.service.ManagementService;
import br.guj.chat.user.AdminUser;

/**
 * Changes template properties
 * @author Guilherme de Azevedo Silveira
 * @version $Revision: 1.2 $, $Date: 2003/10/20 13:24:11 $
 */
public class EditTemplateProperties extends ManagementService {

    /**
	 * Performs the action
	 */
    public void perform(HttpServletRequest request, HttpServletResponse response, AdminUser user) throws ChatCheckedException {
        ChatServer server = getServer(request.getParameter("instance"));
        if (user.isSuperuser() || server.getAdminUser().equals(user)) {
            if (request.getParameter("cache").equals("true")) {
                server.setCacheTemplates(true);
                server.setCacheTemplatesTime(Integer.parseInt(request.getParameter("cache.time")));
            } else {
                server.setCacheTemplates(false);
            }
            server.getLogger().info(user.getID() + " changed caching properties");
        }
        displayPage(request, response, user);
    }
}
