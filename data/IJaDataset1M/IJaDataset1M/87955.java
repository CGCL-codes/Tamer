package v201101;

import com.google.api.adwords.lib.AdWordsService;
import com.google.api.adwords.lib.AdWordsServiceLogger;
import com.google.api.adwords.lib.AdWordsUser;
import com.google.api.adwords.v201101.cm.Campaign;
import com.google.api.adwords.v201101.cm.CampaignPage;
import com.google.api.adwords.v201101.cm.CampaignServiceInterface;
import com.google.api.adwords.v201101.cm.OrderBy;
import com.google.api.adwords.v201101.cm.Selector;
import com.google.api.adwords.v201101.cm.SortOrder;

/**
 * This example gets all campaigns. To add a campaign, run AddCampaign.java.
 *
 * Tags: CampaignService.get
 *
 * @author api.arogal@gmail.com (Adam Rogal)
 */
public class GetAllCampaigns {

    public static void main(String[] args) {
        try {
            AdWordsServiceLogger.log();
            AdWordsUser user = new AdWordsUser();
            CampaignServiceInterface campaignService = user.getService(AdWordsService.V201101.CAMPAIGN_SERVICE);
            Selector selector = new Selector();
            selector.setFields(new String[] { "Id", "Name" });
            selector.setOrdering(new OrderBy[] { new OrderBy("Name", SortOrder.ASCENDING) });
            CampaignPage page = campaignService.get(selector);
            if (page.getEntries() != null) {
                for (Campaign campaign : page.getEntries()) {
                    System.out.println("Campaign with name \"" + campaign.getName() + "\" and id \"" + campaign.getId() + "\" was found.");
                }
            } else {
                System.out.println("No campaigns were found.");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
