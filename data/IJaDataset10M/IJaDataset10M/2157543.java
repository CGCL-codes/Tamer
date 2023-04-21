package ocsf.server;

import java.io.IOException;
import java.io.InterruptedIOException;
import java.net.ServerSocket;
import java.net.Socket;

/**
* The <code> AbstractServer </code> class maintains a thread that waits
* for connection attempts from clients. When a connection attempt occurs
* it creates a new <code> ConnectionToClient </code> instance which
* runs as a thread. When a client is thus connected to the
* server, the two programs can then exchange <code> Object </code>
* instances.<p>
*
* Method <code> handleMessageFromClient </code> must be defined by
* a concrete subclass. Several other hook methods may also be
* overriden.<p>
*
* Several public service methods are provided to applications that use
* this framework, and several hook methods are also available<p>
*
* The modifications made to this class in version 2.2 are:
* <ul>
* <li> The synchronization of the <code>close()</code> method
* is now limited to the client threads closing sequence. The
* call to <code>serverClosed()</code> is outside the synchronized
* block and is preceeded by a join that garantees that
* <code>serverStopped()</code> is always called before.
* <li> Method <code>isClosed()</code> has been added.
* <li> When a client is accepted, the corresponding
* connection thread will be created only if the server
* has not been stopped.
* </ul>
* The modifications made to this class in version 2.3 are:
* <ul>
* <li> An instance variable refering to the current connection
* factory. Refer to null value by default, in this case regular
* <code>ConnectionToClient</code> instances are created as in the
* previous versions. 
* <li> Method <code>setConnectionFactory()</code> has been added.
* <li> In the run method, a call to the connection factory
* is made if such a factory is available.  
* <li> Method <code>handleMessageFromClient</code> is not always
* called depending on the value returned by the 
* <code>handleMessageFromClient</code> of the <code>ConnectionToClient</code>
* class.
* <li> The <code>clientException</code> method is still the one called when
* an exception is thrown when handling the connection with one client. However
* <code>ClassNotFoundException</code> and <code>RuntimeException</code> instances
* can now be received.  
* <li> The call to <code>serverStopped()</code> has been moved in 
* the <code>run</code> method.
* <li> Method <code>isListening()</code> has been modified.
* <li> Instance variable <code>readToStop</code> is now initialized to <code>true</code>
* </ul><p>
*
* Project Name: OCSF (Object Client-Server Framework)<p>
*
* @author Dr Robert Lagani&egrave;re
* @author Dr Timothy C. Lethbridge
* @author Fran&ccedil;ois B&eacute;langer
* @author Paul Holden
* @version December 2003 (2.31)
* @see com.lloseng.ocsf.server.ConnectionToClient
* @see com.lloseng.ocsf.server.AbstractConnectionFactory
*/
public abstract class AbstractServer implements Runnable {

    /**
   * The server socket: listens for clients who want to connect.
   */
    private ServerSocket serverSocket = null;

    /**
   * The connection listener thread.
   */
    private Thread connectionListener = null;

    /**
   * The port number
   */
    private int port;

    /**
   * The server timeout while for accepting connections.
   * After timing out, the server will check to see if a command to
   * stop the server has been issued; it not it will resume accepting
   * connections.
   * Set to half a second by default.
   */
    private int timeout = 500;

    /**
   * The maximum queue length; i.e. the maximum number of clients that
   * can be waiting to connect.
   * Set to 10 by default.
   */
    private int backlog = 10;

    /**
   * The thread group associated with client threads. Each member of the
   * thread group is a <code> ConnectionToClient </code>.
   */
    private ThreadGroup clientThreadGroup;

    /**
   * Indicates if the listening thread is ready to stop.  Set to
   * false by default.
   */
    private boolean readyToStop = true;

    /**
   * The factory used to create new connections to clients.
   * Is null by default, meaning that regular <code>ConnectionToClient</code>
   * instances will be created. Added in version 2.3
   */
    private AbstractConnectionFactory connectionFactory = null;

    /**
   * Constructs a new server.
   *
   * @param port the port number on which to listen.
   */
    public AbstractServer(int port) {
        this.port = port;
        this.clientThreadGroup = new ThreadGroup("ConnectionToClient threads") {

            @Override
            public void uncaughtException(Thread thread, Throwable exception) {
                if (thread instanceof ConnectionToClient) {
                    clientException((ConnectionToClient) thread, exception);
                }
            }
        };
    }

    /**
   * Begins the thread that waits for new clients.
   * If the server is already in listening mode, this
   * call has no effect.
   *
   * @exception IOException if an I/O error occurs
   * when creating the server socket.
   */
    public final void listen() throws IOException {
        if (!isListening()) {
            if (serverSocket == null) {
                serverSocket = new ServerSocket(getPort(), backlog);
            }
            serverSocket.setSoTimeout(timeout);
            connectionListener = new Thread(this);
            connectionListener.start();
        }
    }

    /**
   * Causes the server to stop accepting new connections.
   */
    public final void stopListening() {
        readyToStop = true;
    }

    /**
   * Closes the server socket and the connections with all clients.
   * Any exception thrown while closing a client is ignored.
   * If one wishes to catch these exceptions, then clients
   * should be individually closed before calling this method.
   * The method also stops listening if this thread is running.
   * If the server is already closed, this
   * call has no effect.
   *
   * @exception IOException if an I/O error occurs while
   * closing the server socket.
   */
    public final void close() throws IOException {
        if (serverSocket == null) return;
        stopListening();
        try {
            serverSocket.close();
        } finally {
            synchronized (this) {
                Thread[] clientThreadList = getClientConnections();
                for (int i = 0; i < clientThreadList.length; i++) {
                    try {
                        ((ConnectionToClient) clientThreadList[i]).close();
                    } catch (Exception ex) {
                    }
                }
                serverSocket = null;
            }
            try {
                connectionListener.join();
            } catch (InterruptedException ex) {
            } catch (NullPointerException ex) {
            }
            serverClosed();
        }
    }

    /**
   * Sends a message to every client connected to the server.
   * This is merely a utility; a subclass may want to do some checks
   * before actually sending messages to all clients.
   * This method can be overriden, but if so it should still perform
   * the general function of sending to all clients, perhaps after some kind
   * of filtering is done. Any exception thrown while
   * sending the message to a particular client is ignored.
   *
   * @param msg   Object The message to be sent
   */
    public void sendToAllClients(Object msg) {
        Thread[] clientThreadList = getClientConnections();
        for (int i = 0; i < clientThreadList.length; i++) {
            try {
                ((ConnectionToClient) clientThreadList[i]).sendToClient(msg);
            } catch (Exception ex) {
            }
        }
    }

    /**
   * Returns true if the server is ready to accept new clients.
   *
   * @return true if the server is listening.
   */
    public final boolean isListening() {
        return connectionListener != null && connectionListener.isAlive();
    }

    /**
   * Returns true if the server is closed.
   *
   * @return true if the server is closed.
   * @since version 2.2
   */
    public final boolean isClosed() {
        return (serverSocket == null);
    }

    /**
   * Returns an array containing the existing
   * client connections. This can be used by
   * concrete subclasses to implement messages that do something with
   * each connection (e.g. kill it, send a message to it etc.).
   * Remember that after this array is obtained, some clients
   * in this migth disconnect. New clients can also connect,
   * these later will not appear in the array.
   *
   * @return an array of <code>Thread</code> containing
   * <code>ConnectionToClient</code> instances.
   */
    public final synchronized Thread[] getClientConnections() {
        Thread[] clientThreadList = new Thread[clientThreadGroup.activeCount()];
        clientThreadGroup.enumerate(clientThreadList);
        return clientThreadList;
    }

    /**
   * Counts the number of clients currently connected.
   *
   * @return the number of clients currently connected.
   */
    public final int getNumberOfClients() {
        return clientThreadGroup.activeCount();
    }

    /**
   * Returns the port number.
   *
   * @return the port number.
   */
    public final int getPort() {
        return port;
    }

    /**
   * Sets the port number for the next connection.
   * The server must be closed and restarted for the port
   * change to be in effect.
   *
   * @param port the port number.
   */
    public final void setPort(int port) {
        this.port = port;
    }

    /**
   * Sets the timeout time when accepting connections.
   * The default is half a second. This means that stopping the
   * server may take up to timeout duration to actually stop.
   * The server must be stopped and restarted for the timeout
   * change to be effective.
   *
   * @param timeout the timeout time in ms.
   */
    public final void setTimeout(int timeout) {
        this.timeout = timeout;
    }

    /**
   * Sets the maximum number of waiting connections accepted by the
   * operating system. The default is 20.
   * The server must be closed and restarted for the backlog
   * change to be in effect.
   *
   * @param backlog the maximum number of connections.
   */
    public final void setBacklog(int backlog) {
        this.backlog = backlog;
    }

    /**
   * Sets the connection factory.
   * Once set, this one will be used in the creation
   * of new <code>ConnectionToClient</code> instances.
   * The call to this method is optional; if not called
   * Then regular <code>ConnectionToClient</code> instances
   * are created. Added in version 2.3
   *
   * @param factory the connection factory.
   */
    public final synchronized void setConnectionFactory(AbstractConnectionFactory factory) {
        this.connectionFactory = factory;
    }

    /**
   * Runs the listening thread that allows clients to connect.
   * Not to be called.
   */
    public final void run() {
        readyToStop = false;
        serverStarted();
        try {
            while (!readyToStop) {
                try {
                    Socket clientSocket = serverSocket.accept();
                    synchronized (this) {
                        if (connectionFactory == null) {
                            new ConnectionToClient(this.clientThreadGroup, clientSocket, this);
                        } else {
                            connectionFactory.createConnection(this.clientThreadGroup, clientSocket, this);
                        }
                    }
                } catch (InterruptedIOException exception) {
                }
            }
        } catch (IOException exception) {
            if (!readyToStop) {
                listeningException(exception);
            }
        } finally {
            readyToStop = true;
            connectionListener = null;
            serverStopped();
        }
    }

    /**
   * Hook method called each time a new client connection is
   * accepted. The default implementation does nothing.
   * This method does not have to be synchronized since only
   * one client can be accepted at a time.
   *
   * @param client the connection connected to the client.
   */
    protected void clientConnected(ConnectionToClient client) {
    }

    /**
   * Hook method called each time a client disconnects.
   * The client is garantee to be disconnected but the thread
   * is still active until it is asynchronously removed from the thread group. 
   * The default implementation does nothing. The method
   * may be overridden by subclasses but should remains synchronized.
   *
   * @param client the connection with the client.
   */
    protected synchronized void clientDisconnected(ConnectionToClient client) {
    }

    /**
   * Hook method called each time an exception is thrown in a
   * ConnectionToClient thread.
   * The method may be overridden by subclasses but should remains
   * synchronized. 
   * Most exceptions will cause the end of the client's thread except for
   * <code>ClassNotFoundException</code>s received when an object of
   * unknown class is received and for the <code>RuntimeException</code>s
   * that can be thrown by the message handling method implemented by the user.
   *
   * @param client the client that raised the exception.
   * @param exception the exception thrown.
   */
    protected synchronized void clientException(ConnectionToClient client, Throwable exception) {
    }

    /**
   * Hook method called when the server stops accepting
   * connections because an exception has been raised.
   * The default implementation does nothing.
   * This method may be overriden by subclasses.
   *
   * @param exception the exception raised.
   */
    protected void listeningException(Throwable exception) {
    }

    /**
   * Hook method called when the server starts listening for
   * connections.  The default implementation does nothing.
   * The method may be overridden by subclasses.
   */
    protected void serverStarted() {
    }

    /**
   * Hook method called when the server stops accepting
   * connections.  The default implementation
   * does nothing. This method may be overriden by subclasses.
   */
    protected void serverStopped() {
    }

    /**
   * Hook method called when the server is closed.
   * The default implementation does nothing. This method may be
   * overriden by subclasses. When the server is closed while still
   * listening, serverStopped() will also be called.
   */
    protected void serverClosed() {
    }

    /**
   * Handles a command sent from one client to the server.
   * This MUST be implemented by subclasses, who should respond to
   * messages.
   * This method is called by a synchronized method so it is also
   * implcitly synchronized.
   *
   * @param msg   the message sent.
   * @param client the connection connected to the client that
   *  sent the message.
   */
    protected abstract void handleMessageFromClient(Object msg, ConnectionToClient client);

    /**
   * Receives a command sent from the client to the server.
   * Called by the run method of <code>ConnectionToClient</code>
   * instances that are watching for messages coming from the server
   * This method is synchronized to ensure that whatever effects it has
   * do not conflict with work being done by other threads. The method
   * simply calls the <code>handleMessageFromClient</code> slot method.
   *
   * @param msg   the message sent.
   * @param client the connection connected to the client that
   *  sent the message.
   */
    final synchronized void receiveMessageFromClient(Object msg, ConnectionToClient client) {
        this.handleMessageFromClient(msg, client);
    }
}
