package org.serverdemo.fix;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import org.apache.commons.io.IOUtils;
import org.quickfixj.jmx.JmxExporter;
import org.serverdemo.system.ServerDemoProject;
import protoj.lang.ArgRunnable;
import protoj.lang.ResourceFeature;
import protoj.lang.StandardProject;
import quickfix.Application;
import quickfix.ConfigError;
import quickfix.Connector;
import quickfix.DefaultMessageFactory;
import quickfix.FileLogFactory;
import quickfix.FileStoreFactory;
import quickfix.LogFactory;
import quickfix.Message;
import quickfix.MessageFactory;
import quickfix.MessageStoreFactory;
import quickfix.Session;
import quickfix.SessionID;
import quickfix.SessionSettings;
import quickfix.SocketAcceptor;
import quickfix.SocketInitiator;

public final class FixEndPoint {

    /**
	 * See {@link #getMarket()}.
	 */
    private final Application market;

    /**
	 * Holds the received messages.
	 */
    private final LinkedBlockingQueue<Message> inboundMessages;

    /**
	 * Holds the messages ready to be sent.
	 */
    private final LinkedBlockingQueue<Message> outboundMessages;

    /**
	 * Used to signal to blocking takes that they should complete.
	 */
    private final Message poisonedMessage = new Message() {

        private static final long serialVersionUID = 1L;
    };

    /**
	 * Cached reference to the ProtoJ helper.
	 */
    private final StandardProject delegate;

    /**
	 * See {@link #getWorkingDir()}.
	 */
    private final String workingDir;

    /**
	 * See {@link #getLogDir()}.
	 */
    private final String logDir;

    /**
	 * See {@link #getStoreDir()}.
	 */
    private final String storeDir;

    /**
	 * See {@link #getSessionId()}.
	 */
    private final SessionID sessionId;

    /**
	 * The classpath resource location of the quickfix config file.
	 */
    private final String configResource;

    /**
	 * See {@link #isExchange()}.
	 */
    private final boolean isExchange;

    /**
	 * See {@link #getConnector()}.
	 */
    private Connector connector;

    private final ServerDemoProject parent;

    /**
	 * See {@link #getHost()}.
	 */
    private final String host;

    /**
	 * See {@link #getPort()}.
	 */
    private final int port;

    /**
	 * Initializes this server with the owning parent and configuration details.
	 * to use.
	 * @param parent
	 *            the owning parent
	 * @param market
	 *            the underlying market
	 * @param isExchange
	 *            specified whether or not this is an exchange side connection,
	 *            see {@link #isExchange()}.
	 * @param configResource
	 *            the resource path of the quickfix config file, e.g.
	 *            "org/serverdemo/fix/fixserver.cfg"
	 * @param sessionId
	 *            required fix info
	 */
    public FixEndPoint(ServerDemoProject parent, String host, int port, Application market, boolean isExchange, String configResource, SessionID sessionId) {
        this.host = host;
        this.port = port;
        this.market = market;
        this.parent = parent;
        this.inboundMessages = new LinkedBlockingQueue<Message>();
        this.outboundMessages = new LinkedBlockingQueue<Message>();
        this.isExchange = isExchange;
        this.configResource = configResource;
        this.sessionId = sessionId;
        this.delegate = parent.getDelegate();
        this.workingDir = createWorkingDir();
        this.logDir = new File(workingDir, "log").getAbsolutePath();
        this.storeDir = new File(workingDir, "store").getAbsolutePath();
    }

    /**
	 * Creates the working directory under the root. Helper for
	 * {@link #FixEndPoint(SystemProject)}.
	 * 
	 * @return
	 */
    private String createWorkingDir() {
        File rootDir = delegate.getLayout().getRootDir();
        File dir = new File(rootDir, "quickfix");
        dir.mkdirs();
        return dir.getAbsolutePath();
    }

    /**
	 * Starts the fix server and returns when it is ready to send and receive
	 * messages.
	 */
    public void openConnection() {
        SessionSettings settings = loadSettings();
        MessageStoreFactory storeFactory = new FileStoreFactory(settings);
        LogFactory logFactory = new FileLogFactory(settings);
        MessageFactory messageFactory = new DefaultMessageFactory();
        if (isExchange()) {
            connector = new SocketAcceptor(market, storeFactory, settings, logFactory, messageFactory);
        } else {
            connector = new SocketInitiator(market, storeFactory, settings, logFactory, messageFactory);
        }
        connector.start();
        JmxExporter exporter = new JmxExporter();
        exporter.export(connector);
    }

    /**
	 * Causes the fix end point to complete as soon as possible.
	 * <p>
	 * The outbound message queue is consumed on a blocking loop inside
	 * {@link #monitorOutboundMessages()}. The inbound message queue is consumed
	 * on a blocking loop inside {@link #monitorInboundMessages(ArgRunnable)}.
	 * This method places poisoned messages in both queues so that they will
	 * unblock and those methods therefore complete.
	 */
    public void completeConnection() {
        outboundMessages.put(poisonedMessage);
        inboundMessages.put(poisonedMessage);
        connector.stop();
    }

    /**
	 * Registers the callback for notification of inbound messages. Blocks until
	 * the poisoned message is retrieved, which is injected with a call to
	 * {@link #completeConnection()}. Process all messages, except the poisoned
	 * one, with the specified callback.
	 * 
	 * @param callback
	 *            handler for each inbound message.
	 */
    public void monitorInboundMessages(ArgRunnable<Message> callback) {
        blockingTake(inboundMessages, callback);
    }

    /**
	 * Sends messages that were added with a call to
	 * {@link #putOutboundMessage(Message)}. Blocks until the poisoned message
	 * is retrieved, which is injected with a call to
	 * {@link #completeConnection()}. Process all messages, except the poisoned
	 * one, with the specified callback.
	 */
    public void monitorOutboundMessages() {
        blockingTake(outboundMessages, new ArgRunnable<Message>() {

            public void run(Message message) {
                Session.sendToTarget(message, sessionId);
            }
        });
    }

    /**
	 * Takes messages from the specified queue until the poisoned message is
	 * received. Normal messages are passed to the specified runnable for custom
	 * processing.
	 * 
	 * @param queue
	 * @param callback
	 */
    private void blockingTake(BlockingQueue<Message> queue, ArgRunnable<Message> callback) {
        boolean shouldTake = true;
        while (shouldTake) {
            Message element = queue.take();
            if (element == poisonedMessage) {
                shouldTake = false;
            } else {
                callback.run(element);
            }
        }
    }

    /**
	 * Helper for {@link #openConnection()}. Extracts the quickfix config file
	 * to disk and reads it into the returned sessions instance. The config file
	 * contains velocity markup so that certain values don't require hardcoding.
	 * 
	 * @return
	 * @throws FileNotFoundException
	 * @throws ConfigError
	 */
    private SessionSettings loadSettings() throws FileNotFoundException, ConfigError {
        ResourceFeature feature = delegate.getResourceFeature();
        File configFile = feature.filterResourceToDir(configResource, new File(workingDir));
        FileInputStream is = new FileInputStream(configFile);
        SessionSettings settings;
        try {
            settings = new SessionSettings(is);
        } finally {
            IOUtils.closeQuietly(is);
        }
        return settings;
    }

    /**
	 * Specifies whether this is an exchange side or client side end point.
	 * Exchange side end points accept connections and client side end points
	 * initiate them.
	 * 
	 * @return
	 */
    public boolean isExchange() {
        return isExchange;
    }

    /**
	 * The directory used to perform quickfix logging.
	 * 
	 * @return
	 */
    public String getLogDir() {
        return logDir;
    }

    /**
	 * The directory used to store message information.
	 * 
	 * @return
	 */
    public String getStoreDir() {
        return storeDir;
    }

    /**
	 * Contains the log and messages files.
	 * 
	 * @return
	 */
    public String getWorkingDir() {
        return workingDir;
    }

    /**
	 * The host where socket connections are accepted/initiated. Should be
	 * overridden with system properties, see UserOverride.aj.
	 * 
	 * @return
	 */
    public String getHost() {
        return host;
    }

    /**
	 * The port where socket connections are accepted/initiated. Should be
	 * overridden with system properties, see UserOverride.aj.
	 * 
	 * @return
	 */
    public int getPort() {
        return port;
    }

    /**
	 * Gets the session id that this client represents.
	 * 
	 * @return
	 */
    public SessionID getSessionId() {
        return sessionId;
    }

    /**
	 * Puts the specified message ready to be sent to the other fix endpoint.
	 * 
	 * @param message
	 */
    public void putOutboundMessage(Message message) {
        outboundMessages.put(message);
    }

    /**
	 * This should be called when an application message is received from fix.
	 * The callback registered with {@link #monitorInboundMessages(ArgRunnable)}
	 * will then be notified with the message.
	 * 
	 * @param message
	 */
    public void fromApp(Message message) {
        inboundMessages.put(message);
    }

    public ServerDemoProject getParent() {
        return parent;
    }
}
