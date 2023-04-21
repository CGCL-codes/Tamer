package fulmine.distribution.connection;

import static fulmine.distribution.connection.tcp.ProtocolMessageConstants.DELIMITER;
import static fulmine.distribution.connection.tcp.ProtocolMessageConstants.PULSE_MSG;
import static fulmine.util.Utils.COLON;
import static fulmine.util.Utils.SPACE;
import static fulmine.util.Utils.logException;
import static fulmine.util.Utils.safeToString;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import fulmine.AbstractLifeCycle;
import fulmine.context.IContextWatchdog;
import fulmine.context.IFrameworkContext;
import fulmine.distribution.IHeartbeatMonitor;
import fulmine.distribution.connection.tcp.ProtocolMessageConstants;
import fulmine.distribution.events.ConnectionDestroyedEvent;
import fulmine.distribution.events.ContextDiscoveredEvent;
import fulmine.distribution.events.ContextNotAvailableEvent;
import fulmine.event.EventFrameExecution;
import fulmine.model.container.IContainer;
import fulmine.model.field.BooleanField;
import fulmine.model.field.IntegerField;
import fulmine.model.field.LongField;
import fulmine.model.field.StringField;
import fulmine.protocol.specification.ByteConstants;
import fulmine.protocol.specification.ByteWriter;
import fulmine.util.Utils;
import fulmine.util.collection.CollectionFactory;
import fulmine.util.collection.CollectionUtils;
import fulmine.util.collection.TtlSet;
import fulmine.util.concurrent.ThreadUtils;
import fulmine.util.log.AsyncLog;

/**
 * A base-class for {@link IConnectionDiscoverer} instances. The implementation
 * uses a UDP discovery process.
 * <p>
 * This discoverer sends out a heartbeat pulse message at a period specified by
 * the {@link #heartbeatPeriod} variable. This period can be changed dynamically
 * at runtime using {@link #setNetworkHeartbeatPeriod(long)}. The pulse message
 * encapsulates the parameters to make a connection to this local context. Once
 * a pulse message is received by a remote context, the heartbeat pulses are
 * expected periodically by the remote context. The expected period for
 * receiving heartbeat pulses is determined by the remote context; it equals the
 * remote context's heartbeat period multiplied by an allowed heartbeat miss
 * counter. The count of missed heartbeats can also be set dynamically at
 * runtime using {@link #setAllowableNetworkHeartbeatMissCount(int)}. This
 * mechanism allows for a difference in the heartbeat periods between contexts
 * but with a certain tolerance.
 * <p>
 * The following events are raised by this:
 * <ul>
 * <li>{@link ContextDiscoveredEvent} when a new heartbeat pulse is received.
 * <li>{@link ContextNotAvailableEvent} when the heartbeat pulse from a
 * previously found context is not received for more than the allowed number of
 * missed pulses multiplied by this discoverer's heartbeat period
 * </ul>
 * <p>
 * The discoverer uses an {@link IContextWatchdog} component to determine if
 * heartbeat pulses should be sent out. The {@link IContextWatchdog} component
 * is also informed if the discoverer encounters any delay between sending out
 * consecutive pulses; this would generally indicate some form of CPU time
 * starvation for the heartbeat generator thread.
 * <p>
 * The parameters for the UDP network and port can be configured by setting
 * system properties {@link AbstractConnectionDiscoverer#UDP_DISCOVERY_NETWORK
 * UDP_DISCOVERY_NETWORK} and
 * {@link AbstractConnectionDiscoverer#UDP_DISCOVERY_PORT UDP_DISCOVERY_PORT}.
 * These have defaults of
 * {@link AbstractConnectionDiscoverer#DEFAULT_UDP_DISCOVERY_NETWORK
 * DEFAULT_UDP_DISCOVERY_NETWORK} and
 * {@link AbstractConnectionDiscoverer#DEFAULT_UDP_DISCOVERY_PORT
 * DEFAULT_UDP_DISCOVERY_PORT} respectively. The network interface to bind to
 * can be specified using the system property
 * {@link AbstractConnectionDiscoverer#NETWORK_INTERFACE_NAME
 * NETWORK_INTERFACE_NAME}. This must specify the name of the NIC to bind to.
 * 
 * @author Ramon Servadei
 */
public abstract class AbstractConnectionDiscoverer extends AbstractLifeCycle implements IConnectionDiscoverer {

    private static final String HEARTBEAT_PERIOD = "heartbeatPeriod";

    private static final String ALLOWED_MISSED_HEARTBEATS = "allowedMissedHeartbeats";

    private static final String SENDING_HEARTBEATS = "sendingHeartbeats";

    private static final AsyncLog LOG = new AsyncLog(AbstractConnectionDiscoverer.class);

    /**
     * The system property to override the default UDP multicast network for
     * discovery.
     * 
     * @see AbstractConnectionDiscoverer#DEFAULT_UDP_DISCOVERY_NETWORK
     */
    public static final String UDP_DISCOVERY_NETWORK = "discover.udp.network";

    /** The default UDP multicast network for discovery */
    public static final String DEFAULT_UDP_DISCOVERY_NETWORK = "228.160.2.5";

    /**
     * The system property to use to set the NIC. This is an optional setting
     * overriding the default. The value must be the NIC name.
     */
    public static final String NETWORK_INTERFACE_NAME = "discover.udp.nic";

    /**
     * The system property to override the default UDP multicast port for
     * discovery.
     * 
     * @see AbstractConnectionDiscoverer#DEFAULT_UDP_DISCOVERY_PORT
     */
    public static final String UDP_DISCOVERY_PORT = "discover.udp.port";

    /** The default UDP multicast port for discovery */
    public static final String DEFAULT_UDP_DISCOVERY_PORT = "16025";

    /** The default time window to use to ignore duplicate pulse messages; 5sec */
    public static final int DEFAULT_DUPLICATE_PING_WINDOW = 5000;

    /** The multicast socket to use for discovering other connections */
    protected MulticastSocket socket;

    /** Listens for heartbeats messages from other contexts */
    protected final HeartbeatListener heartbeatListener;

    /** The thread running the {@link #heartbeatListener} */
    protected final Thread heartbeatListenerThread;

    /** Processes heartbeats from other contexts */
    protected final HeartbeatProcessor heartbeatProcessor;

    /** The thread running the {@link #heartbeatProcessor} */
    protected final Thread heartbeatProcessorThread;

    /**
     * Handles sending out heartbeats and listening for heartbeats from other
     * contexts
     */
    protected final HeartbeatGenerator heartbeatGenerator;

    /** The thread running the {@link #heartbeatGenerator} */
    protected final Thread heartbeatGeneratorThread;

    /**
     * The pulse message this context sends out.
     */
    protected String pulse;

    /** The UDP discovery port */
    protected final int port;

    /** The UDP discovery network */
    protected final InetAddress network;

    /** The string description */
    protected String description;

    /** The context to use to raise {@link ContextDiscoveredEvent} events */
    private final IFrameworkContext context;

    /** The heartbeat period */
    private long heartbeatPeriod = IHeartbeatMonitor.DEFAULT_HEARTBEAT_PERIOD;

    /** The number of allowed missed heartbeats */
    private int allowedMissedHeartbeats = IHeartbeatMonitor.DEFAULT_ALLOWED_MISSED_COUNT;

    /**
     * The heartbeats received waiting to be processed by the
     * {@link HeartbeatProcessor}
     */
    private List<String> receivedHeartbeats;

    /**
     * Holds the set of known dead contexts found during the heartbeat
     * processing period.
     */
    private Set<String> deadContexts;

    /**
     * The heartbeats received in the current heartbeat window. Access must be
     * synchronized on the map.
     */
    private final Map<String, IConnectionParameters> current;

    /**
     * The heartbeats received in the previous heartbeat window. Access must be
     * synchronized on the {@link #current} map.
     */
    private final Map<String, IConnectionParameters> previous;

    /** Indicates if the {@link HeartbeatGenerator} should send out a pulse */
    private boolean pulsingEnabled = true;

    /**
     * Processes the heartbeat messages received by the
     * {@link HeartbeatListener}.
     * <p>
     * This raises {@link ContextDiscoveredEvent}s when a newly discovered
     * context heartbeat is received.
     * <p>
     * If the heartbeat from a remote context is missed for more than
     * {@link AbstractConnectionDiscoverer#allowedMissedHeartbeats
     * allowedMissedHeartbeats} then a {@link ContextNotAvailableEvent} is
     * raised.
     * 
     * @author Ramon Servadei
     */
    final class HeartbeatProcessor extends AbstractLifeCycle implements Runnable {

        private HeartbeatProcessor() {
            super();
        }

        public void run() {
            int count = 0;
            while (AbstractConnectionDiscoverer.this.isActive()) {
                count++;
                try {
                    Thread.sleep(AbstractConnectionDiscoverer.this.heartbeatPeriod);
                    scanForNewContexts();
                    if (count % AbstractConnectionDiscoverer.this.allowedMissedHeartbeats != 0) {
                        continue;
                    }
                    count = 0;
                    scanForLostContexts();
                } catch (Exception e) {
                    logException(getLog(), this, e);
                }
            }
        }

        @Override
        protected void doDestroy() {
            AbstractConnectionDiscoverer.this.heartbeatProcessorThread.interrupt();
        }

        @Override
        protected void doStart() {
        }

        @Override
        protected AsyncLog getLog() {
            return AbstractConnectionDiscoverer.this.getLog();
        }

        /**
         * Helper method that checks for new contexts.
         * <p>
         * Raises {@link ContextDiscoveredEvent}.
         */
        private void scanForNewContexts() {
            List<String> rxCopy;
            Set<String> deadContextsCopy;
            synchronized (AbstractConnectionDiscoverer.this.receivedHeartbeats) {
                rxCopy = AbstractConnectionDiscoverer.this.receivedHeartbeats;
                AbstractConnectionDiscoverer.this.receivedHeartbeats = CollectionFactory.newList();
            }
            synchronized (AbstractConnectionDiscoverer.this.deadContexts) {
                deadContextsCopy = CollectionFactory.newSet(AbstractConnectionDiscoverer.this.deadContexts);
                AbstractConnectionDiscoverer.this.deadContexts.clear();
            }
            for (String deadContext : deadContextsCopy) {
                for (Iterator<String> iterator = deadContextsCopy.iterator(); iterator.hasNext(); ) {
                    String pulse = iterator.next();
                    if (AbstractConnectionDiscoverer.this.isPulseFromContext(deadContext, pulse)) {
                        iterator.remove();
                    }
                }
            }
            for (String pulse : rxCopy) {
                if (!AbstractConnectionDiscoverer.this.pulse.equals(pulse)) {
                    IConnectionParameters params = AbstractConnectionDiscoverer.this.getConnectionParameters(pulse);
                    if (params != null) {
                        boolean raiseDiscoveredEvent = false;
                        final IConnectionParameters putResult;
                        synchronized (AbstractConnectionDiscoverer.this.current) {
                            putResult = AbstractConnectionDiscoverer.this.current.put(params.getRemoteContextIdentity(), params);
                            if (putResult == null) {
                                final IConnectionParameters instanceInPrevious = AbstractConnectionDiscoverer.this.previous.get(params.getRemoteContextIdentity());
                                raiseDiscoveredEvent = ((instanceInPrevious == null) || (!instanceInPrevious.equals(params)));
                            } else {
                                raiseDiscoveredEvent = !putResult.equals(params);
                            }
                        }
                        if (raiseDiscoveredEvent) {
                            if (getLog().isInfoEnabled()) {
                                getLog().info("Context discovered: " + safeToString(params));
                            }
                            if (AbstractConnectionDiscoverer.this.context.isActive()) {
                                AbstractConnectionDiscoverer.this.context.queueEvent(new ContextDiscoveredEvent(AbstractConnectionDiscoverer.this.context, params));
                            }
                        }
                    }
                }
            }
        }

        /**
         * Helper method to check for any contexts that have missed the alotted
         * number of heartbeats.
         * <p>
         * Raises {@link ContextNotAvailableEvent}.
         */
        private void scanForLostContexts() {
            final Set<String> notAvailable;
            synchronized (AbstractConnectionDiscoverer.this.current) {
                if (getLog().isDebugEnabled()) {
                    getLog().debug("Contexts found in this " + (AbstractConnectionDiscoverer.this.allowedMissedHeartbeats * AbstractConnectionDiscoverer.this.heartbeatPeriod) + "ms period:" + CollectionUtils.toFormattedString(AbstractConnectionDiscoverer.this.current) + "Contexts found in the previous period:" + CollectionUtils.toFormattedString(AbstractConnectionDiscoverer.this.previous));
                }
                notAvailable = CollectionFactory.newSet(AbstractConnectionDiscoverer.this.previous.keySet());
                notAvailable.removeAll(AbstractConnectionDiscoverer.this.current.keySet());
                AbstractConnectionDiscoverer.this.previous.clear();
                AbstractConnectionDiscoverer.this.previous.putAll(AbstractConnectionDiscoverer.this.current);
                AbstractConnectionDiscoverer.this.current.clear();
            }
            for (String remoteContextId : notAvailable) {
                if (getLog().isInfoEnabled()) {
                    getLog().info("Context not available: " + remoteContextId);
                }
                AbstractConnectionDiscoverer.this.context.queueEvent(new ContextNotAvailableEvent(AbstractConnectionDiscoverer.this.context, remoteContextId));
            }
        }
    }

    /**
     * Listens for heartbeat pulse messages on the discovery network and adds
     * them to the {@link AbstractConnectionDiscoverer#receivedHeartbeats
     * receivedHeartbeats}. These are processed by the
     * {@link HeartbeatProcessor}.
     * 
     * @author Ramon Servadei
     */
    final class HeartbeatListener extends AbstractLifeCycle implements Runnable {

        private HeartbeatListener() {
            super();
        }

        public void run() {
            while (AbstractConnectionDiscoverer.this.isActive()) {
                byte[] buf = new byte[1024];
                DatagramPacket recv = new DatagramPacket(buf, buf.length);
                try {
                    socket.receive(recv);
                    String data = new String(recv.getData(), ByteConstants.ENCODING).trim();
                    synchronized (AbstractConnectionDiscoverer.this.receivedHeartbeats) {
                        AbstractConnectionDiscoverer.this.receivedHeartbeats.add(data);
                    }
                } catch (Exception e) {
                    logException(getLog(), this, e);
                }
            }
        }

        @Override
        protected void doDestroy() {
            AbstractConnectionDiscoverer.this.heartbeatListenerThread.interrupt();
        }

        @Override
        protected void doStart() {
        }

        @Override
        protected AsyncLog getLog() {
            return AbstractConnectionDiscoverer.this.getLog();
        }
    }

    /**
     * Sends out the heartbeat pulse for this context.
     * <p>
     * This class uses an {@link IContextWatchdog} instance that is queried
     * using the {@link IContextWatchdog#isContextHealthy()} method before
     * sending out the heartbeat. If the context is not healthy, the heartbeat
     * pulse is not sent out.
     * <p>
     * This class also provides a mechanism to detect performance issue in the
     * current runtime by checking that the thread running this is activated at
     * the required heartbeat period. If the time between activations exceeds
     * the heartbeat period plus a tolerance, the
     * {@link IContextWatchdog#cpuResourcesLow()} method is invoked, otherwise,
     * if the activation time is within the period plus tolerance the
     * {@link IContextWatchdog#systemNormal()} method is invoked.
     * 
     * @author Ramon Servadei
     */
    final class HeartbeatGenerator extends AbstractLifeCycle implements Runnable {

        private HeartbeatGenerator() {
            super();
        }

        @Override
        protected void doStart() {
        }

        @Override
        protected void doDestroy() {
            AbstractConnectionDiscoverer.this.heartbeatGeneratorThread.interrupt();
        }

        @Override
        protected AsyncLog getLog() {
            return AbstractConnectionDiscoverer.this.getLog();
        }

        public void run() {
            while (AbstractConnectionDiscoverer.this.isActive()) {
                try {
                    final IContextWatchdog contextWatchdog = AbstractConnectionDiscoverer.this.context.getContextWatchdog();
                    boolean pulse = true;
                    if (contextWatchdog != null) {
                        if (!contextWatchdog.isContextHealthy()) {
                            if (getLog().isWarnEnabled()) {
                                getLog().warn(AbstractConnectionDiscoverer.this.context + " is not healthy, not sending out heartbeat");
                            }
                            pulse = false;
                        }
                    }
                    if (ThreadUtils.findDeadlocks()) {
                        if (getLog().isWarnEnabled()) {
                            getLog().warn("Deadlock found, not sending out heartbeat");
                        }
                        pulse = false;
                    }
                    if (AbstractConnectionDiscoverer.this.isPulsingEnabled() && pulse) {
                        AbstractConnectionDiscoverer.this.pulse();
                    }
                    long now = System.currentTimeMillis();
                    Thread.sleep(AbstractConnectionDiscoverer.this.heartbeatPeriod);
                    final long actualPeriod = System.currentTimeMillis() - now;
                    final int maxTolerance = 20;
                    if (actualPeriod > AbstractConnectionDiscoverer.this.heartbeatPeriod + (maxTolerance)) {
                        if (getLog().isWarnEnabled()) {
                            getLog().warn(AbstractConnectionDiscoverer.this + " low CPU resources; heartbeatPeriod " + AbstractConnectionDiscoverer.this.heartbeatPeriod + "ms but actually took " + actualPeriod + "ms");
                        }
                        if (contextWatchdog != null) {
                            contextWatchdog.cpuResourcesLow();
                        }
                    } else {
                        if (contextWatchdog != null) {
                            contextWatchdog.systemNormal();
                        }
                    }
                } catch (Exception e) {
                    logException(getLog(), this, e);
                }
            }
        }
    }

    /**
     * Constructor that retrieves network values from system properties or use
     * default values if no system properties are set.
     * 
     * @param context
     *            the local context
     */
    public AbstractConnectionDiscoverer(IFrameworkContext context) {
        this(context, System.getProperty(UDP_DISCOVERY_NETWORK, DEFAULT_UDP_DISCOVERY_NETWORK), Integer.parseInt(System.getProperty(UDP_DISCOVERY_PORT, DEFAULT_UDP_DISCOVERY_PORT)), System.getProperty(NETWORK_INTERFACE_NAME));
    }

    /**
     * Constructor that uses parameters for the network values.
     * 
     * @param context
     *            the local context
     * @param udpNetwork
     *            the UDP network
     * @param udpPort
     *            the UDP port
     * @param udpNic
     *            the network interface card name to bind to, <code>null</code>
     *            for default
     */
    public AbstractConnectionDiscoverer(IFrameworkContext context, String udpNetwork, int udpPort, String udpNic) {
        super();
        this.port = udpPort;
        this.context = context;
        try {
            this.network = InetAddress.getByName(udpNetwork);
            this.socket = new MulticastSocket(this.port);
            if (udpNic != null) {
                this.socket.setNetworkInterface(NetworkInterface.getByName(udpNic));
            }
            this.socket.joinGroup(this.network);
        } catch (Exception e) {
            throw new IllegalStateException("Could not create " + this, e);
        }
        this.deadContexts = new TtlSet<String>();
        this.receivedHeartbeats = CollectionFactory.newList();
        this.current = CollectionFactory.newMap();
        this.previous = CollectionFactory.newMap();
        final String threadDetails = this.context.getEventProcessorIdentityPrefix() + COLON + this.network + COLON + this.port;
        this.heartbeatListener = new HeartbeatListener();
        this.heartbeatListenerThread = new Thread(this.heartbeatListener, this.heartbeatListener.getClass().getSimpleName() + SPACE + threadDetails);
        this.heartbeatListenerThread.setDaemon(true);
        this.heartbeatProcessor = new HeartbeatProcessor();
        this.heartbeatProcessorThread = new Thread(this.heartbeatProcessor, this.heartbeatProcessor.getClass().getSimpleName() + SPACE + threadDetails);
        this.heartbeatProcessorThread.setDaemon(true);
        this.heartbeatGenerator = new HeartbeatGenerator();
        this.heartbeatGeneratorThread = new Thread(this.heartbeatGenerator, this.heartbeatGenerator.getClass().getSimpleName() + SPACE + threadDetails);
        this.heartbeatGeneratorThread.setPriority(Thread.MAX_PRIORITY);
        this.heartbeatGeneratorThread.setDaemon(true);
    }

    /**
     * Get the UDP port used for connection discovery
     * 
     * @return the UDP port for connection discovery
     */
    public final int getPort() {
        return this.port;
    }

    /**
     * Get the UDP network for connection discovery
     * 
     * @return the UDP network for connection discovery
     */
    public final InetAddress getNetwork() {
        return this.network;
    }

    /**
     * Get the multicast socket used for connection discovery
     * 
     * @return the multicast socket used for connection discovery
     */
    public final MulticastSocket getSocket() {
        return this.socket;
    }

    /**
     * Get the pulse message sent out to other {@link IConnectionDiscoverer}
     * instances over the discovery network
     * 
     * @return the pulse message sent
     */
    public final String getPulse() {
        return this.pulse;
    }

    /**
     * This removes the connection details in the
     * {@link ConnectionDestroyedEvent} from the
     * {@link AbstractConnectionDiscoverer#current} map of known connections.
     */
    public final void connectionDestroyed(String remoteContextIdentity) {
        synchronized (AbstractConnectionDiscoverer.this.current) {
            AbstractConnectionDiscoverer.this.current.remove(remoteContextIdentity);
            AbstractConnectionDiscoverer.this.previous.remove(remoteContextIdentity);
        }
        synchronized (AbstractConnectionDiscoverer.this.deadContexts) {
            AbstractConnectionDiscoverer.this.deadContexts.add(remoteContextIdentity);
        }
    }

    public final long getNetworkHeartbeatPeriod() {
        return heartbeatPeriod;
    }

    public final int getAllowableNetworkHeartbeatMissCount() {
        return allowedMissedHeartbeats;
    }

    public final void setAllowableNetworkHeartbeatMissCount(int allowedHeartbeatMissCount) {
        this.allowedMissedHeartbeats = allowedHeartbeatMissCount;
    }

    public final void setNetworkHeartbeatPeriod(long periodInMillis) {
        this.heartbeatPeriod = periodInMillis;
    }

    public void disablePulsing() {
        this.pulsingEnabled = false;
        updateSystemInfo();
    }

    public void enablePulsing() {
        this.pulsingEnabled = true;
        updateSystemInfo();
    }

    private void updateSystemInfo() {
        final IContainer systemInfo = this.context.getSystemInfo();
        systemInfo.beginFrame(new EventFrameExecution());
        try {
            systemInfo.add(new BooleanField(SENDING_HEARTBEATS, isPulsingEnabled()));
        } finally {
            systemInfo.endFrame();
        }
    }

    @Override
    protected final void doDestroy() {
        if (getLog().isInfoEnabled()) {
            getLog().info("Destroying " + this);
        }
        this.heartbeatListener.destroy();
        this.socket.close();
        this.heartbeatProcessor.destroy();
        this.heartbeatGenerator.destroy();
        if (getLog().isInfoEnabled()) {
            getLog().info("Destroyed " + this);
        }
    }

    @Override
    protected void doStart() {
        if (getLog().isInfoEnabled()) {
            getLog().info("Starting " + this);
        }
        this.pulse = PULSE_MSG + DELIMITER + this.context.getIdentity() + DELIMITER + this.context.getContextHashCode() + DELIMITER + getProtocolConnectionParameters();
        this.description = getClass().getSimpleName() + " for " + this.pulse;
        this.heartbeatListenerThread.start();
        this.heartbeatProcessorThread.start();
        this.heartbeatGeneratorThread.start();
        final IContainer systemInfo = this.context.getSystemInfo();
        systemInfo.beginFrame(new EventFrameExecution());
        try {
            systemInfo.add(new StringField(UDP_DISCOVERY_NETWORK, getNetwork().toString()));
            systemInfo.add(new IntegerField(UDP_DISCOVERY_PORT, getPort()));
            try {
                systemInfo.add(new StringField(NETWORK_INTERFACE_NAME, this.socket.getNetworkInterface().getDisplayName()));
            } catch (SocketException e) {
                Utils.logException(getLog(), "Could not update systemInfo record with UDP NIC", e);
            }
            systemInfo.add(new LongField(HEARTBEAT_PERIOD, getNetworkHeartbeatPeriod()));
            systemInfo.add(new LongField(ALLOWED_MISSED_HEARTBEATS, getAllowableNetworkHeartbeatMissCount()));
            systemInfo.add(new BooleanField(SENDING_HEARTBEATS, isPulsingEnabled()));
        } finally {
            systemInfo.endFrame();
        }
    }

    /**
     * Determine if the pulse message is from the named context
     * 
     * @param contextIdentity
     *            the context to compare against the pulse message
     * @param pulseMessage
     *            the pulse message
     * @return <code>true</code> if the pulse message is from the identified
     *         context
     */
    protected boolean isPulseFromContext(String contextIdentity, String pulseMessage) {
        return pulseMessage.startsWith(PULSE_MSG + DELIMITER + contextIdentity + DELIMITER);
    }

    /**
     * Send the message to the multicast socket
     * 
     * @param msg
     */
    protected void send(String msg) {
        DatagramPacket data = new DatagramPacket(ByteWriter.getBytes(msg), msg.length(), this.network, this.port);
        try {
            this.socket.send(data);
        } catch (IOException e) {
            logException(getLog(), "Could not send message '" + safeToString(msg) + "'", e);
        }
    }

    @Override
    public String toString() {
        return this.description;
    }

    @Override
    protected AsyncLog getLog() {
        return LOG;
    }

    public final void pulse() {
        if (getLog().isDebugEnabled()) {
            getLog().debug(this + " pulse");
        }
        send(this.pulse);
    }

    /**
     * Template method for sub-classes to provide the implementation of how to
     * handle the 'pulse' message received from a peer
     * {@link IConnectionDiscoverer}. This method inspects the data string and
     * creates an appropriate {@link IConnectionParameters} instance
     * representing it.
     * 
     * @param data
     *            the pulse message from a peer {@link IConnectionDiscoverer}
     * @return the {@link IConnectionParameters} extracted from the pulse
     *         message or <code>null</code> if the message could not be
     *         deciphered.
     */
    protected abstract IConnectionParameters getConnectionParameters(String data);

    protected boolean isPulsingEnabled() {
        return this.pulsingEnabled;
    }

    /**
     * Overridden by subclasses to provide the protocol specific parameters for
     * connecting to the connection broker of this context. The parameters must
     * be delimited using {@link ProtocolMessageConstants#DELIMITER}. There is
     * no need to prepend the delimiter.
     * 
     * @return a string of parameters for the connection protocol of the
     *         connection broker, delimited using
     *         {@link ProtocolMessageConstants#DELIMITER}
     */
    protected abstract String getProtocolConnectionParameters();
}
