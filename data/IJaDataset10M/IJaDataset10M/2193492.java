package net.sf.sail.jetty.test.basic;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import net.sf.sail.jetty.JettyServiceRunner;
import org.mortbay.jetty.Connector;
import org.mortbay.jetty.Server;
import org.mortbay.jetty.bio.SocketConnector;
import org.mortbay.jetty.handler.ContextHandler;
import org.mortbay.jetty.servlet.ServletHandler;
import org.mortbay.thread.BoundedThreadPool;

/**
 * Tests a basic hello world servlet
 * 
 * http://docs.codehaus.org/display/JETTY/Embedding+Jetty
 * @author aperritano
 *
 */
public class WebstartProxyTest {

    /**
	 * Start up that server!
	 * 
	 * @param args
	 * @throws Exception
	 */
    public static void main(String[] args) throws Exception {
        Server server = new Server();
        BoundedThreadPool boundedThreadPool = new BoundedThreadPool();
        boundedThreadPool.setMinThreads(10);
        boundedThreadPool.setMaxThreads(250);
        boundedThreadPool.setLowThreads(20);
        server.setThreadPool(boundedThreadPool);
        Connector connector = new SocketConnector();
        connector.setPort(8080);
        server.setConnectors(new Connector[] { connector });
        ServletHandler servletHandler = new ServletHandler();
        servletHandler.addServletWithMapping("net.sf.sail.jetty.WebstartProxyServlet", "/");
        ContextHandler handler = new ContextHandler();
        handler.setContextPath("/");
        handler.setHandler(servletHandler);
        server.setHandler(handler);
        server.start();
        JettyServiceRunner.startJmDNSService(8080);
        server.join();
    }

    /**
	 * A basic servlet that prints HelloWorld
	 * 
	 * @author aperritano
	 *
	 */
    public static class HelloServlet extends HttpServlet {

        protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
            response.setContentType("text/html");
            response.setStatus(HttpServletResponse.SC_OK);
            response.getWriter().println("<h1>Hello SimpleServlet</h1>");
        }
    }
}
