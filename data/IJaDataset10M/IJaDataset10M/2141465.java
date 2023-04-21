package com.googlecode.mycontainer.web;

import static org.junit.Assert.assertEquals;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import com.googlecode.mycontainer.kernel.ShutdownCommand;
import com.googlecode.mycontainer.web.ContextWebServer;
import com.googlecode.mycontainer.web.FilterDesc;
import com.googlecode.mycontainer.web.ServletDesc;
import com.googlecode.mycontainer.web.WebServerDeployer;
import com.googlecode.mycontainer.web.jetty.JettyServerDeployer;

public class ServletDeployerTest {

    @Before
    public void boot() throws NamingException {
        InitialContext ic = new InitialContext();
        WebServerDeployer server = new JettyServerDeployer();
        server.setContext(ic);
        server.setName("WebServer");
        server.bindPort(8380);
        ContextWebServer web = server.createContextWebServer();
        web.setContext("/");
        web.getServlets().add(new ServletDesc(new TestServlet(), "/test.txt"));
        web.getFilters().add(new FilterDesc(new TestFilter(), "/*"));
        server.deploy();
    }

    private void testURL(String urlStr) throws MalformedURLException, IOException {
        HttpURLConnection conn = null;
        try {
            URL url = new URL(urlStr);
            conn = (HttpURLConnection) url.openConnection();
            int code = conn.getResponseCode();
            assertEquals(HttpURLConnection.HTTP_OK, code);
        } finally {
            if (conn != null) {
                conn.disconnect();
            }
        }
    }

    @Test
    public void basicTest() throws Exception {
        testURL("http://localhost:8380/test.txt");
        testURL("http://localhost:8380/filter.txt");
    }

    @After
    public void shutdown() throws Exception {
        ShutdownCommand shutdown = new ShutdownCommand();
        shutdown.setContext(new InitialContext());
        shutdown.shutdown();
    }
}
