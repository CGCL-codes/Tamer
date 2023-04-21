package test.servlets;

import org.mortbay.jetty.*;
import org.mortbay.jetty.bio.SocketConnector;
import org.mortbay.jetty.servlet.ServletHandler;

public class SimpleContainer {

    Server server = null;

    ServletHandler handler = new ServletHandler();

    public final int port;

    public SimpleContainer(int port) {
        this.port = port;
    }

    public void addServlet(Class class_, String mapping) {
        handler.addServletWithMapping(class_, "/" + mapping);
    }

    public void start() throws Exception {
        server = new Server();
        Connector connector = new SocketConnector();
        connector.setPort(port);
        server.setConnectors(new Connector[] { connector });
        server.setHandler(handler);
        server.start();
    }

    public void stop() throws Exception {
        if (server != null) {
            server.stop();
            server = null;
        }
    }
}
