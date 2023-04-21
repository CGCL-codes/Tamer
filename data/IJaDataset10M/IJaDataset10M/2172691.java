package com.juanfer.travelcostlog;

import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import com.google.appengine.api.users.User;

public class Main extends BaseServlet {

    @Override
    public void Get(HttpServletRequest request, HttpServletResponse response, PrintWriter writer, User user) {
        forwardTo(request, response, "/JSP/Trip/list.jsp");
    }

    @Override
    public void Post(HttpServletRequest request, HttpServletResponse response, PrintWriter writer, User user) {
    }

    private void forwardTo(HttpServletRequest req, HttpServletResponse resp, String goTo) {
        try {
            getServletConfig().getServletContext().getRequestDispatcher(goTo).forward(req, resp);
        } catch (ServletException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
