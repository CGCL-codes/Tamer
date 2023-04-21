package org.mobicents.servlet.sip.arquillian.testsuite.click2call;

import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.Properties;
import java.util.logging.Logger;
import javax.sip.message.Response;
import org.cafesip.sipunit.SipCall;
import org.cafesip.sipunit.SipPhone;
import org.cafesip.sipunit.SipStack;
import org.cafesip.sipunit.SipTestCase;
import org.jboss.arquillian.container.mobicents.api.annotations.GetDeployableContainer;
import org.jboss.arquillian.container.mss.extension.ContainerManagerTool;
import org.jboss.arquillian.container.mss.extension.SipStackTool;
import org.jboss.arquillian.container.test.api.Deployer;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.container.test.api.RunAsClient;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.arquillian.test.api.ArquillianResource;
import org.jboss.shrinkwrap.api.ArchivePaths;
import org.jboss.shrinkwrap.api.GenericArchive;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.shrinkwrap.resolver.api.DependencyResolvers;
import org.jboss.shrinkwrap.resolver.api.maven.MavenDependencyResolver;
import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mobicents.servlet.sip.arquillian.testsuite.click2call.Click2DialSipServlet;
import org.mobicents.servlet.sip.arquillian.testsuite.click2call.SimpleWebServlet;

/**
 * Tests that SIP Servlets deployments into the  Mobicents Sip Servlets server work through the
 * Arquillian lifecycle
 * 
 * This test is currently commented since the Weld integration in Mobicents Sip Servlets has not yet been done
 * 
 * @author gvagenas@gmail.com / devrealm.org
 * @author Jean Deruelle
 * @version $Revision: $
 */
@RunWith(Arquillian.class)
public class Click2CallBasicTest extends SipTestCase {

    private SipStack sipStackA;

    private SipStack sipStackB;

    private SipPhone ua;

    private SipPhone ub;

    private String myPortA = "5057";

    private String myPortB = "5056";

    private String proxyPort = "5070";

    @GetDeployableContainer
    private ContainerManagerTool containerManager;

    @ArquillianResource
    URL deploymentUrl;

    private static String testProtocol;

    private static final int timeout = 10000;

    String CLICK2DIAL_URL = "http://127.0.0.1:8888/click2call/call";

    String RESOURCE_LEAK_URL = "http://127.0.0.1:8888/click2call/index.html";

    String EXPIRATION_TIME_PARAMS = "?expirationTime";

    String CLICK2DIAL_PARAMS = "?from=sip:from@127.0.0.1:5056&to=sip:to@127.0.0.1:5057";

    private static SipStackTool sipStackTool;

    private String testArchive = "simple";

    @BeforeClass
    public static void beforeClass() {
        sipStackTool = new SipStackTool();
    }

    @Before
    public void setUp() throws Exception {
        log.info("Creating sipStackA");
        sipStackA = sipStackTool.initializeSipStack(SipStack.PROTOCOL_UDP, "127.0.0.1", myPortA, "127.0.0.1:" + proxyPort);
        log.info("Creating sipStackB");
        sipStackB = sipStackTool.initializeSipStack(SipStack.PROTOCOL_UDP, "127.0.0.1", myPortB, "127.0.0.1:" + proxyPort);
        log.info("About to start creating sipPhones");
        ua = sipStackA.createSipPhone("localhost", SipStack.PROTOCOL_UDP, Integer.valueOf(proxyPort), "sip:to@127.0.0.1");
        log.info("SipPhone A created!");
        ub = sipStackB.createSipPhone("localhost", SipStack.PROTOCOL_UDP, Integer.valueOf(proxyPort), "sip:from@127.0.0.1");
        log.info("SipPhone B created!");
    }

    @After
    public void tearDown() throws Exception {
        ua.dispose();
        sipStackA.dispose();
        ub.dispose();
        sipStackB.dispose();
    }

    /**
	 * Logger
	 */
    private static final Logger log = Logger.getLogger(Click2CallBasicTest.class.getName());

    @Deployment(testable = false)
    public static WebArchive createTestArchive() {
        WebArchive webArchive = ShrinkWrap.create(WebArchive.class, "click2call.war");
        webArchive.addClasses(Click2DialSipServlet.class, SimpleWebServlet.class);
        webArchive.addAsWebInfResource("in-container-web.xml", "web.xml");
        webArchive.addAsWebInfResource("in-container-sip.xml", "sip.xml");
        return webArchive;
    }

    @Test
    public void testClickToCallNoConvergedSession() throws Exception {
        SipCall sipCallA = ua.createSipCall();
        SipCall sipCallB = ub.createSipCall();
        sipCallA.listenForIncomingCall();
        sipCallB.listenForIncomingCall();
        log.info("Trying to reach url : " + CLICK2DIAL_URL + CLICK2DIAL_PARAMS);
        URL url = new URL(CLICK2DIAL_URL + CLICK2DIAL_PARAMS);
        InputStream in = url.openConnection().getInputStream();
        byte[] buffer = new byte[10000];
        int len = in.read(buffer);
        String httpResponse = "";
        for (int q = 0; q < len; q++) httpResponse += (char) buffer[q];
        log.info("Received the following HTTP response: " + httpResponse);
        assertTrue(sipCallA.waitForIncomingCall(timeout));
        assertTrue(sipCallA.sendIncomingCallResponse(Response.RINGING, "Ringing", 0));
        assertTrue(sipCallA.sendIncomingCallResponse(Response.OK, "OK", 0));
        assertTrue(sipCallB.waitForIncomingCall(timeout));
        assertTrue(sipCallB.sendIncomingCallResponse(Response.RINGING, "Ringing", 0));
        assertTrue(sipCallB.sendIncomingCallResponse(Response.OK, "OK", 0));
        assertTrue(sipCallB.waitForAck(timeout));
        assertTrue(sipCallA.waitForAck(timeout));
        assertTrue(sipCallA.disconnect());
        assertTrue(sipCallB.waitForDisconnect(timeout));
        assertTrue(sipCallB.respondToDisconnect());
    }

    /**
	 * http://code.google.com/p/mobicents/issues/detail?id=882 
	 * HTTP requests to a SIP application always create an HTTP session, even for static resources
	 */
    @Test
    @Ignore
    public void testClickToCallHttpSessionLeak() throws Exception {
        final int sessionsNumber = containerManager.getSipStandardManager().getActiveSessions();
        log.info("Trying to reach url : " + RESOURCE_LEAK_URL);
        URL url = new URL(RESOURCE_LEAK_URL);
        URLConnection uc = url.openConnection();
        InputStream in = uc.getInputStream();
        byte[] buffer = new byte[10000];
        int len = in.read(buffer);
        String httpResponse = "";
        for (int q = 0; q < len; q++) httpResponse += (char) buffer[q];
        log.info("Received the follwing HTTP response: " + httpResponse);
        assertEquals(sessionsNumber, containerManager.getSipStandardManager().getActiveSessions());
    }

    /**
	 * http://code.google.com/p/mobicents/issues/detail?id=1853 
	 * SipApplicationSession.getExpirationTime() returns 0 in converged app
	 */
    @Test
    public void testClickToCallExpirationTime() throws Exception {
        log.info("Trying to reach url : " + CLICK2DIAL_URL + EXPIRATION_TIME_PARAMS);
        URL url = new URL(CLICK2DIAL_URL + EXPIRATION_TIME_PARAMS);
        InputStream in = url.openConnection().getInputStream();
        byte[] buffer = new byte[10000];
        int len = in.read(buffer);
        String httpResponse = "";
        for (int q = 0; q < len; q++) httpResponse += (char) buffer[q];
        log.info("Received the follwing HTTP response: " + httpResponse);
        assertFalse("0".equals(httpResponse.trim()));
    }
}
