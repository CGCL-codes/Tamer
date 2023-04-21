package br.guj.chat.service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import br.guj.chat.ChatCheckedException;
import br.guj.chat.user.AdminUser;

/**
 * Basic service for entrance page
 * @author Guilherme de Azevedo Silveira
 * @version $Revision: 1.5 $, $Date: 2003/04/23 14:39:27 $
 */
public class GujChat extends ManagementService {

    /**
	 * Displays the entrance page for the server
	 */
    public void perform(HttpServletRequest request, HttpServletResponse response, AdminUser user) throws ChatCheckedException {
        String view = request.getParameter("view");
        String viewAttribute = (String) request.getAttribute("view");
        displayPage(request, response, view == null ? (viewAttribute == null ? "index.html" : viewAttribute) : view, user);
    }
}
