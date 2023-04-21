package v201111.inventoryservice;

import com.google.api.ads.dfp.lib.DfpService;
import com.google.api.ads.dfp.lib.DfpServiceLogger;
import com.google.api.ads.dfp.lib.DfpUser;
import com.google.api.ads.dfp.lib.utils.v201111.StatementBuilder;
import com.google.api.ads.dfp.v201111.AdUnit;
import com.google.api.ads.dfp.v201111.AdUnitPage;
import com.google.api.ads.dfp.v201111.DeactivateAdUnits;
import com.google.api.ads.dfp.v201111.InventoryServiceInterface;
import com.google.api.ads.dfp.v201111.InventoryStatus;
import com.google.api.ads.dfp.v201111.Statement;
import com.google.api.ads.dfp.v201111.UpdateResult;
import org.apache.commons.lang.StringUtils;
import java.util.ArrayList;
import java.util.List;

/**
 * This example deactivates all active ad units. To determine which ad units
 * exist, run GetAllAdUnitsExample.java or GetInventoryTreeExample.java.
 *
 * Tags: InventoryService.getAdUnitsByStatement
 * Tags: InventoryService.performAdUnitAction
 *
 * @author api.arogal@gmail.com (Adam Rogal)
 */
public class DeactivateAdUnitsExample {

    public static void main(String[] args) {
        try {
            DfpServiceLogger.log();
            DfpUser user = new DfpUser();
            InventoryServiceInterface inventoryService = user.getService(DfpService.V201111.INVENTORY_SERVICE);
            String statementText = "WHERE status = :status LIMIT 500";
            Statement filterStatement = new StatementBuilder("").putValue("status", InventoryStatus.ACTIVE.toString()).toStatement();
            AdUnitPage page = new AdUnitPage();
            int offset = 0;
            List<String> adUnitIds = new ArrayList<String>();
            do {
                filterStatement.setQuery(statementText + " OFFSET " + offset);
                page = inventoryService.getAdUnitsByStatement(filterStatement);
                if (page.getResults() != null) {
                    int i = page.getStartIndex();
                    for (AdUnit adUnit : page.getResults()) {
                        System.out.println(i + ") Ad unit with ID \"" + adUnit.getId() + "\", name \"" + adUnit.getName() + "\", and status \"" + adUnit.getStatus() + "\" will be deactivated.");
                        adUnitIds.add(adUnit.getId());
                        i++;
                    }
                }
                offset += 500;
            } while (offset < page.getTotalResultSetSize());
            System.out.println("Number of ad units to be deactivated: " + adUnitIds.size());
            if (adUnitIds.size() > 0) {
                filterStatement.setQuery("WHERE id IN (" + StringUtils.join(adUnitIds, ",") + ")");
                DeactivateAdUnits action = new DeactivateAdUnits();
                UpdateResult result = inventoryService.performAdUnitAction(action, filterStatement);
                if (result != null && result.getNumChanges() > 0) {
                    System.out.println("Number of ad units deactivated: " + result.getNumChanges());
                } else {
                    System.out.println("No ad units were deactivated.");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
