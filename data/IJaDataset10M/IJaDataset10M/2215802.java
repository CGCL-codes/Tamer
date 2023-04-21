package playground.mrieser.core.sim.api;

import org.matsim.api.core.v01.population.Plan;
import org.matsim.api.core.v01.population.PlanElement;

public interface PlanAgent {

    public Plan getPlan();

    public PlanElement getCurrentPlanElement();

    /**
	 * Tells the PlanAgent to use the next PlanElement as current PlanElement.
	 *
	 * @return the PlanElement becoming the current PlanElement.
	 */
    public PlanElement useNextPlanElement();
}
