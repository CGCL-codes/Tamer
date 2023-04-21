package org.oxyus.web;

import java.io.IOException;
import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.log4j.Logger;
import org.oxyus.admin.Configuration;
import org.oxyus.admin.ConfigurationException;
import org.oxyus.admin.Manager;

/**
 * @author Carlos Saltos (csaltos[@]users.sourceforge.net)
 */
public class OxyusInitializerServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;

    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        ServletContext context = config.getServletContext();
        String oxyusHome = context.getRealPath("/WEB-INF/oxyus");
        this.log("oxyus home is " + oxyusHome);
        Configuration.setHome(oxyusHome);
        try {
            this.log("Starting Oxyus");
            Manager.initEnvironment();
        } catch (ConfigurationException ce) {
            if (Configuration.isLoggerActive()) {
                Logger log = Logger.getLogger(OxyusInitializerServlet.class);
                log.error("Unable to initialize oxyus", ce);
            } else {
                this.log("Unable to initialize oxyus", ce);
                throw new ServletException("Unable to initialize oxyus", ce);
            }
        }
    }

    public void destroy() {
        this.log("Stopping Oxyus");
        Manager.shutdownEnvironment();
    }

    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.sendError(HttpServletResponse.SC_FORBIDDEN);
    }

    public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.sendError(HttpServletResponse.SC_FORBIDDEN);
    }
}
