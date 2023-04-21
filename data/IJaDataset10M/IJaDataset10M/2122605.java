package org.matsim.planomat.costestimators;

import org.matsim.api.core.v01.Id;
import org.matsim.api.core.v01.population.Activity;
import org.matsim.api.core.v01.population.Leg;
import org.matsim.api.core.v01.population.Plan;
import org.matsim.core.population.LegImpl;

public abstract class AbstractLegTravelTimeEstimator implements LegTravelTimeEstimator {

    protected Plan plan;

    public AbstractLegTravelTimeEstimator(Plan plan) {
        super();
        this.plan = plan;
    }

    @Override
    public abstract double getLegTravelTimeEstimation(Id personId, double departureTime, Activity actOrigin, Activity actDestination, Leg legIntermediate, boolean doModifyLeg);

    @Override
    public abstract LegImpl getNewLeg(String mode, Activity actOrigin, Activity actDestination, int legPlanElementIndex, double departureTime);
}
