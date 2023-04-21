package com.heavylead.models.interfaces;

import java.util.List;

/**
 * The Interface ISelectCampaignModel.
 */
public interface ISelectCampaignModel {

    /**
     * Gets the campaign names.
     * 
     * @return the campaign names
     */
    List<String> getCampaignNames();

    /**
     * Gets the campaign by name.
     * 
     * @param selectedCampaignName the selected campaign name
     * 
     * @return the campaign by name
     */
    ICampaign selectCampaignByName(String selectedCampaignName);

    /**
     * Gets the selected campaign.
     * 
     * @return the selected campaign
     */
    ICampaign getSelectedCampaign();
}
