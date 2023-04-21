package v201101;

import com.google.api.adwords.lib.AdWordsService;
import com.google.api.adwords.lib.AdWordsServiceLogger;
import com.google.api.adwords.lib.AdWordsUser;
import com.google.api.adwords.v201101.cm.AdGroupCriterion;
import com.google.api.adwords.v201101.cm.AdGroupCriterionOperation;
import com.google.api.adwords.v201101.cm.AdGroupCriterionReturnValue;
import com.google.api.adwords.v201101.cm.AdGroupCriterionServiceInterface;
import com.google.api.adwords.v201101.cm.Criterion;
import com.google.api.adwords.v201101.cm.Operator;

/**
 * This example deletes an ad group criterion using the 'REMOVE' operator. To
 * get ad group criteria, run GetAllAdGroupCriteria.java.
 *
 * Tags: AdGroupCriterionService.mutate
 *
 * @author api.arogal@gmail.com (Adam Rogal)
 */
public class DeleteAdGroupCriterion {

    public static void main(String[] args) {
        try {
            AdWordsServiceLogger.log();
            AdWordsUser user = new AdWordsUser();
            AdGroupCriterionServiceInterface adGroupCriterionService = user.getService(AdWordsService.V201101.ADGROUP_CRITERION_SERVICE);
            long adGroupId = Long.parseLong("INSERT_AD_GROUP_ID_HERE");
            long criterionId = Long.parseLong("INSERT_CRITERION_ID_HERE");
            Criterion criterion = new Criterion();
            criterion.setId(criterionId);
            AdGroupCriterion adGroupCriterion = new AdGroupCriterion();
            adGroupCriterion.setAdGroupId(adGroupId);
            adGroupCriterion.setCriterion(criterion);
            AdGroupCriterionOperation operation = new AdGroupCriterionOperation();
            operation.setOperand(adGroupCriterion);
            operation.setOperator(Operator.REMOVE);
            AdGroupCriterionOperation[] operations = new AdGroupCriterionOperation[] { operation };
            AdGroupCriterionReturnValue result = adGroupCriterionService.mutate(operations);
            if (result != null && result.getValue() != null) {
                for (AdGroupCriterion adGroupCriterionResult : result.getValue()) {
                    System.out.println("Ad group criterion with ad group id \"" + adGroupCriterionResult.getAdGroupId() + "\", criterion id \"" + adGroupCriterionResult.getCriterion().getId() + "\", and type \"" + adGroupCriterionResult.getCriterion().getCriterionType() + "\" was deleted.");
                }
            } else {
                System.out.println("No ad group criteria were deleted.");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
