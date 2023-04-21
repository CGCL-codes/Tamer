package org.fpdev.apps.web.servlets;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author demory
 */
public class BatchCoordResolveServlet extends FPServlet {

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        count_++;
        response.setContentType("text/xml");
        PrintWriter out = response.getWriter();
        String coords = request.getParameter("coords");
        try {
            out.println(server_.batchCoordResolve(coords));
        } catch (Exception e) {
            out.println("exception: " + e);
            StackTraceElement stes[] = e.getStackTrace();
            for (int i = 0; i < stes.length; i++) {
                out.println(stes[i]);
            }
        }
    }

    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request, response);
    }

    @Override
    protected String getLoggerCode() {
        return "batchcoord";
    }
}
