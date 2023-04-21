package org.matsim.core.network;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import org.matsim.api.basic.v01.Coord;
import org.matsim.api.basic.v01.Id;
import org.matsim.api.basic.v01.TransportMode;
import org.matsim.core.api.network.Link;
import org.matsim.core.api.network.Network;
import org.matsim.core.api.network.NetworkBuilder;
import org.matsim.core.api.network.Node;
import org.matsim.core.api.population.Route;
import org.matsim.core.population.routes.GenericRouteFactory;
import org.matsim.core.population.routes.NodeNetworkRouteFactory;
import org.matsim.core.population.routes.RouteFactory;

/**
 * @author dgrether
 */
public class NetworkFactory implements Serializable, NetworkBuilder {

    private LinkFactory linkFactory = null;

    private final Map<TransportMode, RouteFactory> routeFactories = new HashMap<TransportMode, RouteFactory>();

    private RouteFactory defaultFactory = new GenericRouteFactory();

    private Network network;

    public NetworkFactory(Network network) {
        this();
        this.network = network;
    }

    public NetworkFactory() {
        this.linkFactory = new LinkFactoryImpl();
        this.routeFactories.put(TransportMode.car, new NodeNetworkRouteFactory());
        this.routeFactories.put(TransportMode.pt, new GenericRouteFactory());
    }

    public Node createNode(final Id id) {
        return new NodeImpl(id);
    }

    /**
	 * TODO how to set other attributes of link consistently without invalidating time variant attributes
	 */
    public Link createLink(final Id id, final Id fromNodeId, final Id toNodeId) {
        return this.linkFactory.createLink(id, this.network.getNode(fromNodeId), this.network.getNode(toNodeId), this.network, 1.0, 1.0, 1.0, 1.0);
    }

    /**
	 * @deprecated use createNode(Id id) instead and set the rest yourself
	 */
    @Deprecated
    public Node createNode(final Id id, final Coord coord, final String type) {
        return new NodeImpl(id, coord, type);
    }

    public Link createLink(final Id id, final Node from, final Node to, final Network network, final double length, final double freespeedTT, final double capacity, final double lanes) {
        return this.linkFactory.createLink(id, from, to, network, length, freespeedTT, capacity, lanes);
    }

    /**
	 * @param mode the transport mode the route should be for
	 * @param startLink the link where the route starts
	 * @param endLink the link where the route ends
	 * @return a new Route for the specified mode
	 *
	 * @see #setRouteFactory(TransportMode, RouteFactory)
	 */
    public Route createRoute(final TransportMode mode, final Link startLink, final Link endLink) {
        RouteFactory factory = this.routeFactories.get(mode);
        if (factory == null) {
            factory = this.defaultFactory;
        }
        return factory.createRoute(startLink, endLink);
    }

    /**
	 * Registers a {@link RouteFactory} for the specified mode. If <code>factory</code> is <code>null</code>,
	 * the existing entry for this <code>mode</code> will be deleted. If <code>mode</code> is <code>null</code>,
	 * then the default factory is set that is used if no specific RouteFactory for a mode is set.
	 *
	 * @param mode
	 * @param factory
	 */
    public void setRouteFactory(final TransportMode mode, final RouteFactory factory) {
        if (mode == null) {
            this.defaultFactory = factory;
        } else {
            if (factory == null) {
                this.routeFactories.remove(mode);
            } else {
                this.routeFactories.put(mode, factory);
            }
        }
    }

    public void setLinkFactory(final LinkFactory factory) {
        this.linkFactory = factory;
    }

    public boolean isTimeVariant() {
        return (this.linkFactory instanceof TimeVariantLinkFactory);
    }

    public void setNetwork(NetworkLayer networkLayer) {
        this.network = networkLayer;
    }
}
