package v201203.customtargetingservice;

import com.google.api.ads.dfp.lib.DfpService;
import com.google.api.ads.dfp.lib.DfpServiceLogger;
import com.google.api.ads.dfp.lib.DfpUser;
import com.google.api.ads.dfp.lib.utils.v201203.StatementBuilder;
import com.google.api.ads.dfp.v201203.CustomTargetingServiceInterface;
import com.google.api.ads.dfp.v201203.CustomTargetingValue;
import com.google.api.ads.dfp.v201203.CustomTargetingValuePage;
import com.google.api.ads.dfp.v201203.DeleteCustomTargetingValues;
import com.google.api.ads.dfp.v201203.UpdateResult;
import org.apache.commons.lang.StringUtils;
import java.util.ArrayList;
import java.util.List;

/**
 * This example deletes custom targeting values for a given custom targeting
 * key. To determine which custom targeting keys and values exist, run
 * GetAllCustomTargetingKeysAndValuesExample.java.
 *
 * Tags: CustomTargetingService.getCustomTargetingValuesByStatement
 * Tags: CustomTargetingService.performCustomTargetingValueAction
 *
 * @author api.arogal@gmail.com (Adam Rogal)
 */
public class DeleteCustomTargetingValuesExample {

    public static void main(String[] args) {
        try {
            DfpServiceLogger.log();
            DfpUser user = new DfpUser();
            CustomTargetingServiceInterface customTargetingService = user.getService(DfpService.V201203.CUSTOM_TARGETING_SERVICE);
            long customTargetingKeyId = Long.parseLong("INSERT_CUSTOM_TARGETING_KEY_ID_HERE");
            String statementText = "WHERE customTargetingKeyId = :customTargetingKeyId";
            StatementBuilder statementBuilder = new StatementBuilder("").putValue("customTargetingKeyId", customTargetingKeyId);
            CustomTargetingValuePage page = new CustomTargetingValuePage();
            int offset = 0;
            List<Long> customTargetingValueIds = new ArrayList<Long>();
            do {
                statementBuilder.setQuery(statementText + " LIMIT 500 OFFSET " + offset);
                page = customTargetingService.getCustomTargetingValuesByStatement(statementBuilder.toStatement());
                if (page.getResults() != null) {
                    for (CustomTargetingValue customTargetingValue : page.getResults()) {
                        customTargetingValueIds.add(customTargetingValue.getId());
                    }
                }
                offset += 500;
            } while (offset < page.getTotalResultSetSize());
            System.out.println("Number of custom targeting values to be deleted: " + customTargetingValueIds.size());
            if (customTargetingValueIds.size() > 0) {
                statementBuilder.setQuery("WHERE customTargetingKeyId = :customTargetingKeyId AND id IN (" + StringUtils.join(customTargetingValueIds, ",") + ")");
                DeleteCustomTargetingValues action = new DeleteCustomTargetingValues();
                UpdateResult result = customTargetingService.performCustomTargetingValueAction(action, statementBuilder.toStatement());
                if (result != null && result.getNumChanges() > 0) {
                    System.out.println("Number of custom targeting values deleted: " + result.getNumChanges());
                } else {
                    System.out.println("No custom targeting values were deleted.");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
