package org.matsim.replanning.modules;

import org.matsim.controler.Controler;
import org.matsim.plans.algorithms.PlanAlgorithmI;

/**
 * Uses the routing algorithm provided by the {@linkplain Controler} for 
 * calculating the routes of plans during Replanning.
 *
 * @author mrieser
 */
public class ReRoute extends MultithreadedModuleA {

    private final Controler controler;

    public ReRoute(final Controler controler) {
        this.controler = controler;
    }

    @Override
    public PlanAlgorithmI getPlanAlgoInstance() {
        return this.controler.getRoutingAlgorithm();
    }
}
