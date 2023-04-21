package org.mobicents.servlet.sip.testsuite.reinvite;

import javax.sip.ListeningPoint;
import javax.sip.SipProvider;
import javax.sip.address.SipURI;
import javax.sip.message.Request;
import org.apache.catalina.deploy.ApplicationParameter;
import org.apache.log4j.Logger;
import org.mobicents.servlet.sip.SipServletTestCase;
import org.mobicents.servlet.sip.catalina.SipStandardManager;
import org.mobicents.servlet.sip.startup.SipContextConfig;
import org.mobicents.servlet.sip.startup.SipStandardContext;
import org.mobicents.servlet.sip.testsuite.ProtocolObjects;
import org.mobicents.servlet.sip.testsuite.TestSipListener;

/**
 * Testing of RFC3665-3.7 -- Session with re-INVITE
 * 
 * @author <A HREF="mailto:jean.deruelle@gmail.com">Jean Deruelle</A> 
 *
 */
public class ReInviteSipServletTest extends SipServletTestCase {

    private static transient Logger logger = Logger.getLogger(ReInviteSipServletTest.class);

    private static final String TRANSPORT = "udp";

    private static final boolean AUTODIALOG = true;

    private static final int TIMEOUT = 10000;

    TestSipListener sender;

    ProtocolObjects senderProtocolObjects;

    public ReInviteSipServletTest(String name) {
        super(name);
        autoDeployOnStartup = false;
    }

    public SipStandardContext deployApplication(String name, String value) {
        SipStandardContext context = new SipStandardContext();
        context.setDocBase(projectHome + "/sip-servlets-test-suite/applications/simple-sip-servlet/src/main/sipapp");
        context.setName("sip-test-context");
        context.setPath("sip-test");
        context.addLifecycleListener(new SipContextConfig());
        context.setManager(new SipStandardManager());
        ApplicationParameter applicationParameter = new ApplicationParameter();
        applicationParameter.setName(name);
        applicationParameter.setValue(value);
        context.addApplicationParameter(applicationParameter);
        assertTrue(tomcat.deployContext(context));
        return context;
    }

    @Override
    public void deployApplication() {
        assertTrue(tomcat.deployContext(projectHome + "/sip-servlets-test-suite/applications/simple-sip-servlet/src/main/sipapp", "sip-test-context", "sip-test"));
    }

    @Override
    protected String getDarConfigurationFile() {
        return "file:///" + projectHome + "/sip-servlets-test-suite/testsuite/src/test/resources/" + "org/mobicents/servlet/sip/testsuite/simple/simple-sip-servlet-dar.properties";
    }

    @Override
    protected void setUp() throws Exception {
        super.sipIpAddress = "0.0.0.0";
        super.setUp();
    }

    public void testReInvite() throws Exception {
        senderProtocolObjects = new ProtocolObjects("reinvite", "gov.nist", TRANSPORT, AUTODIALOG, null, null, null);
        sender = new TestSipListener(5080, 5070, senderProtocolObjects, false);
        SipProvider senderProvider = sender.createProvider();
        senderProvider.addSipListener(sender);
        senderProtocolObjects.start();
        String fromName = "reinvite";
        String fromSipAddress = "sip-servlets.com";
        SipURI fromAddress = senderProtocolObjects.addressFactory.createSipURI(fromName, fromSipAddress);
        String toUser = "receiver";
        String toSipAddress = "sip-servlets.com";
        SipURI toAddress = senderProtocolObjects.addressFactory.createSipURI(toUser, toSipAddress);
        deployApplication();
        sender.sendSipRequest("INVITE", fromAddress, toAddress, null, null, false);
        Thread.sleep(TIMEOUT);
        assertTrue(sender.getByeReceived());
    }

    public void testReInviteSending() throws Exception {
        senderProtocolObjects = new ProtocolObjects("reinvite", "gov.nist", TRANSPORT, AUTODIALOG, null, null, null);
        sender = new TestSipListener(5080, 5070, senderProtocolObjects, false);
        SipProvider senderProvider = sender.createProvider();
        senderProvider.addSipListener(sender);
        senderProtocolObjects.start();
        deployApplication();
        String fromName = "isendreinvite";
        String fromSipAddress = "sip-servlets.com";
        SipURI fromAddress = senderProtocolObjects.addressFactory.createSipURI(fromName, fromSipAddress);
        String toUser = "receiver";
        String toSipAddress = "sip-servlets.com";
        SipURI toAddress = senderProtocolObjects.addressFactory.createSipURI(toUser, toSipAddress);
        sender.setSendReinvite(true);
        sender.setSendBye(true);
        sender.sendSipRequest("INVITE", fromAddress, toAddress, null, null, false);
        Thread.sleep(TIMEOUT);
        assertTrue(sender.getOkToByeReceived());
    }

    public void testReInviteTCPSending() throws Exception {
        senderProtocolObjects = new ProtocolObjects("reinvite", "gov.nist", TRANSPORT, AUTODIALOG, null, null, null);
        sender = new TestSipListener(5081, 5070, senderProtocolObjects, false);
        SipProvider senderProvider = sender.createProvider();
        senderProvider.addSipListener(sender);
        tomcat.addSipConnector(serverName, super.sipIpAddress, 5070, ListeningPoint.TCP);
        String fromName = "SSsendBye";
        String fromSipAddress = "sip-servlets.com";
        SipURI fromAddress = senderProtocolObjects.addressFactory.createSipURI(fromName, fromSipAddress);
        String toUser = "receiver";
        String toSipAddress = "sip-servlets.com";
        SipURI toAddress = senderProtocolObjects.addressFactory.createSipURI(toUser, toSipAddress);
        sender.addListeningPoint("" + System.getProperty("org.mobicents.testsuite.testhostaddr") + "", 5081, ListeningPoint.TCP);
        senderProtocolObjects.start();
        sender.setSendBye(false);
        sender.setTransport(false);
        deployApplication();
        sender.sendSipRequest("INVITE", fromAddress, toAddress, null, null, false);
        Thread.sleep(TIMEOUT);
        sender.sendInDialogSipRequest(Request.INFO, null, null, null, null, ListeningPoint.TCP);
        Thread.sleep(TIMEOUT * 2);
        assertTrue(sender.getByeReceived());
    }

    public void testNoAckOnReInvite() throws Exception {
        senderProtocolObjects = new ProtocolObjects("reinvite", "gov.nist", TRANSPORT, AUTODIALOG, null, null, null);
        sender = new TestSipListener(5080, 5070, senderProtocolObjects, false);
        SipProvider senderProvider = sender.createProvider();
        senderProvider.addSipListener(sender);
        String fromName = "SSsendBye";
        String fromSipAddress = "sip-servlets.com";
        SipURI fromAddress = senderProtocolObjects.addressFactory.createSipURI(fromName, fromSipAddress);
        String toUser = "receiver";
        String toSipAddress = "sip-servlets.com";
        SipURI toAddress = senderProtocolObjects.addressFactory.createSipURI(toUser, toSipAddress);
        senderProtocolObjects.start();
        sender.setSendBye(false);
        sender.setTransport(false);
        deployApplication("byeDelay", "50000");
        sender.sendSipRequest("INVITE", fromAddress, toAddress, null, null, false);
        Thread.sleep(TIMEOUT);
        assertEquals(sender.getFinalResponseStatus(), 200);
        sender.setSendAck(false);
        sender.sendInDialogSipRequest(Request.INVITE, null, null, null, null, ListeningPoint.UDP);
        Thread.sleep(TIMEOUT * 6);
        assertTrue(sender.getAllMessagesContent().contains("noAckReceived"));
        assertTrue(sender.getByeReceived());
    }

    @Override
    protected void tearDown() throws Exception {
        senderProtocolObjects.destroy();
        logger.info("Test completed");
        super.tearDown();
    }
}
