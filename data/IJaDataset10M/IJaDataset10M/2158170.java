package org.xsocket.web.http.servlet;

import java.io.InputStream;
import java.io.StringBufferInputStream;
import java.util.logging.ConsoleHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import junit.framework.JUnit4TestAdapter;
import junit.textui.TestRunner;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.methods.PostMethod;
import org.junit.Assert;
import org.junit.Test;
import org.xsocket.stream.IServer;
import org.xsocket.stream.Server;

public class HttpConnectionTest {

    private IServer createServer() throws Exception {
        WebAppContainer container = new WebAppContainer();
        Context ctx = new Context("/info");
        ctx.addServlet("InfoServlet", InfoServlet.class);
        ctx.addServletMapping("InfoServlet", "/InfoServlet");
        container.addContext(ctx);
        Context ctx2 = new Context("/echo");
        ctx2.addServlet("EchoServlet", PongServlet.class);
        ctx2.addServletMapping("EchoServlet", "/EchoServlet");
        container.addContext(ctx2);
        Context ctx3 = new Context("/session");
        ctx3.addServlet("SessionServlet", SessionServlet.class);
        ctx3.addServletMapping("SessionServlet", "/SessionServlet");
        container.addContext(ctx3);
        Context ctx4 = new Context("/post");
        ctx4.addServlet("PostServlet", PostServlet.class);
        ctx4.addServletMapping("PostServlet", "/PostServlet");
        container.addContext(ctx4);
        return new Server(container);
    }

    @Test
    public void testPost() throws Exception {
        IServer server = createServer();
        new Thread(server).start();
        for (int i = 1; i < 55; i++) {
            HttpClient client = new HttpClient();
            PostMethod method = new PostMethod("http://" + server.getLocalAddress().getHostName() + ":" + server.getLocalPort() + "/post/PostServlet");
            method.addRequestHeader("test1", "value1");
            method.addRequestHeader("test1", "value2");
            String testRequest = generateTestData(i);
            InputStream is = new StringBufferInputStream(testRequest);
            method.setRequestBody(is);
            client.executeMethod(method);
            Assert.assertEquals(testRequest, method.getResponseBodyAsString());
            method.releaseConnection();
        }
        server.close();
    }

    private String generateTestData(int length) {
        StringBuilder sb = new StringBuilder();
        int u = 64;
        for (int i = 0; i < length; i++) {
            u++;
            if (u > 80) {
                u = 64;
            }
            sb.append((char) u);
        }
        return sb.toString();
    }

    public static junit.framework.Test suite() {
        return new JUnit4TestAdapter(HttpConnectionTest.class);
    }

    public static void main(String... args) {
        Logger logger = Logger.getLogger("org.xsocket");
        logger.setLevel(Level.FINE);
        ConsoleHandler hdl = new ConsoleHandler();
        hdl.setLevel(Level.FINE);
        logger.addHandler(hdl);
        TestRunner.run(suite());
    }
}
