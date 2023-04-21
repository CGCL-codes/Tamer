package net.jxta.impl.endpoint.router;

import net.jxta.document.AdvertisementFactory;
import net.jxta.endpoint.EndpointAddress;
import net.jxta.endpoint.Messenger;
import net.jxta.endpoint.MessengerEvent;
import net.jxta.endpoint.Message;
import net.jxta.id.ID;
import net.jxta.logging.Logging;
import net.jxta.peer.PeerID;
import net.jxta.protocol.AccessPointAdvertisement;
import net.jxta.protocol.RouteAdvertisement;
import java.util.Iterator;
import java.util.Map;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.io.IOException;

/**
 * Provides an "IOCTL" style interface to the JXTA router transport
 */
public class RouteControl {

    /**
     * Logger
     */
    private static final transient Logger LOG = Logger.getLogger(RouteControl.class.getName());

    /**
     * return value for operation
     */
    public static final int OK = 0;

    public static final int ALREADY_EXIST = 1;

    public static final int FAILED = -1;

    public static final int DIRECT_ROUTE = 2;

    public static final int INVALID_ROUTE = 3;

    /**
     * Endpoint Router pointer
     */
    private final EndpointRouter router;

    /**
     * Router CM cache
     */
    private final RouteCM routeCM;

    /**
     * local Peer Id
     */
    private final ID localPeerId;

    /**
     * initialize RouteControl
     *
     * @param router the router
     * @param pid    the PeerID
     */
    public RouteControl(EndpointRouter router, ID pid) {
        this.router = router;
        this.routeCM = router.getRouteCM();
        this.localPeerId = pid;
    }

    /**
     * get my local route
     *
     * @return RoutAdvertisement of the local route
     */
    public RouteAdvertisement getMyLocalRoute() {
        return router.getMyLocalRoute();
    }

    /**
     * add a new route. For the route to be useful, we actively verify
     * the route by trying it
     *
     * @param newRoute route to add
     * @return Integer status (OK, FAILED, INVALID_ROUTE or ALREADY_EXIST)
     */
    public int addRoute(RouteAdvertisement newRoute) {
        RouteAdvertisement route = newRoute.clone();
        if (route.getDestPeerID().equals(localPeerId)) {
            if (Logging.SHOW_FINE && LOG.isLoggable(Level.FINE)) {
                LOG.fine("Skipping Local peer addRoute");
            }
            return ALREADY_EXIST;
        }
        AccessPointAdvertisement firstHop = route.getFirstHop();
        PeerID firstHopPid;
        EndpointAddress firstHopAddr;
        if (firstHop != null) {
            firstHopPid = firstHop.getPeerID();
            if (localPeerId.equals(firstHopPid)) {
                route.removeHop(firstHopPid);
                firstHop = route.getFirstHop();
            }
        }
        if (firstHop == null) {
            EndpointAddress destAddress = EndpointRouter.pid2addr(route.getDestPeerID());
            if (router.ensureLocalRoute(destAddress, route) != null) {
                routeCM.publishRoute(newRoute);
                return OK;
            }
            if (router.isLocalRoute(destAddress) || router.isRoutedRoute(route.getDestPeerID())) {
                if (Logging.SHOW_FINE && LOG.isLoggable(Level.FINE)) {
                    LOG.fine("Skipping add Route " + destAddress + " already exists");
                    LOG.fine("isLocalRoute() " + router.isLocalRoute(destAddress) + " isRoutedRoute() : " + router.isRoutedRoute(route.getDestPeerID()));
                }
                return ALREADY_EXIST;
            }
            if (router.ensureLocalRoute(destAddress, route) == null) {
                if (Logging.SHOW_WARNING && LOG.isLoggable(Level.WARNING)) {
                    LOG.warning("Failed to connect to address :" + destAddress);
                }
                return FAILED;
            }
            if (Logging.SHOW_FINE && LOG.isLoggable(Level.FINE)) {
                LOG.fine("Publishing route :" + newRoute);
            }
            routeCM.publishRoute(newRoute);
            return OK;
        }
        RouteAdvertisement firstHopRoute = null;
        firstHopPid = firstHop.getPeerID();
        firstHopAddr = EndpointRouter.pid2addr(firstHopPid);
        if (!router.isLocalRoute(firstHopAddr) && !router.isRoutedRoute(firstHopPid)) {
            firstHopRoute = (RouteAdvertisement) AdvertisementFactory.newAdvertisement(RouteAdvertisement.getAdvertisementType());
            firstHopRoute.setDest(firstHop.clone());
            router.updateRouteAdv(firstHopRoute);
        }
        if (router.ensureLocalRoute(firstHopAddr, firstHopRoute) == null) {
            return FAILED;
        }
        router.setRoute(route.clone(), false);
        return OK;
    }

    /**
     * Get a current route info
     *
     * @param pId destination of the route
     * @return RouteAdvertisement current route info
     */
    public RouteAdvertisement getRouteInfo(PeerID pId) {
        RouteAdvertisement route;
        EndpointRouter.ClearPendingQuery entry;
        EndpointAddress addr = EndpointRouter.pid2addr(pId);
        Messenger oneOfThem = router.getCachedMessenger(addr);
        EndpointAddress pcaddr = (oneOfThem == null) ? null : oneOfThem.getDestinationAddress();
        if (pcaddr != null) {
            AccessPointAdvertisement ap = (AccessPointAdvertisement) AdvertisementFactory.newAdvertisement(AccessPointAdvertisement.getAdvertisementType());
            ap.setPeerID(pId);
            Vector<String> eas = new Vector<String>();
            eas.add(pcaddr.getProtocolName() + "://" + pcaddr.getProtocolAddress());
            ap.setEndpointAddresses(eas);
            route = (RouteAdvertisement) AdvertisementFactory.newAdvertisement(RouteAdvertisement.getAdvertisementType());
            route.setDest(ap);
            return route;
        } else {
            route = router.getRoute(addr, false);
            if (route != null) {
                return route;
            } else {
                entry = router.getPendingRouteQuery(pId);
                if (entry != null) {
                    AccessPointAdvertisement ap = (AccessPointAdvertisement) AdvertisementFactory.newAdvertisement(AccessPointAdvertisement.getAdvertisementType());
                    ap.setPeerID(pId);
                    Vector<String> eas = new Vector<String>();
                    eas.add("pending " + (entry.isFailed() ? "(failed)" : "(new)"));
                    ap.setEndpointAddresses(eas);
                    route = (RouteAdvertisement) AdvertisementFactory.newAdvertisement(RouteAdvertisement.getAdvertisementType());
                    route.setDest(ap);
                    return route;
                } else {
                    AccessPointAdvertisement ap = (AccessPointAdvertisement) AdvertisementFactory.newAdvertisement(AccessPointAdvertisement.getAdvertisementType());
                    ap.setPeerID(pId);
                    route = (RouteAdvertisement) AdvertisementFactory.newAdvertisement(RouteAdvertisement.getAdvertisementType());
                    route.setDest(ap);
                    return route;
                }
            }
        }
    }

    /**
     * Delete route info
     *
     * @param pId destination route to be removed
     * @return Integer status
     */
    public int deleteRoute(PeerID pId) {
        if (pId.equals(localPeerId)) {
            return INVALID_ROUTE;
        }
        EndpointAddress addr = EndpointRouter.pid2addr(pId);
        if (router.isLocalRoute(addr)) {
            return DIRECT_ROUTE;
        }
        router.removeRoute(pId);
        routeCM.flushRoute(pId);
        return OK;
    }

    /**
     * get all the know routes by the router. Return a vector of all
     * the routes known.
     * <p/>
     * This method which is meant for informational purposes, does not lock the maps that
     * it browses. As a result, it could in some cases generate a concurrent modification
     * exception.
     *
     * @return vector of known routes
     */
    public Vector<RouteAdvertisement> getAllRoutesInfo() {
        Vector<RouteAdvertisement> routes = new Vector<RouteAdvertisement>();
        EndpointAddress ea;
        try {
            for (Iterator it = router.getAllCachedMessengerDestinations(); it.hasNext(); ) {
                ea = (EndpointAddress) it.next();
                AccessPointAdvertisement ap = (AccessPointAdvertisement) AdvertisementFactory.newAdvertisement(AccessPointAdvertisement.getAdvertisementType());
                ap.setPeerID(EndpointRouter.addr2pid(ea));
                Vector<String> eas = new Vector<String>();
                Messenger oneOfThem = router.getCachedMessenger(ea);
                EndpointAddress pcaddr = (oneOfThem == null) ? null : oneOfThem.getDestinationAddress();
                if (pcaddr == null) {
                    eas.add("unknown");
                } else {
                    eas.add(pcaddr.getProtocolName() + "://" + pcaddr.getProtocolAddress());
                }
                ap.setEndpointAddresses(eas);
                RouteAdvertisement r = (RouteAdvertisement) AdvertisementFactory.newAdvertisement(RouteAdvertisement.getAdvertisementType());
                r.setDest(ap);
                routes.add(r);
            }
            for (Iterator<Map.Entry<ID, RouteAdvertisement>> i = router.getRoutedRouteAllDestinations(); i.hasNext(); ) {
                Map.Entry<ID, RouteAdvertisement> entry = i.next();
                routes.add(entry.getValue());
            }
            for (Map.Entry<PeerID, EndpointRouter.ClearPendingQuery> entry : router.getPendingQueriesAllDestinations()) {
                PeerID pid = entry.getKey();
                AccessPointAdvertisement ap = (AccessPointAdvertisement) AdvertisementFactory.newAdvertisement(AccessPointAdvertisement.getAdvertisementType());
                ap.setPeerID(pid);
                Vector<String> eas = new Vector<String>();
                eas.add("pending " + (entry.getValue().isFailed() ? "(failed)" : "(new)"));
                ap.setEndpointAddresses(eas);
                RouteAdvertisement r = (RouteAdvertisement) AdvertisementFactory.newAdvertisement(RouteAdvertisement.getAdvertisementType());
                r.setDest(ap);
                routes.add(r);
            }
        } catch (Exception ex) {
            if (Logging.SHOW_WARNING && LOG.isLoggable(Level.WARNING)) {
                LOG.log(Level.WARNING, "getAllRoutesInfo error : ", ex);
            }
        }
        return routes;
    }

    /**
     * get RouteCM usage
     *
     * @return true if use route CM is set
     */
    public boolean useRouteCM() {
        return router.getRouteCM().useRouteCM();
    }

    /**
     * get RouteResolver usage
     *
     * @return true of use route resolver
     */
    public boolean useRouteResolver() {
        return router.getRouteResolver().useRouteResolver();
    }

    /**
     * enable usage of Route CM cache
     */
    public void enableRouteCM() {
        router.getRouteCM().enableRouteCM(true);
    }

    /**
     * disable usage of Route CM cache
     */
    public void disableRouteCM() {
        router.getRouteCM().enableRouteCM(false);
    }

    /**
     * enable usage of Route Resolver
     */
    public void enableRouteResolver() {
        router.getRouteResolver().enableRouteResolver(true);
    }

    /**
     * disable usage of Route resolver
     */
    public void disableRouteResolver() {
        router.getRouteResolver().enableRouteResolver(false);
    }

    /**
     * Get the low level messenger for a destination.
     *
     * @param source  the source endpoint address
     * @param destination the destination endpoint address
     * @param messenger the messenger to add
     * @return true if successful
     */
    public boolean addMessengerFor(Object source, EndpointAddress destination, Messenger messenger) {
        return router.newMessenger(new MessengerEvent(source, messenger, destination));
    }

    /**
     * Get the low level messenger for a destination.
     *
     * @param destination the destination endpoint address
     * @param hint        route hint
     * @return  the messenger for the destination
     */
    public Messenger getMessengerFor(EndpointAddress destination, Object hint) {
        if (!(hint instanceof RouteAdvertisement)) {
            hint = null;
        }
        return router.ensureLocalRoute(destination, (RouteAdvertisement) hint);
    }

    /**
     * Determines whether a connection to a specific node exists, or if one can be created.
     * This method can block to ensure a usable connection exists, it does so by sending an empty
     * message.
     *
     * @param pid Node ID
     * @return true, if a connection already exists, or a new was sucessfully created
     */
    public boolean isConnected(PeerID pid) {
        Messenger messenger = getMessengerFor(new EndpointAddress("jxta", pid.getUniqueValue().toString(), null, null), null);
        if (messenger == null) {
            return false;
        }
        if ((messenger.getState() & Messenger.USABLE) != 0) {
            try {
                messenger.sendMessageB(new Message(), null, null);
            } catch (IOException io) {
                return (messenger.getState() & Messenger.USABLE) != 0;
            }
        }
        return (messenger.getState() & Messenger.CLOSED) != Messenger.CLOSED;
    }
}
