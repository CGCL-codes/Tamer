package org.matsim.socialnetworks.replanning;

import org.matsim.gbl.Gbl;
import org.matsim.network.NetworkLayer;
import org.matsim.plans.Plan;
import org.matsim.plans.algorithms.PlanAlgorithmI;
import org.matsim.replanning.modules.StrategyModuleI;
import org.matsim.router.util.TravelCostI;
import org.matsim.router.util.TravelTimeI;
import org.matsim.replanning.modules.*;

public class SNRandomFacilitySwitcher extends MultithreadedModuleA {

    private NetworkLayer network = null;

    private TravelCostI tcost = null;

    private TravelTimeI ttime = null;

    /** 
	 * TODO [JH] this is hard-coded here but has to match the standard facility types
	 * in the facilities object. Need to make this change in the SNControllers, too.
	 */
    private String[] factypes = { "home", "work", "shop", "education", "leisure" };

    public SNRandomFacilitySwitcher(NetworkLayer network, TravelCostI tcost, TravelTimeI ttime) {
        System.out.println("initializing SNRandomFacilitySwitcher");
        this.network = network;
        this.tcost = tcost;
        this.ttime = ttime;
    }

    public PlanAlgorithmI getPlanAlgoInstance() {
        return new SNSecLocRandom(factypes, network, tcost, ttime);
    }
}
