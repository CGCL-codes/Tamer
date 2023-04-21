package com.google.appengine.twitter.interfaces.web;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import com.google.inject.Singleton;

/**
 *
 * @author politics wang
 * @since Apr 23, 2009
 *
 */
@Singleton
public class SignupServlet extends HttpServlet {

    private static final long serialVersionUID = 5153764183348099860L;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.sendRedirect("signup.html");
    }
}
