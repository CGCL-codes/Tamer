package com.pbxworkbench.campaign.svc.impl;

import com.pbxworkbench.campaign.svc.CampaignEvent;

public interface ICampaignEventPublisher {

    void publish(CampaignEvent event);
}
