package org.matsim.contrib.freight.mobsim;

import org.matsim.contrib.freight.carrier.Carrier;
import org.matsim.population.algorithms.PlanAlgorithm;

/**
* Created by IntelliJ IDEA.
* User: zilske
* Date: 10/31/11
* Time: 12:41 PM
* To change this template use File | Settings | File Templates.
*/
public class SimpleCarrierAgentFactory implements CarrierAgentFactory {

    private PlanAlgorithm router;

    public void setRouter(PlanAlgorithm router) {
        this.router = router;
    }

    @Override
    public CarrierAgent createAgent(CarrierAgentTracker tracker, Carrier carrier) {
        CarrierAgentImpl agent = new CarrierAgentImpl(tracker, carrier, router, new CarrierDriverAgentFactoryImpl());
        return agent;
    }
}
