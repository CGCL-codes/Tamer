package org.matsim.core.replanning.modules;

import org.matsim.core.controler.Controler;
import org.matsim.population.algorithms.PlanAlgorithm;

/**
 * Uses the routing algorithm provided by the {@linkplain Controler} for 
 * calculating the routes of plans during Replanning.
 *
 * @author mrieser
 */
public class ReRoute extends AbstractMultithreadedModule {

    private final Controler controler;

    public ReRoute(final Controler controler) {
        super(controler.getConfig().global());
        this.controler = controler;
    }

    @Override
    public PlanAlgorithm getPlanAlgoInstance() {
        return this.controler.createRoutingAlgorithm();
    }
}
