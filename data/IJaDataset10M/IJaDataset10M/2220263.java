package org.jdmp.jetty;

import org.mortbay.jetty.Server;
import org.mortbay.jetty.handler.ContextHandlerCollection;
import org.mortbay.jetty.servlet.Context;
import org.mortbay.jetty.servlet.ServletHolder;

public class JettyObjectServer {

    private Server server = null;

    private Object object = null;

    private int port = 5555;

    public JettyObjectServer(Object o, int port) {
        this.object = o;
        this.port = port;
    }

    public boolean isConnected() {
        return server != null && server.isRunning();
    }

    public void start() throws Exception {
        server = new Server(port);
        ContextHandlerCollection contexts = new ContextHandlerCollection();
        server.setHandler(contexts);
        Context root = new Context(contexts, "/", Context.SESSIONS);
        root.addServlet(new ServletHolder(new JettyObjectServlet(object)), "/*");
        server.start();
    }

    public void stop() throws Exception {
        server.stop();
    }
}
