package org.mobicents.tools.sip.balancer.algorithms;

import java.util.Properties;
import javax.sip.address.SipURI;
import javax.sip.header.RecordRouteHeader;
import junit.framework.TestCase;
import org.mobicents.tools.sip.balancer.AppServer;
import org.mobicents.tools.sip.balancer.BalancerRunner;
import org.mobicents.tools.sip.balancer.EventListener;
import org.mobicents.tools.sip.balancer.HeaderConsistentHashBalancerAlgorithm;
import org.mobicents.tools.sip.balancer.PureConsistentHashBalancerAlgorithm;
import org.mobicents.tools.sip.balancer.WorstCaseUdpTestAffinityAlgorithm;
import org.mobicents.tools.sip.balancer.operation.Shootist;

public class PureConsistentHashNodeDeathTest extends TestCase {

    BalancerRunner balancer;

    int numNodes = 2;

    AppServer[] servers = new AppServer[numNodes];

    Shootist shootist;

    protected void setUp() throws Exception {
        super.setUp();
        shootist = new Shootist();
        balancer = new BalancerRunner();
        Properties properties = new Properties();
        properties.setProperty("javax.sip.STACK_NAME", "SipBalancerForwarder");
        properties.setProperty("javax.sip.AUTOMATIC_DIALOG_SUPPORT", "off");
        properties.setProperty("gov.nist.javax.sip.TRACE_LEVEL", "32");
        properties.setProperty("gov.nist.javax.sip.DEBUG_LOG", "logs/sipbalancerforwarderdebug.txt");
        properties.setProperty("gov.nist.javax.sip.SERVER_LOG", "logs/sipbalancerforwarder.xml");
        properties.setProperty("gov.nist.javax.sip.THREAD_POOL_SIZE", "2");
        properties.setProperty("gov.nist.javax.sip.REENTRANT_LISTENER", "true");
        properties.setProperty("gov.nist.javax.sip.CANCEL_CLIENT_TRANSACTION_CHECKED", "false");
        properties.setProperty("algorithmClass", PureConsistentHashBalancerAlgorithm.class.getName());
        properties.setProperty("host", "127.0.0.1");
        properties.setProperty("internalPort", "5065");
        properties.setProperty("externalPort", "5060");
        balancer.start(properties);
        for (int q = 0; q < servers.length; q++) {
            servers[q] = new AppServer("node" + q, 4060 + q);
            servers[q].start();
        }
        Thread.sleep(5000);
    }

    protected void tearDown() throws Exception {
        super.tearDown();
        shootist.stop();
        for (int q = 0; q < servers.length; q++) {
            servers[q].stop();
        }
        balancer.stop();
    }

    static AppServer invite;

    static AppServer bye;

    static AppServer ack;

    public void testInviteByeLandOnDifferentNodes() throws Exception {
        EventListener failureEventListener = new EventListener() {

            @Override
            public void uasAfterResponse(int statusCode, AppServer source) {
            }

            @Override
            public void uasAfterRequestReceived(String method, AppServer source) {
                if (method.equals("INVITE")) invite = source;
                if (method.equals("ACK")) {
                    ack = source;
                    ack.sendCleanShutdownToBalancers();
                }
                if (method.equals("BYE")) {
                    bye = source;
                }
            }

            @Override
            public void uacAfterRequestSent(String method, AppServer source) {
            }

            @Override
            public void uacAfterResponse(int statusCode, AppServer source) {
            }
        };
        for (AppServer as : servers) as.setEventListener(failureEventListener);
        shootist.callerSendsBye = true;
        shootist.sendInitialInvite();
        Thread.sleep(9000);
        shootist.sendBye();
        Thread.sleep(2000);
        if (ack != invite) TestCase.fail("INVITE and ACK should have landed on same node");
        if (bye == invite) TestCase.fail("INVITE and BYE should have landed on different nodes");
        assertNotNull(invite);
        assertNotNull(bye);
    }

    public void testAllNodesDead() throws Exception {
        for (AppServer as : servers) {
            as.sendCleanShutdownToBalancers();
            as.sendHeartbeat = false;
        }
        Thread.sleep(1000);
        shootist.callerSendsBye = true;
        shootist.sendInitialInvite();
        Thread.sleep(5000);
        assertEquals(500, shootist.responses.get(0).getStatusCode());
    }

    AppServer ringingAppServer;

    AppServer okAppServer;

    public void testOKRingingLandOnDifferentNode() throws Exception {
        EventListener failureEventListener = new EventListener() {

            @Override
            public void uasAfterResponse(int statusCode, AppServer source) {
            }

            @Override
            public void uasAfterRequestReceived(String method, AppServer source) {
            }

            @Override
            public void uacAfterRequestSent(String method, AppServer source) {
            }

            @Override
            public void uacAfterResponse(int statusCode, AppServer source) {
                if (statusCode == 180) {
                    ringingAppServer = source;
                    source.sendCleanShutdownToBalancers();
                } else {
                    okAppServer = source;
                }
            }
        };
        for (AppServer as : servers) as.setEventListener(failureEventListener);
        shootist.callerSendsBye = true;
        String fromName = "sender";
        String fromHost = "sip-servlets.com";
        SipURI fromAddress = servers[0].protocolObjects.addressFactory.createSipURI(fromName, fromHost);
        String toUser = "replaces";
        String toHost = "sip-servlets.com";
        SipURI toAddress = servers[0].protocolObjects.addressFactory.createSipURI(toUser, toHost);
        SipURI ruri = servers[0].protocolObjects.addressFactory.createSipURI("usera", "127.0.0.1:5033");
        ruri.setLrParam();
        SipURI route = servers[0].protocolObjects.addressFactory.createSipURI("lbint", "127.0.0.1:5065");
        route.setParameter("node_host", "127.0.0.1");
        route.setParameter("node_port", "4060");
        route.setLrParam();
        shootist.start();
        servers[0].sipListener.sendSipRequest("INVITE", fromAddress, toAddress, null, route, false, null, null, ruri);
        Thread.sleep(16000);
        assertTrue(shootist.inviteRequest.getHeader(RecordRouteHeader.NAME).toString().contains("node_host"));
        assertNotSame(ringingAppServer, okAppServer);
        assertNotNull(ringingAppServer);
        assertNotNull(okAppServer);
    }
}
