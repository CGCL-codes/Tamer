package org.matsim.population.algorithms;

import org.matsim.api.core.v01.population.Person;
import org.matsim.api.core.v01.population.Plan;

/**
 * Removes all non-selected plans from a person. If a person has no
 * plan selected, the person will be left with zero plans.
 *
 * @author mrieser
 */
public class PersonFilterSelectedPlan extends AbstractPersonAlgorithm {

    public PersonFilterSelectedPlan() {
        super();
    }

    @Override
    public void run(final Person person) {
        int nofPlans = person.getPlans().size();
        for (int planId = 0; planId < nofPlans; planId++) {
            Plan plan = person.getPlans().get(planId);
            if (!plan.isSelected()) {
                person.getPlans().remove(planId);
                planId--;
                nofPlans--;
            }
        }
    }
}
