package org.matsim.ptproject.qsim.multimodalsimengine.tools;

import java.util.HashSet;
import java.util.Set;
import org.apache.log4j.Logger;
import org.matsim.api.core.v01.population.Leg;
import org.matsim.api.core.v01.population.Person;
import org.matsim.api.core.v01.population.Plan;
import org.matsim.api.core.v01.population.PlanElement;
import org.matsim.core.config.groups.MultiModalConfigGroup;
import org.matsim.core.utils.collections.CollectionUtils;
import org.matsim.population.algorithms.AbstractPersonAlgorithm;
import org.matsim.population.algorithms.PlanAlgorithm;

/**
 * Drops all non car routes which are specified in the multiModalConfigGroup
 * ("simulatedModes").
 */
public class NonCarRouteDropper extends AbstractPersonAlgorithm implements PlanAlgorithm {

    private static final Logger log = Logger.getLogger(NonCarRouteDropper.class);

    private Set<String> modesToDrop = new HashSet<String>();

    public NonCarRouteDropper(MultiModalConfigGroup multiModalConfigGroup) {
        if (!multiModalConfigGroup.isDropNonCarRoutes()) {
            log.warn("Dropping of non car routes is not enabled in the config group - routes will not be dropped!");
            return;
        }
        for (String mode : CollectionUtils.stringToArray(multiModalConfigGroup.getSimulatedModes())) {
            this.modesToDrop.add(mode);
        }
    }

    @Override
    public void run(Plan plan) {
        for (PlanElement planElement : plan.getPlanElements()) {
            if (planElement instanceof Leg) {
                Leg leg = (Leg) planElement;
                if (modesToDrop.contains(leg.getMode())) {
                    leg.setRoute(null);
                }
            }
        }
    }

    @Override
    public void run(Person person) {
        for (Plan plan : person.getPlans()) {
            run(plan);
        }
    }
}
