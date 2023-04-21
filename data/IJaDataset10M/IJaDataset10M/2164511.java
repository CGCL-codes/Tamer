package org.nexopenframework.management.agent.httpserver;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import java.io.IOException;
import java.util.concurrent.atomic.AtomicBoolean;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.junit.Test;

/**
 * <p>NexOpen Framework</p>
 * 
 * <p>Simple JUnit 4.4 TestCase for dealing with {@link Jetty6Server}</p>
 * 
 * @see org.nexopenframework.management.agent.httpserver.Jetty6Server
 * @author Francesc Xavier Magdaleno
 * @version 1.0
 * @since 1.0.0.m2
 */
public class Jetty6ServerTest {

    /**logging facility*/
    private static final Log logger = LogFactory.getLog(Jetty6ServerTest.class);

    /***/
    private static final AtomicBoolean init = new AtomicBoolean();

    /***/
    private final Jetty6Server server = new Jetty6Server();

    @Test
    public void handleRegister() throws HttpException, IOException {
        server.setContextPath("nexopen-monitoring");
        server.setPort(9999);
        final MyServlet servlet = new MyServlet();
        server.registerServlet(servlet, "/example");
        server.startServer();
        assertEquals(true, init.get());
        assertInvoke("http://localhost:9999/nexopen-monitoring/example");
        server.stopServer();
        assertEquals(false, init.get());
    }

    protected void assertInvoke(final String url) throws HttpException, IOException {
        final HttpClient client = new HttpClient();
        final GetMethod getMethod = new GetMethod(url);
        try {
            final int code = client.executeMethod(getMethod);
            if (logger.isInfoEnabled()) {
                logger.info("Executed shutdown with code :: " + code);
            }
            assertTrue(code > 0);
            assertEquals(200, code);
        } finally {
            getMethod.releaseConnection();
        }
    }

    public static class MyServlet extends HttpServlet {

        /**serialization stuff*/
        private static final long serialVersionUID = 1L;

        @Override
        public void init(final ServletConfig config) {
            init.set(true);
        }

        @Override
        public void destroy() {
            init.set(false);
        }

        @Override
        protected void doGet(final HttpServletRequest req, final HttpServletResponse resp) throws ServletException, IOException {
            System.out.println("Invoked doGet");
        }
    }
}
