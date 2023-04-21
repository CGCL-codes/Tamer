package lebah.portal.velocity;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.Velocity;
import org.apache.velocity.app.VelocityEngine;

/**
 * @author Shamsul Bahrin Abd Mutalib
 * @version 1.01
 */
public abstract class VServlet extends HttpServlet {

    protected VelocityEngine engine = new VelocityEngine();

    protected VelocityContext context = new VelocityContext();

    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        try {
            Properties p = loadConfiguration(config);
            engine.init(p);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    private Properties loadConfiguration(ServletConfig config) throws IOException, FileNotFoundException {
        String propsFile = config.getInitParameter("properties");
        Properties p = new Properties();
        if (propsFile != null) {
            String realPath = getServletContext().getRealPath(propsFile);
            if (realPath != null) propsFile = realPath;
            p.load(new FileInputStream(propsFile));
        }
        String log = p.getProperty(Velocity.RUNTIME_LOG);
        if (log != null) {
            log = getServletContext().getRealPath(log);
            if (log != null) p.setProperty(Velocity.RUNTIME_LOG, log);
        }
        String path = p.getProperty(Velocity.FILE_RESOURCE_LOADER_PATH);
        if (path != null) {
            path = getServletContext().getRealPath(path);
            if (path != null) {
                p.setProperty(Velocity.FILE_RESOURCE_LOADER_PATH, path);
            }
        }
        p.setProperty(Velocity.FILE_RESOURCE_LOADER_CACHE, "true");
        return p;
    }

    private Properties loadConfigurationSimple(ServletConfig config) throws IOException, FileNotFoundException {
        String path = config.getServletContext().getRealPath("/");
        Properties p = new Properties();
        p.setProperty(Velocity.FILE_RESOURCE_LOADER_PATH, path);
        p.setProperty(Velocity.FILE_RESOURCE_LOADER_CACHE, "true");
        return p;
    }
}
