package test.unit.net.sf.bimbo;

import net.sf.bimbo.BimboServlet;
import org.mortbay.http.HttpContext;
import org.mortbay.http.HttpListener;
import org.mortbay.http.HttpServer;
import org.mortbay.http.SocketListener;
import org.mortbay.jetty.servlet.ServletHandler;
import org.mortbay.jetty.servlet.ServletHolder;

public class ServletTestContainer {

    private HttpServer server;

    private ServletHolder servletHolder;

    public ServletTestContainer() throws Exception {
        this.server = new HttpServer();
        HttpListener httpListener = new SocketListener();
        this.server.addListener(httpListener);
        HttpContext context = server.getContext("/");
        ServletHandler handler = new ServletHandler();
        this.servletHolder = handler.addServlet(BimboServlet.class.getSimpleName(), "/*", BimboServlet.class.getName());
        context.addHandler(handler);
        this.server.start();
    }

    public void start() throws Exception {
        this.server.start();
    }

    public String getLocation() {
        int port = this.server.getListeners()[0].getPort();
        String location = "http://localhost:" + port + "/";
        return location;
    }

    public void stop() throws Exception {
        this.server.stop();
    }

    public void setInitParameter(String name, String value) {
        this.servletHolder.setInitParameter(name, value);
    }
}
