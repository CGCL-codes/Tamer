package com.coder.gaeblogger;

import java.io.IOException;
import javax.servlet.http.*;

@SuppressWarnings("serial")
public class GaebloggerServlet extends HttpServlet {

    public void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        resp.setContentType("text/plain");
        resp.getWriter().println("Wel Come GAE-Blog");
    }
}
