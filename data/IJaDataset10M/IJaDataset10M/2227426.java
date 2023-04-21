package net.sf.sail.jetty.test.basic;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.mortbay.jetty.Connector;
import org.mortbay.jetty.Server;
import org.mortbay.jetty.bio.SocketConnector;
import org.mortbay.jetty.servlet.ServletHandler;

/**
 * Tests a basic hello world servlet
 * 
 * http://docs.codehaus.org/display/JETTY/Embedding+Jetty
 * @author aperritano
 *
 */
public class BasicServletTest {

    /**
	 * Start up that server!
	 * 
	 * @param args
	 * @throws Exception
	 */
    public static void main(String[] args) throws Exception {
        Server server = new Server();
        Connector connector = new SocketConnector();
        connector.setPort(8080);
        server.setConnectors(new Connector[] { connector });
        ServletHandler handler = new ServletHandler();
        server.setHandler(handler);
        handler.addServletWithMapping("net.sf.sail.jetty.test.basic.BasicServletTest$HelloServlet", "/");
        server.start();
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
