package org.aacc.campaigns.dialingmethods;

import org.aacc.campaigns.AbstractCampaign;

/**
 * Provides the logic for a very simple dialing algorithm. See if there are available
 * free resources (e.g. agents, ivr channels), then dial. 
 * 
 * @author Fernando
 */
public class SimpleDialMethod extends AbstractDialerMethod {

    public SimpleDialMethod(AbstractCampaign campaign) {
        super(campaign);
    }

    /**
     * Dial if calls being placed / available resources < dial ratio
     * @see SimpleDialMethod
     * @return
     */
    @Override
    boolean dialingMethod() {
        AbstractCampaign c = getCampaign();
        float freeResources = c.getAvailableResourcesCount();
        if ((int) freeResources <= 0) {
            return false;
        }
        float beingDialed = c.getDialingThreads();
        float ratio = c.getDialsPerFreeResourceRatio();
        return (beingDialed / freeResources < ratio);
    }
}
