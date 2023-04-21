package v201101;

import com.google.api.adwords.lib.AdWordsService;
import com.google.api.adwords.lib.AdWordsServiceLogger;
import com.google.api.adwords.lib.AdWordsUser;
import com.google.api.adwords.v201101.cm.AdGroup;
import com.google.api.adwords.v201101.cm.AdGroupCriterionOperation;
import com.google.api.adwords.v201101.cm.AdGroupCriterionServiceInterface;
import com.google.api.adwords.v201101.cm.AdGroupExperimentData;
import com.google.api.adwords.v201101.cm.AdGroupOperation;
import com.google.api.adwords.v201101.cm.AdGroupServiceInterface;
import com.google.api.adwords.v201101.cm.BidMultiplier;
import com.google.api.adwords.v201101.cm.BiddableAdGroupCriterion;
import com.google.api.adwords.v201101.cm.BiddableAdGroupCriterionExperimentData;
import com.google.api.adwords.v201101.cm.Criterion;
import com.google.api.adwords.v201101.cm.Experiment;
import com.google.api.adwords.v201101.cm.ExperimentDeltaStatus;
import com.google.api.adwords.v201101.cm.ExperimentOperation;
import com.google.api.adwords.v201101.cm.ExperimentReturnValue;
import com.google.api.adwords.v201101.cm.ExperimentServiceInterface;
import com.google.api.adwords.v201101.cm.ManualCPCAdGroupCriterionExperimentBidMultiplier;
import com.google.api.adwords.v201101.cm.ManualCPCAdGroupExperimentBidMultipliers;
import com.google.api.adwords.v201101.cm.Operator;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * This example creates an experiment using a query percentage of 10, which
 * defines what fraction of auctions should go to the control split (90%) vs.
 * the experiment split (10%), then adds experimental bid changes for criteria
 * and ad groups. To get campaigns, run GetAllCampaigns.java. To get ad groups,
 * run GetAllAdGroups.java. To get criteria, run GetAllAdGroupCriteria.java.
 *
 * Tags: ExperimentService.mutate
 *
 * @author api.arogal@gmail.com (Adam Rogal)
 */
public class AddExperiment {

    public static void main(String[] args) {
        try {
            AdWordsServiceLogger.log();
            AdWordsUser user = new AdWordsUser();
            ExperimentServiceInterface experimentService = user.getService(AdWordsService.V201101.EXPERIMENT_SERVICE);
            AdGroupServiceInterface adGroupService = user.getService(AdWordsService.V201101.ADGROUP_SERVICE);
            AdGroupCriterionServiceInterface adGroupCriterionService = user.getService(AdWordsService.V201101.ADGROUP_CRITERION_SERVICE);
            long campaignId = Long.parseLong("INSERT_CAMPAIGN_ID_HERE");
            long adGroupId = Long.parseLong("INSERT_AD_GROUP_ID_HERE");
            long criterionId = Long.parseLong("INSERT_CRITERION_ID_HERE");
            Experiment experiment = new Experiment();
            experiment.setCampaignId(campaignId);
            experiment.setName("Interplanetary Experiment #" + System.currentTimeMillis());
            experiment.setQueryPercentage(10);
            experiment.setStartDateTime(new SimpleDateFormat("yyyyMMdd HHmmss").format(new Date()));
            ExperimentOperation experimentOperation = new ExperimentOperation();
            experimentOperation.setOperand(experiment);
            experimentOperation.setOperator(Operator.ADD);
            ExperimentReturnValue result = experimentService.mutate(new ExperimentOperation[] { experimentOperation });
            if (result.getValue() != null) {
                for (Experiment experimentResult : result.getValue()) {
                    System.out.println("Experiment with name \"" + experimentResult.getName() + "\" and id \"" + experimentResult.getId() + "\" was added.");
                }
            } else {
                System.out.println("No experiments were added.");
                return;
            }
            Long experimentId = result.getValue()[0].getId();
            AdGroup adGroup = new AdGroup();
            adGroup.setId(adGroupId);
            ManualCPCAdGroupExperimentBidMultipliers adGroupExperimentBidMultipliers = new ManualCPCAdGroupExperimentBidMultipliers();
            adGroupExperimentBidMultipliers.setMaxCpcMultiplier(new BidMultiplier(1.5, null));
            AdGroupExperimentData adGroupExperimentData = new AdGroupExperimentData();
            adGroupExperimentData.setExperimentId(experimentId);
            adGroupExperimentData.setExperimentDeltaStatus(ExperimentDeltaStatus.MODIFIED);
            adGroupExperimentData.setExperimentBidMultipliers(adGroupExperimentBidMultipliers);
            adGroup.setExperimentData(adGroupExperimentData);
            AdGroupOperation adGroupOperation = new AdGroupOperation();
            adGroupOperation.setOperand(adGroup);
            adGroupOperation.setOperator(Operator.SET);
            adGroup = adGroupService.mutate(new AdGroupOperation[] { adGroupOperation }).getValue()[0];
            System.out.println("Ad group with name \"" + adGroup.getName() + "\" and id \"" + adGroup.getId() + "\" was updated for the experiment.");
            BiddableAdGroupCriterion adGroupCriterion = new BiddableAdGroupCriterion();
            adGroupCriterion.setCriterion(new Criterion(criterionId, null));
            adGroupCriterion.setAdGroupId(adGroupId);
            ManualCPCAdGroupCriterionExperimentBidMultiplier adGroupCriterionExperimentBidMultiplier = new ManualCPCAdGroupCriterionExperimentBidMultiplier();
            adGroupCriterionExperimentBidMultiplier.setMaxCpcMultiplier(new BidMultiplier(1.5, null));
            BiddableAdGroupCriterionExperimentData adGroupCriterionExperimentData = new BiddableAdGroupCriterionExperimentData();
            adGroupCriterionExperimentData.setExperimentId(experimentId);
            adGroupCriterionExperimentData.setExperimentDeltaStatus(ExperimentDeltaStatus.MODIFIED);
            adGroupCriterionExperimentData.setExperimentBidMultiplier(adGroupCriterionExperimentBidMultiplier);
            adGroupCriterion.setExperimentData(adGroupCriterionExperimentData);
            AdGroupCriterionOperation adGroupCriterionOperation = new AdGroupCriterionOperation();
            adGroupCriterionOperation.setOperand(adGroupCriterion);
            adGroupCriterionOperation.setOperator(Operator.SET);
            adGroupCriterion = (BiddableAdGroupCriterion) adGroupCriterionService.mutate(new AdGroupCriterionOperation[] { adGroupCriterionOperation }).getValue()[0];
            System.out.println("Ad group criterion with ad group id \"" + adGroupCriterion.getAdGroupId() + "\" and criterion id \"" + adGroupCriterion.getCriterion().getId() + "\" was updated for the experiment.");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
