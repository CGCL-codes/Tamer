package org.matsim.contrib.evacuation.travelcosts;

import java.util.ArrayList;
import java.util.List;
import org.matsim.api.core.v01.network.Link;
import org.matsim.api.core.v01.population.Person;
import org.matsim.core.router.util.PersonalizableTravelDisutility;
import org.matsim.core.router.util.TravelDisutility;
import org.matsim.core.router.util.TravelTime;

public class PluggableTravelCostCalculator implements PersonalizableTravelDisutility {

    private final List<TravelDisutility> calcs = new ArrayList<TravelDisutility>();

    private final TravelTime tt;

    public PluggableTravelCostCalculator(TravelTime tt) {
        this.tt = tt;
    }

    public void addTravelCost(TravelDisutility calc) {
        this.calcs.add(calc);
    }

    @Override
    public double getLinkTravelDisutility(Link link, double time) {
        double ret = this.tt.getLinkTravelTime(link, time);
        for (TravelDisutility calc : this.calcs) {
            ret += calc.getLinkTravelDisutility(link, time);
        }
        return ret;
    }

    @Override
    public void setPerson(Person person) {
    }
}
