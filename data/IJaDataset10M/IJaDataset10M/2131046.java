package org.nexopenframework.modules.httpserver.jetty6;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import java.io.IOException;
import java.io.InputStream;
import java.sql.SQLException;
import java.util.Properties;
import java.util.concurrent.atomic.AtomicBoolean;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.junit.Test;
import org.nexopenframework.modules.httpserver.resources.C3P0DataSourceProvider;
import org.nexopenframework.util.sql.HSQLServer;

/**
 * <p>NexOpen Framework</p>
 *
 * <p>Simple JUnit 4.4 TestCase for dealing with {@link Jetty6Server}</p>
 *
 * @see org.nexopenframework.modules.httpserver.jetty6.Jetty6Server
 * @author Francesc Xavier Magdaleno
 * @version $Revision ,$Date 04/05/2009 0:47:34
 * @since 1.0.0.m3
 */
public class Jetty6ServerTest {

    /**logging facility*/
    private static final Log logger = LogFactory.getLog(Jetty6ServerTest.class);

    /**Check if we have initialized Servlets*/
    private static final AtomicBoolean init = new AtomicBoolean();

    /**component to be tested*/
    private final Jetty6Server server = new Jetty6Server();

    @Test
    public void handleRegister() throws HttpException, IOException {
        server.setContextPath("nexopen-modules");
        final MyServlet servlet = new MyServlet();
        server.registerServlet(servlet, "/example");
        server.startServer();
        assertEquals(true, init.get());
        assertInvoke("http://localhost:8888/nexopen-modules/example");
        server.stopServer();
        assertEquals(false, init.get());
    }

    @Test
    public void handleJNDI() throws HttpException, IOException, NamingException {
        server.setContextPath("nexopen-modules");
        final MyServlet servlet = new MyServlet();
        server.registerServlet(servlet, "/example");
        server.startServer();
        assertEquals(true, init.get());
        assertInvoke("http://localhost:8888/nexopen-modules/example");
        final InitialContext ic = new InitialContext();
        assertNotNull(ic);
        final Context ctx = (Context) ic.lookup("java:comp");
        assertNotNull(ctx);
        server.stopServer();
        assertEquals(false, init.get());
    }

    @Test
    public void handleDataSource() throws HttpException, IOException, NamingException, SQLException {
        final HSQLServer hsqldb = new HSQLServer();
        {
            hsqldb.setDatabaseName("nexopen-modules");
            hsqldb.setSilent(true);
            hsqldb.setTrace(false);
            hsqldb.setPort(9002);
            hsqldb.afterPropertiesSet();
        }
        server.setContextPath("nexopen-modules");
        {
            final Properties properties = new Properties();
            final ClassLoader cls = Thread.currentThread().getContextClassLoader();
            final InputStream is = cls.getResourceAsStream("c3p0.properties");
            properties.load(is);
            server.registerDataSource("jdbc/exampleDS", new C3P0DataSourceProvider().createDataSource(properties));
        }
        final MyServlet2 servlet = new MyServlet2();
        server.registerServlet(servlet, "/example2");
        server.startServer();
        assertEquals(true, init.get());
        assertInvoke("http://localhost:8888/nexopen-modules/example2");
        server.stopServer();
        assertEquals(false, init.get());
        hsqldb.destroy();
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

    public static class MyServlet2 extends HttpServlet {

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
            try {
                final DataSource ds = (DataSource) new InitialContext().lookup("java:comp/env/jdbc/exampleDS");
                assertNotNull(ds);
                System.out.println("Invoked doGet with DataSource");
            } catch (final NamingException e) {
                throw new ServletException(e);
            }
        }
    }
}
