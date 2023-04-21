package net.jxta.impl.endpoint.router;

import net.jxta.document.Advertisement;
import net.jxta.document.AdvertisementFactory;
import net.jxta.document.XMLElement;
import net.jxta.endpoint.EndpointAddress;
import net.jxta.endpoint.EndpointListener;
import net.jxta.endpoint.EndpointService;
import net.jxta.endpoint.Message;
import net.jxta.endpoint.MessageReceiver;
import net.jxta.endpoint.MessageSender;
import net.jxta.endpoint.MessageTransport;
import net.jxta.endpoint.Messenger;
import net.jxta.endpoint.MessengerEvent;
import net.jxta.endpoint.MessengerEventListener;
import net.jxta.exception.PeerGroupException;
import net.jxta.id.ID;
import net.jxta.id.IDFactory;
import net.jxta.impl.endpoint.LoopbackMessenger;
import net.jxta.impl.util.TimeUtils;
import net.jxta.impl.util.TimerThreadNamer;
import net.jxta.logging.Logging;
import net.jxta.peer.PeerID;
import net.jxta.peergroup.PeerGroup;
import net.jxta.platform.Module;
import net.jxta.protocol.AccessPointAdvertisement;
import net.jxta.protocol.ModuleImplAdvertisement;
import net.jxta.protocol.PeerAdvertisement;
import net.jxta.protocol.RouteAdvertisement;
import net.jxta.service.Service;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;

public class EndpointRouter implements EndpointListener, MessageReceiver, MessageSender, MessengerEventListener, Module {

    /**
     * Logger
     */
    private static final transient Logger LOG = Logger.getLogger(EndpointRouter.class.getName());

    /**
     * Until we decide otherwise, the router is *by definition* handling
     * virtually addressed messages.
     */
    private static final String ROUTER_PROTOCOL_NAME = "jxta";

    /**
     * Router Service Name
     */
    private static final String ROUTER_SERVICE_NAME = "EndpointRouter";

    /**
     * how long we are willing to wait for a response from an async
     * getMessenger. We do not wait long at all because it is non-critical
     * that we get the answer synchronously. The goal is to avoid starting
     * a route discovery if there's a chance to get a direct connection.
     * However, we will still take advantage of the direct route if it is
     * found while we wait for the route discovery result. If that happens,
     * the only wrong is that we used some bandwidth doing a route discovery
     * that wasn't needed after all.
     */
    public static final long ASYNC_MESSENGER_WAIT = 3L * TimeUtils.ASECOND;

    /**
     * MessageTransport Control operation
     */
    public static final Integer GET_ROUTE_CONTROL = 0;

    public static final int RouteControlOp = 0;

    /**
     * MAX timeout (seconds) for route discovery after that timeout
     * the peer will bail out from finding a route
     */
    private static final long MAX_FINDROUTE_TIMEOUT = 60L * TimeUtils.ASECOND;

    /**
     * How long do we wait (seconds) before retrying to make a connection
     * to an endpoint destination
     */
    private static final long MAX_ASYNC_GETMESSENGER_RETRY = 30L * TimeUtils.ASECOND;

    /**
     * These are peers which we know multi-hop routes for.
     */
    private final Map<ID, RouteAdvertisement> routedRoutes = new HashMap<ID, RouteAdvertisement>(16);

    /**
     * A record of failures.
     * <p/>
     * Values are the time of failure as {@link java.lang.Long}. If
     * {@code Long.MAX_VALUE} then a connect attempt is current in progress.
     */
    private final Map<PeerID, Long> triedAndFailed = new HashMap<PeerID, Long>();

    /**
     * local peer ID as an endpointAddress.
     */
    private EndpointAddress localPeerAddr = null;

    /**
     * local Peer ID
     */
    private PeerID localPeerId = null;

    /**
     * The Endpoint Service we are routing for.
     */
    private EndpointService endpoint = null;

    /**
     * PeerGroup handle
     */
    private PeerGroup group = null;

    private ID assignedID = null;

    /**
     * If {@code true} this service has been closed.
     */
    private boolean stopped = false;

    /**
     * Whenever we initiate connectivity to a peer (creating a direct route).
     * we remember that we need to send our route adv to that peer. So that
     * it has a chance to re-establish the connection from its side, if need
     * be. The route adv is actually sent piggy-backed on the first message
     * that goes there.
     */
    private final Set<EndpointAddress> newDestinations = Collections.synchronizedSet(new HashSet<EndpointAddress>());

    /**
     * A pool of messengers categorized by logical address.
     * This actually is the direct routes map.
     */
    private Destinations destinations;

    /**
     * A record of expiration time of known bad routes we received a NACK route
     */
    private final Map<EndpointAddress, BadRoute> badRoutes = new HashMap<EndpointAddress, BadRoute>();

    /**
     * We record queries when first started and keep them pending for
     * a while. Threads coming in the meanwhile wait for a result without
     * initiating a query. Thus threads may wait passed the point where
     * the query is no-longer pending, and, although they could initiate
     * a new one, they do not.
     * <p/>
     * However, other threads coming later may initiate a new query. So a
     * query is not re-initiated systematically on a fixed schedule. This
     * mechanism also serves to avoid infinite recursions if we're looking
     * for the route to a rendezvous (route queries will try to go there
     * themselves).
     * <p/>
     * FIXME: jice@jxta.org 20020903 this is approximate. We can do
     * cleaner/better than this, but it's already an inexpensive improvement
     * over what used before.
     * <p/>
     * FIXME: tra@jxta.org 20030818 the pending hashmap should be moved
     * in the routeResolver class.
     */
    private final Map<PeerID, ClearPendingQuery> pendingQueries = Collections.synchronizedMap(new HashMap<PeerID, ClearPendingQuery>());

    /**
     * Timer by which we schedule the clearing of pending queries.
     */
    private final Timer timer = new Timer("EndpointRouter Timer", true);

    /**
     * PeerAdv tracking.
     * The peer adv is modified every time a new public address is
     * enabled/disabled. One of such cases is the connection/disconnection
     * from a relay. Since these changes are to the embedded route adv
     * and since we may embed our route adv in messages, we must keep it
     * up-to-date.
     */
    private PeerAdvertisement lastPeerAdv = null;

    private int lastModCount = -1;

    /**
     * Route info for the local peer (updated along with lastPeerAdv).
     */
    private RouteAdvertisement localRoute = null;

    /**
     * Route CM persistent cache
     */
    private RouteCM routeCM = null;

    /**
     * Route Resolver
     */
    private RouteResolver routeResolver;

    class ClearPendingQuery extends TimerTask {

        final PeerID peerID;

        volatile boolean failed = false;

        long nextRouteResolveAt = 0;

        ClearPendingQuery(PeerID peerID) {
            this.peerID = peerID;
            timer.schedule(this, TimeUtils.AMINUTE, 5L * TimeUtils.AMINUTE);
            nextRouteResolveAt = TimeUtils.toAbsoluteTimeMillis(20L * TimeUtils.ASECOND);
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public void run() {
            try {
                if (failed) {
                    pendingQueries.remove(peerID);
                    this.cancel();
                } else {
                    failed = true;
                }
            } catch (Throwable all) {
                if (Logging.SHOW_SEVERE && LOG.isLoggable(Level.SEVERE)) {
                    LOG.log(Level.SEVERE, "Uncaught Throwable in timer task " + Thread.currentThread().getName() + " for " + peerID, all);
                }
            }
        }

        public synchronized boolean isTimeToResolveRoute() {
            if (TimeUtils.toRelativeTimeMillis(nextRouteResolveAt) > 0) {
                return false;
            }
            nextRouteResolveAt = TimeUtils.toAbsoluteTimeMillis(20L * TimeUtils.ASECOND);
            return true;
        }

        public boolean isFailed() {
            return failed;
        }
    }

    RouteAdvertisement getMyLocalRoute() {
        PeerAdvertisement newPadv = group.getPeerAdvertisement();
        int newModCount = newPadv.getModCount();
        if ((lastPeerAdv != newPadv) || (lastModCount != newModCount) || (null == localRoute)) {
            lastPeerAdv = newPadv;
            lastModCount = newModCount;
        } else {
            return localRoute;
        }
        XMLElement endpParam = (XMLElement) newPadv.getServiceParam(PeerGroup.endpointClassID);
        if (endpParam == null) {
            if (Logging.SHOW_SEVERE && LOG.isLoggable(Level.SEVERE)) {
                LOG.severe("no Endpoint SVC Params");
            }
            return localRoute;
        }
        Enumeration paramChilds = endpParam.getChildren(RouteAdvertisement.getAdvertisementType());
        XMLElement param;
        if (paramChilds.hasMoreElements()) {
            param = (XMLElement) paramChilds.nextElement();
        } else {
            if (Logging.SHOW_SEVERE && LOG.isLoggable(Level.SEVERE)) {
                LOG.severe("no Endpoint Route Adv");
            }
            return localRoute;
        }
        try {
            RouteAdvertisement route = (RouteAdvertisement) AdvertisementFactory.newAdvertisement(param);
            route.setDestPeerID(localPeerId);
            localRoute = route;
        } catch (Exception ex) {
            if (Logging.SHOW_WARNING && LOG.isLoggable(Level.WARNING)) {
                LOG.log(Level.WARNING, "Failure extracting route", ex);
            }
        }
        return localRoute;
    }

    /**
     * listener object to synchronize on asynchronous getMessenger
     */
    private static class EndpointGetMessengerAsyncListener implements MessengerEventListener {

        private final EndpointRouter router;

        private final EndpointAddress logDest;

        volatile boolean hasResponse = false;

        volatile boolean isGone = false;

        private Messenger messenger = null;

        /**
         * Constructor
         *
         * @param router the router
         * @param dest   logical destination
         */
        EndpointGetMessengerAsyncListener(EndpointRouter router, EndpointAddress dest) {
            this.router = router;
            this.logDest = dest;
        }

        /**
         * {@inheritDoc}
         */
        public boolean messengerReady(MessengerEvent event) {
            Messenger toClose = null;
            synchronized (this) {
                hasResponse = true;
                if (event != null) {
                    messenger = event.getMessenger();
                    if (null != messenger) {
                        if (!logDest.equals(messenger.getLogicalDestinationAddress())) {
                            if (Logging.SHOW_WARNING && LOG.isLoggable(Level.WARNING)) {
                                LOG.warning("Incorrect Messenger logical destination : " + logDest + "!=" + messenger.getLogicalDestinationAddress());
                            }
                            toClose = messenger;
                            messenger = null;
                        }
                    } else {
                        if (Logging.SHOW_WARNING && LOG.isLoggable(Level.WARNING)) {
                            LOG.warning("null messenger for dest :" + logDest);
                        }
                    }
                } else {
                    if (Logging.SHOW_WARNING && LOG.isLoggable(Level.WARNING)) {
                        LOG.warning("null messenger event for dest :" + logDest);
                    }
                }
            }
            if (Logging.SHOW_FINE && LOG.isLoggable(Level.FINE)) {
                if (messenger == null) {
                    LOG.fine("error creating messenger for dest :" + logDest);
                } else {
                    LOG.fine("got a new messenger for dest :" + logDest);
                }
            }
            if (messenger == null) {
                if (toClose != null) {
                    toClose.close();
                }
                router.noMessenger(logDest);
                synchronized (this) {
                    notify();
                }
                return false;
            }
            synchronized (this) {
                if (!isGone) {
                    notify();
                    return true;
                }
            }
            if (Logging.SHOW_FINE && LOG.isLoggable(Level.FINE)) {
                LOG.fine("async caller gone add the messenger " + logDest);
            }
            return router.newMessenger(event);
        }

        /**
         * Wait on the async call for ASYNC_MESSENGER_WAIT
         * then bailout. The messenger will be added whenever
         * the async getMessenger will return
         *
         * @param quick if true return a messenger immediately if available,
         *              otherwise wait the Messenger resolution to be completed
         * @return the Messenger if one available
         */
        public synchronized Messenger waitForMessenger(boolean quick) {
            if (!quick) {
                long quitAt = TimeUtils.toAbsoluteTimeMillis(ASYNC_MESSENGER_WAIT);
                while (TimeUtils.toRelativeTimeMillis(quitAt) > 0) {
                    try {
                        if (hasResponse) {
                            break;
                        }
                        wait(ASYNC_MESSENGER_WAIT);
                    } catch (InterruptedException woken) {
                        Thread.interrupted();
                        break;
                    }
                }
            }
            isGone = true;
            return messenger;
        }
    }

    /**
     * isLocalRoute is a shallow test. It tells you that there used to be a
     * local route that worked the last time it was tried.
     *
     * @param peerAddress Address of the destination who's route is desired.
     * @return {@code true} if we know a direct route to the specified address
     *         otherwise {@code false}.
     */
    boolean isLocalRoute(EndpointAddress peerAddress) {
        return destinations.isCurrentlyReachable(peerAddress);
    }

    /**
     * Get a Messenger for the specified destination if a direct route is known.
     *
     * @param peerAddress The peer who's messenger is desired.
     * @param hint        A route hint to use if a new Messenger must be created.
     * @return Messenger for direct route or {@code null} if none could be
     *         found or created.
     */
    Messenger ensureLocalRoute(EndpointAddress peerAddress, RouteAdvertisement hint) {
        Messenger messenger = destinations.getCurrentMessenger(peerAddress);
        if (messenger != null) {
            return messenger;
        }
        messenger = findReachableEndpoint(peerAddress, false, hint);
        if (messenger == null) {
            destinations.noOutgoingMessenger(peerAddress);
            return null;
        }
        destinations.addOutgoingMessenger(peerAddress, messenger);
        synchronized (this) {
            notifyAll();
        }
        return messenger;
    }

    /**
     * Send a message to a given logical destination if it maps to some
     * messenger in our messenger pool or if such a mapping can be found and
     * added.
     *
     * @param destination peer-based address to send the message to.
     * @param message     the message to be sent.
     * @throws java.io.IOException if an io error occurs
     */
    void sendOnLocalRoute(EndpointAddress destination, Message message) throws IOException {
        IOException lastIoe = null;
        Messenger sendVia;
        while ((sendVia = ensureLocalRoute(destination, null)) != null) {
            if (Logging.SHOW_FINE && LOG.isLoggable(Level.FINE)) {
                LOG.fine("Sending " + message + " to " + destination + " via " + sendVia);
            }
            try {
                sendVia.sendMessageB(message, EndpointRouter.ROUTER_SERVICE_NAME, null);
                if (Logging.SHOW_FINE && LOG.isLoggable(Level.FINE)) {
                    LOG.fine("Sent " + message + " to " + destination);
                }
                return;
            } catch (IOException ioe) {
                lastIoe = ioe;
            }
            if (Logging.SHOW_FINE && LOG.isLoggable(Level.FINE)) {
                LOG.fine("Trying next messenger to " + destination);
            }
        }
        if (lastIoe == null) {
            lastIoe = new IOException("No reachable endpoints for " + destination);
        }
        if (Logging.SHOW_FINE && LOG.isLoggable(Level.FINE)) {
            LOG.log(Level.FINE, "Could not send to " + destination, lastIoe);
        }
        throw lastIoe;
    }

    /**
     * Default constructor
     */
    public EndpointRouter() {
    }

    /**
     * {@inheritDoc}
     */
    public void init(PeerGroup group, ID assignedID, Advertisement impl) throws PeerGroupException {
        timer.schedule(new TimerThreadNamer("EndpointRouter Timer for " + group.getPeerGroupID()), 0);
        this.group = group;
        this.assignedID = assignedID;
        ModuleImplAdvertisement implAdvertisement = (ModuleImplAdvertisement) impl;
        localPeerId = group.getPeerID();
        localPeerAddr = pid2addr(group.getPeerID());
        if (Logging.SHOW_CONFIG && LOG.isLoggable(Level.CONFIG)) {
            StringBuilder configInfo = new StringBuilder("Configuring Router Transport : " + assignedID);
            if (implAdvertisement != null) {
                configInfo.append("\n\tImplementation :");
                configInfo.append("\n\t\tModule Spec ID: ").append(implAdvertisement.getModuleSpecID());
                configInfo.append("\n\t\tImpl Description : ").append(implAdvertisement.getDescription());
                configInfo.append("\n\t\tImpl URI : ").append(implAdvertisement.getUri());
                configInfo.append("\n\t\tImpl Code : ").append(implAdvertisement.getCode());
            }
            configInfo.append("\n\tGroup Params :");
            configInfo.append("\n\t\tGroup : ").append(group);
            configInfo.append("\n\t\tPeer ID : ").append(group.getPeerID());
            configInfo.append("\n\tConfiguration :");
            configInfo.append("\n\t\tProtocol : ").append(getProtocolName());
            configInfo.append("\n\t\tPublic Address : ").append(localPeerAddr);
            LOG.config(configInfo.toString());
        }
    }

    /**
     * {@inheritDoc}
     */
    public synchronized int startApp(String[] arg) {
        endpoint = group.getEndpointService();
        if (null == endpoint) {
            if (Logging.SHOW_WARNING && LOG.isLoggable(Level.WARNING)) {
                LOG.warning("Stalled until there is an endpoint service");
            }
            return START_AGAIN_STALLED;
        }
        Service needed = group.getResolverService();
        if (null == needed) {
            if (Logging.SHOW_WARNING && LOG.isLoggable(Level.WARNING)) {
                LOG.warning("Endpoint Router start stalled until resolver service available");
            }
            return Module.START_AGAIN_STALLED;
        }
        needed = group.getMembershipService();
        if (null == needed) {
            if (Logging.SHOW_WARNING && LOG.isLoggable(Level.WARNING)) {
                LOG.warning("Endpoint Router start stalled until membership service available");
            }
            return Module.START_AGAIN_STALLED;
        }
        needed = group.getRendezVousService();
        if (null == needed) {
            if (Logging.SHOW_WARNING && LOG.isLoggable(Level.WARNING)) {
                LOG.warning("Endpoint Router start stalled until rendezvous service available");
            }
            return Module.START_AGAIN_STALLED;
        }
        destinations = new Destinations(endpoint);
        try {
            routeCM = new RouteCM();
            routeResolver = new RouteResolver(this);
            routeCM.init(group, assignedID, null);
            routeResolver.init(group, assignedID, null);
        } catch (PeerGroupException failure) {
            return -1;
        }
        int status;
        status = routeCM.startApp(arg);
        if (status != 0) {
            if (Logging.SHOW_WARNING && LOG.isLoggable(Level.WARNING)) {
                LOG.warning("Route CM failed to start : " + status);
            }
            return status;
        }
        status = routeResolver.startApp(arg);
        if (status != 0) {
            if (Logging.SHOW_WARNING && LOG.isLoggable(Level.WARNING)) {
                LOG.warning("Route Resolver failed to start : " + status);
            }
            return status;
        }
        routeCM.publishRoute(getMyLocalRoute());
        endpoint.addMessengerEventListener(this, EndpointService.MediumPrecedence);
        endpoint.addIncomingMessageListener(this, ROUTER_SERVICE_NAME, null);
        if (endpoint.addMessageTransport(this) == null) {
            if (Logging.SHOW_SEVERE && LOG.isLoggable(Level.SEVERE)) {
                LOG.severe("Transport registration refused");
            }
            return -1;
        }
        if (Logging.SHOW_INFO && LOG.isLoggable(Level.INFO)) {
            LOG.info(group + " : Router Message Transport started.");
        }
        return status;
    }

    /**
     * {@inheritDoc}
     * <p/>
     * Careful that stopApp() could in theory be called before startApp().
     */
    public synchronized void stopApp() {
        stopped = true;
        if (endpoint != null) {
            endpoint.removeIncomingMessageListener(ROUTER_SERVICE_NAME, null);
            endpoint.removeMessengerEventListener(this, EndpointService.MediumPrecedence);
            endpoint.removeMessageTransport(this);
        }
        routeCM.stopApp();
        routeResolver.stopApp();
        destinations.close();
        if (Logging.SHOW_INFO && LOG.isLoggable(Level.INFO)) {
            LOG.info(group + " : Router Message Transport stopped.");
        }
    }

    /**
     * {@inheritDoc}
     */
    public boolean isConnectionOriented() {
        return false;
    }

    /**
     * {@inheritDoc}
     */
    public boolean allowsRouting() {
        return false;
    }

    /**
     * {@inheritDoc}
     */
    public EndpointService getEndpointService() {
        return endpoint;
    }

    /**
     * {@inheritDoc}
     */
    public EndpointAddress getPublicAddress() {
        return localPeerAddr;
    }

    /**
     * {@inheritDoc}
     */
    public Iterator<EndpointAddress> getPublicAddresses() {
        return Collections.singletonList(getPublicAddress()).iterator();
    }

    /**
     * {@inheritDoc}
     */
    public String getProtocolName() {
        return ROUTER_PROTOCOL_NAME;
    }

    /**
     * Given a peer id, return an address to reach that peer.
     * The address may be for a directly reachable peer, or
     * for the first gateway along a route to reach the peer.
     * If we do not have a route to the peer, we will use the
     * Peer Routing Protocol to try to discover one.  We will
     * wait up to 30 seconds for a route to be discovered.
     *
     * @param peerAddress the peer we are trying to reach.
     * @param seekRoute   whether to go as far as issuing a route query, or just fish in our cache.
     *                    when forwarding a message we allow ourselves to mend a broken source-issued route but we
     *                    won't go as far as seeking one from other peers. When originating a message, on the other end
     *                    we will aggressively try to find route.
     * @param hint        whether we are passed a route hint to be used, in that case that route
     *                    hint should be used
     * @return an EndpointAddress at which that peer should be reachable.
     */
    EndpointAddress getGatewayAddress(EndpointAddress peerAddress, boolean seekRoute, RouteAdvertisement hint) {
        PeerID peerID = addr2pid(peerAddress);
        try {
            if (Logging.SHOW_FINE && LOG.isLoggable(Level.FINE)) {
                LOG.fine("Searching local" + (seekRoute ? " & remote" : "") + " for route for " + peerAddress);
            }
            long quitAt = TimeUtils.toAbsoluteTimeMillis(MAX_FINDROUTE_TIMEOUT);
            long findRouteAt = TimeUtils.toAbsoluteTimeMillis(ASYNC_MESSENGER_WAIT);
            EndpointAddress addr;
            while (TimeUtils.toRelativeTimeMillis(quitAt) > 0) {
                Messenger directMessenger = ensureLocalRoute(peerAddress, hint);
                if (null != directMessenger) {
                    if (Logging.SHOW_FINE && LOG.isLoggable(Level.FINE)) {
                        LOG.fine("Found direct route for " + peerAddress + " via " + directMessenger.getDestinationAddress());
                    }
                    return peerAddress;
                }
                RouteAdvertisement route;
                if (hint != null) {
                    route = hint;
                } else {
                    route = getRoute(peerAddress, seekRoute);
                }
                if (route != null && route.size() > 0) {
                    addr = pid2addr(route.getLastHop().getPeerID());
                    if (ensureLocalRoute(addr, null) != null) {
                        if (Logging.SHOW_FINE && LOG.isLoggable(Level.FINE)) {
                            LOG.fine("Found last hop remote address: " + peerAddress + " -> " + route.getLastHop().getPeerID());
                        }
                        return addr;
                    } else {
                        addr = pid2addr(route.getFirstHop().getPeerID());
                        if (ensureLocalRoute(addr, null) != null) {
                            if (Logging.SHOW_FINE && LOG.isLoggable(Level.FINE)) {
                                LOG.fine("Found first hop remote address first hop: " + peerAddress + " -> " + route.getFirstHop().getPeerID());
                            }
                            return addr;
                        } else {
                            removeRoute(peerID);
                            if (Logging.SHOW_FINE && LOG.isLoggable(Level.FINE)) {
                                LOG.fine("Found no reachable route to " + peerAddress);
                            }
                        }
                    }
                }
                if (!seekRoute) {
                    break;
                }
                if (!routeResolver.useRouteResolver()) {
                    break;
                }
                Long nextTry = triedAndFailed.get(peerID);
                if ((nextTry == null) || (nextTry < TimeUtils.toAbsoluteTimeMillis(MAX_ASYNC_GETMESSENGER_RETRY)) || (TimeUtils.toRelativeTimeMillis(findRouteAt) <= 0)) {
                    boolean doFind = false;
                    ClearPendingQuery t;
                    synchronized (pendingQueries) {
                        t = pendingQueries.get(peerID);
                        if (t == null) {
                            doFind = true;
                            t = new ClearPendingQuery(peerID);
                            pendingQueries.put(peerID, t);
                        } else {
                            if (t.isFailed()) {
                                break;
                            }
                            if (t.isTimeToResolveRoute()) {
                                doFind = true;
                            }
                        }
                    }
                    if (doFind) {
                        routeResolver.findRoute(peerAddress);
                        seekRoute = false;
                    }
                }
                synchronized (this) {
                    try {
                        if (destinations.getCurrentMessenger(peerAddress) == null) {
                            wait(ASYNC_MESSENGER_WAIT);
                        }
                    } catch (InterruptedException woken) {
                        Thread.interrupted();
                    }
                }
            }
            if (Logging.SHOW_FINE && LOG.isLoggable(Level.FINE)) {
                LOG.fine("No route to " + peerAddress);
            }
            return null;
        } catch (Exception ex) {
            if (Logging.SHOW_WARNING && LOG.isLoggable(Level.WARNING)) {
                LOG.log(Level.WARNING, "getGatewayAddress exception", ex);
            }
            return null;
        }
    }

    /**
     * {@inheritDoc}
     */
    @Deprecated
    public boolean ping(EndpointAddress addr) {
        EndpointAddress plainAddr = new EndpointAddress(addr, null, null);
        try {
            return (getGatewayAddress(plainAddr, true, null) != null);
        } catch (Exception e) {
            if (Logging.SHOW_WARNING && LOG.isLoggable(Level.WARNING)) {
                LOG.log(Level.FINE, "Ping failure (exception) for : " + plainAddr, e);
            }
            return false;
        }
    }

    /**
     * Receives notifications of new messengers being generated by the
     * underlying network transports.
     * <p/>
     * IMPORTANT: Incoming messengers only. If/when this is used for
     * outgoing, some things have to change:
     * <p/>
     * For example we do not need to send the welcome msg, but for
     * outgoing messengers, we would need to.
     * <p/>
     * Currently, newMessenger handles the outgoing side.
     *
     * @param event the new messenger event.
     */
    public boolean messengerReady(MessengerEvent event) {
        Messenger messenger = event.getMessenger();
        Object source = event.getSource();
        EndpointAddress logDest = messenger.getLogicalDestinationAddress();
        if (source instanceof MessageSender && !((MessageSender) source).allowsRouting()) {
            if (Logging.SHOW_FINE && LOG.isLoggable(Level.FINE)) {
                LOG.fine("Ignoring messenger to :" + logDest);
            }
            return false;
        }
        boolean taken = destinations.addIncomingMessenger(logDest, messenger);
        synchronized (this) {
            notifyAll();
        }
        return taken;
    }

    /**
     * Call when an asynchronous new messenger could not be obtained.
     *
     * @param logDest the failed logical destination
     */
    void noMessenger(EndpointAddress logDest) {
        PeerID peerID = addr2pid(logDest);
        synchronized (this) {
            Long curr = triedAndFailed.get(peerID);
            if (curr != null && curr > TimeUtils.toAbsoluteTimeMillis(MAX_ASYNC_GETMESSENGER_RETRY)) {
                triedAndFailed.put(peerID, TimeUtils.toAbsoluteTimeMillis(MAX_ASYNC_GETMESSENGER_RETRY));
            }
        }
    }

    /**
     * Call when an asynchronous new messenger is ready.
     * (name is not great).
     *
     * @param event the new messenger event.
     * @return always returns true
     */
    boolean newMessenger(MessengerEvent event) {
        Messenger messenger = event.getMessenger();
        EndpointAddress logDest = messenger.getLogicalDestinationAddress();
        destinations.addOutgoingMessenger(logDest, messenger);
        synchronized (this) {
            notifyAll();
        }
        return true;
    }

    /**
     * Get the routed route, if any, for a given peer id.
     *
     * @param peerAddress the peer who's route is desired.
     * @param seekRoute   boolean to indicate  if we should search for a route
     *                    if we don't have one
     * @return a route advertisement describing the direct route to the peer.
     */
    RouteAdvertisement getRoute(EndpointAddress peerAddress, boolean seekRoute) {
        ID peerID = addr2pid(peerAddress);
        RouteAdvertisement route;
        synchronized (this) {
            route = routedRoutes.get(peerID);
        }
        if (route != null || !seekRoute) {
            return route;
        }
        Iterator<RouteAdvertisement> allRadvs = routeCM.getRouteAdv(peerID);
        while (allRadvs.hasNext()) {
            route = allRadvs.next();
            Vector<AccessPointAdvertisement> hops = route.getVectorHops();
            if (hops.isEmpty()) {
                continue;
            }
            RouteAdvertisement newRoute = (RouteAdvertisement) AdvertisementFactory.newAdvertisement(RouteAdvertisement.getAdvertisementType());
            newRoute.setDest(route.getDest().clone());
            Vector<AccessPointAdvertisement> newHops = new Vector<AccessPointAdvertisement>();
            for (int i = hops.size() - 1; i >= 0; i--) {
                ID hopID = hops.elementAt(i).getPeerID();
                if (localPeerId.equals(hopID)) {
                    break;
                }
                EndpointAddress addr = pid2addr(hopID);
                if (ensureLocalRoute(addr, null) != null) {
                    for (int j = i; j <= hops.size() - 1; j++) {
                        newHops.add(hops.elementAt(j).clone());
                    }
                    if (newHops.isEmpty()) {
                        break;
                    }
                    newRoute.setHops(newHops);
                    if (setRoute(newRoute, false)) {
                        return newRoute;
                    } else {
                        break;
                    }
                }
            }
        }
        return null;
    }

    /**
     * Check if a route is valid.
     * Currently, only loops are detected.
     *
     * @param routeAdvertisement The route to check.
     * @return {@code true} if the route is valid otherwise {@code false}.
     */
    private boolean checkRoute(RouteAdvertisement routeAdvertisement) {
        if (0 == routeAdvertisement.size()) {
            if (Logging.SHOW_FINE && LOG.isLoggable(Level.FINE)) {
                LOG.fine("route is empty");
            }
            return false;
        }
        if (routeAdvertisement.containsHop(localPeerId)) {
            if (Logging.SHOW_FINE && LOG.isLoggable(Level.FINE)) {
                LOG.fine("route contains this peer - loopback");
            }
            return false;
        }
        PeerID destPid = routeAdvertisement.getDest().getPeerID();
        if (routeAdvertisement.containsHop(destPid)) {
            Vector<AccessPointAdvertisement> hops = routeAdvertisement.getVectorHops();
            hops.remove(hops.lastElement());
            if (routeAdvertisement.containsHop(destPid)) {
                return false;
            }
        }
        if (routeAdvertisement.hasALoop()) {
            if (Logging.SHOW_FINE && LOG.isLoggable(Level.FINE)) {
                LOG.fine("route has a loop ");
            }
            return false;
        } else {
            if (Logging.SHOW_FINE && LOG.isLoggable(Level.FINE)) {
                LOG.fine("route is ok");
            }
            return true;
        }
    }

    /**
     * set new route info
     *
     * @param route new route to learn
     * @param force true if the route was obtained by receiving
     *              a message
     * @return true if route was truly new
     */
    boolean setRoute(RouteAdvertisement route, boolean force) {
        PeerID peerID;
        EndpointAddress peerAddress;
        boolean pushNeeded = false;
        boolean status;
        if (Logging.SHOW_FINE && LOG.isLoggable(Level.FINE)) {
            LOG.fine("setRoute:");
        }
        if (route == null) {
            return false;
        }
        synchronized (this) {
            try {
                if (Logging.SHOW_FINE && LOG.isLoggable(Level.FINE)) {
                    LOG.fine(route.display());
                }
                peerID = route.getDest().getPeerID();
                peerAddress = pid2addr(peerID);
                if (!force) {
                    BadRoute badRoute = badRoutes.get(peerAddress);
                    if (badRoute != null) {
                        Long nextTry = badRoute.getExpiration();
                        if (nextTry > System.currentTimeMillis()) {
                            RouteAdvertisement routeClean = route.cloneOnlyPIDs();
                            if (routeClean.equals(badRoute.getRoute())) {
                                if (Logging.SHOW_FINE && LOG.isLoggable(Level.FINE)) {
                                    LOG.fine("try to use a known bad route");
                                }
                                return false;
                            }
                        } else {
                            badRoutes.remove(peerAddress);
                        }
                    }
                } else {
                    badRoutes.remove(peerAddress);
                }
                if (!checkRoute(route)) {
                    if (Logging.SHOW_FINE && LOG.isLoggable(Level.FINE)) {
                        LOG.fine("Route is invalid");
                    }
                    return false;
                }
                if (!isLocalRoute(pid2addr(route.getFirstHop().getPeerID()))) {
                    if (Logging.SHOW_FINE && LOG.isLoggable(Level.FINE)) {
                        LOG.fine("Unreachable route - ignore");
                    }
                    return false;
                }
            } catch (Exception ez1) {
                if (Logging.SHOW_FINE && LOG.isLoggable(Level.FINE)) {
                    LOG.fine("Got an empty route - discard" + route.display());
                }
                return false;
            }
            try {
                if (group.isRendezvous()) {
                    if (!routedRoutes.containsKey(peerID)) {
                        if (Logging.SHOW_FINE && LOG.isLoggable(Level.FINE)) {
                            LOG.fine("push new SRDI route " + peerID);
                        }
                        pushNeeded = true;
                    }
                }
                if (!routedRoutes.containsKey(peerID)) {
                    routeCM.createRoute(route);
                    newDestinations.add(peerAddress);
                }
                RouteAdvertisement newRoute = route.cloneOnlyPIDs();
                routedRoutes.put(peerID, newRoute);
                badRoutes.remove(peerAddress);
                notifyAll();
                status = true;
            } catch (Exception e2) {
                if (Logging.SHOW_FINE && LOG.isLoggable(Level.FINE)) {
                    LOG.fine("   failed setting route with " + e2);
                }
                status = false;
            }
        }
        if (pushNeeded && status) {
            routeResolver.pushSrdi(null, peerID);
        }
        return status;
    }

    /**
     * This method is used to remove a route
     *
     * @param peerID route to peerid to be removed
     */
    void removeRoute(PeerID peerID) {
        boolean needRemove;
        synchronized (this) {
            needRemove = false;
            if (routedRoutes.containsKey(peerID)) {
                if (group.isRendezvous()) {
                    needRemove = true;
                    if (Logging.SHOW_FINE && LOG.isLoggable(Level.FINE)) {
                        LOG.fine("remove SRDI route " + peerID);
                    }
                }
                routedRoutes.remove(peerID);
            }
        }
        if (needRemove) {
            routeResolver.removeSrdi(null, peerID);
        }
    }

    /**
     * {@inheritDoc}
     */
    public void processIncomingMessage(Message msg, EndpointAddress srcAddr, EndpointAddress dstAddr) {
        EndpointAddress srcPeerAddress;
        EndpointAddress destPeer;
        EndpointAddress lastHop = null;
        boolean connectLastHop = false;
        EndpointAddress origSrcAddr;
        EndpointAddress origDstAddr;
        Vector origHops = null;
        EndpointRouterMessage routerMsg;
        EndpointAddress nextHop = null;
        RouteAdvertisement radv;
        routerMsg = new EndpointRouterMessage(msg, false);
        if (!routerMsg.msgExists()) {
            if (Logging.SHOW_FINE && LOG.isLoggable(Level.WARNING)) {
                LOG.warning("Discarding " + msg + ". No routing info.");
            }
            return;
        }
        try {
            if (Logging.SHOW_FINE && LOG.isLoggable(Level.FINE)) {
                LOG.fine(routerMsg.display());
            }
            origSrcAddr = routerMsg.getSrcAddress();
            origDstAddr = routerMsg.getDestAddress();
            srcPeerAddress = new EndpointAddress(origSrcAddr, null, null);
            destPeer = new EndpointAddress(origDstAddr, null, null);
            lastHop = routerMsg.getLastHop();
            radv = routerMsg.getRouteAdv();
            if (radv != null) {
                if (pid2addr(radv.getDestPeerID()).equals(lastHop)) {
                    connectLastHop = true;
                }
                setRoute(radv, true);
                updateRouteAdv(radv);
            }
        } catch (Exception badHdr) {
            if (Logging.SHOW_WARNING && LOG.isLoggable(Level.WARNING)) {
                LOG.warning("Bad routing header or bad message. Dropping " + msg);
            }
            if (Logging.SHOW_FINE && LOG.isLoggable(Level.FINE)) {
                LOG.log(Level.FINE, "Exception: ", badHdr);
            }
            return;
        }
        if ((srcPeerAddress != null) && srcPeerAddress.equals(localPeerAddr)) {
            if (Logging.SHOW_FINE && LOG.isLoggable(Level.FINE)) {
                LOG.fine("dropped loopback");
            }
            return;
        }
        if ((lastHop != null) && lastHop.equals(localPeerAddr)) {
            if (Logging.SHOW_FINE && LOG.isLoggable(Level.FINE)) {
                LOG.fine("dropped loopback from impersonating Peer");
            }
            return;
        }
        if (connectLastHop) {
            ensureLocalRoute(lastHop, radv);
        }
        try {
            Vector<AccessPointAdvertisement> reverseHops = routerMsg.getReverseHops();
            if (reverseHops == null) {
                reverseHops = new Vector<AccessPointAdvertisement>();
            }
            if (!isLocalRoute(srcPeerAddress)) {
                if (lastHop != null) {
                    if ((reverseHops.size() > 0) && reverseHops.firstElement().getPeerID().equals(addr2pid(lastHop))) {
                        setRoute(RouteAdvertisement.newRoute(addr2pid(srcPeerAddress), null, (Vector<AccessPointAdvertisement>) reverseHops.clone()), true);
                    }
                }
            }
            if (destPeer.equals(localPeerAddr)) {
                routerMsg.clearAll();
                routerMsg.updateMessage();
                endpoint.processIncomingMessage(msg, origSrcAddr, origDstAddr);
                return;
            }
            AccessPointAdvertisement selfAp = (AccessPointAdvertisement) AdvertisementFactory.newAdvertisement(AccessPointAdvertisement.getAdvertisementType());
            selfAp.setPeerID(localPeerId);
            if (reverseHops.contains(selfAp)) {
                if (Logging.SHOW_WARNING && LOG.isLoggable(Level.WARNING)) {
                    LOG.warning("Routing loop detected. Message dropped");
                }
                removeRoute(addr2pid(destPeer));
                return;
            }
            if (isLocalRoute(lastHop)) {
                reverseHops.add(0, selfAp);
                routerMsg.prependReverseHop(selfAp);
            } else {
                RouteAdvertisement newReverseRoute = routedRoutes.get(addr2pid(srcPeerAddress));
                if (newReverseRoute != null) {
                    reverseHops = (Vector<AccessPointAdvertisement>) newReverseRoute.getVectorHops().clone();
                    reverseHops.add(0, selfAp);
                } else {
                    reverseHops = null;
                }
                routerMsg.setReverseHops(reverseHops);
            }
            origHops = routerMsg.getForwardHops();
            if (origHops != null) {
                nextHop = getNextHop(origHops);
            }
            if (isLocalRoute(destPeer)) {
                routerMsg.setForwardHops(null);
                nextHop = destPeer;
            } else {
                if (nextHop == null) {
                    if (Logging.SHOW_FINE && LOG.isLoggable(Level.FINE)) {
                        LOG.fine("No next hop in forward route - Using destination as next hop");
                    }
                    nextHop = destPeer;
                    routerMsg.setForwardHops(null);
                }
                if (ensureLocalRoute(nextHop, null) == null) {
                    if (Logging.SHOW_FINE && LOG.isLoggable(Level.FINE)) {
                        LOG.fine("Forward route element broken - trying alternate route");
                    }
                    RouteAdvertisement route = getRoute(destPeer, false);
                    if (route == null) {
                        cantRoute("No new route to repair the route - drop message", null, origSrcAddr, destPeer, origHops);
                        return;
                    }
                    if (pid2addr(route.getFirstHop().getPeerID()).equals(nextHop)) {
                        removeRoute(addr2pid(destPeer));
                        cantRoute("No better route to repair the route - drop message", null, origSrcAddr, destPeer, origHops);
                        return;
                    }
                    EndpointAddress addr = pid2addr(route.getLastHop().getPeerID());
                    if (isLocalRoute(addr)) {
                        if (Logging.SHOW_FINE && LOG.isLoggable(Level.FINE)) {
                            LOG.fine("Found new remote route via : " + addr);
                        }
                        routerMsg.setForwardHops(null);
                    } else {
                        Vector<AccessPointAdvertisement> newHops = (Vector<AccessPointAdvertisement>) route.getVectorHops().clone();
                        addr = pid2addr(newHops.remove(0).getPeerID());
                        if (!isLocalRoute(addr)) {
                            removeRoute(addr2pid(destPeer));
                            cantRoute("No usable route to repair the route - drop message", null, origSrcAddr, destPeer, origHops);
                            return;
                        }
                        if (Logging.SHOW_FINE && LOG.isLoggable(Level.FINE)) {
                            LOG.fine("Found new remote route via : " + addr);
                        }
                        routerMsg.setForwardHops(newHops);
                    }
                    nextHop = addr;
                }
            }
            RouteAdvertisement myRoute = getMyLocalRoute();
            if ((myRoute != null) && destinations.isWelcomeNeeded(nextHop)) {
                routerMsg.setRouteAdv(myRoute);
            }
            routerMsg.setLastHop(localPeerAddr);
            routerMsg.updateMessage();
            if (Logging.SHOW_FINE && LOG.isLoggable(Level.FINE)) {
                LOG.fine("Trying to forward to " + nextHop);
            }
            sendOnLocalRoute(nextHop, msg);
            if (Logging.SHOW_FINE && LOG.isLoggable(Level.FINE)) {
                LOG.fine("Successfully forwarded to " + nextHop);
            }
        } catch (Exception e) {
            cantRoute("Failed to deliver or forward message for " + destPeer, e, origSrcAddr, destPeer, origHops);
        }
    }

    private void cantRoute(String logMsg, Exception exception, EndpointAddress origSrcAddr, EndpointAddress destPeer, Vector origHops) {
        if (Logging.SHOW_WARNING && LOG.isLoggable(Level.WARNING)) {
            if (exception == null) {
                LOG.warning(logMsg);
            } else {
                LOG.log(Level.WARNING, logMsg, exception);
            }
        }
        routeResolver.generateNACKRoute(addr2pid(origSrcAddr), addr2pid(destPeer), origHops);
    }

    /**
     * Return the address of the next hop in this vector
     *
     * @param hops of forward hops in the route
     * @return next hop to be used
     */
    private EndpointAddress getNextHop(Vector hops) {
        if ((hops == null) || (hops.size() == 0)) {
            return null;
        }
        for (Enumeration e = hops.elements(); e.hasMoreElements(); ) {
            AccessPointAdvertisement ap = (AccessPointAdvertisement) e.nextElement();
            if (localPeerId.equals(ap.getPeerID())) {
                if (!e.hasMoreElements()) {
                    return null;
                }
                return pid2addr(((AccessPointAdvertisement) e.nextElement()).getPeerID());
            }
        }
        return pid2addr(((AccessPointAdvertisement) hops.elementAt(0)).getPeerID());
    }

    /**
     * lame hard-coding
     *
     * @param p message transport
     * @return true if fast
     */
    private boolean isFast(MessageTransport p) {
        String name = p.getProtocolName();
        return name.equals("tcp") || name.equals("beep");
    }

    private boolean isRelay(MessageTransport p) {
        String name = p.getProtocolName();
        return name.equals("relay");
    }

    /**
     * Given a list of addresses, find the best reachable endpoint.
     * <p/>
     * <ul>
     * <li>The address returned must be reachable.</li>
     * <li>We prefer an address whose protocol is, in order:</li>
     * <ol>
     * <li>connected and fast.</li>
     * <li>connected and slow.</li>
     * <li>unconnected and fast.</li>
     * <li>unconnected and slow</li>
     * </ol></li>
     * </ul>
     *
     * @param dest      destination address.
     * @param mightWork A list of addresses to evaluate reachability.
     * @param exist     true if there already are existing messengers for
     *                  the given destinations but we want one more. It may lead us to reject
     *                  certain addresses that we would otherwise accept.
     * @return The endpoint address for which we found a local route otherwise
     *         null
     */
    Messenger findBestReachableEndpoint(EndpointAddress dest, List<EndpointAddress> mightWork, boolean exist) {
        List<Integer> rankings = new ArrayList<Integer>(mightWork.size());
        List<EndpointAddress> worthTrying = new ArrayList<EndpointAddress>(mightWork.size());
        for (Object aMightWork : mightWork) {
            EndpointAddress addr = (EndpointAddress) aMightWork;
            if (getProtocolName().equals(addr.getProtocolName())) {
                continue;
            }
            int rank = -1;
            Iterator<MessageTransport> eachTransport = endpoint.getAllMessageTransports();
            while (eachTransport.hasNext()) {
                MessageTransport transpt = eachTransport.next();
                if (!transpt.getProtocolName().equals(addr.getProtocolName())) {
                    continue;
                }
                if (!(transpt instanceof MessageSender)) {
                    continue;
                }
                MessageSender sender = (MessageSender) transpt;
                if (!sender.allowsRouting()) {
                    continue;
                }
                rank += 1;
                if (sender.isConnectionOriented()) {
                    rank += 2;
                }
                if (isRelay(transpt)) {
                    if (exist) {
                        rank -= 1000;
                    }
                }
                if (isFast(transpt)) {
                    rank += 4;
                }
            }
            if (rank >= 0) {
                for (int eachCurrent = 0; eachCurrent <= rankings.size(); eachCurrent++) {
                    if (rankings.size() == eachCurrent) {
                        rankings.add(rank);
                        worthTrying.add(addr);
                        break;
                    }
                    if (rank > rankings.get(eachCurrent)) {
                        rankings.add(eachCurrent, rank);
                        worthTrying.add(eachCurrent, addr);
                        break;
                    }
                }
            }
        }
        rankings = null;
        for (EndpointAddress addr : worthTrying) {
            try {
                if (Logging.SHOW_FINE && LOG.isLoggable(Level.FINE)) {
                    LOG.fine("Trying : " + addr);
                }
                EndpointGetMessengerAsyncListener getMessengerListener = new EndpointGetMessengerAsyncListener(this, dest);
                boolean stat = endpoint.getMessenger(getMessengerListener, new EndpointAddress(addr, ROUTER_SERVICE_NAME, null), null);
                if (!stat) {
                    if (Logging.SHOW_FINE && LOG.isLoggable(Level.FINE)) {
                        LOG.fine("Failed to create async messenger to : " + addr);
                    }
                    synchronized (this) {
                        triedAndFailed.put(addr2pid(dest), TimeUtils.toAbsoluteTimeMillis(MAX_ASYNC_GETMESSENGER_RETRY));
                    }
                    continue;
                }
                boolean quick = (getRoute(dest, false) != null);
                Messenger messenger = getMessengerListener.waitForMessenger(quick);
                if (messenger == null) {
                    if (Logging.SHOW_FINE && LOG.isLoggable(Level.FINE)) {
                        LOG.fine("did not get our async messenger. continue");
                    }
                } else {
                    if (Logging.SHOW_FINE && LOG.isLoggable(Level.FINE)) {
                        LOG.fine("we got our async messenger, proceed");
                    }
                    synchronized (this) {
                        triedAndFailed.remove(addr2pid(dest));
                        notifyAll();
                    }
                    return messenger;
                }
            } catch (RuntimeException e) {
                if (Logging.SHOW_FINE && LOG.isLoggable(Level.FINE)) {
                    LOG.log(Level.FINE, "failed checking route", e);
                }
            }
        }
        return null;
    }

    /**
     * Read the route advertisement for a peer and find a suitable transport
     * endpoint for sending to that peer either directly or via one of
     * the advertised peer router
     *
     * @param destPeerAddress dest address
     * @param exist           use existing messengers, avoid creating a new one
     * @param hint            route hint
     * @return a reachable messenger
     */
    Messenger findReachableEndpoint(EndpointAddress destPeerAddress, boolean exist, RouteAdvertisement hint) {
        PeerID destPeerID = addr2pid(destPeerAddress);
        synchronized (this) {
            Long nextTry = triedAndFailed.get(destPeerID);
            if (nextTry != null) {
                if (nextTry > TimeUtils.timeNow()) {
                    return null;
                }
            }
            if (Logging.SHOW_FINE && LOG.isLoggable(Level.FINE)) {
                LOG.fine("Temporarly adding " + destPeerAddress.toString() + " to triedAndFailed, while attempting connection");
            }
            triedAndFailed.put(destPeerID, TimeUtils.toAbsoluteTimeMillis(Long.MAX_VALUE));
        }
        Iterator<RouteAdvertisement> advs;
        try {
            if (hint != null) {
                advs = Collections.singletonList(hint).iterator();
            } else {
                advs = routeCM.getRouteAdv(destPeerID);
            }
            List<EndpointAddress> addrs = new ArrayList<EndpointAddress>();
            while (advs.hasNext()) {
                RouteAdvertisement adv = advs.next();
                String saddr = null;
                for (Enumeration<String> e = adv.getDest().getEndpointAddresses(); e.hasMoreElements(); ) {
                    try {
                        saddr = e.nextElement();
                        addrs.add(new EndpointAddress(saddr));
                    } catch (Throwable ex) {
                        if (Logging.SHOW_FINE && LOG.isLoggable(Level.FINE)) {
                            LOG.fine(" bad address in route adv : " + saddr);
                        }
                    }
                }
            }
            if (!addrs.isEmpty()) {
                Messenger bestMessenger = findBestReachableEndpoint(destPeerAddress, addrs, exist);
                if (bestMessenger != null) {
                    if (Logging.SHOW_FINE && LOG.isLoggable(Level.FINE)) {
                        LOG.fine("found direct route");
                    }
                    return bestMessenger;
                }
            } else {
                if (Logging.SHOW_FINE && LOG.isLoggable(Level.FINE)) {
                    LOG.fine("findReachableEndpoint : Failed due to empty address list");
                }
            }
        } catch (RuntimeException e) {
            if (Logging.SHOW_WARNING && LOG.isLoggable(Level.WARNING)) {
                LOG.log(Level.WARNING, "Failure looking for an address ", e);
            }
        }
        synchronized (this) {
            triedAndFailed.put(destPeerID, TimeUtils.toAbsoluteTimeMillis(MAX_ASYNC_GETMESSENGER_RETRY));
        }
        if (Logging.SHOW_FINE && LOG.isLoggable(Level.FINE)) {
            LOG.fine("did not find a direct route to :" + destPeerAddress);
        }
        return null;
    }

    /**
     * {@inheritDoc}
     */
    public Messenger getMessenger(EndpointAddress addr, Object hint) {
        RouteAdvertisement routeHint = null;
        EndpointAddress plainAddr = new EndpointAddress(addr, null, null);
        if (plainAddr.equals(localPeerAddr)) {
            if (Logging.SHOW_FINE && LOG.isLoggable(Level.FINE)) {
                LOG.fine("return LoopbackMessenger");
            }
            return new LoopbackMessenger(group, endpoint, localPeerAddr, addr, addr);
        }
        try {
            if (hint != null && hint instanceof RouteAdvertisement) {
                routeHint = ((RouteAdvertisement) hint).clone();
                AccessPointAdvertisement firstHop = routeHint.getFirstHop();
                PeerID firstHopPid;
                EndpointAddress firstHopAddr = null;
                if (firstHop != null) {
                    firstHopPid = firstHop.getPeerID();
                    firstHopAddr = pid2addr(firstHopPid);
                    if (firstHopAddr.equals(addr)) {
                        routeHint.removeHop(firstHopPid);
                        firstHop = null;
                    } else if (firstHopPid.equals(localPeerId)) {
                        firstHop = null;
                    }
                }
                if (firstHop == null) {
                    EndpointAddress da = pid2addr(routeHint.getDestPeerID());
                    if (!isLocalRoute(da) && !routedRoutes.containsKey(routeHint.getDestPeerID())) {
                        routeCM.publishRoute(routeHint);
                    }
                } else {
                    RouteAdvertisement routeFirstHop = null;
                    if (!isLocalRoute(firstHopAddr) && !routedRoutes.containsKey(firstHop.getPeerID())) {
                        routeFirstHop = (RouteAdvertisement) AdvertisementFactory.newAdvertisement(RouteAdvertisement.getAdvertisementType());
                        routeFirstHop.setDest(firstHop.clone());
                        updateRouteAdv(routeFirstHop);
                    }
                    if (ensureLocalRoute(firstHopAddr, routeFirstHop) != null) {
                        setRoute(routeHint.clone(), false);
                    }
                }
            }
        } catch (Throwable ioe) {
            return null;
        }
        try {
            return new RouterMessenger(addr, this, routeHint);
        } catch (IOException caught) {
            if (Logging.SHOW_FINE && LOG.isLoggable(Level.FINE)) {
                LOG.log(Level.FINE, "Can\'t generate messenger for addr " + addr, caught);
            }
            return null;
        }
    }

    /**
     * Updates the router element of a message and returns the peerAddress address of
     * the next hop (where to send the message).
     * <p/>
     * Currently, address message is only called for messages that we
     * originate. As a result we will always aggressively seek a route if needed.
     *
     * @param message    the message for which to compute/update a route.
     * @param dstAddress the final destination of the route which the message be set to follow.
     * @return EndpointAddress The address (logical) where to send the message next. Null if there
     *         is nowhere to send it to.
     */
    EndpointAddress addressMessage(Message message, EndpointAddress dstAddress) {
        if (endpoint == null) {
            return null;
        }
        if (Logging.SHOW_FINE && LOG.isLoggable(Level.FINE)) {
            LOG.fine("Create a new EndpointRouterMessage " + dstAddress);
        }
        EndpointRouterMessage routerMsg = new EndpointRouterMessage(message, true);
        if (routerMsg.isDirty()) {
            if (Logging.SHOW_WARNING && LOG.isLoggable(Level.WARNING)) {
                LOG.warning("Probable transport recursion");
            }
            throw new IllegalStateException("RouterMessage element already present");
        }
        routerMsg.setSrcAddress(localPeerAddr);
        routerMsg.setDestAddress(dstAddress);
        EndpointAddress theGatewayAddress;
        EndpointAddress dstAddressPlain = new EndpointAddress(dstAddress, null, null);
        try {
            RouteAdvertisement route = null;
            theGatewayAddress = getGatewayAddress(dstAddressPlain, true, null);
            if (theGatewayAddress == null) {
                routerMsg.clearAll();
                routerMsg.updateMessage();
                return null;
            }
            if (!theGatewayAddress.equals(dstAddressPlain)) {
                route = getRoute(dstAddressPlain, false);
            }
            if (route != null) {
                routerMsg.setForwardHops((Vector<AccessPointAdvertisement>) route.getVectorHops().clone());
            }
            routerMsg.setLastHop(localPeerAddr);
            RouteAdvertisement myRoute = getMyLocalRoute();
            if (myRoute != null) {
                boolean newDest = newDestinations.remove(dstAddressPlain);
                boolean newGatw = destinations.isWelcomeNeeded(theGatewayAddress);
                if (newDest || newGatw) {
                    routerMsg.setRouteAdv(myRoute);
                }
            }
            routerMsg.updateMessage();
        } catch (Exception ez1) {
            if (Logging.SHOW_WARNING && LOG.isLoggable(Level.WARNING)) {
                LOG.log(Level.WARNING, "Could not fully address message", ez1);
            }
            return null;
        }
        return theGatewayAddress;
    }

    /**
     * {@inheritDoc}
     */
    public Object transportControl(Object operation, Object value) {
        if (!(operation instanceof Integer)) {
            return null;
        }
        int op = (Integer) operation;
        switch(op) {
            case RouteControlOp:
                return new RouteControl(this, localPeerId);
            default:
                if (Logging.SHOW_WARNING && LOG.isLoggable(Level.WARNING)) {
                    LOG.warning("Invalid Transport Control operation argument");
                }
                return null;
        }
    }

    /**
     * Convert a Router EndpointAddress into a PeerID
     *
     * @param addr the address to extract peerAddress from
     * @return the PeerID
     */
    static PeerID addr2pid(EndpointAddress addr) {
        URI asURI = null;
        try {
            asURI = new URI(ID.URIEncodingName, ID.URNNamespace + ":" + addr.getProtocolAddress(), null);
            return (PeerID) IDFactory.fromURI(asURI);
        } catch (URISyntaxException ex) {
            if (Logging.SHOW_WARNING && LOG.isLoggable(Level.WARNING)) {
                LOG.log(Level.WARNING, "Error converting a source address into a virtual address : " + addr, ex);
            }
        } catch (ClassCastException cce) {
            if (Logging.SHOW_WARNING && LOG.isLoggable(Level.WARNING)) {
                LOG.log(Level.WARNING, "Error converting a source address into a virtual address: " + addr, cce);
            }
        }
        return null;
    }

    /**
     * Convert an ID into a Router Endpoint Address
     *
     * @param pid The ID who's equivalent Endpoint Address is desired.
     * @return The ID as an EndpointAddress.
     */
    static EndpointAddress pid2addr(ID pid) {
        return new EndpointAddress(ROUTER_PROTOCOL_NAME, pid.getUniqueValue().toString(), null, null);
    }

    /**
     * check if it is a new route adv
     *
     * @param route route advertisement
     */
    void updateRouteAdv(RouteAdvertisement route) {
        updateRouteAdv(route, false);
    }

    /**
     * check if it is a new route adv
     *
     * @param route route advertisement
     * @param force enforce the route
     */
    void updateRouteAdv(RouteAdvertisement route, boolean force) {
        try {
            PeerID pID = route.getDestPeerID();
            if (routeCM.updateRoute(route)) {
                synchronized (this) {
                    Long nextTry = triedAndFailed.get(pID);
                    if (nextTry != null) {
                        if (nextTry <= TimeUtils.toAbsoluteTimeMillis(MAX_ASYNC_GETMESSENGER_RETRY)) {
                            triedAndFailed.remove(pID);
                            notifyAll();
                        }
                    }
                }
            } else {
                if (Logging.SHOW_FINE && LOG.isLoggable(Level.FINE)) {
                    LOG.fine("Route for " + pID + " is same as existing route, not publishing it");
                }
                if (force) {
                    synchronized (this) {
                        Long nextTry = triedAndFailed.get(pID);
                        if (nextTry != null) {
                            if (nextTry <= TimeUtils.toAbsoluteTimeMillis(MAX_ASYNC_GETMESSENGER_RETRY)) {
                                triedAndFailed.remove(pID);
                                notifyAll();
                            }
                        }
                    }
                }
            }
        } catch (Exception e) {
            if (Logging.SHOW_WARNING && LOG.isLoggable(Level.WARNING)) {
                LOG.log(Level.WARNING, "Failed to publish route advertisement", e);
            }
        }
    }

    /**
     * is there a pending route query for that destination
     *
     * @param peerID destination address
     * @return true or false
     */
    boolean isPendingRouteQuery(PeerID peerID) {
        return pendingQueries.containsKey(peerID);
    }

    /**
     * get a pending route query info
     *
     * @param peerID destination address
     * @return pending route query info
     */
    ClearPendingQuery getPendingRouteQuery(PeerID peerID) {
        return pendingQueries.get(peerID);
    }

    /**
     * Do we have a long route for that destination
     *
     * @param peerID destination address
     * @return true or false
     */
    boolean isRoutedRoute(PeerID peerID) {
        return peerID != null && routedRoutes.containsKey(peerID);
    }

    /**
     * Snoop if we have a messenger
     *
     * @param addr destination address
     * @return Messenger
     */
    Messenger getCachedMessenger(EndpointAddress addr) {
        return destinations.getCurrentMessenger(addr);
    }

    /**
     * Get all direct route destinations
     *
     * @return Iterator iterations of all endpoint destinations
     */
    Iterator<EndpointAddress> getAllCachedMessengerDestinations() {
        return destinations.allDestinations().iterator();
    }

    /**
     * Get all long route destinations
     *
     * @return Iterator iterations of all routed route destinations
     */
    Iterator<Map.Entry<ID, RouteAdvertisement>> getRoutedRouteAllDestinations() {
        return routedRoutes.entrySet().iterator();
    }

    /**
     * Get all long route destination addresses
     *
     * @return Iterator iterations of all routed route addresses
     */
    Iterator<ID> getAllRoutedRouteAddresses() {
        return routedRoutes.keySet().iterator();
    }

    /**
     * Get all pendingRouteQuery destinations
     *
     * @return All pending route query destinations
     */
    Collection<Map.Entry<PeerID, ClearPendingQuery>> getPendingQueriesAllDestinations() {
        List<Map.Entry<PeerID, ClearPendingQuery>> copy = new ArrayList<Map.Entry<PeerID, ClearPendingQuery>>(pendingQueries.size());
        synchronized (pendingQueries) {
            copy.addAll(pendingQueries.entrySet());
        }
        return copy;
    }

    /**
     * Get the route CM cache Manager
     *
     * @return the route CM cache Manager
     */
    RouteCM getRouteCM() {
        return routeCM;
    }

    /**
     * Get the route resolver manager
     *
     * @return the route resolver Manager
     */
    RouteResolver getRouteResolver() {
        return routeResolver;
    }

    /**
     * set bad route entry
     *
     * @param addr     of the bad route
     * @param badRoute bad route info
     */
    synchronized void setBadRoute(EndpointAddress addr, BadRoute badRoute) {
        badRoutes.put(addr, badRoute);
    }

    /**
     * get bad route entry
     *
     * @param addr of the bad route
     * @return BadRoute bad route info
     */
    synchronized BadRoute getBadRoute(EndpointAddress addr) {
        return badRoutes.get(addr);
    }
}
