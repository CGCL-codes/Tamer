package v201201.customtargetingservice;

import com.google.api.ads.dfp.lib.DfpService;
import com.google.api.ads.dfp.lib.DfpServiceLogger;
import com.google.api.ads.dfp.lib.DfpUser;
import com.google.api.ads.dfp.lib.utils.v201201.StatementBuilder;
import com.google.api.ads.dfp.v201201.CustomTargetingKey;
import com.google.api.ads.dfp.v201201.CustomTargetingKeyPage;
import com.google.api.ads.dfp.v201201.CustomTargetingKeyType;
import com.google.api.ads.dfp.v201201.CustomTargetingServiceInterface;
import com.google.api.ads.dfp.v201201.Statement;

/**
 * This example gets all predefined custom targeting keys. The statement
 * retrieves up to the maximum page size limit of 500. To create custom
 * targeting keys, run CreateCustomTargetingKeysAndValuesExample.java.
 *
 * Tags: CustomTargetingService.getCustomTargetingKeysByStatement
 *
 * @author api.arogal@gmail.com (Adam Rogal)
 */
public class GetCustomTargetingKeysByStatementExample {

    public static void main(String[] args) {
        try {
            DfpServiceLogger.log();
            DfpUser user = new DfpUser();
            CustomTargetingServiceInterface customTargetingService = user.getService(DfpService.V201201.CUSTOM_TARGETING_SERVICE);
            Statement filterStatement = new StatementBuilder("WHERE type = :type LIMIT 500").putValue("type", CustomTargetingKeyType.PREDEFINED.toString()).toStatement();
            CustomTargetingKeyPage page = customTargetingService.getCustomTargetingKeysByStatement(filterStatement);
            if (page.getResults() != null) {
                int i = page.getStartIndex();
                for (CustomTargetingKey customTargetingKey : page.getResults()) {
                    System.out.println(i + ") Custom targeting key with ID \"" + customTargetingKey.getId() + "\", name \"" + customTargetingKey.getName() + "\", and display name \"" + customTargetingKey.getDisplayName() + "\" was found.");
                    i++;
                }
            }
            System.out.println("Number of results found: " + page.getTotalResultSetSize());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
