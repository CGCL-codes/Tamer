package com.technoetic.xplanner.webservers;

import java.io.File;
import org.mortbay.http.HttpServer;
import org.mortbay.http.SocketListener;
import org.mortbay.jetty.Server;
import org.mortbay.jetty.servlet.WebApplicationContext;
import org.mortbay.start.Monitor;

public class JettyServer {

    public static void main(String[] args) throws Exception {
        start();
    }

    public static void start() throws Exception {
        HttpServer server = new Server();
        SocketListener listener = new SocketListener();
        listener.setPort(8080);
        server.addListener(listener);
        WebApplicationContext context = new WebApplicationContext("war");
        context.setContextPath("/xplanner");
        context.setTempDirectory(new File("build"));
        context.setIgnoreWebJetty(true);
        server.addContext(context);
        Monitor.monitor();
        server.start();
    }

    public static void stop() {
    }
}
