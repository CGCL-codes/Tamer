package Listener;

import Beans.Connection;
import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

/**
 * Web application lifecycle listener.
 *
 * @author Rudolp
 */
public class OnLoadListener implements ServletContextListener {

    private Connection conn;

    public void contextInitialized(ServletContextEvent sce) {
        ServletContext context = sce.getServletContext();
        this.conn = new Connection(context.getInitParameter("driver"), context.getInitParameter("url"));
        sce.getServletContext().setAttribute("connection", conn);
    }

    public void contextDestroyed(ServletContextEvent sce) {
        this.conn.closeConnection();
    }
}
