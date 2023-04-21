package v201203.creativeservice;

import com.google.api.ads.dfp.lib.DfpService;
import com.google.api.ads.dfp.lib.DfpServiceLogger;
import com.google.api.ads.dfp.lib.DfpUser;
import com.google.api.ads.dfp.lib.utils.v201203.StatementBuilder;
import com.google.api.ads.dfp.v201203.Creative;
import com.google.api.ads.dfp.v201203.CreativePage;
import com.google.api.ads.dfp.v201203.CreativeServiceInterface;
import com.google.api.ads.dfp.v201203.ImageCreative;
import com.google.api.ads.dfp.v201203.Statement;

/**
 * This example updates the destination URL of all image creatives up to
 * the first 500. To determine which image creatives exist, run
 * GetAllCreativesExample.java.
 *
 * Tags: CreativeService.getCreativesByStatement
 * Tags: CreativeService.updateCreatives
 *
 * @author api.arogal@gmail.com (Adam Rogal)
 */
public class UpdateCreativesExample {

    public static void main(String[] args) {
        try {
            DfpServiceLogger.log();
            DfpUser user = new DfpUser();
            CreativeServiceInterface creativeService = user.getService(DfpService.V201203.CREATIVE_SERVICE);
            Statement filterStatement = new StatementBuilder("WHERE creativeType = :creativeType LIMIT 500").putValue("creativeType", ImageCreative.class.getSimpleName()).toStatement();
            CreativePage page = creativeService.getCreativesByStatement(filterStatement);
            if (page.getResults() != null) {
                Creative[] creatives = page.getResults();
                for (Creative creative : creatives) {
                    if (creative instanceof ImageCreative) {
                        ImageCreative imageCreative = (ImageCreative) creative;
                        imageCreative.setDestinationUrl("http://news.google.com");
                    }
                }
                creatives = creativeService.updateCreatives(creatives);
                if (creatives != null) {
                    for (Creative creative : creatives) {
                        if (creative instanceof ImageCreative) {
                            ImageCreative imageCreative = (ImageCreative) creative;
                            System.out.println("An image creative with ID \"" + imageCreative.getId() + "\" and destination URL \"" + imageCreative.getDestinationUrl() + "\" was updated.");
                        }
                    }
                } else {
                    System.out.println("No creatives updated.");
                }
            } else {
                System.out.println("No creatives found to update.");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
