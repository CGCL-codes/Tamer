package com.pbxworkbench.campaign.ui.controller;

import java.util.ArrayList;
import java.util.List;
import com.pbxworkbench.campaign.domain.Callee;
import com.pbxworkbench.campaign.domain.CalleeId;
import com.pbxworkbench.campaign.domain.Campaign;
import com.pbxworkbench.campaign.domain.CampaignId;
import com.pbxworkbench.campaign.domain.ICallStatus;
import com.pbxworkbench.campaign.svc.CallStateChangeEvent;
import com.pbxworkbench.campaign.svc.CampaignCallCompletedEvent;
import com.pbxworkbench.campaign.svc.CampaignCallStartedEvent;
import com.pbxworkbench.campaign.svc.CampaignFinishedEvent;
import com.pbxworkbench.campaign.svc.CampaignPausedEvent;
import com.pbxworkbench.campaign.svc.CampaignStartedEvent;
import com.pbxworkbench.campaign.svc.ICampaignListener;
import com.pbxworkbench.campaign.svc.ICampaignService;
import com.pbxworkbench.campaign.ui.model.CampaignStatus;
import com.pbxworkbench.commons.DefaultChangeable;

public class CampaignStatusImpl extends DefaultChangeable implements CampaignStatus {

    private final Campaign campaign;

    private final ICampaignService svc;

    public CampaignStatusImpl(Campaign campaign, ICampaignService svc) {
        this.campaign = campaign;
        this.svc = svc;
        svc.addCampaignListener(campaign.getId(), new SynchronizingCampaignListener());
    }

    public List<? extends ICallStatus> getCalls() {
        List<Callee> callees = campaign.getCallees().getCallees();
        List<MyCall> calls = new ArrayList<MyCall>(callees.size());
        for (Callee callee : callees) {
            calls.add(new MyCall(campaign.getId(), callee));
        }
        return calls;
    }

    public String getStatus() {
        return svc.getCampaignStatus(campaign.getId());
    }

    private class MyCall implements ICallStatus {

        private CampaignId campaignId;

        private Callee callee;

        public MyCall(CampaignId campaignId, Callee callee) {
            this.campaignId = campaignId;
            this.callee = callee;
        }

        public String getStatus() {
            return svc.getCallStatus(campaignId, callee).getStatus();
        }

        public String getDialString() {
            return svc.getCallStatus(campaignId, callee).getDialString();
        }

        public String getName() {
            return svc.getCallStatus(campaignId, callee).getName();
        }

        public CalleeId getCalleeId() {
            return svc.getCallStatus(campaignId, callee).getCalleeId();
        }

        public boolean isCompleted() {
            return svc.getCallStatus(campaignId, callee).isCompleted();
        }
    }

    private class SynchronizingCampaignListener implements ICampaignListener {

        public void onCallCompleted(CampaignCallCompletedEvent e) {
            fireChanged();
        }

        public void onCallStateChanged(CallStateChangeEvent e) {
            fireChanged();
        }

        public void onCallStarted(CampaignCallStartedEvent e) {
            fireChanged();
        }

        public void onFinished(CampaignFinishedEvent e) {
            fireChanged();
        }

        public void onPaused(CampaignPausedEvent e) {
            fireChanged();
        }

        public void onStarted(CampaignStartedEvent e) {
            fireChanged();
        }
    }

    public String getDialGroupName() {
        return campaign.getCallees().getName();
    }
}
