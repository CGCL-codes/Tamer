package com.pbxworkbench.campaign.svc.impl;

import com.pbxworkbench.campaign.domain.CampaignId;

public abstract class AbstractCampaignRequest implements ICampaignRequest {

    CampaignId campaignId;

    public AbstractCampaignRequest(CampaignId campaignId) {
        this.campaignId = campaignId;
    }

    public CampaignId getCampaignId() {
        return campaignId;
    }

    public abstract void dispatch(ICampaignRequestHandler rh);
}
