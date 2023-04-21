package annone.client.web;

import java.io.File;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import annone.local.LocalEngine;
import annone.local.Workspace;

public class Application implements ServletContextListener {

    @Override
    public void contextInitialized(ServletContextEvent e) {
        File dir = new File(e.getServletContext().getRealPath("workspace"));
        Workspace workspace = new Workspace(dir);
        LocalEngine engine = new LocalEngine(workspace);
        engine.start();
    }

    @Override
    public void contextDestroyed(ServletContextEvent e) {
    }
}
