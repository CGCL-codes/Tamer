package org.matsim.population.algorithms;

import java.util.List;
import org.matsim.api.core.v01.population.Activity;
import org.matsim.api.core.v01.population.Leg;
import org.matsim.api.core.v01.population.Person;
import org.matsim.api.core.v01.population.Plan;
import org.matsim.api.core.v01.population.PlanElement;
import org.matsim.core.population.LegImpl;
import org.matsim.core.utils.misc.Time;

/**
 * Calculates all time informations in all plans of a person<br/>
 * The algorithm does the following steps:
 * <ul>
 * <li>set start-time of first act to 00:00</li>
 * <li>use end-time of first act as "current" time</li>
 * <li>sets start-time, duration, end-time of acts and departure-time,
 * travel-time, arrival-time of legs by continually adding duration of acts
 * and travel-time of legs to the current time.</li>
 * <li>the last act is extended until 24:00 if it ends before this time.
 * Otherwise, a duration of 0 is assumed for the last act.</li>
 * </ul>
 * If a leg has no travel-time set, 0 will be used instead.
 */
public class PersonCalcTimes extends AbstractPersonAlgorithm {

    public PersonCalcTimes() {
        super();
    }

    @Override
    public void run(Person person) {
        List<? extends Plan> plans = person.getPlans();
        for (int i = 0; i < plans.size(); i++) {
            Plan plan = plans.get(i);
            Activity act = null;
            LegImpl leg = null;
            int cnt = 0;
            int max = plan.getPlanElements().size();
            for (PlanElement pe : plan.getPlanElements()) {
                cnt++;
                if (pe instanceof Activity) {
                    act = (Activity) pe;
                    if (cnt == 1) {
                        act.setStartTime(0);
                        act.setMaximumDuration(act.getEndTime());
                    } else if (cnt == max) {
                        double time = leg.getArrivalTime();
                        act.setStartTime(time);
                        if (time < 24 * 3600) {
                            time = 24 * 3600;
                        }
                        act.setEndTime(time);
                        act.setMaximumDuration(time - act.getStartTime());
                    } else {
                        act.setStartTime(leg.getArrivalTime());
                        act.setEndTime(act.getStartTime() + act.getMaximumDuration());
                    }
                }
                if (pe instanceof Leg) {
                    leg = (LegImpl) pe;
                    leg.setDepartureTime(act.getEndTime());
                    double ttime = leg.getTravelTime();
                    if (ttime == Time.UNDEFINED_TIME) {
                        ttime = 0;
                    }
                    leg.setArrivalTime(leg.getDepartureTime() + ttime);
                }
            }
        }
    }
}
