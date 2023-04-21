package com.appspot.ast.server;

import java.io.IOException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;

@SuppressWarnings("serial")
public class LoginServlet extends HttpServlet {

    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        UserService userService = UserServiceFactory.getUserService();
        String thisURL = request.getRequestURI();
        if (request.getUserPrincipal() != null) {
            response.getWriter().println("<p>Hello, " + request.getUserPrincipal().getName() + "!  You can <a href=\"" + userService.createLogoutURL(thisURL) + "\">sign out</a>.</p>");
            if ("anton.starcev@gmail.com".equals(request.getUserPrincipal().getName())) {
                response.getWriter().println("Your are admin");
                RequestDispatcher requestDispatcher = request.getRequestDispatcher("/admin.jsp");
                try {
                    requestDispatcher.forward(request, response);
                } catch (ServletException e) {
                    e.printStackTrace();
                }
            }
        } else {
            response.getWriter().println("<p>Please <a href=\"" + userService.createLoginURL(thisURL) + "\">sign in</a>.</p>");
        }
    }
}
