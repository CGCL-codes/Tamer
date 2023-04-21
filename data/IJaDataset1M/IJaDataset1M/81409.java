package org.matsim.roadpricing;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.matsim.api.core.v01.Id;

/**
 * A road pricing scheme (sometimes also called toll scheme) contains the type of the toll, a list of the
 * tolled links and the (time-dependent) toll amount agents have to pay.
 *
 * @author mrieser
 */
public class RoadPricingScheme implements RoadPricingSchemeI {

    /** The type to be used for distance tolls (the toll scheme gives a toll per km for each link). */
    public static final String TOLL_TYPE_DISTANCE = "distance";

    /** The type to be used for cordon tolls. */
    public static final String TOLL_TYPE_CORDON = "cordon";

    /** The type to be used for area tolls. */
    public static final String TOLL_TYPE_AREA = "area";

    /** The type to be used for link toll (the toll scheme gives a toll per link). */
    public static final String TOLL_TYPE_LINK = "link";

    private Map<Id, List<Cost>> linkIds = null;

    private String name = null;

    private String type = null;

    private String description = null;

    private ArrayList<Cost> costs = null;

    private boolean cacheIsInvalid = true;

    private Cost[] costCache = null;

    public RoadPricingScheme() {
        this.linkIds = new HashMap<Id, List<Cost>>();
        this.costs = new ArrayList<Cost>();
    }

    public void addLink(final Id linkId) {
        this.linkIds.put(linkId, null);
    }

    public void setName(final String name) {
        this.name = name;
    }

    @Override
    public String getName() {
        return this.name;
    }

    public void setType(final String type) {
        this.type = type.intern();
    }

    @Override
    public String getType() {
        return this.type;
    }

    public void setDescription(final String description) {
        this.description = description;
    }

    @Override
    public String getDescription() {
        return this.description;
    }

    public Cost addCost(final double startTime, final double endTime, final double amount) {
        Cost cost = new Cost(startTime, endTime, amount);
        this.costs.add(cost);
        this.cacheIsInvalid = true;
        return cost;
    }

    public void addLinkCost(Id linkId, double startTime, double endTime, double amount) {
        Cost cost = new Cost(startTime, endTime, amount);
        List<Cost> cs = this.linkIds.get(linkId);
        if (cs == null) {
            cs = new ArrayList<Cost>();
            this.linkIds.put(linkId, cs);
        }
        cs.add(cost);
    }

    public boolean removeCost(final Cost cost) {
        return this.costs.remove(cost);
    }

    public boolean removeLinkCost(final Id linkId, final Cost cost) {
        List<Cost> c = this.linkIds.get(linkId);
        return (c != null) ? c.remove(cost) : false;
    }

    @Override
    public Set<Id> getLinkIdSet() {
        return this.linkIds.keySet();
    }

    @Override
    public Map<Id, List<Cost>> getLinkIds() {
        return this.linkIds;
    }

    public Iterable<Cost> getCosts() {
        return this.costs;
    }

    /** @return all Cost objects as an array for faster iteration. */
    public Cost[] getCostArray() {
        if (this.cacheIsInvalid) buildCache();
        return this.costCache.clone();
    }

    @Override
    public Cost getLinkCostInfo(final Id linkId, final double time, Id personId) {
        if (this.cacheIsInvalid) buildCache();
        if (this.linkIds.containsKey(linkId)) {
            List<Cost> costs = this.linkIds.get(linkId);
            if (costs == null) {
                for (Cost cost : this.costCache) {
                    if ((time >= cost.startTime) && (time < cost.endTime)) {
                        return cost;
                    }
                }
            } else {
                for (Cost cost : costs) {
                    if ((time >= cost.startTime) && (time < cost.endTime)) {
                        return cost;
                    }
                }
            }
        }
        return null;
    }

    private void buildCache() {
        this.costCache = new Cost[this.costs.size()];
        this.costCache = this.costs.toArray(this.costCache);
        this.cacheIsInvalid = false;
    }

    /**
	 * A single, time-dependent toll-amount for a roadpricing scheme.
	 *
	 * @author mrieser
	 */
    public static class Cost {

        public final double startTime;

        public final double endTime;

        public final double amount;

        public Cost(final double startTime, final double endTime, final double amount) {
            this.startTime = startTime;
            this.endTime = endTime;
            this.amount = amount;
        }
    }
}
