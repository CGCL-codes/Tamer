package org.fbc.shogiserver.actions;

import java.io.IOException;
import java.util.Map;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.fbc.shogiserver.dao.MessagesDAO;
import com.google.appengine.api.users.User;
import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;

@SuppressWarnings("serial")
public class SendMessage extends HttpServlet {

    private static final Logger log = Logger.getLogger(SendMessage.class.getName());

    @SuppressWarnings("unchecked")
    public void doPost(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        log.info("doPost method");
        Map<String, String[]> m = (Map<String, String[]>) req.getParameterMap();
        for (String param : m.keySet()) {
            log.info(param);
        }
        UserService userService = UserServiceFactory.getUserService();
        User user = userService.getCurrentUser();
        MessagesDAO.get().postMessage(user, req.getParameter("addressee"), req.getParameter("subject"), req.getParameter("message"));
        res.sendRedirect("/joinGame.jsp");
    }
}
