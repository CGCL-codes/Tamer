package v201111.placementservice;

import com.google.api.ads.dfp.lib.DfpService;
import com.google.api.ads.dfp.lib.DfpServiceLogger;
import com.google.api.ads.dfp.lib.DfpUser;
import com.google.api.ads.dfp.v201111.Statement;
import com.google.api.ads.dfp.v201111.Placement;
import com.google.api.ads.dfp.v201111.PlacementPage;
import com.google.api.ads.dfp.v201111.PlacementServiceInterface;

/**
 * This example gets all placements. To create placements, run
 * CreatePlacementsExample.java.
 *
 * Tags: PlacementService.getPlacementsByStatement
 *
 * @author api.arogal@gmail.com (Adam Rogal)
 */
public class GetAllPlacementsExample {

    public static void main(String[] args) {
        try {
            DfpServiceLogger.log();
            DfpUser user = new DfpUser();
            PlacementServiceInterface placementService = user.getService(DfpService.V201111.PLACEMENT_SERVICE);
            PlacementPage page = new PlacementPage();
            Statement filterStatement = new Statement();
            int offset = 0;
            do {
                filterStatement.setQuery("LIMIT 500 OFFSET " + offset);
                page = placementService.getPlacementsByStatement(filterStatement);
                if (page.getResults() != null) {
                    int i = page.getStartIndex();
                    for (Placement placement : page.getResults()) {
                        System.out.println(i + ") Placement with ID \"" + placement.getId() + "\" and name \"" + placement.getName() + "\" was found.");
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
