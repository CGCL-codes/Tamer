package com.pentagaia.tb.net.impl;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.LinkedList;
import java.util.concurrent.ExecutionException;
import java.util.logging.Level;
import java.util.logging.Logger;
import com.pentagaia.tb.net.api.INetClientHandler;
import com.pentagaia.tb.net.api.INetClientInfo;
import com.pentagaia.tb.net.api.INetMessageReceiver;
import com.pentagaia.tb.net.api.INetMessageSender;
import com.pentagaia.tb.net.api.INetSession;
import com.pentagaia.tb.net.impl.NetworkServer.CommitAction;
import com.sun.sgs.auth.Identity;
import com.sun.sgs.impl.sharedutil.HexDumper;
import com.sun.sgs.impl.sharedutil.LoggerWrapper;
import com.sun.sgs.impl.sharedutil.MessageBuffer;
import com.sun.sgs.impl.util.AbstractKernelRunnable;
import com.sun.sgs.kernel.KernelRunnable;
import com.sun.sgs.kernel.TaskQueue;
import com.sun.sgs.nio.channels.AsynchronousSocketChannel;
import com.sun.sgs.nio.channels.ClosedAsynchronousChannelException;
import com.sun.sgs.nio.channels.CompletionHandler;
import com.sun.sgs.nio.channels.IoFuture;
import com.sun.sgs.nio.channels.ReadPendingException;

/**
 * A handler to accept incoming client connections
 * 
 * @author mepeisen
 * @version 0.1.0
 * @since 0.1.0
 */
public class NetClientHandler implements INetMessageSender, INetClientHandler {

    /** The logger for this class. */
    private static final LoggerWrapper logger = new LoggerWrapper(Logger.getLogger(NetClientHandler.class.getName()));

    /** The current node's id */
    private final long fLocalNodeId;

    /** Network server associated with this connection */
    private final NetworkServer fServer;

    /** Client information */
    private final INetClientInfo fClient;

    /** Client channel/ network connection */
    private final AsynchronousSocketChannel fClientChannel;

    /** The session id */
    private long fSessionID;

    /** {@ode true} if the session was created and registered */
    private boolean fSessionCreated;

    /**
     * The message channel
     */
    private final INetMessageChannel fMessageChannel;

    /** The completion handler for reading from the I/O channel. */
    private volatile ReadHandler readHandler;

    /** The completion handler for writing to the I/O channel. */
    private volatile WriteHandler writeHandler;

    /** {@code true} if this handler is connected */
    private volatile boolean isConnected = true;

    /** {@code true} if this handler was disconnected */
    private volatile boolean disconnectHandled = false;

    /**
     * The lock for accessing the following fields: {@code state}, {@code messageQueue}, {@code disconnectHandled}, and {@code shutdown}.
     */
    private final Object lock = new Object();

    /** The task queue */
    private final TaskQueue taskQueue;

    /** {@code true} if there is a shutdown pending */
    private boolean shutdown;

    /** The default read buffer size: {@value #DEFAULT_READ_BUFFER_SIZE} */
    private static final int DEFAULT_READ_BUFFER_SIZE = 128 * 1024;

    /**
     * Constructor
     * 
     * @param server
     *            Reference to the network server
     * @param client
     *            The client channel
     * @param channel
     *            Network connection
     * 
     * @throws Exception
     *             thrown if there was a problem
     */
    public NetClientHandler(final NetworkServer server, final INetClientInfo client, final AsynchronousSocketChannel channel) throws Exception {
        this.readHandler = this.createConnectedReadHandler();
        this.writeHandler = this.createConnectedWriteHandler();
        this.fLocalNodeId = server.getLocalNodeId();
        this.fServer = server;
        this.fClient = client;
        this.fClientChannel = channel;
        this.fMessageChannel = this.wrap(this.fClientChannel);
        this.taskQueue = this.fServer.createTaskQueue();
        this.initialize();
    }

    /**
     * Creates the connected write handler
     * 
     * @return connected write handler
     */
    private WriteHandler createConnectedWriteHandler() {
        return new ConnectedWriteHandler();
    }

    /**
     * Creates the connected read handler
     * 
     * @return connected read handler
     */
    protected ReadHandler createConnectedReadHandler() {
        return new ConnectedReadHandler();
    }

    /**
     * Wraps the instance of socket channel into a message channel
     * 
     * @param clientChannel
     * 
     * @return message channel
     */
    protected INetMessageChannel wrap(final AsynchronousSocketChannel clientChannel) {
        return new NetAsynchronousMsgChannel(clientChannel, NetClientHandler.DEFAULT_READ_BUFFER_SIZE);
    }

    /**
     * Initializes this client handler.
     * 
     * Calls the following methods:<br /> {@code createSession()}<br /> {@code registerWithServer()}<br /> {@code listen()}<br /><br />
     * 
     * Overwrite to implement your own behavior. F.e. let the client handler ask for a login before you create a valid session.
     * 
     * @throws Exception
     *             thrown if there was a problem
     */
    protected void initialize() throws Exception {
        this.createSession();
        this.registerWithServer();
        this.listen();
    }

    /**
     * Creates a new session object and stores it into the session storage
     * 
     * @throws Exception
     *             thrown if there was a problem
     */
    protected void createSession() throws Exception {
        this.fSessionID = this.fServer.getIdGenerator().next();
        final INetMessageReceiver receiver = this.fServer.getReceiverFactory().createReceiver(this.fClient);
        this.fServer.getStorage().createSession(this.fLocalNodeId, this.fSessionID, this.fClient, receiver, this);
        this.fSessionCreated = true;
    }

    /**
     * Registers this client handler with the server
     */
    protected void registerWithServer() {
        this.fServer.addHandler(this.fSessionID, this);
    }

    /**
     * {@inheritDoc}
     * 
     * @see com.pentagaia.tb.net.api.INetClientHandler#listen()
     */
    public void listen() {
        this.enqueueReadResume();
    }

    /**
     * Destroys this handler and the closes the client connection
     */
    public void shutdown() {
        synchronized (this.lock) {
            if (this.shutdown == true) {
                return;
            }
            this.shutdown = true;
            this.disconnectHandled = true;
            if (this.fClientChannel != null) {
                try {
                    this.fClientChannel.close();
                } catch (final IOException e) {
                }
            }
        }
    }

    /**
     * Returns the session object
     * 
     * @return session object
     */
    protected final INetSession getSession() {
        if (this.fSessionCreated) {
            return this.fServer.getStorage().getSession(this.fSessionID);
        }
        throw new IllegalStateException("Illegal access: Session object was not created.");
    }

    /**
     * A completion handler for reading from a connection.
     */
    protected abstract class ReadHandler implements CompletionHandler<ByteBuffer, Void> {

        /**
         * Initiates the read request.
         */
        abstract void read();
    }

    /**
     * A completion handler for reading that always fails.
     */
    protected class ClosedReadHandler extends ReadHandler {

        /**
         * {@inheritDoc}
         * 
         * @see com.pentagaia.tb.net.impl.NetClientHandler.ReadHandler#read()
         */
        @Override
        void read() {
            throw new ClosedAsynchronousChannelException();
        }

        /**
         * {@inheritDoc}
         * 
         * @see com.sun.sgs.nio.channels.CompletionHandler#completed(com.sun.sgs.nio.channels.IoFuture)
         */
        public void completed(final IoFuture<ByteBuffer, Void> result) {
            throw new AssertionError("should be unreachable");
        }
    }

    /**
     * A completion handler for writing to a connection.
     */
    protected abstract class WriteHandler implements CompletionHandler<Void, Void> {

        /**
         * Writes the specified message.
         * 
         * @param message
         *            outgoing message
         */
        abstract void write(ByteBuffer message);
    }

    /**
     * A completion handler for writing that always fails.
     */
    protected class ClosedWriteHandler extends WriteHandler {

        /**
         * {@inheritDoc}
         * 
         * @see com.pentagaia.tb.net.impl.NetClientHandler.WriteHandler#write(java.nio.ByteBuffer)
         */
        @Override
        void write(final ByteBuffer message) {
            throw new ClosedAsynchronousChannelException();
        }

        /**
         * {@inheritDoc}
         * 
         * @see com.sun.sgs.nio.channels.CompletionHandler#completed(com.sun.sgs.nio.channels.IoFuture)
         */
        public void completed(final IoFuture<Void, Void> result) {
            throw new AssertionError("should be unreachable");
        }
    }

    /**
     * A completion handler for reading from the session's channel.
     */
    protected class ConnectedReadHandler extends ReadHandler {

        /**
         * The lock for accessing the {@code isReading} field. The locks {@code lock} and {@code readLock} should only be acquired in that specified
         * order.
         */
        private final Object readLock = new Object();

        /** Whether a read is underway. */
        private boolean isReading = false;

        /**
         * {@inheritDoc}
         * 
         * @see com.pentagaia.tb.net.impl.NetClientHandler.ReadHandler#read()
         */
        @Override
        void read() {
            synchronized (this.readLock) {
                if (this.isReading) {
                    throw new ReadPendingException();
                }
                this.isReading = true;
            }
            NetClientHandler.this.fMessageChannel.read(this);
        }

        /**
         * {@inheritDoc}
         * 
         * @see com.sun.sgs.nio.channels.CompletionHandler#completed(com.sun.sgs.nio.channels.IoFuture)
         */
        public final void completed(final IoFuture<ByteBuffer, Void> result) {
            synchronized (this.readLock) {
                this.isReading = false;
            }
            try {
                final ByteBuffer message = result.getNow();
                if (message == null) {
                    NetClientHandler.this.scheduleHandleDisconnect(false);
                    return;
                }
                if (NetClientHandler.logger.isLoggable(Level.FINEST)) {
                    NetClientHandler.logger.log(Level.FINEST, "completed read session:{0} message:{1}", NetClientHandler.this, HexDumper.format(message, 0x50));
                }
                final byte[] payload = new byte[message.remaining()];
                message.get(payload);
                this.bytesReceived(payload);
            } catch (final Exception e) {
                if (NetClientHandler.logger.isLoggable(Level.FINE)) {
                    NetClientHandler.logger.logThrow(Level.FINE, e, "Read completion exception {0}", NetClientHandler.this);
                }
                NetClientHandler.this.scheduleHandleDisconnect(false);
            }
        }

        /**
         * Processes the received message.
         * 
         * @param buffer
         */
        protected void bytesReceived(final byte[] buffer) {
            final MessageBuffer msg = new MessageBuffer(buffer);
            NetClientHandler.this.taskQueue.addTask(new AbstractKernelRunnable() {

                public void run() throws Exception {
                    final INetSession session = NetClientHandler.this.getSession();
                    if (session != null) {
                        try {
                            NetClientHandler.this.fServer.setCurrentSession(session);
                            session.registerClientActivity();
                            if (NetClientHandler.this.isConnected && session.getMessageReceiver().process(session, msg)) {
                                NetClientHandler.this.enqueueReadResume();
                            }
                        } finally {
                            NetClientHandler.this.fServer.setCurrentSession(session);
                        }
                    } else {
                        NetClientHandler.this.scheduleHandleDisconnect(false);
                    }
                }
            }, NetClientHandler.this.getIdentitiy());
            NetClientHandler.this.enqueueReadResume();
        }
    }

    /**
     * A completion handler for writing to the session's channel.
     */
    protected class ConnectedWriteHandler extends WriteHandler {

        /**
         * The lock for accessing the fields {@code pendingWrites} and {@code isWriting}. The locks {@code lock} and {@code writeLock} should only be
         * acquired in that specified order.
         */
        private final Object writeLock = new Object();

        /** An unbounded queue of messages waiting to be written. */
        private final LinkedList<ByteBuffer> pendingWrites = new LinkedList<ByteBuffer>();

        /** Whether a write is underway. */
        private boolean isWriting = false;

        /**
         * Adds the message to the queue, and starts processing the queue if needed.
         */
        @Override
        void write(final ByteBuffer message) {
            boolean first;
            synchronized (this.writeLock) {
                first = this.pendingWrites.isEmpty();
                this.pendingWrites.add(message);
            }
            if (NetClientHandler.logger.isLoggable(Level.FINEST)) {
                NetClientHandler.logger.log(Level.FINEST, "write session:{0} message:{1} first:{2}", NetClientHandler.this, HexDumper.format(message, 0x50), first);
            }
            if (first) {
                this.processQueue();
            }
        }

        /** Start processing the first element of the queue, if present. */
        private void processQueue() {
            ByteBuffer message;
            synchronized (this.writeLock) {
                if (this.isWriting) {
                    return;
                }
                message = this.pendingWrites.peek();
                if (message == null) {
                    return;
                }
                this.isWriting = true;
            }
            if (NetClientHandler.logger.isLoggable(Level.FINEST)) {
                NetClientHandler.logger.log(Level.FINEST, "processQueue session:{0} size:{1,number,#} head={2}", NetClientHandler.this, this.pendingWrites.size(), HexDumper.format(message, 0x50));
            }
            try {
                final INetSession session = NetClientHandler.this.getSession();
                if (session != null) {
                    session.registerServerActivity();
                }
                NetClientHandler.this.fMessageChannel.write(message, this);
            } catch (final RuntimeException e) {
                NetClientHandler.logger.logThrow(Level.SEVERE, e, "{0} processing message {1}", NetClientHandler.this, HexDumper.format(message, 0x50));
                throw e;
            }
        }

        /**
         * Done writing the first request in the queue.
         * 
         * @param result
         */
        public void completed(final IoFuture<Void, Void> result) {
            ByteBuffer message;
            synchronized (this.writeLock) {
                message = this.pendingWrites.remove();
                this.isWriting = false;
            }
            if (NetClientHandler.logger.isLoggable(Level.FINEST)) {
                final ByteBuffer resetMessage = message.duplicate();
                resetMessage.reset();
                NetClientHandler.logger.log(Level.FINEST, "completed write session:{0} message:{1}", NetClientHandler.this, HexDumper.format(resetMessage, 0x50));
            }
            try {
                result.getNow();
                this.processQueue();
            } catch (final ExecutionException e) {
                if (NetClientHandler.logger.isLoggable(Level.FINE)) {
                    NetClientHandler.logger.logThrow(Level.FINE, e, "write session:{0} message:{1} throws", NetClientHandler.this, HexDumper.format(message, 0x50));
                }
                NetClientHandler.this.scheduleHandleDisconnect(false);
            }
        }
    }

    /**
     * Schedule a non-transactional task for disconnecting the client.
     * 
     * @param graceful
     *            if {@code true}, disconnection is graceful (i.e., a LOGOUT_SUCCESS protocol message is sent before disconnecting the client session)
     */
    private void scheduleHandleDisconnect(final boolean graceful) {
        synchronized (this.lock) {
            if (!this.isConnected) {
                return;
            }
            this.isConnected = false;
        }
        this.scheduleNonTransactionalTask(new AbstractKernelRunnable() {

            public void run() {
                NetClientHandler.this.handleDisconnect(graceful);
            }
        });
    }

    /**
     * Schedules a non-durable, transactional task.
     * 
     * @param task
     */
    private void scheduleTask(final KernelRunnable task) {
        this.fServer.scheduleTask(task, this.getIdentitiy());
    }

    /**
     * Schedules a non-durable, non-transactional task.
     * 
     * @param task
     */
    private void scheduleNonTransactionalTask(final KernelRunnable task) {
        this.fServer.scheduleNonTransactionalTask(task, this.getIdentitiy());
    }

    /**
     * Schedule a task to resume reading. Use this method to delay reading until a task resulting from an earlier read request has been completed.
     */
    void enqueueReadResume() {
        final Identity identity = this.getIdentitiy();
        this.taskQueue.addTask(new AbstractKernelRunnable() {

            public void run() {
                NetClientHandler.logger.log(Level.FINER, "resuming reads session:{0}", this);
                if (NetClientHandler.this.isConnected) {
                    NetClientHandler.this.readHandler.read();
                }
            }
        }, identity);
    }

    /**
     * returns the current identitiy
     * 
     * @return current identity
     */
    private Identity getIdentitiy() {
        final INetSession session = this.getSession();
        Identity identity = session.getIdentity();
        final String userName = session.getName();
        if (identity == null) {
            identity = new Identity() {

                /**
                 * {@inheritDoc}
                 * 
                 * @see com.sun.sgs.auth.Identity#getName()
                 */
                public String getName() {
                    return userName == null ? "" : userName;
                }

                /**
                 * {@inheritDoc}
                 * 
                 * @see com.sun.sgs.auth.Identity#notifyLoggedIn()
                 */
                public void notifyLoggedIn() {
                }

                /**
                 * {@inheritDoc}
                 * 
                 * @see com.sun.sgs.auth.Identity#notifyLoggedOut()
                 */
                public void notifyLoggedOut() {
                }
            };
        }
        return identity;
    }

    /**
     * Handles a disconnect request (if not already handled)
     * 
     * @param graceful
     *            if the disconnection was graceful (i.e., due to a logout request).
     */
    void handleDisconnect(final boolean graceful) {
        NetClientHandler.logger.log(Level.FINEST, "handleDisconnect handler:{0}", this);
        synchronized (this.lock) {
            if (this.disconnectHandled) {
                return;
            }
            this.disconnectHandled = true;
            this.isConnected = false;
        }
        if (this.fSessionCreated) {
            this.fServer.removeHandler(this.fSessionID);
        }
        final Identity identity = this.getIdentitiy();
        if (identity != null) {
            this.scheduleTask(new AbstractKernelRunnable() {

                public void run() {
                    identity.notifyLoggedOut();
                }
            });
        }
        try {
            this.fClientChannel.close();
        } catch (final IOException e) {
            if (NetClientHandler.logger.isLoggable(Level.WARNING)) {
                NetClientHandler.logger.logThrow(Level.WARNING, e, "handleDisconnect (close) handle:{0} throws", this.fClientChannel);
            }
        }
        this.readHandler = new ClosedReadHandler();
        this.writeHandler = new ClosedWriteHandler();
        if (this.fSessionCreated) {
            this.scheduleTask(new AbstractKernelRunnable() {

                public void run() {
                    NetClientHandler.this.fServer.getStorage().handleDisconnect(NetClientHandler.this.fSessionID);
                }
            });
        }
    }

    /**
     * {@inheritDoc}
     * 
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public boolean equals(final Object obj) {
        return super.equals(obj);
    }

    /**
     * {@inheritDoc}
     * 
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {
        return this.fClient.hashCode();
    }

    /**
     * {@inheritDoc}
     * 
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return "ClientHandler[" + this.fClient.toString() + "]";
    }

    /**
     * {@inheritDoc}
     * 
     * @see com.pentagaia.tb.net.api.INetMessageSender#send(byte[])
     */
    public void send(final byte[] data) {
        synchronized (this.lock) {
            if (!this.isConnected) {
                return;
            }
        }
        this.sendTransactionally(data);
    }

    /**
     * Sends a network transaction on transaction commit
     * 
     * @param data
     */
    private void sendTransactionally(final byte[] data) {
        this.fServer.getTx().addAction(new CommitAction() {

            /**
             * {@inheritDoc}
             * 
             * @see com.pentagaia.tb.net.impl.NetworkServer.CommitAction#commit()
             */
            public void commit() {
                NetClientHandler.this.sendForce(data);
            }
        });
    }

    /**
     * {@inheritDoc}
     * 
     * @see com.pentagaia.tb.net.api.INetMessageSender#send(com.sun.sgs.impl.sharedutil.MessageBuffer)
     */
    public void send(final MessageBuffer data) {
        synchronized (this.lock) {
            if (!this.isConnected) {
                return;
            }
        }
        this.sendTransactionally(data.getBuffer());
    }

    /**
     * {@inheritDoc}
     * 
     * @see com.pentagaia.tb.net.api.INetMessageSender#sendForce(byte[])
     */
    public void sendForce(final byte[] data) {
        synchronized (this.lock) {
            if (!this.isConnected) {
                return;
            }
        }
        try {
            final ByteBuffer message = ByteBuffer.wrap(data);
            this.writeHandler.write(message);
        } catch (final RuntimeException e) {
            if (NetClientHandler.logger.isLoggable(Level.WARNING)) {
                NetClientHandler.logger.logThrow(Level.WARNING, e, "sendProtocolMessage session:{0} throws", this);
            }
        }
    }

    /**
     * {@inheritDoc}
     * 
     * @see com.pentagaia.tb.net.api.INetMessageSender#sendForce(com.sun.sgs.impl.sharedutil.MessageBuffer)
     */
    public void sendForce(final MessageBuffer data) {
        synchronized (this.lock) {
            if (!this.isConnected) {
                return;
            }
        }
        try {
            final ByteBuffer message = ByteBuffer.wrap(data.getByteArray());
            this.writeHandler.write(message);
        } catch (final RuntimeException e) {
            if (NetClientHandler.logger.isLoggable(Level.WARNING)) {
                NetClientHandler.logger.logThrow(Level.WARNING, e, "sendProtocolMessage session:{0} throws", this);
            }
        }
    }

    /**
     * {@inheritDoc}
     * 
     * @see com.pentagaia.tb.net.api.INetClientHandler#disconnect()
     */
    public void disconnect() {
        this.handleDisconnect(true);
    }
}
