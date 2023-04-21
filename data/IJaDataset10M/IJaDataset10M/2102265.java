package v201111.creativeservice;

import com.google.api.ads.dfp.lib.DfpService;
import com.google.api.ads.dfp.lib.DfpServiceLogger;
import com.google.api.ads.dfp.lib.DfpUser;
import com.google.api.ads.dfp.v201111.Creative;
import com.google.api.ads.dfp.v201111.CreativePage;
import com.google.api.ads.dfp.v201111.CreativeServiceInterface;
import com.google.api.ads.dfp.v201111.Statement;

/**
 * This example gets all creatives. To create creatives, run
 * CreateCreativesExample.java.
 *
 * Tags: CreativeService.getCreativesByStatement
 *
 * @author api.arogal@gmail.com (Adam Rogal)
 */
public class GetAllCreativesExample {

    public static void main(String[] args) {
        try {
            DfpServiceLogger.log();
            DfpUser user = new DfpUser();
            CreativeServiceInterface creativeService = user.getService(DfpService.V201111.CREATIVE_SERVICE);
            CreativePage page = new CreativePage();
            Statement filterStatement = new Statement();
            int offset = 0;
            do {
                filterStatement.setQuery("LIMIT 500 OFFSET " + offset);
                page = creativeService.getCreativesByStatement(filterStatement);
                if (page.getResults() != null) {
                    int i = page.getStartIndex();
                    for (Creative creative : page.getResults()) {
                        System.out.println(i + ") Creative with ID \"" + creative.getId() + "\", name \"" + creative.getName() + "\", and type \"" + creative.getCreativeType() + "\" was found.");
                        i++;
                    }
                }
                offset += 500;
            } while (offset < page.getTotalResultSetSize());
            System.out.println("Number of results found: " + page.getTotalResultSetSize());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
