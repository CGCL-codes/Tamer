package playground.johannes.mz2005.analysis;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import org.matsim.api.core.v01.population.Activity;
import org.matsim.api.core.v01.population.Leg;
import org.matsim.api.core.v01.population.Plan;
import playground.johannes.coopsim.pysical.Trajectory;

/**
 * @author illenberger
 *
 */
public class TrajectoryPlanBuilder {

    public Map<Plan, Trajectory> buildTrajectory(Set<Plan> plans) {
        Map<Plan, Trajectory> trajectories = new HashMap<Plan, Trajectory>();
        for (Plan plan : plans) {
            Trajectory t = new Trajectory(plan.getPerson());
            for (int i = 0; i < plan.getPlanElements().size(); i++) {
                if (i % 2 == 0) {
                    Activity act = (Activity) plan.getPlanElements().get(i);
                    double endTime = act.getEndTime();
                    if (Double.isInfinite(endTime)) {
                        endTime = 86400;
                        if (i > 0) {
                            endTime = Math.max(t.getTransitions().get(i), 86400);
                        }
                    }
                    t.addElement(act, endTime);
                } else {
                    Leg leg = (Leg) plan.getPlanElements().get(i);
                    Activity act = (Activity) plan.getPlanElements().get(i + 1);
                    double endTime = act.getStartTime();
                    if (Double.isInfinite(endTime) || endTime == 0) endTime = leg.getDepartureTime() + leg.getTravelTime();
                    t.addElement(leg, endTime);
                }
            }
            trajectories.put(plan, t);
        }
        return trajectories;
    }
}
