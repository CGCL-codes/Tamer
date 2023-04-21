package flex.messaging.endpoints;

import flex.messaging.FlexContext;
import flex.messaging.FlexSession;
import flex.messaging.client.FlexClient;
import flex.messaging.client.FlushResult;
import flex.messaging.client.PollFlushResult;
import flex.messaging.client.PollWaitListener;
import flex.messaging.client.UserAgentSettings;
import flex.messaging.config.ConfigMap;
import flex.messaging.config.ConfigurationConstants;
import flex.messaging.log.Log;
import flex.messaging.messages.CommandMessage;
import flex.messaging.util.UserAgentManager;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Base for HTTP-based endpoints that support regular polling and long polling
 * which means placing request threads that are polling for messages into a wait
 * state until messages are available to deliver or the configurable wait interval
 * is reached.
 */
public abstract class BasePollingHTTPEndpoint extends BaseHTTPEndpoint implements PollWaitListener {

    private static final String POLLING_ENABLED = "polling-enabled";

    private static final String POLLING_INTERVAL_MILLIS = "polling-interval-millis";

    private static final String POLLING_INTERVAL_SECONDS = "polling-interval-seconds";

    private static final String MAX_WAITING_POLL_REQUESTS = "max-waiting-poll-requests";

    private static final String WAIT_INTERVAL_MILLIS = "wait-interval-millis";

    private static final String CLIENT_WAIT_INTERVAL_MILLIS = "client-wait-interval-millis";

    private static final int DEFAULT_WAIT_FOR_EXCESS_POLL_WAIT_CLIENTS = 3000;

    private UserAgentManager userAgentManager = new UserAgentManager();

    /**
     * Constructs an unmanaged <code>BasePollingHTTPEndpoint</code>.
     */
    public BasePollingHTTPEndpoint() {
        this(false);
    }

    /**
     * Constructs an <code>BasePollingHTTPEndpoint</code> with the indicated management.
     *
     * @param enableManagement <code>true</code> if the <code>BasePollingHTTPEndpoint</code>
     * is manageable; otherwise <code>false</code>.
     */
    public BasePollingHTTPEndpoint(boolean enableManagement) {
        super(enableManagement);
    }

    /**
     * Initializes the <code>Endpoint</code> with the properties.
     * If subclasses override, they must call <code>super.initialize()</code>.
     *
     * @param id Id of the <code>Endpoint</code>.
     * @param properties Properties for the <code>Endpoint</code>.
     */
    @Override
    public void initialize(String id, ConfigMap properties) {
        super.initialize(id, properties);
        if (properties == null || properties.size() == 0) {
            UserAgentManager.setupUserAgentManager(null, userAgentManager);
            return;
        }
        pollingEnabled = properties.getPropertyAsBoolean(POLLING_ENABLED, false);
        pollingIntervalMillis = properties.getPropertyAsLong(POLLING_INTERVAL_MILLIS, -1);
        long pollingIntervalSeconds = properties.getPropertyAsLong(POLLING_INTERVAL_SECONDS, -1);
        if (pollingIntervalSeconds > -1) pollingIntervalMillis = pollingIntervalSeconds * 1000;
        piggybackingEnabled = properties.getPropertyAsBoolean(ConfigurationConstants.PIGGYBACKING_ENABLED_ELEMENT, false);
        maxWaitingPollRequests = properties.getPropertyAsInt(MAX_WAITING_POLL_REQUESTS, 0);
        waitInterval = properties.getPropertyAsLong(WAIT_INTERVAL_MILLIS, 0);
        clientWaitInterval = properties.getPropertyAsInt(CLIENT_WAIT_INTERVAL_MILLIS, 0);
        UserAgentManager.setupUserAgentManager(properties, userAgentManager);
        if (maxWaitingPollRequests > 0 && (waitInterval == -1 || waitInterval > 0)) {
            waitEnabled = true;
            canWait = true;
        }
    }

    /**
     * This flag is volatile to allow for consistent reads across thread without
     * needing to pay the cost for a synchronized lock for each read.
     */
    private volatile boolean canWait;

    /**
     * Used to synchronize sets and gets to the number of waiting clients.
     */
    protected final Object lock = new Object();

    /**
     * Set when properties are handled; used as a shortcut for logging to determine whether this
     * instance attempts to put request threads in a wait state or not.
     */
    private boolean waitEnabled;

    /**
     * A count of the number of request threads that are currently in the wait state (including
     * those on their way into or out of it).
     */
    protected int waitingPollRequestsCount;

    /**
     * A Map(notification Object for a waited request thread, Boolean.TRUE).
     */
    private ConcurrentHashMap currentWaitedRequests;

    protected int clientWaitInterval = 0;

    /**
     * Returns the number of milliseconds the client will wait after receiving a response for
     * a poll with server wait before it issues its next poll request.
     * A value of zero or less causes the client to use its default polling interval (based on the
     * channel's polling-interval-millis configuration) and this value is ignored.
     * A value greater than zero will cause the client to wait for the specified interval before
     * issuing its next poll request with a value of 1 triggering an immediate poll from the client
     * as soon as a waited poll response is received.
     */
    public int getClientWaitInterval() {
        return clientWaitInterval;
    }

    /**
     * Sets the number of milliseconds a client will wait after receiving a response for a poll
     * with server wait before it issues its next poll request.
     * A value of zero or less causes the client to use its default polling interval (based on the
     * channel's polling-interval-millis configuration) and this value is ignored.
     * A value greater than zero will cause the client to wait for the specified interval before
     * issuing its next poll request with a value of 1 triggering an immediate poll from the client
     * as soon as a waited poll response is received.
     * This property does not effect polling clients that poll the server without a server wait.
     *
     * @value The number of milliseconds a client will wait before issuing its next poll when the
     *        server is configured to wait.
     */
    public void setClientWaitInterval(int value) {
        clientWaitInterval = value;
    }

    protected int maxWaitingPollRequests = 0;

    /**
     * Returns the maximum number of server poll response threads that will be
     * waiting for messages to arrive for clients.
     */
    public int getMaxWaitingPollRequests() {
        return maxWaitingPollRequests;
    }

    /**
     * Sets the maximum number of server poll response threads that will be
     * waiting for messages to arrive for clients.
     *
     * @param maxWaitingPollRequests The maximum number of server poll response threads
     * that will be waiting for messages to arrive for the client.
     */
    public void setMaxWaitingPollRequests(int maxWaitingPollRequests) {
        this.maxWaitingPollRequests = maxWaitingPollRequests;
        if (maxWaitingPollRequests > 0 && (waitInterval == -1 || waitInterval > 0)) {
            waitEnabled = true;
            canWait = (waitingPollRequestsCount < maxWaitingPollRequests);
        }
    }

    /**
     * @exclude
     * This is a property used on the client.
     */
    protected boolean piggybackingEnabled;

    /**
     * @exclude
     * This is a property used on the client.
     */
    protected boolean pollingEnabled;

    /**
     * @exclude
     * This is a property used on the client.
     */
    protected long pollingIntervalMillis = -1;

    protected long waitInterval = 0;

    /**
     * Returns the number of milliseconds the server poll response thread will be
     * waiting for messages to arrive for the client.
     */
    public long getWaitInterval() {
        return waitInterval;
    }

    /**
     * Sets the number of milliseconds the server poll response thread will be
     * waiting for messages to arrive for the client.
     *
     * @param waitInterval The number of milliseconds the server poll response thread will be
     * waiting for messages to arrive for the client.
     */
    public void setWaitInterval(long waitInterval) {
        this.waitInterval = waitInterval;
        if (maxWaitingPollRequests > 0 && (waitInterval == -1 || waitInterval > 0)) {
            waitEnabled = true;
            canWait = (waitingPollRequestsCount < maxWaitingPollRequests);
        }
    }

    /**
     * Return the count of the number of request threads that are currently in the wait state
     * (including those on their way into or out of it).
     *
     * @return The count of the number of request threads that are currently in the wait state.
     */
    public int getWaitingPollRequestsCount() {
        return waitingPollRequestsCount;
    }

    /**
     * @exclude
     * Returns a <code>ConfigMap</code> of endpoint properties that the client
     * needs. This includes properties from <code>super.describeEndpoint</code>
     * and additional <code>BaseHTTPEndpoint</code> specific properties under
     * "properties" key.
     */
    @Override
    public ConfigMap describeEndpoint() {
        ConfigMap endpointConfig = super.describeEndpoint();
        boolean createdProperties = false;
        ConfigMap properties = endpointConfig.getPropertyAsMap(PROPERTIES_ELEMENT, null);
        if (properties == null) {
            properties = new ConfigMap();
            createdProperties = true;
        }
        if (pollingEnabled) {
            ConfigMap pollingEnabled = new ConfigMap();
            pollingEnabled.addProperty(EMPTY_STRING, TRUE_STRING);
            properties.addProperty(POLLING_ENABLED, pollingEnabled);
        }
        if (pollingIntervalMillis > -1) {
            ConfigMap pollingInterval = new ConfigMap();
            pollingInterval.addProperty(EMPTY_STRING, String.valueOf(pollingIntervalMillis));
            properties.addProperty(POLLING_INTERVAL_MILLIS, pollingInterval);
        }
        if (piggybackingEnabled) {
            ConfigMap piggybackingEnabled = new ConfigMap();
            piggybackingEnabled.addProperty(EMPTY_STRING, String.valueOf(piggybackingEnabled));
            properties.addProperty(ConfigurationConstants.PIGGYBACKING_ENABLED_ELEMENT, piggybackingEnabled);
        }
        if (createdProperties && properties.size() > 0) endpointConfig.addProperty(ConfigurationConstants.PROPERTIES_ELEMENT, properties);
        return endpointConfig;
    }

    /**
     * Sets up monitoring of waited poll requests so they can be notified and exit when the
     * endpoint stops.
     *
     * @see flex.messaging.endpoints.AbstractEndpoint#start()
     */
    @Override
    public void start() {
        if (isStarted()) return;
        super.start();
        currentWaitedRequests = new ConcurrentHashMap();
    }

    /**
     * Ensures that no poll requests in a wait state are left un-notified when the endpoint stops.
     *
     * @see flex.messaging.endpoints.AbstractEndpoint#stop()
     */
    @Override
    public void stop() {
        if (!isStarted()) return;
        for (Object notifier : currentWaitedRequests.keySet()) {
            synchronized (notifier) {
                notifier.notifyAll();
            }
        }
        currentWaitedRequests = null;
        super.stop();
    }

    /**
     * @see flex.messaging.client.PollWaitListener#waitStart(Object)
     */
    public void waitStart(Object notifier) {
        currentWaitedRequests.put(notifier, Boolean.TRUE);
    }

    /**
     * @see flex.messaging.client.PollWaitListener#waitEnd(Object)
     */
    public void waitEnd(Object notifier) {
        if (currentWaitedRequests != null) currentWaitedRequests.remove(notifier);
    }

    /**
     * Overrides the base poll handling to support optionally putting Http request handling threads
     * into a wait state until messages are available to be delivered in the poll response or a timeout is reached.
     * The number of threads that may be put in a wait state is bounded by <code>max-waiting-poll-requests</code>
     * and waits will only be attempted if the canWait flag that is based on the <code>max-waiting-poll-requests</code>
     * and the specified <code>wait-interval</code> is true.
     *
     * @param flexClient The FlexClient that issued the poll request.
     * @param pollCommand The poll command from the client.
     * @return The flush info used to build the poll response.
     */
    @Override
    protected FlushResult handleFlexClientPoll(FlexClient flexClient, CommandMessage pollCommand) {
        FlushResult flushResult = null;
        if (canWait && !pollCommand.headerExists(CommandMessage.SUPPRESS_POLL_WAIT_HEADER)) {
            FlexSession session = FlexContext.getFlexSession();
            boolean thisThreadCanWait;
            synchronized (lock) {
                ++waitingPollRequestsCount;
                if (waitingPollRequestsCount == maxWaitingPollRequests) {
                    thisThreadCanWait = true;
                    canWait = false;
                } else if (waitingPollRequestsCount > maxWaitingPollRequests) {
                    thisThreadCanWait = false;
                    --waitingPollRequestsCount;
                    canWait = false;
                } else {
                    thisThreadCanWait = true;
                }
            }
            if (thisThreadCanWait) {
                String userAgentValue = FlexContext.getHttpRequest().getHeader(UserAgentManager.USER_AGENT_HEADER_NAME);
                UserAgentSettings agentSettings = userAgentManager.match(userAgentValue);
                synchronized (session) {
                    if (agentSettings != null) session.maxConnectionsPerSession = agentSettings.getMaxPersistentConnectionsPerSession();
                    ++session.streamingConnectionsCount;
                    if (session.streamingConnectionsCount <= session.maxConnectionsPerSession) {
                        thisThreadCanWait = true;
                    } else {
                        thisThreadCanWait = false;
                        --session.streamingConnectionsCount;
                    }
                }
                if (!thisThreadCanWait) {
                    synchronized (lock) {
                        --waitingPollRequestsCount;
                        if (waitingPollRequestsCount < maxWaitingPollRequests) canWait = true;
                    }
                    if (Log.isDebug()) {
                        log.debug("Max long-polling requests per session limit (" + session.maxConnectionsPerSession + ") has been reached, this poll won't wait.");
                    }
                }
            }
            if (thisThreadCanWait) {
                if (Log.isDebug()) log.debug("Number of waiting threads for endpoint with id '" + getId() + "' is " + waitingPollRequestsCount + ".");
                try {
                    flushResult = flexClient.pollWithWait(getId(), FlexContext.getFlexSession(), this, waitInterval);
                    if (flushResult != null) {
                        if ((flushResult instanceof PollFlushResult) && ((PollFlushResult) flushResult).isAvoidBusyPolling() && (flushResult.getNextFlushWaitTimeMillis() < DEFAULT_WAIT_FOR_EXCESS_POLL_WAIT_CLIENTS)) {
                            flushResult.setNextFlushWaitTimeMillis(DEFAULT_WAIT_FOR_EXCESS_POLL_WAIT_CLIENTS);
                        } else if ((clientWaitInterval > 0) && (flushResult.getNextFlushWaitTimeMillis() == 0)) {
                            flushResult.setNextFlushWaitTimeMillis(clientWaitInterval);
                        }
                    }
                } finally {
                    synchronized (lock) {
                        --waitingPollRequestsCount;
                        if (waitingPollRequestsCount < maxWaitingPollRequests) canWait = true;
                    }
                    synchronized (session) {
                        --session.streamingConnectionsCount;
                    }
                    if (Log.isDebug()) log.debug("Number of waiting threads for endpoint with id '" + getId() + "' is " + waitingPollRequestsCount + ".");
                }
            }
        } else if (Log.isDebug() && waitEnabled) {
            if (pollCommand.headerExists(CommandMessage.SUPPRESS_POLL_WAIT_HEADER)) log.debug("Suppressing poll wait for this request because it is part of a batch of messages to process."); else log.debug("Max waiting poll requests limit '" + maxWaitingPollRequests + "' has been reached for endpoint '" + getId() + "'. FlexClient with id '" + flexClient.getId() + "' will poll with no wait.");
        }
        if (flushResult == null) {
            flushResult = super.handleFlexClientPoll(flexClient, pollCommand);
            if (waitEnabled && (pollingIntervalMillis < DEFAULT_WAIT_FOR_EXCESS_POLL_WAIT_CLIENTS)) {
                if (flushResult == null) {
                    flushResult = new FlushResult();
                }
                flushResult.setNextFlushWaitTimeMillis(DEFAULT_WAIT_FOR_EXCESS_POLL_WAIT_CLIENTS);
            }
        }
        return flushResult;
    }
}
