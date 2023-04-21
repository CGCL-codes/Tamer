package org.wicketstuff.push.examples.launcher;

import org.mortbay.jetty.Connector;
import org.mortbay.jetty.Server;
import org.mortbay.jetty.nio.SelectChannelConnector;
import org.mortbay.jetty.webapp.WebAppContext;

/**
 * Seperate startup class for people that want to run the examples directly.
 * 
 * Once started the phonebook is accessible under
 * http://localhost:8080/phonebook
 */
public class JettyLauncher {

    /**
     * Main function, starts the jetty server.
     * 
     * @param args
     * @throws Exception
     */
    public static void main(String[] args) throws Exception {
        Server server = new Server();
        SelectChannelConnector connector = new SelectChannelConnector();
        connector.setPort(8080);
        server.setConnectors(new Connector[] { connector });
        WebAppContext web = new WebAppContext();
        web.setServer(server);
        web.setContextPath("/");
        web.setWar("src/main/webapp");
        server.addHandler(web);
        try {
            server.start();
            server.join();
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(100);
        }
    }
}
