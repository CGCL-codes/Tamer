package com.pbxworkbench.campaign.svc.impl;

import com.pbxworkbench.campaign.domain.CampaignId;
import com.pbxworkbench.campaign.svc.CampaignEvent;
import com.pbxworkbench.campaign.svc.ICampaignListener;

public interface ICampaignNotificationTopic extends ICampaignEventPublisher {

    void subscribe(CampaignId campaignId, ICampaignListener listener);

    void publish(CampaignEvent e);
}
