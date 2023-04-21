package com.bardsoftware.foronuvolo.server;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import com.bardsoftware.foronuvolo.data.ForumUser;

public class MyAccountServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        ForumUser user = UserService.getUser(req);
        if (user != null) {
            resp.getWriter().println(user.getDisplayName());
        }
    }
}
