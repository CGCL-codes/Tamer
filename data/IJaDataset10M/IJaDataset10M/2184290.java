package ch.squix.nataware.service.natdiscovery;

import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.UnknownHostException;
import java.util.concurrent.ExecutionException;
import org.apache.mina.handler.demux.MessageHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ch.squix.nataware.configuration.DefaultNATawareConfiguration;
import ch.squix.nataware.configuration.INATawareConfiguration;
import ch.squix.nataware.network.signaling.ServiceSocket;
import ch.squix.nataware.node.PublicNode;
import ch.squix.nataware.node.StringNodeID;
import junit.framework.TestCase;

public class NATDiscoveryClientTest extends TestCase {

    private static Logger logger = LoggerFactory.getLogger(NATDiscoveryClientTest.class);

    /**
	 * Creates a fake server with the chosen behavior
	 * and a client which performs NAT discovery with this server
	 */
    public void testClient(int serverBehavior) {
        INATawareConfiguration configuration = new DefaultNATawareConfiguration("client");
        ServiceSocket socket = new ServiceSocket(configuration);
        NATDiscoveryNetworkService NatService = new NATDiscoveryNetworkService(socket, null);
        FakeNATDiscoveryServer server = new FakeNATDiscoveryServer(serverBehavior);
        socket.addReceivedMessageHandler(NATDiscoveryMessage.class, server);
        socket.addSentMessageHandler(NATDiscoveryMessage.class, MessageHandler.NOOP);
        try {
            NatService.getServiceResult().get();
        } catch (InterruptedException e) {
            logger.warn("Interrupted exception during NAT Discovery :", e);
        } catch (ExecutionException e) {
            logger.warn("Execution exception during NAT Discovery :", e);
        }
        socket.close();
    }

    /**
	 * Tests the client in the normal case
	 */
    public void testClient0() {
        testClient(0);
    }

    /**
	 * Tests the client behind a simulated NAT
	 */
    public void testClient1() {
        testClient(1);
    }

    /**
	 * Tests the client with a non-answering server
	 */
    public void testClient2() {
        testClient(2);
    }

    /**
	 * Tests the client with a server sending unexpected answers
	 */
    public void testClient3() {
        testClient(3);
    }

    /**
	 * Tests the client with a server using wrong session ids
	 */
    public void testClient4() {
        testClient(4);
    }

    /**
	 * Creates a real NAT Discovery Service with a client that will try
	 * to contact a server at the following ip address
	 * The server should run NATDiscoveryServerTest.realServer() at the same time
	 */
    public void testRealClient() {
        PublicNode testNode = null;
        try {
            testNode = new PublicNode(new StringNodeID("server"), new InetSocketAddress(InetAddress.getByName("192.168.133.143"), 7878), 0);
        } catch (UnknownHostException e1) {
            logger.warn("Unknown host exception :", e1);
        }
        INATawareConfiguration configuration = new DefaultNATawareConfiguration("client", testNode);
        ServiceSocket socket = new ServiceSocket(configuration);
        NATDiscoveryNetworkService NatService = new NATDiscoveryNetworkService(socket, null);
        logger.debug("Starting NAT Discovery");
        try {
            NatService.getServiceResult().get();
        } catch (InterruptedException e) {
            logger.warn("Interrupted exception during NAT Discovery :", e);
        } catch (ExecutionException e) {
            logger.warn("Execution exception during NAT Discovery :", e);
        }
        socket.close();
    }
}
