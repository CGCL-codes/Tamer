package v201109;

import com.google.api.adwords.lib.AdWordsService;
import com.google.api.adwords.lib.AdWordsServiceLogger;
import com.google.api.adwords.lib.AdWordsUser;
import com.google.api.adwords.v201109.cm.Criterion;
import com.google.api.adwords.v201109.cm.Keyword;
import com.google.api.adwords.v201109.cm.KeywordMatchType;
import com.google.api.adwords.v201109.cm.Language;
import com.google.api.adwords.v201109.cm.Location;
import com.google.api.adwords.v201109.cm.Money;
import com.google.api.adwords.v201109.o.AdGroupEstimateRequest;
import com.google.api.adwords.v201109.o.CampaignEstimateRequest;
import com.google.api.adwords.v201109.o.KeywordEstimate;
import com.google.api.adwords.v201109.o.KeywordEstimateRequest;
import com.google.api.adwords.v201109.o.TrafficEstimatorResult;
import com.google.api.adwords.v201109.o.TrafficEstimatorSelector;
import com.google.api.adwords.v201109.o.TrafficEstimatorServiceInterface;
import java.util.ArrayList;
import java.util.List;

/**
 * This example gets keyword traffic estimates.
 *
 * Tags: TrafficEstimatorService.get
 *
 * @category adx-exclude
 * @author api.arogal@gmail.com (Adam Rogal)
 */
public class GetTrafficEstimates {

    public static void main(String[] args) {
        try {
            AdWordsServiceLogger.log();
            AdWordsUser user = new AdWordsUser();
            TrafficEstimatorServiceInterface trafficEstimatorService = user.getService(AdWordsService.V201109.TRAFFIC_ESTIMATOR_SERVICE);
            List<Keyword> keywords = new ArrayList<Keyword>();
            keywords.add(new Keyword(null, null, null, "mars cruise", KeywordMatchType.BROAD));
            keywords.add(new Keyword(null, null, null, "cheap cruise", KeywordMatchType.PHRASE));
            keywords.add(new Keyword(null, null, null, "cruise", KeywordMatchType.EXACT));
            List<KeywordEstimateRequest> keywordEstimateRequests = new ArrayList<KeywordEstimateRequest>();
            for (Keyword keyword : keywords) {
                KeywordEstimateRequest keywordEstimateRequest = new KeywordEstimateRequest();
                keywordEstimateRequest.setKeyword(keyword);
                keywordEstimateRequests.add(keywordEstimateRequest);
            }
            KeywordEstimateRequest negativeKeywordEstimateRequest = new KeywordEstimateRequest();
            negativeKeywordEstimateRequest.setKeyword(new Keyword(null, null, null, "hiking tour", KeywordMatchType.BROAD));
            negativeKeywordEstimateRequest.setIsNegative(true);
            keywordEstimateRequests.add(negativeKeywordEstimateRequest);
            List<AdGroupEstimateRequest> adGroupEstimateRequests = new ArrayList<AdGroupEstimateRequest>();
            AdGroupEstimateRequest adGroupEstimateRequest = new AdGroupEstimateRequest();
            adGroupEstimateRequest.setKeywordEstimateRequests(keywordEstimateRequests.toArray(new KeywordEstimateRequest[] {}));
            adGroupEstimateRequest.setMaxCpc(new Money(null, 1000000L));
            adGroupEstimateRequests.add(adGroupEstimateRequest);
            List<CampaignEstimateRequest> campaignEstimateRequests = new ArrayList<CampaignEstimateRequest>();
            CampaignEstimateRequest campaignEstimateRequest = new CampaignEstimateRequest();
            campaignEstimateRequest.setAdGroupEstimateRequests(adGroupEstimateRequests.toArray(new AdGroupEstimateRequest[] {}));
            Location unitedStates = new Location();
            unitedStates.setId(2840L);
            Language english = new Language();
            english.setId(1000L);
            campaignEstimateRequest.setCriteria(new Criterion[] { unitedStates, english });
            campaignEstimateRequests.add(campaignEstimateRequest);
            TrafficEstimatorSelector selector = new TrafficEstimatorSelector();
            selector.setCampaignEstimateRequests(campaignEstimateRequests.toArray(new CampaignEstimateRequest[] {}));
            TrafficEstimatorResult result = trafficEstimatorService.get(selector);
            if (result != null && result.getCampaignEstimates() != null) {
                KeywordEstimate[] keywordEstimates = result.getCampaignEstimates()[0].getAdGroupEstimates()[0].getKeywordEstimates();
                for (int i = 0; i < keywordEstimates.length; i++) {
                    Keyword keyword = keywordEstimateRequests.get(i).getKeyword();
                    KeywordEstimate keywordEstimate = keywordEstimates[i];
                    if (Boolean.TRUE.equals(keywordEstimateRequests.get(i).getIsNegative())) {
                        continue;
                    }
                    double meanAverageCpc = (keywordEstimate.getMin().getAverageCpc().getMicroAmount() + keywordEstimate.getMax().getAverageCpc().getMicroAmount()) / 2.0;
                    double meanAveragePosition = (keywordEstimate.getMin().getAveragePosition() + keywordEstimate.getMax().getAveragePosition()) / 2.0;
                    double meanClicks = (keywordEstimate.getMin().getClicksPerDay() + keywordEstimate.getMax().getClicksPerDay()) / 2.0;
                    double meanTotalCost = (keywordEstimate.getMin().getTotalCost().getMicroAmount() + keywordEstimate.getMax().getTotalCost().getMicroAmount()) / 2.0;
                    System.out.println(String.format("Results for the keyword with text '%s' and match type '%s':", keyword.getText(), keyword.getMatchType()));
                    System.out.printf("\tEstimated average CPC: %.2f\n", meanAverageCpc);
                    System.out.printf("\tEstimated ad position: %.2f\n", meanAveragePosition);
                    System.out.printf("\tEstimated daily clicks: %.2f\n", meanClicks);
                    System.out.printf("\tEstimated daily cost: %.2f\n\n", meanTotalCost);
                }
            } else {
                System.out.println("No traffic estimates were returned.");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
