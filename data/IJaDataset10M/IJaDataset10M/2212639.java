package v201101;

import com.google.api.adwords.lib.AdWordsService;
import com.google.api.adwords.lib.AdWordsServiceLogger;
import com.google.api.adwords.lib.AdWordsUser;
import com.google.api.adwords.v201101.cm.Ad;
import com.google.api.adwords.v201101.cm.AdGroupAd;
import com.google.api.adwords.v201101.cm.AdGroupAdOperation;
import com.google.api.adwords.v201101.cm.AdGroupAdReturnValue;
import com.google.api.adwords.v201101.cm.AdGroupAdServiceInterface;
import com.google.api.adwords.v201101.cm.AdGroupAdStatus;
import com.google.api.adwords.v201101.cm.Operator;

/**
 * This example updates an ad by setting the status to 'PAUSED'. To get ads,
 * run GetAllAds.java.
 *
 * Tags: AdGroupAdService.mutate
 *
 * @author api.arogal@gmail.com (Adam Rogal)
 */
public class UpdateAd {

    public static void main(String[] args) {
        try {
            AdWordsServiceLogger.log();
            AdWordsUser user = new AdWordsUser();
            AdGroupAdServiceInterface adGroupAdService = user.getService(AdWordsService.V201101.ADGROUP_AD_SERVICE);
            long adGroupId = Long.parseLong("INSERT_AD_GROUP_ID_HERE");
            long adId = Long.parseLong("INSERT_AD_ID_HERE");
            Ad ad = new Ad();
            ad.setId(adId);
            AdGroupAd adGroupAd = new AdGroupAd();
            adGroupAd.setAdGroupId(adGroupId);
            adGroupAd.setAd(ad);
            adGroupAd.setStatus(AdGroupAdStatus.PAUSED);
            AdGroupAdOperation operation = new AdGroupAdOperation();
            operation.setOperand(adGroupAd);
            operation.setOperator(Operator.SET);
            AdGroupAdOperation[] operations = new AdGroupAdOperation[] { operation };
            AdGroupAdReturnValue result = adGroupAdService.mutate(operations);
            if (result != null && result.getValue() != null) {
                for (AdGroupAd adGroupAdResult : result.getValue()) {
                    System.out.println("Ad with id \"" + adGroupAdResult.getAd().getId() + "\", type \"" + adGroupAdResult.getAd().getAdType() + "\", and status \"" + adGroupAdResult.getStatus() + "\" was updated.");
                }
            } else {
                System.out.println("No ads were updated.");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
