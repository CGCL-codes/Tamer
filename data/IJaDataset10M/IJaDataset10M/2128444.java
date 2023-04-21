package v201111.customtargetingservice;

import com.google.api.ads.dfp.lib.DfpService;
import com.google.api.ads.dfp.lib.DfpServiceLogger;
import com.google.api.ads.dfp.lib.DfpUser;
import com.google.api.ads.dfp.lib.utils.v201111.StatementBuilder;
import com.google.api.ads.dfp.v201111.CustomTargetingKey;
import com.google.api.ads.dfp.v201111.CustomTargetingKeyPage;
import com.google.api.ads.dfp.v201111.CustomTargetingServiceInterface;
import com.google.api.ads.dfp.v201111.DeleteCustomTargetingKeys;
import com.google.api.ads.dfp.v201111.UpdateResult;
import org.apache.commons.lang.StringUtils;
import java.util.ArrayList;
import java.util.List;

/**
 * This example deletes custom targeting key by its name. To determine which
 * custom targeting keys exist, run
 * GetAllCustomTargetingKeysAndValuesExample.java.
 *
 * Tags: CustomTargetingService.getCustomTargetingKeysByStatement
 * Tags: CustomTargetingService.performCustomTargetingKeyAction
 *
 * @author api.arogal@gmail.com (Adam Rogal)
 */
public class DeleteCustomTargetingKeysExample {

    public static void main(String[] args) {
        try {
            DfpServiceLogger.log();
            DfpUser user = new DfpUser();
            CustomTargetingServiceInterface customTargetingService = user.getService(DfpService.V201111.CUSTOM_TARGETING_SERVICE);
            String customTargetingKeyName = "INSERT_CUSTOM_TARGETING_KEY_NAME_HERE";
            String statementText = "WHERE name = :name";
            StatementBuilder statementBuilder = new StatementBuilder("").putValue("name", customTargetingKeyName);
            CustomTargetingKeyPage page = new CustomTargetingKeyPage();
            int offset = 0;
            List<Long> customTargetingKeyIds = new ArrayList<Long>();
            do {
                statementBuilder.setQuery(statementText + " LIMIT 500 OFFSET " + offset);
                page = customTargetingService.getCustomTargetingKeysByStatement(statementBuilder.toStatement());
                if (page.getResults() != null) {
                    for (CustomTargetingKey customTargetingKey : page.getResults()) {
                        customTargetingKeyIds.add(customTargetingKey.getId());
                    }
                }
                offset += 500;
            } while (offset < page.getTotalResultSetSize());
            System.out.println("Number of custom targeting keys to be deleted: " + customTargetingKeyIds.size());
            if (customTargetingKeyIds.size() > 0) {
                statementBuilder.setQuery("WHERE id IN (" + StringUtils.join(customTargetingKeyIds, ",") + ")");
                DeleteCustomTargetingKeys action = new DeleteCustomTargetingKeys();
                UpdateResult result = customTargetingService.performCustomTargetingKeyAction(action, statementBuilder.toStatement());
                if (result != null && result.getNumChanges() > 0) {
                    System.out.println("Number of custom targeting keys deleted: " + result.getNumChanges());
                } else {
                    System.out.println("No custom targeting keys were deleted.");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
