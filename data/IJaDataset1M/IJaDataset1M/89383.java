package v201203.inventoryservice;

import com.google.api.ads.dfp.lib.DfpService;
import com.google.api.ads.dfp.lib.DfpServiceLogger;
import com.google.api.ads.dfp.lib.DfpUser;
import com.google.api.ads.dfp.v201203.AdUnit;
import com.google.api.ads.dfp.v201203.AdUnitPage;
import com.google.api.ads.dfp.v201203.Statement;
import com.google.api.ads.dfp.v201203.InventoryServiceInterface;

/**
 * This example gets all ad units. To create ad units, run
 * CreateAdUnitsExample.java.
 *
 * Tags: InventoryService.getAdUnitsByStatement
 *
 * @author api.arogal@gmail.com (Adam Rogal)
 */
public class GetAllAdUnitsExample {

    public static void main(String[] args) {
        try {
            DfpServiceLogger.log();
            DfpUser user = new DfpUser();
            InventoryServiceInterface inventoryService = user.getService(DfpService.V201203.INVENTORY_SERVICE);
            AdUnitPage page = new AdUnitPage();
            Statement filterStatement = new Statement();
            int offset = 0;
            do {
                filterStatement.setQuery("LIMIT 500 OFFSET " + offset);
                page = inventoryService.getAdUnitsByStatement(filterStatement);
                if (page.getResults() != null) {
                    int i = page.getStartIndex();
                    for (AdUnit adUnit : page.getResults()) {
                        System.out.println(i + ") Ad unit with ID \"" + adUnit.getId() + "\" name \"" + adUnit.getName() + "\", and status \"" + adUnit.getStatus() + "\" was found.");
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
