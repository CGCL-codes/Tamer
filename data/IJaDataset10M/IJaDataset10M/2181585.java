package com.kenmccrary.jtella;

import java.util.Collections;
import java.util.List;
import java.util.LinkedList;
import java.io.IOException;
import com.kenmccrary.jtella.NodeConnection;
import com.kenmccrary.jtella.util.Log;

/**
 *  A session for initiating searches on the network
 *
 */
public class SearchSession {

    private MessageReceiver receiver;

    private GNUTellaConnection connection;

    private Router router;

    private SendThread sendThread;

    private String query;

    private int queryType;

    private int minSpeed;

    private List<GUID> searchGUIDList;

    public SearchSession(String query, int queryType, int maxResults, int minSpeed, GNUTellaConnection connection, Router router, MessageReceiver receiver) {
        this.connection = connection;
        this.receiver = receiver;
        this.router = router;
        this.query = query;
        this.queryType = queryType;
        this.minSpeed = minSpeed;
        searchGUIDList = Collections.synchronizedList(new LinkedList<GUID>());
        sendThread = new SendThread();
        sendThread.start();
    }

    /**
	 * Fetches the query for this search session.
	 * @return	The query for this search session
	 */
    public String getQuery() {
        return query;
    }

    /**
	 * Fetches the query type for this search session.
	 * @return	The query type for this search session (see constants in SearchMessage)
	 */
    public int getQueryType() {
        return queryType;
    }

    /**
	 * Fetches the minimum accepted response speed for this search session.
	 * @return	The minimum accepted response speed for this search session
	 */
    public int getMinSpeed() {
        return minSpeed;
    }

    /**
	 * Generates the Gnutella message that should be sent to the network for this query.
	 * @return The Gnutella message that should be sent to the network for this query
	 */
    protected Message generateNetworkMessage() {
        return new SearchMessage(getQuery(), getQueryType(), getMinSpeed());
    }

    /**
	 *   Request a replying servant push a file
	 *
	 *   @param searchReplyMessage search reply containing file to push
	 *   @param pushMessage push message
	 *   @return true if the message could be sent
	 */
    public static boolean sendPushMessage(SearchReplyMessage searchReplyMessage, PushMessage pushMessage) {
        Log.getLog().logDebug("In sendPushmessage");
        Connection connection = searchReplyMessage.getOriginatingConnection();
        System.out.println("qr connection status: " + connection.getStatus());
        if (connection.getStatus() == NodeConnection.STATUS_OK) {
            try {
                Log.getLog().logDebug("Sending push");
                connection.prioritySend(pushMessage);
                return true;
            } catch (IOException io) {
            }
        }
        return false;
    }

    /**
	 *  Close the session, ignore future query hits
	 *
	 */
    public void close() {
        router.removeMessageSender(searchGUIDList);
        sendThread.shutdown();
    }

    /**
	 *  Continuously monitors for newly formed active connections to send
	 *  to
	 *
	 */
    class SendThread extends Thread {

        private LinkedList<NodeConnection> sentConnections;

        private boolean shutdown;

        SendThread() {
            super("SearchSession$SendThread");
            sentConnections = new LinkedList<NodeConnection>();
            shutdown = false;
        }

        /**
		 *  Cease operation
		 */
        void shutdown() {
            shutdown = true;
            interrupt();
        }

        /**
		 *  Working loop
		 *
		 */
        public void run() {
            while (!shutdown) {
                try {
                    List<NodeConnection> activeList = connection.getConnections().getActiveConnections();
                    activeList.removeAll(sentConnections);
                    for (int i = 0; i < activeList.size(); i++) {
                        NodeConnection nodeConnection = (NodeConnection) activeList.get(i);
                        Message searchMessage = generateNetworkMessage();
                        searchGUIDList.add(searchMessage.getGUID());
                        nodeConnection.sendAndReceive(searchMessage, receiver);
                        sentConnections.add(nodeConnection);
                    }
                    sleep(5000);
                } catch (Exception e) {
                }
            }
        }
    }
}
