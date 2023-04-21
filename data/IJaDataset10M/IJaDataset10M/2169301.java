package playground.mrieser.routedistance;

import org.matsim.api.core.v01.Id;
import org.matsim.api.core.v01.network.Network;
import org.matsim.api.core.v01.population.Route;
import org.matsim.core.population.routes.NetworkRoute;

/**
 * @author mrieser
 */
public class NetworkRouteDistanceCalculator implements RouteDistanceCalculator {

    private final Network network;

    public NetworkRouteDistanceCalculator(Network network) {
        this.network = network;
    }

    @Override
    public double calcDistance(Route route) {
        if (!(route instanceof NetworkRoute)) {
            throw new IllegalArgumentException("wrong type of route, expected NetworkRoute, but was " + route.getClass());
        }
        NetworkRoute r = (NetworkRoute) route;
        double dist = 0;
        for (Id linkId : r.getLinkIds()) {
            dist += this.network.getLinks().get(linkId).getLength();
        }
        return dist;
    }
}
