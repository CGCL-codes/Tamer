package v201109;

import com.google.api.adwords.lib.AdWordsService;
import com.google.api.adwords.lib.AdWordsServiceLogger;
import com.google.api.adwords.lib.AdWordsUser;
import com.google.api.adwords.v201109.cm.CampaignAdExtension;
import com.google.api.adwords.v201109.cm.CampaignAdExtensionPage;
import com.google.api.adwords.v201109.cm.CampaignAdExtensionServiceInterface;
import com.google.api.adwords.v201109.cm.OrderBy;
import com.google.api.adwords.v201109.cm.Predicate;
import com.google.api.adwords.v201109.cm.PredicateOperator;
import com.google.api.adwords.v201109.cm.Selector;
import com.google.api.adwords.v201109.cm.SortOrder;

/**
 * This example gets all campaign ad extension for a campaign. To add a campaign
 * ad extension, run AddCampaignAdExtension.java. To get campaigns, run
 * GetAllCampaigns.java.
 *
 * Tags: CampaignAdExtensionService.get
 *
 * @category adx-exclude
 * @author api.arogal@gmail.com (Adam Rogal)
 */
public class GetAllCampaignAdExtensions {

    public static void main(String[] args) {
        try {
            AdWordsServiceLogger.log();
            AdWordsUser user = new AdWordsUser();
            CampaignAdExtensionServiceInterface campaignAdExtensionService = user.getService(AdWordsService.V201109.CAMPAIGN_AD_EXTENSION_SERVICE);
            Long campaignId = Long.parseLong("INSERT_CAMPAIGN_ID_HERE");
            Selector selector = new Selector();
            selector.setFields(new String[] { "AdExtensionId", "CampaignId" });
            selector.setOrdering(new OrderBy[] { new OrderBy("AdExtensionId", SortOrder.ASCENDING) });
            Predicate campaignIdPredicate = new Predicate("CampaignId", PredicateOperator.IN, new String[] { campaignId.toString() });
            selector.setPredicates(new Predicate[] { campaignIdPredicate });
            CampaignAdExtensionPage page = campaignAdExtensionService.get(selector);
            if (page.getEntries() != null && page.getEntries().length > 0) {
                for (CampaignAdExtension campaignAdExtension : page.getEntries()) {
                    System.out.println("Campaign ad extension with campaign id \"" + campaignAdExtension.getCampaignId() + "\", ad extension id \"" + campaignAdExtension.getAdExtension().getId() + "\", and type \"" + campaignAdExtension.getAdExtension().getAdExtensionType() + "\" was found.");
                }
            } else {
                System.out.println("No campaign ad extensions were found.");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
