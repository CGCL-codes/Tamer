package org.echarts.servlet.sip.test;

import java.io.FileInputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URLEncoder;
import java.util.Formatter;
import java.util.Locale;
import java.util.Properties;
import java.util.regex.Pattern;
import java.util.Vector;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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

public final class b2buaMachineTest extends org.echarts.test.sip.CATTestCase {

    /** Version control identifier strings. */
    public static final String[] RCS_ID = { "$URL$", "$Id$" };

    String appServer;

    String httpServer;

    String appName;

    int sipListenPort;

    Logger logger;

    int remoteListenPort;

    boolean isCheckEndToEnd;

    String domain = null;

    static final String CALLER_HEADER_NAME = "X-TEST-CALLER-HEADER";

    static final Vector<String> CALLER_HEADER_VALUE = new Vector<String>();

    static final Map<String, List<String>> CALLER_HEADER = new HashMap<String, List<String>>();

    static final String CALLEE_HEADER_NAME = "X-TEST-CALLEE-HEADER";

    static final Vector<String> CALLEE_HEADER_VALUE = new Vector<String>();

    static final Map<String, List<String>> CALLEE_HEADER = new HashMap<String, List<String>>();

    static {
        CALLER_HEADER_VALUE.add("a caller header value");
        CALLER_HEADER.put(CALLER_HEADER_NAME, CALLER_HEADER_VALUE);
        CALLEE_HEADER_VALUE.add("a callee header value");
        CALLEE_HEADER.put(CALLEE_HEADER_NAME, CALLEE_HEADER_VALUE);
    }

    String callerAudioFileName;

    public b2buaMachineTest() throws Exception {
        logger = this.getLogger();
        Properties props = new Properties();
        try {
            props.load(b2buaMachineTest.class.getResourceAsStream("/test.properties"));
            props.putAll(System.getProperties());
            appServer = props.getProperty("SipAS");
            httpServer = props.getProperty("HttpAS");
            domain = props.getProperty("domain");
            sipListenPort = Integer.parseInt(props.getProperty("SipStackListenPort"));
            appName = props.getProperty("AppName", "b2buaTest");
            callerAudioFileName = props.getProperty("callerAudioFileName");
            isCheckEndToEnd = Boolean.parseBoolean(props.getProperty("isCheckEndToEnd", "true"));
            logger.warn("CWD PATH=" + (new java.io.File(".")).getAbsolutePath());
            init(appName, props.getProperty("testOutputDirectory", "out"), sipListenPort);
        } catch (Exception e) {
            logger.error("error loading/reading test configuration file", e);
            throw e;
        }
    }

    @BeforeClass
    public static void runOnceBeforeAllTests() {
    }

    @AfterClass
    public static void runOnceAfterAllTests() {
    }

    private void checkMediaFlow(SIPAgent sender, SIPAgent receiver) throws CATException {
        receiver.clearMediaBuffer();
        sender.playAudio(callerAudioFileName);
        pause(2000);
        assertThat(receiver, has(incomingMedia()));
    }

    private SIPAgent caller;

    private SIPAgent callee;

    private String callerName = "alice";

    private String calleeName = "carol";

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
            if (domain != null) {
                SIPAgent callee = createAgent(calleeName);
                callee.setProxy(appServer);
                CommandId cmdId2 = callee.register(domain, "", "", 0);
                pause(2000);
                assertThat(callee, recvdSuccessResponse(cmdId2));
            }
            this.release();
        } catch (Exception e) {
            logger.error("test cleanup failed", e);
        }
        logger.info("====================");
    }

    /** Create SIPAgent's, registers callee if necessary,
	 *  and set caller's proxy.
	 *  @return callee's SIP URI
	*/
    private String initializeTest(String testName) throws Throwable {
        CATConfig config = new CATConfig();
        config.setListenPort(sipListenPort);
        config.setTestName(testName);
        this.init(config);
        caller = createAgent(callerName);
        callee = createAgent(calleeName);
        String uri;
        if (domain == null) {
            uri = callee.getSipURIAsString();
        } else {
            callee.setProxy(appServer);
            CommandId cmdId2 = callee.register(domain, "", "");
            pause(2000);
            assertThat(callee, recvdSuccessResponse(cmdId2));
            uri = "sip:" + calleeName + "@" + domain;
        }
        caller.setProxy(appServer);
        return uri;
    }

    /** Test basic call setup and callee-initiated teardown.
	 * 
	 * @throws Exception
	 */
    @Test
    public void testSetupCalleeTeardown() throws Throwable {
        try {
            String calleeRequestURI = initializeTest("testSetupCalleeTeardown");
            caller.addMessageModifier(new HeaderAddHelper(CALLER_HEADER));
            callee.addMessageModifier(new HeaderAddHelper(CALLEE_HEADER));
            CommandId inviteCmd = caller.call(calleeRequestURI);
            pause(2000);
            if (isCheckEndToEnd) {
                assertThat(callee, has(recvdRequest("INVITE", Pattern.compile(".*" + CALLER_HEADER_NAME + ".*", Pattern.DOTALL | Pattern.CASE_INSENSITIVE), null)));
            } else {
                assertThat(callee, has(recvdRequest("INVITE")));
            }
            callee.sendResponse(180);
            pause(2000);
            if (isCheckEndToEnd) {
                assertThat(caller, has(recvdResponse(inviteCmd, 180, Pattern.compile(".*" + CALLEE_HEADER_NAME + ".*", Pattern.DOTALL | Pattern.CASE_INSENSITIVE), null)));
            } else {
                assertThat(caller, has(recvdResponse(inviteCmd, 180)));
            }
            callee.answer();
            pause(2000);
            assertThat(caller, is(connectedTo(callee)));
            if (isCheckEndToEnd) {
                assertThat(callee, has(recvdRequest("ACK", Pattern.compile(".*" + CALLER_HEADER_NAME + ".*", Pattern.DOTALL | Pattern.CASE_INSENSITIVE), null)));
            } else {
                assertThat(callee, has(recvdRequest("ACK")));
            }
            if (isCheckEndToEnd) {
                assertThat(caller, has(recvdResponse(inviteCmd, 200, Pattern.compile(".*" + CALLEE_HEADER_NAME + ".*", Pattern.DOTALL | Pattern.CASE_INSENSITIVE), null)));
            } else {
                assertThat(caller, has(recvdResponse(inviteCmd, 200)));
            }
            checkMediaFlow(caller, callee);
            checkMediaFlow(callee, caller);
            callee.end();
            pause(2000);
            assertThat(callee, is(disconnected()));
            assertThat(caller, is(disconnected()));
            if (isCheckEndToEnd) {
                assertThat(caller, has(recvdRequest("BYE", Pattern.compile(".*" + CALLEE_HEADER_NAME + ".*", Pattern.DOTALL | Pattern.CASE_INSENSITIVE), null)));
                assertThat(callee, has(recvdResponse(200, Pattern.compile(".*" + CALLER_HEADER_NAME + ".*", Pattern.DOTALL | Pattern.CASE_INSENSITIVE), null)));
            }
        } catch (Throwable e) {
            logger.error("testing failed ", e);
            throw e;
        }
    }

    /** Test basic call setup and caller-initiated teardown.
	 * 
	 * @throws Exception
	 */
    @Test
    public void testSetupCallerTeardown() throws Throwable {
        try {
            String calleeRequestURI = initializeTest("testSetupCallerTeardown");
            caller.addMessageModifier(new HeaderAddHelper(CALLER_HEADER));
            callee.addMessageModifier(new HeaderAddHelper(CALLEE_HEADER));
            CommandId inviteCmd = caller.call(calleeRequestURI);
            pause(2000);
            assertThat(callee, has(recvdRequest("INVITE")));
            callee.sendResponse(180);
            pause(2000);
            assertThat(caller, has(recvdResponse(180)));
            callee.answer();
            pause(2000);
            assertThat(caller, is(connectedTo(callee)));
            caller.end();
            pause(2000);
            assertThat(callee, is(disconnected()));
            assertThat(caller, is(disconnected()));
            if (isCheckEndToEnd) {
                assertThat(callee, has(recvdRequest("BYE", Pattern.compile(".*" + CALLER_HEADER_NAME + ".*", Pattern.DOTALL | Pattern.CASE_INSENSITIVE), null)));
                assertThat(caller, has(recvdResponse(200, Pattern.compile(".*" + CALLEE_HEADER_NAME + ".*", Pattern.DOTALL | Pattern.CASE_INSENSITIVE), null)));
            }
        } catch (Throwable e) {
            logger.error("testing failed ", e);
            throw e;
        }
    }

    /** Test of a completed call from caller to callee.  
	 *  Caller ends call after callee answers.
	 * 
	 * @throws Throwable
	 */
    @Test
    public void testCompletedCall1() throws Throwable {
        try {
            String calleeRequestURI = initializeTest("testCompletedCall1");
            caller.call(calleeRequestURI);
            pause(2000);
            assertThat(callee, has(recvdRequest("INVITE")));
            callee.sendResponse(183);
            pause(2000);
            assertThat(caller, has(recvdResponse(183)));
            callee.answer();
            pause(2000);
            assertThat(caller, is(connectedTo(callee)));
            checkMediaFlow(caller, callee);
            checkMediaFlow(callee, caller);
            pause(20000);
            caller.end();
            pause(2000);
            assertThat(caller, is(disconnected()));
            assertThat(callee, is(disconnected()));
        } catch (Throwable e) {
            logger.error("test failed", e);
            throw e;
        }
    }

    /** Test of a completed call from caller to callee.  
	 *  Callee ends call after caller answers.  RTP is
	 *  not tested.
	 * 
	 * @throws Throwable
	 */
    @Test
    public void testCompletedCall2() throws Throwable {
        try {
            String calleeRequestURI = initializeTest("testCompletedCall2");
            caller.call(calleeRequestURI);
            pause(2000);
            assertThat(callee, has(recvdRequest("INVITE")));
            callee.answer();
            pause(2000);
            assertThat(caller, is(connectedTo(callee)));
            pause(2000);
            callee.end();
            pause(2000);
            assertThat(caller, is(disconnected()));
            assertThat(callee, is(disconnected()));
        } catch (Throwable e) {
            logger.error("test failed", e);
            throw e;
        }
    }

    /** 
	 * After initial call is setup, caller sends reinvite to callee.
	 *
	 * @throws Exception
	 */
    @Test
    public void testCallerReinvite() throws Throwable {
        try {
            String calleeRequestURI = initializeTest("testCallerReinvite");
            caller.call(calleeRequestURI);
            pause(2000);
            callee.sendResponse(180);
            pause(2000);
            callee.answer();
            pause(2000);
            assertThat(caller, is(connectedTo(callee)));
            caller.addMessageModifier(new HeaderAddHelper(CALLER_HEADER));
            callee.addMessageModifier(new HeaderAddHelper(CALLEE_HEADER));
            caller.reinvite();
            pause(2000);
            if (isCheckEndToEnd) {
                assertThat(callee, has(recvdRequest("INVITE", Pattern.compile(".*" + CALLER_HEADER_NAME + ".*", Pattern.DOTALL | Pattern.CASE_INSENSITIVE), null)));
                assertThat(callee, has(recvdRequest("ACK", Pattern.compile(".*" + CALLER_HEADER_NAME + ".*", Pattern.DOTALL | Pattern.CASE_INSENSITIVE), null)));
                assertThat(caller, has(recvdResponse(200, Pattern.compile(".*" + CALLEE_HEADER_NAME + ".*", Pattern.DOTALL | Pattern.CASE_INSENSITIVE), null)));
            }
            checkMediaFlow(caller, callee);
            checkMediaFlow(callee, caller);
            caller.end();
            pause(2000);
            assertThat(callee, is(disconnected()));
            assertThat(caller, is(disconnected()));
        } catch (Throwable e) {
            logger.error("testing failed ", e);
            throw e;
        }
    }

    /** 
	 * After initial call is setup, callee sends reinvite to caller.
	 *
	 * @throws Exception
	 */
    @Test
    public void testCalleeReinvite() throws Throwable {
        try {
            String calleeRequestURI = initializeTest("testCalleeReinvite");
            caller.call(calleeRequestURI);
            pause(2000);
            callee.sendResponse(180);
            pause(2000);
            callee.answer();
            pause(2000);
            assertThat(caller, is(connectedTo(callee)));
            caller.addMessageModifier(new HeaderAddHelper(CALLER_HEADER));
            callee.addMessageModifier(new HeaderAddHelper(CALLEE_HEADER));
            callee.reinvite();
            pause(2000);
            if (isCheckEndToEnd) {
                assertThat(caller, has(recvdRequest("INVITE", Pattern.compile(".*" + CALLEE_HEADER_NAME + ".*", Pattern.DOTALL | Pattern.CASE_INSENSITIVE), null)));
                assertThat(caller, has(recvdRequest("ACK", Pattern.compile(".*" + CALLEE_HEADER_NAME + ".*", Pattern.DOTALL | Pattern.CASE_INSENSITIVE), null)));
                assertThat(callee, has(recvdResponse(200, Pattern.compile(".*" + CALLER_HEADER_NAME + ".*", Pattern.DOTALL | Pattern.CASE_INSENSITIVE), null)));
            }
            checkMediaFlow(caller, callee);
            checkMediaFlow(callee, caller);
            callee.end();
            pause(2000);
            assertThat(callee, is(disconnected()));
            assertThat(caller, is(disconnected()));
        } catch (Throwable e) {
            logger.error("testing failed ", e);
            throw e;
        }
    }

    /** Test media solicit in caller INVITE.
	 * 
	 * @throws Exception
	 */
    @Test
    public void testCallerSolicit() throws Throwable {
        try {
            String calleeRequestURI = initializeTest("testCallerSolicit");
            caller.addMessageModifier(new HeaderAddHelper(CALLER_HEADER));
            callee.addMessageModifier(new HeaderAddHelper(CALLEE_HEADER));
            CommandId inviteCmd = caller.callNoSdp(calleeRequestURI);
            pause(2000);
            if (isCheckEndToEnd) {
                assertThat(callee, has(recvdRequest("INVITE", Pattern.compile(".*" + CALLER_HEADER_NAME + ".*", Pattern.DOTALL | Pattern.CASE_INSENSITIVE), null)));
            } else {
                assertThat(callee, has(recvdRequest("INVITE")));
            }
            callee.sendResponse(180);
            pause(2000);
            if (isCheckEndToEnd) {
                assertThat(caller, has(recvdResponse(inviteCmd, 180, Pattern.compile(".*" + CALLEE_HEADER_NAME + ".*", Pattern.DOTALL | Pattern.CASE_INSENSITIVE), null)));
            } else {
                assertThat(caller, has(recvdResponse(inviteCmd, 180)));
            }
            callee.answer();
            pause(2000);
            assertThat(caller, is(connectedTo(callee)));
            if (isCheckEndToEnd) {
                assertThat(callee, has(recvdRequest("ACK", Pattern.compile(".*" + CALLER_HEADER_NAME + ".*", Pattern.DOTALL | Pattern.CASE_INSENSITIVE), null)));
            } else {
                assertThat(callee, has(recvdRequest("ACK")));
            }
            if (isCheckEndToEnd) {
                assertThat(caller, has(recvdResponse(200, Pattern.compile(".*" + CALLEE_HEADER_NAME + ".*", Pattern.DOTALL | Pattern.CASE_INSENSITIVE), null)));
            } else {
                assertThat(caller, has(recvdResponse(200)));
            }
            checkMediaFlow(caller, callee);
            checkMediaFlow(callee, caller);
            callee.end();
            pause(2000);
            assertThat(callee, is(disconnected()));
            assertThat(caller, is(disconnected()));
            if (isCheckEndToEnd) {
                assertThat(caller, has(recvdRequest("BYE", Pattern.compile(".*" + CALLEE_HEADER_NAME + ".*", Pattern.DOTALL | Pattern.CASE_INSENSITIVE), null)));
                assertThat(callee, has(recvdResponse(200, Pattern.compile(".*" + CALLER_HEADER_NAME + ".*", Pattern.DOTALL | Pattern.CASE_INSENSITIVE), null)));
            }
        } catch (Throwable e) {
            logger.error("testing failed ", e);
            throw e;
        }
    }

    /** Test callee busy response to caller INVITE.
	 * 
	 * @throws Exception
	 */
    @Test
    public void testCalleeBusy() throws Throwable {
        try {
            String calleeRequestURI = initializeTest("testCalleeBusy");
            callee.addMessageModifier(new HeaderAddHelper(CALLEE_HEADER));
            CommandId inviteCmd = caller.call(calleeRequestURI);
            pause(2000);
            assertThat(callee, has(recvdRequest("INVITE")));
            callee.sendResponse(180);
            pause(2000);
            assertThat(caller, has(recvdResponse(180)));
            callee.sendResponse(486);
            pause(2000);
            if (isCheckEndToEnd) {
                assertThat(caller, has(recvdResponse(inviteCmd, 486, Pattern.compile(".*" + CALLEE_HEADER_NAME + ".*", Pattern.DOTALL | Pattern.CASE_INSENSITIVE), null)));
            } else {
                assertThat(caller, has(recvdResponse(inviteCmd, 486)));
            }
            assertThat(callee, is(disconnected()));
            assertThat(caller, is(disconnected()));
        } catch (Throwable e) {
            logger.error("testing failed ", e);
            throw e;
        }
    }

    /** Test caller canceling its INVITE before callee accepts call.
	 * 
	 * TODO: check that a unique header is propagated from caller to
	 * callee and from callee to caller for teardown messages.
	 *
	 * @throws Exception
	 */
    @Test
    public void testCallerCancelBeforeAccept() throws Throwable {
        try {
            String calleeRequestURI = initializeTest("testCallerCancelBeforeAccept");
            caller.addMessageModifier(new HeaderAddHelper(CALLER_HEADER));
            CommandId inviteCmd = caller.call(calleeRequestURI);
            pause(2000);
            assertThat(callee, has(recvdRequest("INVITE")));
            callee.sendResponse(180);
            pause(2000);
            assertThat(caller, has(recvdResponse(180)));
            CommandId cancelCmd = caller.cancel();
            pause(2000);
            if (isCheckEndToEnd) {
                assertThat(callee, has(recvdRequest("CANCEL", Pattern.compile(".*" + CALLER_HEADER_NAME + ".*", Pattern.DOTALL | Pattern.CASE_INSENSITIVE), null)));
            } else {
                assertThat(callee, has(recvdRequest("CANCEL")));
            }
            assertThat(caller, has(recvdResponse(inviteCmd, 487)));
            assertThat(caller, is(disconnected()));
            assertThat(callee, is(disconnected()));
        } catch (Throwable e) {
            logger.error("testing failed ", e);
            throw e;
        }
    }

    /** Caller sends CANCEL, and callee sends 200 OK to INVITE at the
	 *  same time.
	 * 
	 * @throws Exception
	 */
    @Test
    public void testAcceptCancelRace1() throws Throwable {
        try {
            String calleeRequestURI = initializeTest("testAcceptCancelRace1");
            CommandId inviteCmd = caller.call(calleeRequestURI);
            pause(2000);
            assertThat(callee, has(recvdRequest("INVITE")));
            callee.sendResponse(180);
            pause(2000);
            assertThat(caller, has(recvdResponse(180)));
            callee.setStepMode(true);
            CommandId cancelCmd = caller.cancel();
            callee.answer();
            pause(2000);
            callee.setStepMode(false);
            pause(2000);
            assertThat(callee, has(recvdRequest("BYE")));
            assertThat(caller, has(recvdResponse(cancelCmd, 200)));
            assertThat(caller, has(recvdResponse(inviteCmd, 487)));
            assertThat(caller, is(disconnected()));
            assertThat(callee, is(disconnected()));
        } catch (Throwable e) {
            logger.error("testing failed ", e);
            throw e;
        }
    }

    @Test
    public void testByeRace1() throws Throwable {
        try {
            String calleeRequestURI = initializeTest("testByeRace1");
            CommandId inviteCmd = caller.call(calleeRequestURI);
            pause(2000);
            assertThat(callee, has(recvdRequest("INVITE")));
            callee.answer();
            pause(2000);
            assertThat(caller, is(connectedTo(callee)));
            pause(10000);
            caller.setStepMode(true);
            callee.setStepMode(true);
            caller.end();
            callee.end();
            pause(1000);
            caller.setStepMode(false);
            callee.setStepMode(false);
            pause(2000);
            assertThat(caller, is(disconnected()));
            assertThat(callee, is(disconnected()));
        } catch (Throwable e) {
            logger.error("test failed", e);
            throw e;
        }
    }

    @Test
    public void testByeRace2() throws Throwable {
        try {
            String calleeRequestURI = initializeTest("testByeRace2");
            CommandId inviteCmd = caller.call(calleeRequestURI);
            pause(2000);
            assertThat(callee, has(recvdRequest("INVITE")));
            callee.answer();
            pause(2000);
            assertThat(caller, is(connectedTo(callee)));
            pause(10000);
            caller.setStepMode(true);
            callee.setStepMode(true);
            caller.end();
            callee.end();
            pause(1000);
            assertThat(callee, recvdRequest("BYE"));
            callee.sendResponse(200);
            caller.setStepMode(false);
            callee.setStepMode(false);
            pause(2000);
            assertThat(caller, is(disconnected()));
            assertThat(callee, is(disconnected()));
        } catch (Throwable e) {
            logger.error("test failed", e);
            throw e;
        }
    }

    /** 
	 * After initial call is setup, caller sends reinvite to callee.
	 * But caller sends BYE before sending ACK to 200 OK.
	 *
	 * @throws Exception
	 */
    @Test
    public void testCallerReinviteCallerBye() throws Throwable {
        try {
            String calleeRequestURI = initializeTest("testCallerReinviteCallerBye");
            caller.call(calleeRequestURI);
            pause(2000);
            callee.sendResponse(180);
            pause(2000);
            callee.answer();
            pause(2000);
            assertThat(caller, is(connectedTo(callee)));
            caller.addMessageModifier(new HeaderAddHelper(CALLER_HEADER));
            callee.addMessageModifier(new HeaderAddHelper(CALLEE_HEADER));
            caller.setStepMode(true);
            caller.reinvite();
            pause(1000);
            caller.end();
            pause(1000);
            caller.setStepMode(false);
            assertThat(callee, is(disconnected()));
            assertThat(caller, is(disconnected()));
        } catch (Throwable e) {
            logger.error("testing failed ", e);
            throw e;
        }
    }

    /** 
	 * After initial call is setup, caller sends reinvite to callee.
	 * But callee sends BYE before receiving ACK to 200 OK.
	 *
	 * @throws Exception
	 */
    @Test
    public void testCallerReinviteCalleeBye() throws Throwable {
        try {
            String calleeRequestURI = initializeTest("testCallerReinviteCalleeBye");
            caller.call(calleeRequestURI);
            pause(2000);
            callee.sendResponse(180);
            pause(2000);
            callee.answer();
            pause(2000);
            assertThat(caller, is(connectedTo(callee)));
            caller.addMessageModifier(new HeaderAddHelper(CALLER_HEADER));
            callee.addMessageModifier(new HeaderAddHelper(CALLEE_HEADER));
            caller.setStepMode(true);
            caller.reinvite();
            pause(1000);
            callee.end();
            pause(1000);
            caller.setStepMode(false);
            assertThat(callee, is(disconnected()));
            assertThat(caller, is(disconnected()));
        } catch (Throwable e) {
            logger.error("testing failed ", e);
            throw e;
        }
    }
}
