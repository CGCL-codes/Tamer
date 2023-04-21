package com.bardsoftware.foronuvolo.server;

import java.io.IOException;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import com.bardsoftware.foronuvolo.data.ForumUser;

public class StartDiscussionServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        if (!req.getRequestURL().toString().startsWith(ForoNuvoloConstants.FORUM_DOMAIN)) {
            return;
        }
        RequestDispatcher dispatcher = getServletContext().getRequestDispatcher(JspHelper.getJspName("start_discussion.jsp"));
        ForumUser user = com.bardsoftware.foronuvolo.server.UserService.getUser(req);
        req.setAttribute("nickname", user.getNickname());
        if (!ForumUser.ANONYMOUS.equals(user)) {
            req.setAttribute("user_id", user.getID());
        }
        req.setAttribute("friendConnectID", ForoNuvoloConstants.FRIEND_CONNECT_SITE_ID);
        dispatcher.forward(req, resp);
    }
}
