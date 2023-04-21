package org.apache.jackrabbit.demo.mu.servlets;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.ServletException;
import java.io.IOException;

/**
 * Receive error description from other servlet and forward it to display on Error.jsp page.
 *
 * @author Pavel Konnikov
 * @version $Revision$ $Date$
 */
public class ErrorServlet extends HttpServlet {

    protected void service(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) throws ServletException, IOException {
        httpServletRequest.getRequestDispatcher("/pages/Error.jsp").forward(httpServletRequest, httpServletResponse);
    }
}
