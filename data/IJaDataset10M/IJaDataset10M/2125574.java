package test.org.mbari.observatory.client;

import static org.junit.Assert.*;
import java.util.Date;
import org.apache.log4j.Logger;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.mbari.instrument.OGCMessageFactory;
import org.mbari.observatory.client.ObservatoryClient;
import test.org.mbari.instrument.MessageCollector;

public class ObservatoryClientTests {

    /**
	 * The log4j logger
	 */
    private static Logger logger = Logger.getLogger(ObservatoryClientTests.class);

    /**
	 * These are the ObservatoryClients and MessageCollectors that are used in
	 * the testing. Queue connections take one connection and so it needed to be
	 * made global to make sure all tests had access to the connections
	 */
    private static ObservatoryClient activeMQClient = null;

    private static MessageCollector activeMQMessageCollector = null;

    private static ObservatoryClient qpidClient = null;

    private static MessageCollector qpidMessageCollector = null;

    @BeforeClass
    public static void setUpBeforeClass() throws Exception {
        activeMQMessageCollector = new MessageCollector();
        activeMQClient = new ObservatoryClient("test-activemq", activeMQMessageCollector);
        logger.debug("Constructed a observatory client that is listening on the " + activeMQClient.getObservatoryInputQueueName() + " queue on host " + activeMQClient.getObservatoryHost() + " on port " + activeMQClient.getObservatoryPort() + " over protocol " + activeMQClient.getObservatoryProtocol());
        qpidMessageCollector = new MessageCollector();
        qpidClient = new ObservatoryClient("test-qpid", qpidMessageCollector);
        logger.debug("Constructed a ObservatoryClient that is listening on the " + qpidClient.getObservatoryInputQueueName() + " queue on host " + qpidClient.getObservatoryHost() + " on port " + qpidClient.getObservatoryPort() + " over protocol " + qpidClient.getObservatoryProtocol());
    }

    @AfterClass
    public static void tearDownAfterClass() throws Exception {
    }

    @Before
    public void setUp() throws Exception {
    }

    @After
    public void tearDown() throws Exception {
        activeMQMessageCollector.clearCollectedMessages();
        qpidMessageCollector.clearCollectedMessages();
    }

    /**
	 * Test that an ActiveMQ ObservatoryClient can be created properly
	 */
    @Test
    public void testActiveMQClientCreation() {
        logger.debug("testActiveMQClientCreation called");
        assertEquals("ClientUUID did not match", activeMQClient.getClientUUID(), "5a39851e-b343-11d9-8c1e-00306e389969");
        assertEquals("Messaging host names did not match", activeMQClient.getObservatoryHost(), "prey.mbari.org");
        assertEquals("Messaging host port did not match", activeMQClient.getObservatoryPort(), "61616");
        assertEquals("Observatory's protocol did not match", activeMQClient.getObservatoryProtocol(), "tcp");
    }

    /**
	 * This method creates and ObservatoryClient that creates a test ActiveMQ on
	 * the observatory (ActiveMQ server) and then publishes a message to it. It
	 * also sets up a MessageCollector to make sure the message makes the round
	 * trip.
	 */
    @Test
    public void testActiveMQSendAndReceive() {
        logger.debug("testActiveMQSendAndReceive called");
        String messageToSend = "Test message on " + new Date();
        activeMQClient.sendUnSOAPedAndUnsignedMessage(messageToSend);
        logger.debug("Test message sent");
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        logger.debug("There are " + activeMQMessageCollector.getIncomingMessages().size() + " messages in the collector.");
        logger.debug("The first message in the collector is " + activeMQMessageCollector.getIncomingMessages().iterator().next());
        assertEquals("The number of messages that the collector has is not correct", 1, activeMQMessageCollector.getIncomingMessages().size());
        assertEquals("The message sent does not match the one received", messageToSend, activeMQMessageCollector.getIncomingMessages().iterator().next());
    }

    /**
	 * Test that an QPID ObservatoryClient can be created properly
	 */
    @Test
    public void testQPIDClientCreation() {
        logger.debug("testQPIDClientCreation called");
        assertEquals("Messaging host names did not match", qpidClient.getObservatoryHost(), "cetus.mbari.org");
        assertEquals("Messaging host port did not match", qpidClient.getObservatoryPort(), "5672");
        assertEquals("Observatory Client UUIDs did not match", qpidClient.getClientUUID(), "5a39851e-b343-11d9-8c1e-00306e389969");
        assertEquals("Observatory's protocol did not match", qpidClient.getObservatoryProtocol(), "tcp");
    }

    /**
	 */
    @Test
    public void testQPIDSendAndReceive() {
        logger.debug("testQPIDSendAndReceive called");
        String messageToSend = "Test message on " + new Date();
        qpidClient.sendUnSOAPedAndUnsignedMessage(messageToSend);
        logger.debug("Test message sent");
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        logger.debug("There are " + qpidMessageCollector.getIncomingMessages().size() + " messages in the collector.");
        logger.debug("The first message in the collector is " + qpidMessageCollector.getIncomingMessages().iterator().next());
        assertEquals("The number of messages that the collector has is not correct", 1, qpidMessageCollector.getIncomingMessages().size());
        assertEquals("The message sent does not match the one received", messageToSend, qpidMessageCollector.getIncomingMessages().iterator().next());
    }

    /**
	 * Test the observatory clients ability to send a GetCapabilities message to
	 * the inbox on the messaging broker. This test sets up a client to publish
	 * to the JMS Broker that is running ActiveMQ in Apache ServiceMix.
	 */
    @Test
    public void testActiveMQObservatoryClientGetCapabilities() {
        logger.debug("testActiveMQObservatoryClientGetCapabilities called");
        String wsaTo = "http://prey.mbari.org/d691724b-ebef-11dd-98dd-f9ada8061848";
        logger.debug("Web Services To Address is '" + wsaTo + "'");
        String wsaAction = "http://www.opengis.net/sps/1.0/GetCapabilities";
        logger.debug("Web Services Action is '" + wsaAction + "'");
        String gcMessage = OGCMessageFactory.buildGetCapabilitiesMessage();
        logger.debug("GetCapabilities message =\n" + gcMessage);
        if (activeMQClient != null) activeMQClient.soapSignAndSendXMLMessage(wsaTo, wsaAction, gcMessage);
        logger.debug("GetCapabilities sent");
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        assertEquals("The number of messages that the collector has is not correct", 1, activeMQMessageCollector.getIncomingMessages().size());
    }

    /**
	 * Test the observatory clients ability to send a GetCapabilities message to
	 * the inbox on the messaging broker. This test sets up a client to publish
	 * to the QPID Broker
	 */
    @Test
    public void testQPIDObservatoryClientGetCapabilities() {
        logger.debug("testQPIDObservatoryClientGetCapabilities called");
        String wsaTo = "http://cetus.mbari.org/d691724b-ebef-11dd-98dd-f9ada8061848";
        logger.debug("Web Services To Address is '" + wsaTo + "'");
        String wsaAction = "http://www.opengis.net/sps/1.0/GetCapabilities";
        logger.debug("Web Services Action is '" + wsaAction + "'");
        String gcMessage = OGCMessageFactory.buildGetCapabilitiesMessage();
        logger.debug("GetCapabilities message =\n" + gcMessage);
        if (qpidClient != null) qpidClient.soapSignAndSendXMLMessage(wsaTo, wsaAction, gcMessage);
        logger.debug("GetCapabilities sent");
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        assertEquals("The number of messages that the collector has is not correct", 1, qpidMessageCollector.getIncomingMessages().size());
    }
}
