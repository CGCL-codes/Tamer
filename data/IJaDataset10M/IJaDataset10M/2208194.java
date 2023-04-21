package net.sourceforge.jwakeup;

import org.apache.log4j.Logger;
import javax.servlet.http.HttpServlet;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import java.io.File;

/**
 * Application initializer.
 * Mainly used to set the database path independent from startup path.
 *
 * @author: Matthias
 * Date: 10.02.2008
 */
public class ApplicationServlet extends HttpServlet {

    @SuppressWarnings({ "UnusedDeclaration" })
    private static final Logger LOGGER = Logger.getLogger(ApplicationServlet.class);

    public void init(ServletConfig servletConfig) throws ServletException {
        File file = new File(servletConfig.getServletContext().getRealPath("/WEB-INF"), "sourceforge.db");
        DatabaseAdapter.changeDbFile(file);
    }
}
