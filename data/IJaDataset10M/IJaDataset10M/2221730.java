package uk.nominet.dnsjnio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import org.xbill.DNS.Flags;
import org.xbill.DNS.Message;
import uk.nominet.dnsjnio.NonblockingResolver;
import org.xbill.DNS.ResolverListener;

/**
 * This class provides communication over a single port.
 * Each Resolver will run on a different port, which has one TCP and one UDP connection to a server.
 * When a query comes in, we need to check status of current Connection
 * Either reuse it or reopen it.
 * If a query needs to sent with a header ID which is currently in use on this port, then a new standard Transaction object is used on a new port.
 * When a query ends (response or timeout) then the numQueries should be decremented and the Connection closed if numQueries == 0.
 */
public class SinglePortTransactionController extends AbstractTransaction {

    private Map tcpQueryDataMap = new HashMap();

    private Map udpQueryDataMap = new HashMap();

    private TCPConnection tcpConnection;

    private UDPConnection udpConnection;

    protected InetSocketAddress remoteAddress;

    protected InetSocketAddress localAddress;

    public boolean headerIdNotInUse(int id) {
        synchronized (tcpQueryDataMap) {
            if (tcpQueryDataMap.keySet().contains(new Integer(id))) {
                return false;
            }
        }
        synchronized (udpQueryDataMap) {
            if (udpQueryDataMap.keySet().contains(new Integer(id))) {
                return false;
            }
        }
        return true;
    }

    static int udpOpenedCount = 0;

    static int udpOpeningCount = 0;

    /**
     * Instantiate a new Connection, and start the connect process.
     */
    protected void startConnect(QueryData qData) {
        startTimer(qData);
        if (qData.isTcp()) {
            synchronized (tcpQueryDataMap) {
                tcpQueryDataMap.put(new Integer(qData.getQuery().getHeader().getID()), qData);
            }
            if (tcpConnection != null) {
                qData.setConnection(tcpConnection);
                if (tcpConnection.getState() == Connection.State.OPENING) {
                    return;
                }
                if (tcpConnection.getState() == Connection.State.OPENED) {
                    readyToSend(tcpConnection);
                    return;
                } else if (tcpConnection.getState() == Connection.State.CLOSING) {
                    return;
                } else if (tcpConnection.getState() == Connection.State.CLOSED) {
                    getNewTcpConnection(qData);
                }
            } else {
                getNewTcpConnection(qData);
            }
        } else {
            synchronized (udpQueryDataMap) {
                udpQueryDataMap.put(new Integer(qData.getQuery().getHeader().getID()), qData);
            }
            if (udpConnection != null && !(udpConnection.getState() == Connection.State.CLOSED)) {
                qData.setConnection(udpConnection);
                if (udpConnection.getState() == Connection.State.OPENING) {
                    return;
                }
                if (udpConnection.getState() == Connection.State.OPENED) {
                    readyToSend(udpConnection);
                    return;
                } else if (udpConnection.getState() == Connection.State.CLOSING) {
                    return;
                }
            } else {
                getNewUdpConnection(qData);
            }
        }
        qData.getConnection().connect(remoteAddress, localAddress);
    }

    private void getNewTcpConnection(QueryData qData) {
        tcpConnection = new TCPConnection(this, Connection.SINGLE_PORT_BUFFER_SIZE);
        qData.setConnection(tcpConnection);
    }

    private void getNewUdpConnection(QueryData qData) {
        udpConnection = new SinglePortUDPConnection(this, localAddress.getPort());
        qData.setConnection(udpConnection);
    }

    public void setRemoteAddress(InetSocketAddress addr) {
        this.remoteAddress = addr;
    }

    public void setLocalAddress(InetSocketAddress addr) {
        this.localAddress = addr;
    }

    public SinglePortTransactionController(InetSocketAddress remoteAddr, InetSocketAddress localAddr) {
        this.remoteAddress = remoteAddr;
        this.localAddress = localAddr;
        synchronized (tcpQueryDataMap) {
            tcpQueryDataMap = new HashMap();
        }
        synchronized (udpQueryDataMap) {
            udpQueryDataMap = new HashMap();
        }
    }

    /**
     * Send a query. This kicks off the whole process.
     * @param qData
     * @param id
     * @param responseQueue
     * @param endTime
     */
    public void sendQuery(QueryData qData, Object id, ResponseQueue responseQueue, long endTime) {
        qData.setResponseQueue(responseQueue);
        qData.setId(id);
        qData.setEndTime(endTime);
        startConnect(qData);
    }

    /**
     * Send a query using a ResolverListener. This kicks off the whole process.
     * @param qData
     * @param id
     * @param listener
     * @param endTime
     */
    public void sendQuery(QueryData qData, Object id, ResolverListener listener, long endTime) {
        qData.setListener(listener);
        qData.setId(id);
        qData.setEndTime(endTime);
        startConnect(qData);
    }

    /**
     * ResponseQueue a callback at the timeout time
     */
    private void startTimer(QueryData qData) {
        Timer.addTimeout(qData.getEndTime(), this, qData);
    }

    /**
     * Disconnect.
     */
    protected boolean disconnect(QueryData qData) {
        Map queryMap = getQueryDataMap(qData.getConnection());
        boolean disconnect = false;
        synchronized (queryMap) {
            queryMap.remove(new Integer(qData.getQuery().getHeader().getID()));
            if (queryMap.size() == 0) {
                disconnect = true;
            }
        }
        if (disconnect) {
            disconnect(qData.getConnection());
        }
        return true;
    }

    /**
     * Called to say that we are readyToSend.
     * We can now send the data.
     */
    public void readyToSend(Connection connection) {
        QueryData qData = null;
        do {
            qData = getNextQueryData(connection);
            if (qData != null) {
                qData.setSent(true);
                sendQuery(connection, qData.getQuery());
            }
        } while (qData != null);
    }

    private QueryData getNextQueryData(Connection c) {
        Map queryMap = getQueryDataMap(c);
        synchronized (queryMap) {
            for (Iterator it = queryMap.values().iterator(); it.hasNext(); ) {
                QueryData qData = (QueryData) (it.next());
                if (!(qData.isSent()) && (qData.getConnection() == c)) {
                    return qData;
                }
            }
        }
        return null;
    }

    private Map getQueryDataMap(Connection c) {
        Map queryMap = udpQueryDataMap;
        if ((tcpConnection != null) && (c.equals(tcpConnection))) {
            queryMap = tcpQueryDataMap;
        }
        return queryMap;
    }

    /**
     * The Connection has been closed
     * @param connection
     */
    public void closed(Connection connection) {
        Map queryMap = getQueryDataMap(connection);
        boolean reconnect = false;
        synchronized (queryMap) {
            if (queryMap.size() != 0) {
                reconnect = true;
            }
        }
        if (reconnect) {
            connection.connect(remoteAddress, localAddress);
        }
    }

    /**
     * A packet is available. Decode it and act accordingly.
     * If the packet is truncated over UDP, and ignoreTruncation
     * is false, then a tcp query is run to return the whole response.
     * @param data
     */
    public void dataAvailable(byte[] data, Connection connection) {
        try {
            Message message = NonblockingResolver.parseMessage(data);
            QueryData qData = null;
            Map queryMap = getQueryDataMap(connection);
            synchronized (queryMap) {
                qData = (QueryData) (queryMap.get(new Integer(message.getHeader().getID())));
            }
            if (qData == null) {
                return;
            }
            disconnect(qData);
            NonblockingResolver.verifyTSIG(qData.getQuery(), message, data, qData.getTsig());
            if (!qData.isTcp() && !qData.isIgnoreTruncation() && message.getHeader().getFlag(Flags.TC)) {
                cancelTimer(qData);
                qData.setTcp(true);
                startConnect(qData);
                return;
            }
            returnResponse(message, qData);
        } catch (IOException e) {
            return;
        }
    }

    /**
     * Return the response to the listener
     * @param message the response
     */
    private void returnResponse(Message message, QueryData qData) {
        if (!qData.isAnswered()) {
            qData.setAnswered(true);
            cancelTimer(qData);
            returnResponse(qData.getListener(), qData.getResponseQueue(), message, qData.getId());
        }
    }

    /**
     * Throw an Exception to the listener
     */
    protected void returnException(Exception e, QueryData qData) {
        if (!qData.isAnswered()) {
            qData.setAnswered(true);
            cancelTimer(qData);
            returnException(qData.getListener(), qData.getResponseQueue(), e, qData.getId());
        }
    }

    /**
     * Cancel the timeout callback
     * Also removes the QueryData from the list
     */
    private void cancelTimer(QueryData qData) {
        Timer.cancelTimeout(this, qData);
        qData.setResponded(true);
        Map queryMap = getQueryDataMap(qData.getConnection());
        synchronized (queryMap) {
            queryMap.remove(new Integer(qData.getQuery().getHeader().getID()));
        }
    }
}
