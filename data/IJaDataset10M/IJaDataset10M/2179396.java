package org.hibnet.lune.server;

import java.io.File;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.mortbay.jetty.Server;
import org.mortbay.jetty.webapp.WebAppContext;
import org.mortbay.resource.Resource;
import org.mortbay.xml.XmlConfiguration;
import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;

/**
 * The activator class controls the plug-in life cycle
 */
public class LuneServerActivator implements BundleActivator {

    private static final Log log = LogFactory.getLog(LuneServerActivator.class);

    /** The plug-in ID */
    public static final String PLUGIN_ID = "org.hibnet.lune.server";

    private static LuneServerActivator plugin;

    private Server server;

    /**
     * The constructor
     */
    public LuneServerActivator() {
        plugin = this;
    }

    public void start(BundleContext context) throws Exception {
        server = new Server();
        XmlConfiguration configuration;
        try {
            configuration = new XmlConfiguration(this.getClass().getResourceAsStream("jetty.xml"));
            configuration.configure(server);
            File webappURL = context.getDataFile("/webapp/");
            WebAppContext webapp = new WebAppContext();
            webapp.setContextPath("/lune");
            webapp.setBaseResource(Resource.newResource(webappURL.toURL()));
            webapp.setClassLoader(this.getClass().getClassLoader());
            server.addHandler(webapp);
            server.start();
            log.info("Server started");
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }

    public void stop(BundleContext context) throws Exception {
        plugin = null;
        server.stop();
        server = null;
        log.info("Server stopped");
    }

    /**
     * Returns the shared instance
     * 
     * @return the shared instance
     */
    public static LuneServerActivator getDefault() {
        return plugin;
    }
}
