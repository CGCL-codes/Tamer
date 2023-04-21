package v201008;

import com.google.api.adwords.lib.AdWordsService;
import com.google.api.adwords.lib.AdWordsServiceLogger;
import com.google.api.adwords.lib.AdWordsUser;
import com.google.api.adwords.lib.utils.MapUtils;
import com.google.api.adwords.v201008.cm.Paging;
import com.google.api.adwords.v201008.cm.Placement;
import com.google.api.adwords.v201008.o.Attribute;
import com.google.api.adwords.v201008.o.AttributeType;
import com.google.api.adwords.v201008.o.IdeaType;
import com.google.api.adwords.v201008.o.PlacementAttribute;
import com.google.api.adwords.v201008.o.PlacementTypeAttribute;
import com.google.api.adwords.v201008.o.RelatedToUrlSearchParameter;
import com.google.api.adwords.v201008.o.RequestType;
import com.google.api.adwords.v201008.o.SearchParameter;
import com.google.api.adwords.v201008.o.SiteConstantsPlacementType;
import com.google.api.adwords.v201008.o.TargetingIdea;
import com.google.api.adwords.v201008.o.TargetingIdeaPage;
import com.google.api.adwords.v201008.o.TargetingIdeaSelector;
import com.google.api.adwords.v201008.o.TargetingIdeaServiceInterface;
import java.util.Map;

/**
 * This example gets related placements.
 *
 * Tags: TargetingIdeaService.get
 *
 * @author api.arogal@gmail.com (Adam Rogal)
 */
public class GetRelatedPlacements {

    public static void main(String[] args) throws Exception {
        AdWordsServiceLogger.log();
        AdWordsUser user = new AdWordsUser();
        TargetingIdeaServiceInterface targetingIdeaService = user.getService(AdWordsService.V201008.TARGETING_IDEA_SERVICE);
        String url = "mars.google.com";
        TargetingIdeaSelector selector = new TargetingIdeaSelector();
        selector.setRequestType(RequestType.IDEAS);
        selector.setIdeaType(IdeaType.PLACEMENT);
        selector.setRequestedAttributeTypes(new AttributeType[] { AttributeType.PLACEMENT, AttributeType.PLACEMENT_TYPE });
        Paging paging = new Paging();
        paging.setStartIndex(0);
        paging.setNumberResults(10);
        selector.setPaging(paging);
        RelatedToUrlSearchParameter relatedToUrlSearchParameter = new RelatedToUrlSearchParameter();
        relatedToUrlSearchParameter.setUrls(new String[] { url });
        relatedToUrlSearchParameter.setIncludeSubUrls(false);
        selector.setSearchParameters(new SearchParameter[] { relatedToUrlSearchParameter });
        TargetingIdeaPage page = targetingIdeaService.get(selector);
        if (page.getEntries() != null && page.getEntries().length > 0) {
            for (TargetingIdea targetingIdea : page.getEntries()) {
                Map<AttributeType, Attribute> data = MapUtils.toMap(targetingIdea.getData());
                Placement placement = ((PlacementAttribute) data.get(AttributeType.PLACEMENT)).getValue();
                SiteConstantsPlacementType placementType = ((PlacementTypeAttribute) data.get(AttributeType.PLACEMENT_TYPE)).getValue();
                System.out.println("Placement with url '" + placement.getUrl() + "' and type '" + placementType.toString() + "' was found.");
            }
        } else {
            System.out.println("No related placements were found.");
        }
    }
}
