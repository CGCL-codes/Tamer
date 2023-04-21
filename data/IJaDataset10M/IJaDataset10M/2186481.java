package org.apache.http.client.protocol;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpException;
import org.apache.http.HttpHost;
import org.apache.http.HttpRequest;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.ProtocolException;
import org.apache.http.ProtocolVersion;
import org.apache.http.client.CircularRedirectException;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.CookieStore;
import org.apache.http.client.RedirectException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.params.ClientPNames;
import org.apache.http.cookie.SM;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.cookie.BasicClientCookie;
import org.apache.http.localserver.BasicServerTestBase;
import org.apache.http.localserver.LocalTestServer;
import org.apache.http.message.BasicHeader;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.ExecutionContext;
import org.apache.http.protocol.HTTP;
import org.apache.http.protocol.HttpContext;
import org.apache.http.protocol.HttpRequestHandler;
import junit.framework.Test;
import junit.framework.TestSuite;

/**
 * Redirection test cases.
 *
 */
public class TestRedirects extends BasicServerTestBase {

    public TestRedirects(final String testName) {
        super(testName);
    }

    public static void main(String args[]) {
        String[] testCaseName = { TestRedirects.class.getName() };
        junit.textui.TestRunner.main(testCaseName);
    }

    public static Test suite() {
        return new TestSuite(TestRedirects.class);
    }

    @Override
    protected void setUp() throws Exception {
        localServer = new LocalTestServer(null, null);
        localServer.registerDefaultHandlers();
        localServer.start();
    }

    private class BasicRedirectService implements HttpRequestHandler {

        private int statuscode = HttpStatus.SC_MOVED_TEMPORARILY;

        private String host = null;

        private int port;

        public BasicRedirectService(final String host, int port, int statuscode) {
            super();
            this.host = host;
            this.port = port;
            if (statuscode > 0) {
                this.statuscode = statuscode;
            }
        }

        public BasicRedirectService(final String host, int port) {
            this(host, port, -1);
        }

        public void handle(final HttpRequest request, final HttpResponse response, final HttpContext context) throws HttpException, IOException {
            ProtocolVersion ver = request.getRequestLine().getProtocolVersion();
            String uri = request.getRequestLine().getUri();
            if (uri.equals("/oldlocation/")) {
                response.setStatusLine(ver, this.statuscode);
                response.addHeader(new BasicHeader("Location", "http://" + this.host + ":" + this.port + "/newlocation/"));
                response.addHeader(new BasicHeader("Connection", "close"));
            } else if (uri.equals("/newlocation/")) {
                response.setStatusLine(ver, HttpStatus.SC_OK);
                StringEntity entity = new StringEntity("Successful redirect");
                response.setEntity(entity);
            } else {
                response.setStatusLine(ver, HttpStatus.SC_NOT_FOUND);
            }
        }
    }

    private class CircularRedirectService implements HttpRequestHandler {

        public CircularRedirectService() {
            super();
        }

        public void handle(final HttpRequest request, final HttpResponse response, final HttpContext context) throws HttpException, IOException {
            ProtocolVersion ver = request.getRequestLine().getProtocolVersion();
            String uri = request.getRequestLine().getUri();
            if (uri.startsWith("/circular-oldlocation")) {
                response.setStatusLine(ver, HttpStatus.SC_MOVED_TEMPORARILY);
                response.addHeader(new BasicHeader("Location", "/circular-location2"));
            } else if (uri.startsWith("/circular-location2")) {
                response.setStatusLine(ver, HttpStatus.SC_MOVED_TEMPORARILY);
                response.addHeader(new BasicHeader("Location", "/circular-oldlocation"));
            } else {
                response.setStatusLine(ver, HttpStatus.SC_NOT_FOUND);
            }
        }
    }

    private class RelativeRedirectService implements HttpRequestHandler {

        public RelativeRedirectService() {
            super();
        }

        public void handle(final HttpRequest request, final HttpResponse response, final HttpContext context) throws HttpException, IOException {
            ProtocolVersion ver = request.getRequestLine().getProtocolVersion();
            String uri = request.getRequestLine().getUri();
            if (uri.equals("/oldlocation/")) {
                response.setStatusLine(ver, HttpStatus.SC_MOVED_TEMPORARILY);
                response.addHeader(new BasicHeader("Location", "/relativelocation/"));
            } else if (uri.equals("/relativelocation/")) {
                response.setStatusLine(ver, HttpStatus.SC_OK);
                StringEntity entity = new StringEntity("Successful redirect");
                response.setEntity(entity);
            } else {
                response.setStatusLine(ver, HttpStatus.SC_NOT_FOUND);
            }
        }
    }

    private class RelativeRedirectService2 implements HttpRequestHandler {

        public RelativeRedirectService2() {
            super();
        }

        public void handle(final HttpRequest request, final HttpResponse response, final HttpContext context) throws HttpException, IOException {
            ProtocolVersion ver = request.getRequestLine().getProtocolVersion();
            String uri = request.getRequestLine().getUri();
            if (uri.equals("/test/oldlocation")) {
                response.setStatusLine(ver, HttpStatus.SC_MOVED_TEMPORARILY);
                response.addHeader(new BasicHeader("Location", "relativelocation"));
            } else if (uri.equals("/test/relativelocation")) {
                response.setStatusLine(ver, HttpStatus.SC_OK);
                StringEntity entity = new StringEntity("Successful redirect");
                response.setEntity(entity);
            } else {
                response.setStatusLine(ver, HttpStatus.SC_NOT_FOUND);
            }
        }
    }

    private class BogusRedirectService implements HttpRequestHandler {

        private String url;

        public BogusRedirectService(String redirectUrl) {
            super();
            this.url = redirectUrl;
        }

        public void handle(final HttpRequest request, final HttpResponse response, final HttpContext context) throws HttpException, IOException {
            ProtocolVersion ver = request.getRequestLine().getProtocolVersion();
            String uri = request.getRequestLine().getUri();
            if (uri.equals("/oldlocation/")) {
                response.setStatusLine(ver, HttpStatus.SC_MOVED_TEMPORARILY);
                response.addHeader(new BasicHeader("Location", url));
            } else if (uri.equals("/relativelocation/")) {
                response.setStatusLine(ver, HttpStatus.SC_OK);
                StringEntity entity = new StringEntity("Successful redirect");
                response.setEntity(entity);
            } else {
                response.setStatusLine(ver, HttpStatus.SC_NOT_FOUND);
            }
        }
    }

    public void testBasicRedirect300() throws Exception {
        int port = this.localServer.getServicePort();
        String host = "localhost";
        this.localServer.register("*", new BasicRedirectService(host, port, HttpStatus.SC_MULTIPLE_CHOICES));
        DefaultHttpClient client = new DefaultHttpClient();
        HttpContext context = new BasicHttpContext();
        HttpGet httpget = new HttpGet("/oldlocation/");
        HttpResponse response = client.execute(getServerHttp(), httpget, context);
        HttpEntity e = response.getEntity();
        if (e != null) {
            e.consumeContent();
        }
        HttpRequest reqWrapper = (HttpRequest) context.getAttribute(ExecutionContext.HTTP_REQUEST);
        assertEquals(HttpStatus.SC_MULTIPLE_CHOICES, response.getStatusLine().getStatusCode());
        assertEquals("/oldlocation/", reqWrapper.getRequestLine().getUri());
    }

    public void testBasicRedirect301() throws Exception {
        int port = this.localServer.getServicePort();
        String host = "localhost";
        this.localServer.register("*", new BasicRedirectService(host, port, HttpStatus.SC_MOVED_PERMANENTLY));
        DefaultHttpClient client = new DefaultHttpClient();
        HttpContext context = new BasicHttpContext();
        HttpGet httpget = new HttpGet("/oldlocation/");
        HttpResponse response = client.execute(getServerHttp(), httpget, context);
        HttpEntity e = response.getEntity();
        if (e != null) {
            e.consumeContent();
        }
        HttpRequest reqWrapper = (HttpRequest) context.getAttribute(ExecutionContext.HTTP_REQUEST);
        HttpHost targetHost = (HttpHost) context.getAttribute(ExecutionContext.HTTP_TARGET_HOST);
        assertEquals(HttpStatus.SC_OK, response.getStatusLine().getStatusCode());
        assertEquals("/newlocation/", reqWrapper.getRequestLine().getUri());
        assertEquals(host, targetHost.getHostName());
        assertEquals(port, targetHost.getPort());
    }

    public void testBasicRedirect302() throws Exception {
        int port = this.localServer.getServicePort();
        String host = "localhost";
        this.localServer.register("*", new BasicRedirectService(host, port, HttpStatus.SC_MOVED_TEMPORARILY));
        DefaultHttpClient client = new DefaultHttpClient();
        HttpContext context = new BasicHttpContext();
        HttpGet httpget = new HttpGet("/oldlocation/");
        HttpResponse response = client.execute(getServerHttp(), httpget, context);
        HttpEntity e = response.getEntity();
        if (e != null) {
            e.consumeContent();
        }
        HttpRequest reqWrapper = (HttpRequest) context.getAttribute(ExecutionContext.HTTP_REQUEST);
        HttpHost targetHost = (HttpHost) context.getAttribute(ExecutionContext.HTTP_TARGET_HOST);
        assertEquals(HttpStatus.SC_OK, response.getStatusLine().getStatusCode());
        assertEquals("/newlocation/", reqWrapper.getRequestLine().getUri());
        assertEquals(host, targetHost.getHostName());
        assertEquals(port, targetHost.getPort());
    }

    public void testBasicRedirect303() throws Exception {
        int port = this.localServer.getServicePort();
        String host = "localhost";
        this.localServer.register("*", new BasicRedirectService(host, port, HttpStatus.SC_SEE_OTHER));
        DefaultHttpClient client = new DefaultHttpClient();
        HttpContext context = new BasicHttpContext();
        HttpGet httpget = new HttpGet("/oldlocation/");
        HttpResponse response = client.execute(getServerHttp(), httpget, context);
        HttpEntity e = response.getEntity();
        if (e != null) {
            e.consumeContent();
        }
        HttpRequest reqWrapper = (HttpRequest) context.getAttribute(ExecutionContext.HTTP_REQUEST);
        HttpHost targetHost = (HttpHost) context.getAttribute(ExecutionContext.HTTP_TARGET_HOST);
        assertEquals(HttpStatus.SC_OK, response.getStatusLine().getStatusCode());
        assertEquals("/newlocation/", reqWrapper.getRequestLine().getUri());
        assertEquals(host, targetHost.getHostName());
        assertEquals(port, targetHost.getPort());
    }

    public void testBasicRedirect304() throws Exception {
        int port = this.localServer.getServicePort();
        String host = "localhost";
        this.localServer.register("*", new BasicRedirectService(host, port, HttpStatus.SC_NOT_MODIFIED));
        DefaultHttpClient client = new DefaultHttpClient();
        HttpContext context = new BasicHttpContext();
        HttpGet httpget = new HttpGet("/oldlocation/");
        HttpResponse response = client.execute(getServerHttp(), httpget, context);
        HttpEntity e = response.getEntity();
        if (e != null) {
            e.consumeContent();
        }
        HttpRequest reqWrapper = (HttpRequest) context.getAttribute(ExecutionContext.HTTP_REQUEST);
        assertEquals(HttpStatus.SC_NOT_MODIFIED, response.getStatusLine().getStatusCode());
        assertEquals("/oldlocation/", reqWrapper.getRequestLine().getUri());
    }

    public void testBasicRedirect305() throws Exception {
        int port = this.localServer.getServicePort();
        String host = "localhost";
        this.localServer.register("*", new BasicRedirectService(host, port, HttpStatus.SC_USE_PROXY));
        DefaultHttpClient client = new DefaultHttpClient();
        HttpContext context = new BasicHttpContext();
        HttpGet httpget = new HttpGet("/oldlocation/");
        HttpResponse response = client.execute(getServerHttp(), httpget, context);
        HttpEntity e = response.getEntity();
        if (e != null) {
            e.consumeContent();
        }
        HttpRequest reqWrapper = (HttpRequest) context.getAttribute(ExecutionContext.HTTP_REQUEST);
        assertEquals(HttpStatus.SC_USE_PROXY, response.getStatusLine().getStatusCode());
        assertEquals("/oldlocation/", reqWrapper.getRequestLine().getUri());
    }

    public void testBasicRedirect307() throws Exception {
        int port = this.localServer.getServicePort();
        String host = "localhost";
        this.localServer.register("*", new BasicRedirectService(host, port, HttpStatus.SC_TEMPORARY_REDIRECT));
        DefaultHttpClient client = new DefaultHttpClient();
        HttpContext context = new BasicHttpContext();
        HttpGet httpget = new HttpGet("/oldlocation/");
        HttpResponse response = client.execute(getServerHttp(), httpget, context);
        HttpEntity e = response.getEntity();
        if (e != null) {
            e.consumeContent();
        }
        HttpRequest reqWrapper = (HttpRequest) context.getAttribute(ExecutionContext.HTTP_REQUEST);
        HttpHost targetHost = (HttpHost) context.getAttribute(ExecutionContext.HTTP_TARGET_HOST);
        assertEquals(HttpStatus.SC_OK, response.getStatusLine().getStatusCode());
        assertEquals("/newlocation/", reqWrapper.getRequestLine().getUri());
        assertEquals(host, targetHost.getHostName());
        assertEquals(port, targetHost.getPort());
    }

    public void testMaxRedirectCheck() throws Exception {
        this.localServer.register("*", new CircularRedirectService());
        DefaultHttpClient client = new DefaultHttpClient();
        client.getParams().setBooleanParameter(ClientPNames.ALLOW_CIRCULAR_REDIRECTS, true);
        client.getParams().setIntParameter(ClientPNames.MAX_REDIRECTS, 5);
        HttpGet httpget = new HttpGet("/circular-oldlocation/");
        try {
            client.execute(getServerHttp(), httpget);
            fail("ClientProtocolException exception should have been thrown");
        } catch (ClientProtocolException e) {
            assertTrue(e.getCause() instanceof RedirectException);
        }
    }

    public void testCircularRedirect() throws Exception {
        this.localServer.register("*", new CircularRedirectService());
        DefaultHttpClient client = new DefaultHttpClient();
        client.getParams().setBooleanParameter(ClientPNames.ALLOW_CIRCULAR_REDIRECTS, false);
        HttpGet httpget = new HttpGet("/circular-oldlocation/");
        try {
            client.execute(getServerHttp(), httpget);
            fail("ClientProtocolException exception should have been thrown");
        } catch (ClientProtocolException e) {
            assertTrue(e.getCause() instanceof CircularRedirectException);
        }
    }

    public void testPostNoRedirect() throws Exception {
        int port = this.localServer.getServicePort();
        String host = "localhost";
        this.localServer.register("*", new BasicRedirectService(host, port));
        DefaultHttpClient client = new DefaultHttpClient();
        HttpContext context = new BasicHttpContext();
        HttpPost httppost = new HttpPost("/oldlocation/");
        httppost.setEntity(new StringEntity("stuff"));
        HttpResponse response = client.execute(getServerHttp(), httppost, context);
        HttpEntity e = response.getEntity();
        if (e != null) {
            e.consumeContent();
        }
        HttpRequest reqWrapper = (HttpRequest) context.getAttribute(ExecutionContext.HTTP_REQUEST);
        assertEquals(HttpStatus.SC_MOVED_TEMPORARILY, response.getStatusLine().getStatusCode());
        assertEquals("/oldlocation/", reqWrapper.getRequestLine().getUri());
        assertEquals("POST", reqWrapper.getRequestLine().getMethod());
    }

    public void testPostRedirectSeeOther() throws Exception {
        int port = this.localServer.getServicePort();
        String host = "localhost";
        this.localServer.register("*", new BasicRedirectService(host, port, HttpStatus.SC_SEE_OTHER));
        DefaultHttpClient client = new DefaultHttpClient();
        HttpContext context = new BasicHttpContext();
        HttpPost httppost = new HttpPost("/oldlocation/");
        httppost.setEntity(new StringEntity("stuff"));
        HttpResponse response = client.execute(getServerHttp(), httppost, context);
        HttpEntity e = response.getEntity();
        if (e != null) {
            e.consumeContent();
        }
        HttpRequest reqWrapper = (HttpRequest) context.getAttribute(ExecutionContext.HTTP_REQUEST);
        assertEquals(HttpStatus.SC_OK, response.getStatusLine().getStatusCode());
        assertEquals("/newlocation/", reqWrapper.getRequestLine().getUri());
        assertEquals("GET", reqWrapper.getRequestLine().getMethod());
    }

    public void testRelativeRedirect() throws Exception {
        int port = this.localServer.getServicePort();
        String host = this.localServer.getServiceHostName();
        this.localServer.register("*", new RelativeRedirectService());
        DefaultHttpClient client = new DefaultHttpClient();
        HttpContext context = new BasicHttpContext();
        client.getParams().setBooleanParameter(ClientPNames.REJECT_RELATIVE_REDIRECT, false);
        HttpGet httpget = new HttpGet("/oldlocation/");
        HttpResponse response = client.execute(getServerHttp(), httpget, context);
        HttpEntity e = response.getEntity();
        if (e != null) {
            e.consumeContent();
        }
        HttpRequest reqWrapper = (HttpRequest) context.getAttribute(ExecutionContext.HTTP_REQUEST);
        HttpHost targetHost = (HttpHost) context.getAttribute(ExecutionContext.HTTP_TARGET_HOST);
        assertEquals(HttpStatus.SC_OK, response.getStatusLine().getStatusCode());
        assertEquals("/relativelocation/", reqWrapper.getRequestLine().getUri());
        assertEquals(host, targetHost.getHostName());
        assertEquals(port, targetHost.getPort());
    }

    public void testRelativeRedirect2() throws Exception {
        int port = this.localServer.getServicePort();
        String host = this.localServer.getServiceHostName();
        this.localServer.register("*", new RelativeRedirectService2());
        DefaultHttpClient client = new DefaultHttpClient();
        HttpContext context = new BasicHttpContext();
        client.getParams().setBooleanParameter(ClientPNames.REJECT_RELATIVE_REDIRECT, false);
        HttpGet httpget = new HttpGet("/test/oldlocation");
        HttpResponse response = client.execute(getServerHttp(), httpget, context);
        HttpEntity e = response.getEntity();
        if (e != null) {
            e.consumeContent();
        }
        HttpRequest reqWrapper = (HttpRequest) context.getAttribute(ExecutionContext.HTTP_REQUEST);
        HttpHost targetHost = (HttpHost) context.getAttribute(ExecutionContext.HTTP_TARGET_HOST);
        assertEquals(HttpStatus.SC_OK, response.getStatusLine().getStatusCode());
        assertEquals("/test/relativelocation", reqWrapper.getRequestLine().getUri());
        assertEquals(host, targetHost.getHostName());
        assertEquals(port, targetHost.getPort());
    }

    public void testRejectRelativeRedirect() throws Exception {
        this.localServer.register("*", new RelativeRedirectService());
        DefaultHttpClient client = new DefaultHttpClient();
        client.getParams().setBooleanParameter(ClientPNames.REJECT_RELATIVE_REDIRECT, true);
        HttpGet httpget = new HttpGet("/oldlocation/");
        try {
            client.execute(getServerHttp(), httpget);
            fail("ClientProtocolException exception should have been thrown");
        } catch (ClientProtocolException e) {
            assertTrue(e.getCause() instanceof ProtocolException);
        }
    }

    public void testRejectBogusRedirectLocation() throws Exception {
        this.localServer.register("*", new BogusRedirectService("xxx://bogus"));
        DefaultHttpClient client = new DefaultHttpClient();
        HttpGet httpget = new HttpGet("/oldlocation/");
        try {
            client.execute(getServerHttp(), httpget);
            fail("IllegalStateException should have been thrown");
        } catch (IllegalStateException e) {
        }
    }

    public void testRejectInvalidRedirectLocation() throws Exception {
        String host = "localhost";
        int port = this.localServer.getServicePort();
        this.localServer.register("*", new BogusRedirectService("http://" + host + ":" + port + "/newlocation/?p=I have spaces"));
        DefaultHttpClient client = new DefaultHttpClient();
        HttpGet httpget = new HttpGet("/oldlocation/");
        try {
            client.execute(getServerHttp(), httpget);
            fail("ClientProtocolException should have been thrown");
        } catch (ClientProtocolException e) {
            assertTrue(e.getCause() instanceof ProtocolException);
        }
    }

    public void testRedirectWithCookie() throws Exception {
        String host = "localhost";
        int port = this.localServer.getServicePort();
        this.localServer.register("*", new BasicRedirectService(host, port));
        DefaultHttpClient client = new DefaultHttpClient();
        CookieStore cookieStore = new BasicCookieStore();
        client.setCookieStore(cookieStore);
        BasicClientCookie cookie = new BasicClientCookie("name", "value");
        cookie.setDomain("localhost");
        cookie.setPath("/");
        cookieStore.addCookie(cookie);
        HttpContext context = new BasicHttpContext();
        HttpGet httpget = new HttpGet("/oldlocation/");
        HttpResponse response = client.execute(getServerHttp(), httpget, context);
        HttpEntity e = response.getEntity();
        if (e != null) {
            e.consumeContent();
        }
        HttpRequest reqWrapper = (HttpRequest) context.getAttribute(ExecutionContext.HTTP_REQUEST);
        assertEquals(HttpStatus.SC_OK, response.getStatusLine().getStatusCode());
        assertEquals("/newlocation/", reqWrapper.getRequestLine().getUri());
        Header[] headers = reqWrapper.getHeaders(SM.COOKIE);
        assertEquals("There can only be one (cookie)", 1, headers.length);
    }

    public void testDefaultHeadersRedirect() throws Exception {
        String host = "localhost";
        int port = this.localServer.getServicePort();
        this.localServer.register("*", new BasicRedirectService(host, port));
        DefaultHttpClient client = new DefaultHttpClient();
        HttpContext context = new BasicHttpContext();
        List<Header> defaultHeaders = new ArrayList<Header>(1);
        defaultHeaders.add(new BasicHeader(HTTP.USER_AGENT, "my-test-client"));
        client.getParams().setParameter(ClientPNames.DEFAULT_HEADERS, defaultHeaders);
        HttpGet httpget = new HttpGet("/oldlocation/");
        HttpResponse response = client.execute(getServerHttp(), httpget, context);
        HttpEntity e = response.getEntity();
        if (e != null) {
            e.consumeContent();
        }
        HttpRequest reqWrapper = (HttpRequest) context.getAttribute(ExecutionContext.HTTP_REQUEST);
        assertEquals(HttpStatus.SC_OK, response.getStatusLine().getStatusCode());
        assertEquals("/newlocation/", reqWrapper.getRequestLine().getUri());
        Header header = reqWrapper.getFirstHeader(HTTP.USER_AGENT);
        assertEquals("my-test-client", header.getValue());
    }
}
