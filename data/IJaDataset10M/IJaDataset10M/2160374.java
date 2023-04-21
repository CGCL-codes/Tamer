package org.archive.crawler.webui;

import java.io.File;
import org.mortbay.jetty.Handler;
import org.mortbay.jetty.Server;
import org.mortbay.jetty.bio.SocketConnector;
import org.mortbay.jetty.handler.DefaultHandler;
import org.mortbay.jetty.handler.HandlerList;
import org.mortbay.jetty.webapp.WebAppContext;

public class WebUITestMain {

    public static void main(String args[]) throws Exception {
        Server server = new Server();
        SocketConnector sc = new SocketConnector();
        sc.setHost("localhost");
        sc.setPort(7777);
        server.addConnector(sc);
        String webAppPath = getWebAppDir().getAbsolutePath();
        WebAppContext webapp = new WebAppContext(webAppPath, "/heritrix");
        HandlerList handlers = new HandlerList();
        handlers.setHandlers(new Handler[] { webapp, new DefaultHandler() });
        server.setHandler(handlers);
        server.start();
        Object eternity = new Object();
        synchronized (eternity) {
            eternity.wait();
        }
    }

    static File getWebAppDir() {
        File r = new File("src/main/webapp");
        if (r.isDirectory()) {
            return r;
        }
        r = new File("webui/src/main/webapp");
        if (r.isDirectory()) {
            return r;
        }
        throw new IllegalStateException("Can't find src/main/webapps");
    }
}
