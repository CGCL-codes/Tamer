package com.beardediris.ajaqs.util;

import java.io.IOException;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import com.beardediris.ajaqs.session.Constants;
import org.apache.log4j.Logger;

/**
 * <p>All servlets that serve up error information though the
 * JSP <tt>error.jsp</tt> need to sub-class <tt>ServeError</tt>,
 * or one of its derivative classes.</p>
 */
public abstract class ServeError extends HttpServlet implements Constants {

    private static final Logger logger = Logger.getLogger(ServeError.class.getName());

    /**
     * URL triggered when an exception occurs.
     */
    private static final String URL_SHOW_EXCEPTION = "/jsp/site/error.jsp";

    /**
     * Called by subclasses when an exception is caught for which
     * there is no special error message.  In this case, we display
     * the stack trace of the exception in a JSP, notify the user
     * that a problem occurred, and request that the user email us
     * a copy of the stack trace.
     *
     * @throws ServletException
     * @throws IOException
     */
    protected void serveError(HttpServletRequest req, HttpServletResponse resp, Exception ex) throws ServletException, IOException {
        RequestDispatcher rd = req.getRequestDispatcher(URL_SHOW_EXCEPTION);
        if (null == rd) {
            throw new ServletException("Could not get RequestDispatcher to forward request.");
        }
        req.setAttribute(INTERNAL_EXCEPTION, ex);
        try {
            rd.forward(req, resp);
        } finally {
            req.removeAttribute(INTERNAL_EXCEPTION);
        }
    }
}
