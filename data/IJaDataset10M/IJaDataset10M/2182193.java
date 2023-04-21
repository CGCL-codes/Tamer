package de.haumacher.timecollect.ui.http;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * {@link HttpServlet} that redirects to the value of the given {@link PageFlow#NEXT_PARAM}.
 * 
 * @author haui
 */
public class PingServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String next = req.getParameter(PageFlow.NEXT_PARAM);
        resp.sendRedirect(next);
    }
}
