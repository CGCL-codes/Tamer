package org.matsim.plans.filters;

import java.util.HashSet;
import java.util.Set;
import org.matsim.basic.v01.Id;
import org.matsim.basic.v01.BasicPlanImpl.LegIterator;
import org.matsim.network.Link;
import org.matsim.plans.Leg;
import org.matsim.plans.Plan;
import org.matsim.plans.algorithms.PlanAlgorithmI;

public class RouteLinkFilter extends AbstractPlanFilter {

    private final Set<Id> linkIds;

    public RouteLinkFilter(final PlanAlgorithmI nextAlgo) {
        this.nextAlgorithm = nextAlgo;
        this.linkIds = new HashSet<Id>();
    }

    public void addLink(final Id linkId) {
        this.linkIds.add(linkId);
    }

    @Override
    public boolean judge(final Plan plan) {
        LegIterator iter = plan.getIteratorLeg();
        while (iter.hasNext()) {
            Leg leg = (Leg) iter.next();
            Link[] links = leg.getRoute().getLinkRoute();
            for (Link link : links) {
                if (this.linkIds.contains(link.getId())) {
                    return true;
                }
            }
        }
        return false;
    }
}
