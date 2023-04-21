package com.pbxworkbench.campaign.ui.controller;

import com.pbxworkbench.campaign.domain.Campaign;

public class DefaultTestCallController implements TestCallController {

    private Campaign campaign;

    private CampaignWizardController wizController;

    public DefaultTestCallController(Campaign campaign, CampaignWizardController wizController) {
        super();
        this.campaign = campaign;
        this.wizController = wizController;
    }

    public void placeTestCall(String dialString) {
        wizController.placeTestCall(campaign, dialString);
    }
}
