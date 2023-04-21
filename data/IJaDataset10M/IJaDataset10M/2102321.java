package org.chiba.web.servlet;

import org.apache.log4j.xml.DOMConfigurator;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Initialization servlet for the log4j system.  should be loaded on tomcat
 * start.
 *
 * @author <a href="mailto:gregor@eyestep.org">Gregor Klinke</a>
 * @version $Id: Log4jInit.java,v 1.1 2006/09/10 19:50:51 joernt Exp $
 */
public class Log4jInit extends HttpServlet {

    /**
     * __UNDOCUMENTED__
     *
     * @param req  __UNDOCUMENTED__
     * @param resp __UNDOCUMENTED__
     */
    public void doGet(HttpServletRequest req, HttpServletResponse resp) {
    }

    /**
     * __UNDOCUMENTED__
     *
     * @throws ServletException __UNDOCUMENTED__
     */
    public void init() throws ServletException {
        String file = getInitParameter("log4j-init-file");
        String path = getServletContext().getRealPath(file.startsWith("/") ? file : ("/" + file));
        if (path != null) {
            DOMConfigurator.configure(path);
        }
    }
}
