package v201108.inventoryservice;

import com.google.api.ads.dfp.lib.DfpService;
import com.google.api.ads.dfp.lib.DfpServiceLogger;
import com.google.api.ads.dfp.lib.DfpUser;
import com.google.api.ads.dfp.v201108.AdUnitSize;
import com.google.api.ads.dfp.v201108.InventoryServiceInterface;

/**
 * This example gets all ad unit sizes in the network.
 *
 * Tags: InventoryService.getAdUnitSizes
 *
 * @author api.arogal@gmail.com (Adam Rogal)
 */
public class GetAdUnitSizesExample {

    public static void main(String[] args) {
        try {
            DfpServiceLogger.log();
            DfpUser user = new DfpUser();
            InventoryServiceInterface inventoryService = user.getService(DfpService.V201108.INVENTORY_SERVICE);
            AdUnitSize[] adUnitSizes = inventoryService.getAdUnitSizes();
            if (adUnitSizes != null) {
                for (int i = 0; i < adUnitSizes.length; i++) {
                    AdUnitSize adUnitSize = adUnitSizes[i];
                    System.out.printf("%s) Ad unit size (%sx%s) was found.\n", i, adUnitSize.getSize().getWidth(), adUnitSize.getSize().getHeight());
                }
            } else {
                System.out.println("No ad unit sizes found.");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
