package org.echarts.servlet.sip.features.proxyRequest.test;

import static org.junit.Assert.*;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.MalformedURLException;
import java.net.URLEncoder;
import java.util.Formatter;
import java.util.Locale;
import java.util.Properties;
import org.echarts.test.sip.*;
import org.apache.log4j.Logger;
import org.junit.After;
import org.junit.Before;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.Ignore;
import static org.echarts.test.sip.CATMatchers.*;
import static org.hamcrest.Matchers.*;
import com.gargoylesoftware.htmlunit.FailingHttpStatusCodeException;
import com.gargoylesoftware.htmlunit.WebClient;

public final class ProxyRequestServletTest extends org.echarts.test.sip.CATTestCase {

    /** Version control identifier strings. */
    public static final String[] RCS_ID = { "$URL$", "$Id$" };

    String appServer;

    String httpServer;

    String appName;

    int sipListenPort;

    String sipListenAddress;

    String outputDir;

    Logger logger;

    int remoteListenPort;

    public ProxyRequestServletTest() throws Exception {
        logger = this.getLogger();
        Properties props = new Properties();
        try {
            File propertiesFile = new File("test.properties");
            if (propertiesFile.exists()) {
                props.load(new FileInputStream(propertiesFile));
            } else {
                props.load(ProxyRequestServletTest.class.getResourceAsStream("/test.properties"));
            }
            props.putAll(System.getProperties());
            appServer = props.getProperty("SipAS");
            httpServer = props.getProperty("HttpAS");
            sipListenPort = Integer.parseInt(props.getProperty("SipStackListenPort"));
            sipListenAddress = props.getProperty("SipStackListenIP", InetAddress.getLocalHost().getHostAddress());
            outputDir = props.getProperty("OutputDir", "out");
            appName = "proxyRequestTest";
        } catch (Exception e) {
            logger.error("error loading/reading test configuration file", e);
            throw e;
        }
    }

    private void init(String testName) throws CATException {
        CATConfig config = new CATConfig();
        config.setListenIP(this.sipListenAddress);
        config.setListenPort(this.sipListenPort);
        config.setTestName(testName);
        config.setOutputDir(outputDir);
        this.init(config);
    }

    @BeforeClass
    public static void runOnceBeforeAllTests() {
    }

    @AfterClass
    public static void runOnceAfterAllTests() {
    }

    @Before
    public void runBeforeEachTest() {
        try {
            logger.info("running test setup");
        } catch (Exception e) {
            logger.error("test setup failed", e);
        }
    }

    @After
    public void runAfterEachTest() {
        try {
            logger.info("running test cleanup");
            this.release();
        } catch (Exception e) {
            logger.error("test cleanup failed", e);
        }
        logger.info("====================");
    }

    /** Tests the forwarding to original request URI. Caller calls
	 * callee. Call should be forwarded to callee.
	 * 
	 * @throws Exception
	 */
    @Test
    public void testDefaultForwarding() throws Exception {
        try {
            this.init("testDefaultForwarding");
            SIPAgent caller = createAgent("caller");
            SIPAgent callee = createAgent("callee");
            clearAll();
            Thread.sleep(1000);
            caller.setProxy(appServer);
            caller.call(callee);
            pause(2000);
            assertThat(callee, has(recvdRequest("INVITE")));
            callee.sendResponse(180);
            pause(2000);
            assertThat(caller, has(recvdResponse(180)));
            callee.answer();
            pause(2000);
            assertThat(caller, is(connectedTo(callee)));
            callee.end();
            pause(2000);
            assertThat(callee, is(disconnected()));
            assertThat(caller, is(disconnected()));
        } catch (Exception e) {
            logger.error("test failed", e);
            throw e;
        } catch (Error e) {
            logger.error("test failed", e);
            throw e;
        }
    }

    /** Confirms that caller's call fails with specified status code
	 * response.
	 * 
	 * @throws Exception
	 */
    @Test
    public void testFailure() throws Exception {
        try {
            this.init("testFailure");
            SIPAgent caller = createAgent("caller");
            SIPAgent subscriber = createAgent("subscriber");
            clearAll();
            setFailureStatusCode(410);
            Thread.sleep(1000);
            caller.setProxy(appServer);
            caller.call(subscriber);
            pause(2000);
            assertThat(subscriber, is(idle()));
            assertThat(caller, has(recvdResponse(410)));
            assertThat(caller, is(disconnected()));
        } catch (Exception e) {
            logger.error("test failed", e);
            throw e;
        } catch (Error e) {
            logger.error("test failed", e);
            throw e;
        }
    }

    /** Just tests the forwarding capability. Caller calls subscriber,
	 * call should be forwarded to forwardingCallee.
	 * 
	 * @throws Exception
	 */
    @Test
    public void testForwarding() throws Exception {
        try {
            this.init("testForwarding");
            SIPAgent caller = createAgent("caller");
            SIPAgent subscriber = createAgent("subscriber");
            SIPAgent forwardedTo = createAgent("forwardedTo");
            clearAll();
            setForwardingUser("forwardedTo");
            Thread.sleep(1000);
            caller.setProxy(appServer);
            caller.call(subscriber);
            pause(2000);
            assertThat(subscriber, is(idle()));
            assertThat(forwardedTo, has(recvdRequest("INVITE")));
            forwardedTo.sendResponse(180);
            pause(2000);
            assertThat(caller, has(recvdResponse(180)));
            forwardedTo.answer();
            pause(2000);
            assertThat(caller, is(connectedTo(forwardedTo)));
            forwardedTo.end();
            pause(2000);
            assertThat(forwardedTo, is(disconnected()));
            assertThat(caller, is(disconnected()));
        } catch (Exception e) {
            logger.error("test failed", e);
            throw e;
        } catch (Error e) {
            logger.error("test failed", e);
            throw e;
        }
    }

    private void setFailureStatusCode(int sc) throws FailingHttpStatusCodeException, MalformedURLException, IOException {
        StringBuilder sb = new StringBuilder();
        Formatter formatter = new Formatter(sb, Locale.US);
        formatter.format("http://%s/%s/setFailureStatusCode.jsp?", httpServer, appName);
        sb.append("sc=");
        sb.append(URLEncoder.encode(Integer.toString(sc), "UTF-8"));
        WebClient wc = new WebClient();
        wc.getPage(sb.toString());
    }

    private void setForwardingUser(String nextUser) throws FailingHttpStatusCodeException, MalformedURLException, IOException {
        StringBuilder sb = new StringBuilder();
        Formatter formatter = new Formatter(sb, Locale.US);
        formatter.format("http://%s/%s/setForwardingURI.jsp?", httpServer, appName);
        sb.append("nextUser=");
        sb.append(URLEncoder.encode(nextUser, "UTF-8"));
        WebClient wc = new WebClient();
        wc.getPage(sb.toString());
    }

    private void clearAll() throws FailingHttpStatusCodeException, MalformedURLException, IOException {
        StringBuilder sb = new StringBuilder();
        Formatter formatter = new Formatter(sb, Locale.US);
        formatter.format("http://%s/%s/clearAll.jsp?", httpServer, appName);
        WebClient wc = new WebClient();
        wc.getPage(sb.toString());
    }
}
