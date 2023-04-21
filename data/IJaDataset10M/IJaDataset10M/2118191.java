package net.internetrail.network;

import java.io.IOException;
import java.net.Socket;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

/** <p>A message dispatcher for the server side.</p>
 * 
 * @author Bjoern Wuest, Germany
 */
public final class CServerMessageDispatcher extends AMessageDispatcher {

    /** <p>A collection of all registered server connection handlers.</p> */
    private final Map<Short, CServerConnectionHandler> m_ConnectionHandlers = new HashMap<Short, CServerConnectionHandler>();

    /** <p>The next address to assign to a communication handler.</p> */
    private short m_NextAddress = Short.MIN_VALUE;

    /** <p>A locking object to prevent concurrent use.</p> */
    private final Object m_Lock = new Object();

    /** <p>Default constructor.</p> */
    public CServerMessageDispatcher() {
        p_LOG = Logger.getLogger(getClass().getName());
    }

    /** <p>Add a new connection between server and mediator.</p>
	 * 
	 * <p>This method will create a new, free address for the connection and
	 * instantiates {@link CServerConnectionHandler} to take care of the
	 * connection.</p>
	 * 
	 * @param Sock The socket of this connection.
	 * @throws IOException If there is any problem with the socket.
	 */
    public void addConnection(Socket Sock) throws IOException {
        synchronized (m_Lock) {
            synchronized (m_ConnectionHandlers) {
                while (m_ConnectionHandlers.containsKey(m_NextAddress)) {
                    m_NextAddress++;
                }
                m_ConnectionHandlers.put(m_NextAddress, new CServerConnectionHandler(this, Sock, m_NextAddress));
            }
            m_NextAddress++;
        }
    }

    /** <p>Removes a connection from this dispatcher.</p>
	 * 
	 * @param Address The address of the connection to remove.
	 */
    public void removeConnection(short Address) {
        synchronized (m_ConnectionHandlers) {
            m_ConnectionHandlers.remove(Address);
        }
    }

    @Override
    public void sendMessage(IMessage Message) throws EMessageTooLarge, EMessageSending {
        CServerConnectionHandler handler;
        synchronized (m_ConnectionHandlers) {
            handler = m_ConnectionHandlers.get(((CServerMessage) Message).getMediatorAddress());
        }
        if (handler != null) {
            handler.sendMessage(Message);
        }
    }

    @Override
    public void dispatch(IMessage Message) {
        AService service;
        synchronized (p_Services) {
            service = p_Services.get(Message.getReceiverServiceAddress());
        }
        if (service != null) {
            service.processMessage(Message);
        }
    }

    /** <p>Broadcast the message to all mediator connection handlers.</p>
	 * 
	 * @param Message The message to broadcast.
	 * @throws EMessageTooLarge If the message to sent is too large. Maybe some
	 * mediators may have already received the message.
	 */
    public void broadcastMessage(IMessage Message) throws EMessageTooLarge {
        Collection<CServerConnectionHandler> connections;
        synchronized (m_ConnectionHandlers) {
            connections = m_ConnectionHandlers.values();
        }
        for (CServerConnectionHandler current : connections) {
            try {
                current.sendMessage(Message);
            } catch (EMessageSending Ignore) {
            }
        }
    }

    @Override
    protected void p_Shutdown() {
        synchronized (m_Lock) {
            synchronized (m_ConnectionHandlers) {
                for (CServerConnectionHandler current : m_ConnectionHandlers.values()) {
                    try {
                        current.shutdown();
                    } catch (Throwable T) {
                        p_LOG.throwing("net.internetrail.network.CServerMessageDispatcher", "p_Shutdown()", T);
                    }
                }
                m_ConnectionHandlers.clear();
            }
        }
        super.p_Shutdown();
    }
}
