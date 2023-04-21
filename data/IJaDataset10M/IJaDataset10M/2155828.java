package org.matsim.core.replanning.selectors;

import java.util.HashMap;
import org.matsim.api.core.v01.population.Person;
import org.matsim.api.core.v01.population.Plan;
import org.matsim.core.population.PlanImpl;

/**
 * <p>Selects the worst plan of a person (most likely for removal), but respects
 * the set plan types in a way the no plan is selected that is the last one of
 * its type.</p> 
 * <p>(I would say that it can select the last of its type if it finds nothing else.  However, this algo should only
 * be used if an agent has more plans than maxPlansPerAgent, so make sure that that parameter is set large enough for
 * your purposes.  kai, oct'09)</p>
 * <p>Plans without a score are seen as worst and selected accordingly.</p>
 *
 * @author mrieser
 */
public class WorstPlanForRemovalSelector implements PlanSelector {

    @Override
    public Plan selectPlan(Person person) {
        HashMap<String, Integer> typeCounts = new HashMap<String, Integer>();
        for (Plan plan : person.getPlans()) {
            Integer cnt = typeCounts.get(((PlanImpl) plan).getType());
            if (cnt == null) {
                typeCounts.put(((PlanImpl) plan).getType(), Integer.valueOf(1));
            } else {
                typeCounts.put(((PlanImpl) plan).getType(), Integer.valueOf(cnt.intValue() + 1));
            }
        }
        Plan worst = null;
        double worstScore = Double.POSITIVE_INFINITY;
        for (Plan plan : person.getPlans()) {
            if (typeCounts.get(((PlanImpl) plan).getType()).intValue() > 1) {
                if (plan.getScore() == null) {
                    worst = plan;
                    worstScore = Double.NEGATIVE_INFINITY;
                } else if (plan.getScore().doubleValue() < worstScore) {
                    worst = plan;
                    worstScore = plan.getScore().doubleValue();
                }
            }
        }
        if (worst == null) {
            for (Plan plan : person.getPlans()) {
                if (plan.getScore() == null) {
                    return plan;
                }
                if (plan.getScore().doubleValue() < worstScore) {
                    worst = plan;
                    worstScore = plan.getScore().doubleValue();
                }
            }
        }
        return worst;
    }
}
