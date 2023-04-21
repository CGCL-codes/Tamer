package ch.squix.nataware.service.directory;

import java.net.SocketAddress;
import java.util.LinkedList;
import java.util.concurrent.Future;
import org.apache.mina.handler.demux.MessageHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ch.squix.nataware.network.signaling.Connector;
import ch.squix.nataware.network.signaling.IServiceSocket;
import ch.squix.nataware.node.INode;
import ch.squix.nataware.node.IRendezvousNodes;
import ch.squix.nataware.node.Node;
import ch.squix.nataware.service.AbstractNetworkService;
import ch.squix.nataware.service.rendezvous.RendezVousNetworkService;

public class DirectoryNetworkService extends AbstractNetworkService<DirectoryResult> {

    private static Logger logger = LoggerFactory.getLogger(DirectoryNetworkService.class);

    private DirectoryServerMessageHandler server;

    private DirectoryClientMessageHandler client;

    private Connector directoryConnection;

    /**
	 * Creates the directory service
	 * @param serviceSocket
	 * @param rendezvousNodes
	 * @param rdv = rendez vous service (must be created before the directory if this is the directory, null otherwise)
	 * @param directory = Node object corresponding to the directory (null if we are not the directory)
	 */
    public DirectoryNetworkService(IServiceSocket serviceSocket, IRendezvousNodes rendezvousNodes, RendezVousNetworkService rdv, Node directory) {
        super(serviceSocket, rendezvousNodes);
        if (getServiceSocketConfig().isDirectory()) {
            logger.info("Starting Directory Server");
            server = new DirectoryServerMessageHandler(rdv, directory);
            addReceivedMessageHandler(DirectoryMessage.class, server);
            addSentMessageHandler(DirectoryMessage.class, MessageHandler.NOOP);
        }
    }

    /**
	 * Opens an uninterruptible connection to the directory server
	 * @param a = address of the server
	 * @return if the connection has succeeded
	 */
    public boolean connectToDirectory(SocketAddress a) {
        client = new DirectoryClientMessageHandler();
        directoryConnection = new Connector(a, client);
        if (!directoryConnection.start()) {
            logger.warn("The directory server is unavailable");
            return false;
        }
        directoryConnection.getSession().getConfig().setBothIdleTime(3600);
        logger.info("Connection established to the directory server");
        return true;
    }

    /**
	 * Registers to the directory
	 * @param id = id we want to use
	 * @return if the directory accepted us
	 */
    public boolean joinNetwork(String id) {
        logger.info("Registering to the directory");
        client.setReceivedAck(false);
        DirectoryMessage message = new DirectoryMessage(DirectoryMessageType.NEW_NODE, id);
        directoryConnection.write(message);
        long begin = System.currentTimeMillis();
        while (!client.hasReceivedAck() && (System.currentTimeMillis() - begin < 3000)) {
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                logger.error("Interrupted exception", e);
            }
        }
        if (!client.hasReceivedAck()) {
            logger.warn("The directory did not answer");
            return false;
        } else if (!client.isCorrectAnswer()) {
            logger.warn("ID already used, please choose another one");
            return false;
        }
        logger.info("Successfully registered to the directory");
        return true;
    }

    /**
	 * Updates information about us
	 * @param n = Node object corresponding to us
	 * @return if the directory correctly processed the information
	 */
    public boolean updateInfo(Node n) {
        logger.info("Updating the directory");
        client.setReceivedAck(false);
        DirectoryMessage message = new DirectoryMessage(DirectoryMessageType.NODE_UPDATE, n);
        directoryConnection.write(message);
        long begin = System.currentTimeMillis();
        while (!client.hasReceivedAck() && (System.currentTimeMillis() - begin < 3000)) {
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                logger.error("Interrupted exception", e);
            }
        }
        if (!client.hasReceivedAck()) {
            logger.warn("The directory did not answer");
            return false;
        }
        logger.info("Successfully updated the directory");
        return true;
    }

    /**
	 * Requests the list of all nodes to the directory
	 * @return the list of known nodes, or null if the directory did not answer
	 */
    public LinkedList<String> listOfNodes() {
        logger.info("Getting the list of known nodes");
        client.setReceivedAnswer(false);
        DirectoryMessage message = new DirectoryMessage(DirectoryMessageType.GET_LIST);
        directoryConnection.write(message);
        long begin = System.currentTimeMillis();
        while (!client.hasReceivedAnswer() && (System.currentTimeMillis() - begin < 3000)) {
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                logger.error("Interrupted exception", e);
            }
        }
        if (!client.hasReceivedAnswer()) {
            logger.warn("The directory did not answer");
        } else {
            logger.info("Received the list of known nodes from the directory");
        }
        return client.getList();
    }

    /**
	 * Requests information about one node
	 * @return the node object, or null if the directory did not answer
	 */
    public INode requestNode(String id) {
        logger.info("Asking information about node " + id);
        client.setReceivedAnswer(false);
        DirectoryMessage message = new DirectoryMessage(DirectoryMessageType.NODE_REQUEST, id);
        directoryConnection.write(message);
        long begin = System.currentTimeMillis();
        while (!client.hasReceivedAnswer() && (System.currentTimeMillis() - begin < 3000)) {
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                logger.error("Interrupted exception", e);
            }
        }
        if (!client.hasReceivedAnswer()) {
            logger.warn("The directory did not answer");
            return null;
        }
        if (!client.isCorrectAnswer()) {
            logger.warn("The directory did not know that node id");
            return null;
        }
        logger.info("Received information about the node");
        return client.getNode();
    }

    /**
	 * Stops the connection to the directory
	 */
    public void stop() {
        if (!getServiceSocketConfig().isDirectory()) {
            directoryConnection.close();
        }
    }

    @Override
    public Future<DirectoryResult> getServiceResult() {
        return null;
    }
}
