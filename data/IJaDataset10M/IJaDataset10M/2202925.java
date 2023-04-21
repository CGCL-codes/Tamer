package com.googlecode.wargo.jetty;

import org.mortbay.jetty.Server;
import org.mortbay.jetty.webapp.WebAppClassLoader;
import org.mortbay.jetty.webapp.WebAppContext;

public class JettyStandaloneServer implements Runnable {

    public void run() {
        Server server = null;
        try {
            server = new Server(wargo.WarGo_Start.port);
            WebAppContext appContext = new WebAppContext(server, wargo.WarGo_Start.war.getAbsolutePath(), wargo.WarGo_Start.context);
            appContext.setClassLoader(new WebAppClassLoader(JettyStandaloneServer.class.getClassLoader(), appContext));
            server.start();
        } catch (Exception e) {
            e.printStackTrace(System.out);
        } finally {
            wargo.WarGo_Start.serverStarted();
        }
        if (server != null) {
            try {
                server.stop();
            } catch (Exception e) {
                e.printStackTrace(System.out);
            }
        }
    }
}
