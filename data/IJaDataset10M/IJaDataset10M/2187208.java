package test.integ.be.fedict.eid.idp;

import static org.junit.Assert.assertEquals;
import java.awt.Component;
import java.io.IOException;
import java.net.CookieHandler;
import java.net.CookieManager;
import java.net.CookiePolicy;
import java.net.CookieStore;
import java.net.InetAddress;
import java.net.MalformedURLException;
import java.net.Socket;
import java.net.URI;
import java.net.URL;
import java.net.UnknownHostException;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.httpclient.ConnectTimeoutException;
import org.apache.commons.httpclient.Header;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpState;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.params.HttpConnectionParams;
import org.apache.commons.httpclient.protocol.Protocol;
import org.apache.commons.httpclient.protocol.ProtocolSocketFactory;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.easymock.EasyMock;
import org.junit.After;
import org.junit.Test;
import org.mortbay.jetty.servlet.ServletHolder;
import org.mortbay.jetty.testing.ServletTester;
import be.fedict.eid.applet.Applet;
import be.fedict.eid.applet.Controller;
import be.fedict.eid.applet.DiagnosticTests;
import be.fedict.eid.applet.Messages;
import be.fedict.eid.applet.Messages.MESSAGE_ID;
import be.fedict.eid.applet.Runtime;
import be.fedict.eid.applet.Status;
import be.fedict.eid.applet.View;
import be.fedict.eid.idp.sp.protocol.openid.AuthenticationRequestServlet;
import be.fedict.eid.idp.sp.protocol.openid.AuthenticationResponseServlet;

/**
 * Integration tests for the OpenID protocol.
 * 
 * @author Frank Cornelis
 * 
 */
public class OpenIDTest {

    private static final Log LOG = LogFactory.getLog(OpenIDTest.class);

    private ServletTester servletTester;

    @After
    public void tearDown() throws Exception {
        if (null != this.servletTester) {
            this.servletTester.stop();
        }
    }

    @Test
    public void testOpenID() throws Exception {
        LOG.debug("OpenID integration test");
        MyCookieManager cookieManager = new MyCookieManager();
        cookieManager.setCookiePolicy(CookiePolicy.ACCEPT_ALL);
        CookieHandler.setDefault(cookieManager);
        this.servletTester = new ServletTester();
        ServletHolder reqServletHolder = this.servletTester.addServlet(AuthenticationRequestServlet.class, "/openid-request");
        reqServletHolder.setInitParameter("ParametersFromRequest", "true");
        reqServletHolder.setInitParameter("SPDestination", "http://localhost/openid-response");
        reqServletHolder.setInitParameter("UserIdentifier", "https://localhost/eid-idp/endpoints/openid-identity");
        reqServletHolder.setInitParameter("TrustServer", "true");
        ServletHolder responseServletHolder = this.servletTester.addServlet(AuthenticationResponseServlet.class, "/openid-response");
        responseServletHolder.setInitParameter("RedirectPage", "/target");
        responseServletHolder.setInitParameter("IdentifierSessionAttribute", "identifier");
        this.servletTester.start();
        String location = this.servletTester.createSocketConnector(true);
        LOG.debug("location: " + location);
        HttpState httpState = new HttpState();
        HttpClient httpClient = new HttpClient();
        httpClient.setState(httpState);
        httpClient.getParams().setCookiePolicy(org.apache.commons.httpclient.cookie.CookiePolicy.BROWSER_COMPATIBILITY);
        httpClient.getParams().setParameter("http.protocol.allow-circular-redirects", Boolean.TRUE);
        GetMethod getMethod = new GetMethod(location + "/openid-request?SPDestination=" + location + "/openid-response&UserIdentifier=https://localhost/eid-idp/endpoints/openid-identity");
        getMethod.setFollowRedirects(false);
        ProtocolSocketFactory protocolSocketFactory = new MyProtocolSocketFactory();
        Protocol myProtocol = new Protocol("https", protocolSocketFactory, 443);
        Protocol.registerProtocol("https", myProtocol);
        int statusCode = httpClient.executeMethod(getMethod);
        LOG.debug("status code: " + statusCode);
        assertEquals(HttpServletResponse.SC_MOVED_TEMPORARILY, statusCode);
        LOG.debug("response body: " + getMethod.getResponseBodyAsString());
        Header jettySetCookieHeader = getMethod.getResponseHeader("Set-Cookie");
        String jettySessionCookieValue = jettySetCookieHeader.getValue();
        LOG.debug("jetty session cookie value: " + jettySessionCookieValue);
        String idpLocation = getMethod.getResponseHeader("Location").getValue();
        LOG.debug("IdP location: " + idpLocation);
        getMethod = new GetMethod(idpLocation);
        getMethod.setFollowRedirects(false);
        statusCode = httpClient.executeMethod(getMethod);
        assertEquals(HttpServletResponse.SC_MOVED_TEMPORARILY, statusCode);
        LOG.debug("response body: " + getMethod.getResponseBodyAsString());
        String idpSessionCookieValue = getMethod.getResponseHeader("Set-Cookie").getValue();
        LOG.debug("IdP session cookie value: " + idpSessionCookieValue);
        String idpSeamLocation = getMethod.getResponseHeader("Location").getValue();
        getMethod = new GetMethod(idpSeamLocation);
        getMethod.setFollowRedirects(false);
        getMethod.addRequestHeader("Cookie", idpSessionCookieValue);
        statusCode = httpClient.executeMethod(getMethod);
        assertEquals(HttpServletResponse.SC_OK, statusCode);
        cookieManager.setSessionCookieValue(idpSessionCookieValue);
        Messages messages = new Messages(Locale.getDefault());
        Runtime runtime = new TestRuntime();
        View view = new TestView();
        Controller controller = new Controller(view, runtime, messages);
        controller.run();
        LOG.debug("continue to eID IdP exit page...");
        getMethod = new GetMethod("https://localhost/eid-idp/protocol-exit");
        getMethod.addRequestHeader("Cookie", idpSessionCookieValue);
        getMethod.setFollowRedirects(false);
        statusCode = httpClient.executeMethod(getMethod);
        LOG.debug("status code: " + statusCode);
        assertEquals(HttpServletResponse.SC_MOVED_TEMPORARILY, statusCode);
        String jettyResponseLocation = getMethod.getResponseHeader("Location").getValue();
        getMethod = new GetMethod(jettyResponseLocation);
        getMethod.setFollowRedirects(false);
        statusCode = httpClient.executeMethod(getMethod);
        LOG.debug("status code: " + statusCode);
        assertEquals(HttpServletResponse.SC_MOVED_TEMPORARILY, statusCode);
        LOG.debug("response body: " + getMethod.getResponseBodyAsString());
    }

    private static class MyCookieManager extends CookieManager {

        private static final Log LOG = LogFactory.getLog(MyCookieManager.class);

        private String sessionCookieValue;

        public void setSessionCookieValue(String sessionCookieValue) {
            this.sessionCookieValue = sessionCookieValue;
        }

        @Override
        public Map<String, List<String>> get(URI uri, Map<String, List<String>> requestHeaders) throws IOException {
            LOG.debug("get: " + uri + ": " + requestHeaders);
            Map<String, List<String>> result = super.get(uri, requestHeaders);
            if (uri.toString().contains("/eid-idp")) {
                if (null != this.sessionCookieValue) {
                    result.get("Cookie").add(this.sessionCookieValue);
                }
            }
            LOG.debug("result: " + result);
            return result;
        }

        @Override
        public CookieStore getCookieStore() {
            LOG.debug("getCookieStore");
            return super.getCookieStore();
        }

        @Override
        public void put(URI uri, Map<String, List<String>> responseHeaders) throws IOException {
            LOG.debug("put: " + uri + "; " + responseHeaders);
            super.put(uri, responseHeaders);
        }

        @Override
        public void setCookiePolicy(CookiePolicy cookiePolicy) {
            LOG.debug("setCookiePolicy");
            super.setCookiePolicy(cookiePolicy);
        }
    }

    private static class MyTrustManager implements X509TrustManager {

        private static final Log LOG = LogFactory.getLog(MyTrustManager.class);

        public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException {
            LOG.error("checkClientTrusted");
            throw new UnsupportedOperationException();
        }

        public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException {
            LOG.debug("check server trusted");
            LOG.debug("auth type: " + authType);
        }

        public X509Certificate[] getAcceptedIssuers() {
            LOG.error("getAcceptedIssuers");
            throw new UnsupportedOperationException();
        }
    }

    public static class MyProtocolSocketFactory implements ProtocolSocketFactory {

        private final SSLContext sslContext;

        public MyProtocolSocketFactory() throws NoSuchAlgorithmException, KeyManagementException {
            this.sslContext = SSLContext.getInstance("SSL");
            TrustManager trustManager = new MyTrustManager();
            TrustManager[] trustManagers = { trustManager };
            this.sslContext.init(null, trustManagers, null);
        }

        public Socket createSocket(String host, int port) throws IOException, UnknownHostException {
            return this.sslContext.getSocketFactory().createSocket(host, port);
        }

        public Socket createSocket(String host, int port, InetAddress localHost, int localPort) throws IOException, UnknownHostException {
            return this.sslContext.getSocketFactory().createSocket(host, port, localHost, localPort);
        }

        public Socket createSocket(String host, int port, InetAddress localHost, int localPort, HttpConnectionParams params) throws IOException, UnknownHostException, ConnectTimeoutException {
            return this.sslContext.getSocketFactory().createSocket(host, port, localHost, localPort);
        }
    }

    private class TestRuntime implements Runtime {

        private Applet applet;

        @Override
        public URL getDocumentBase() {
            LOG.debug("getDocumentBase()");
            try {
                return new URL("https://localhost/eid-idp/authentication-identification.seam");
            } catch (MalformedURLException e) {
                throw new RuntimeException("URL error");
            }
        }

        @Override
        public String getParameter(String name) {
            LOG.debug("getParameter(\"" + name + "\")");
            if ("AppletService".equals(name)) {
                return "applet-authentication-identification-service";
            }
            return null;
        }

        @Override
        public void gotoTargetPage() {
            LOG.debug("gotoTargetPage()");
        }

        @Override
        public Applet getApplet() {
            if (null == this.applet) {
                Applet mockApplet = EasyMock.createMock(Applet.class);
                EasyMock.expect(mockApplet.getParameter("Language")).andStubReturn(null);
                EasyMock.replay(mockApplet);
                this.applet = mockApplet;
            }
            return this.applet;
        }
    }

    private static class TestView implements View {

        @Override
        public void addDetailMessage(String detailMessage) {
            LOG.debug("detail message: " + detailMessage);
        }

        @Override
        public Component getParentComponent() {
            LOG.debug("getParentComponent()");
            return null;
        }

        @Override
        public boolean privacyQuestion(boolean includeAddress, boolean includePhoto, String identityDataUsage) {
            LOG.debug("privacyQuestion()");
            return true;
        }

        @Override
        public void setStatusMessage(Status status, MESSAGE_ID messageId) {
            LOG.debug("status message: " + status + ": " + messageId.getId());
            if (Status.ERROR == status) {
            }
        }

        @Override
        public void addTestResult(DiagnosticTests diagnosticTest, boolean success, String description) {
        }

        @Override
        public void setProgressIndeterminate() {
        }

        @Override
        public void resetProgress(int max) {
        }

        @Override
        public void increaseProgress() {
        }
    }
}
