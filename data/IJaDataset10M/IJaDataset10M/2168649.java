package org.matsim.utils.vis.routervis.multipathrouter;

import org.matsim.network.NetworkLayer;
import org.matsim.router.util.TravelCost;
import org.matsim.router.util.TravelTime;
import org.matsim.utils.vis.routervis.RouterNetStateWriter;

public class PSLogitRouter extends MultiPathRouter {

    public PSLogitRouter(final NetworkLayer network, final TravelCost costFunction, final TravelTime timeFunction, final RouterNetStateWriter writer) {
        super(network, costFunction, timeFunction, writer);
    }

    @Override
    void initSelector() {
        this.selector = new PSLogitSelector();
    }
}
