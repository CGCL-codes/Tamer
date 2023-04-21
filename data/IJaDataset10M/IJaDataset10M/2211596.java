package v201101;

import com.google.api.adwords.lib.AdWordsService;
import com.google.api.adwords.lib.AdWordsServiceLogger;
import com.google.api.adwords.lib.AdWordsUser;
import com.google.api.adwords.v201101.cm.AdGroupCriterion;
import com.google.api.adwords.v201101.cm.AdGroupCriterionOperation;
import com.google.api.adwords.v201101.cm.AdGroupCriterionReturnValue;
import com.google.api.adwords.v201101.cm.AdGroupCriterionServiceInterface;
import com.google.api.adwords.v201101.cm.Bid;
import com.google.api.adwords.v201101.cm.BiddableAdGroupCriterion;
import com.google.api.adwords.v201101.cm.Criterion;
import com.google.api.adwords.v201101.cm.ManualCPCAdGroupCriterionBids;
import com.google.api.adwords.v201101.cm.Money;
import com.google.api.adwords.v201101.cm.Operator;

/**
 * This example updates the bid of an ad group criterion. To get ad group
 * criteria, run GetAllAdGroupCriteria.java.
 *
 * Tags: AdGroupCriterionService.mutate
 *
 * @author api.arogal@gmail.com (Adam Rogal)
 */
public class UpdateAdGroupCriterion {

    public static void main(String[] args) {
        try {
            AdWordsServiceLogger.log();
            AdWordsUser user = new AdWordsUser();
            AdGroupCriterionServiceInterface adGroupCriterionService = user.getService(AdWordsService.V201101.ADGROUP_CRITERION_SERVICE);
            long adGroupId = Long.parseLong("INSERT_AD_GROUP_ID_HERE");
            long criterionId = Long.parseLong("INSERT_CRITERION_ID_HERE");
            Criterion criterion = new Criterion();
            criterion.setId(criterionId);
            BiddableAdGroupCriterion biddableAdGroupCriterion = new BiddableAdGroupCriterion();
            biddableAdGroupCriterion.setAdGroupId(adGroupId);
            biddableAdGroupCriterion.setCriterion(criterion);
            ManualCPCAdGroupCriterionBids bids = new ManualCPCAdGroupCriterionBids();
            bids.setMaxCpc(new Bid(new Money(null, 1000000L)));
            biddableAdGroupCriterion.setBids(bids);
            AdGroupCriterionOperation operation = new AdGroupCriterionOperation();
            operation.setOperand(biddableAdGroupCriterion);
            operation.setOperator(Operator.SET);
            AdGroupCriterionOperation[] operations = new AdGroupCriterionOperation[] { operation };
            AdGroupCriterionReturnValue result = adGroupCriterionService.mutate(operations);
            if (result != null && result.getValue() != null) {
                for (AdGroupCriterion adGroupCriterionResult : result.getValue()) {
                    if (adGroupCriterionResult instanceof BiddableAdGroupCriterion) {
                        biddableAdGroupCriterion = (BiddableAdGroupCriterion) adGroupCriterionResult;
                        System.out.println("Ad group criterion with ad group id \"" + biddableAdGroupCriterion.getAdGroupId() + "\", criterion id \"" + biddableAdGroupCriterion.getCriterion().getId() + "\", type \"" + biddableAdGroupCriterion.getCriterion().getCriterionType() + "\", and bid \"" + ((ManualCPCAdGroupCriterionBids) biddableAdGroupCriterion.getBids()).getMaxCpc().getAmount().getMicroAmount() + "\" was updated.");
                    }
                }
            } else {
                System.out.println("No ad group criteria were updated.");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
