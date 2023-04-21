package org.examcity.webtest.util;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.mortbay.jetty.Server;
import org.mortbay.jetty.webapp.WebAppContext;
import org.mortbay.resource.Resource;
import java.io.IOException;
import java.io.File;
import java.net.MalformedURLException;

public class JettyRunner {

    private static final String TARGET_WEBAPP_DIR = "../examcity-webapp-springmvc/src/main/webapp/";

    private Log logger = LogFactory.getLog(getClass());

    public String suite;

    public boolean passed;

    private Server server;

    private int port = 8084;

    private String contextName = "examcity";

    public void run() throws Exception {
        run(port);
    }

    public void run(int port) throws Exception {
        this.port = port;
        WebAppContext context;
        server = new Server(port);
        context = new MyWebApplicationContext(TARGET_WEBAPP_DIR, "/" + contextName);
        server.addHandler(context);
        logger.info("Starting Jetty container on port " + port + ", serving context " + context);
        try {
            server.start();
        } catch (Exception e) {
            server.stop();
            throw e;
        }
        logger.debug("started");
    }

    public void stop() throws Exception {
        if (server.isStarted()) server.stop();
    }

    public String getUrl() {
        return "http://localhost:" + port + "/" + contextName;
    }

    class MyWebApplicationContext extends WebAppContext {

        private Resource realWebApp;

        MyWebApplicationContext(String webapp, String context) {
            super(webapp, context);
        }

        public Resource getResource(String resourceName) {
            try {
                if (realWebApp == null) {
                    realWebApp = Resource.newResource("../webapp/src/main/webapp");
                }
                Resource r = realWebApp.addPath(resourceName);
                if (r.exists()) {
                    return r;
                }
                return super.getResource(resourceName);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
    }

    public static void main(String[] args) throws Exception {
        JettyRunner run = new JettyRunner();
        run.run();
    }
}
