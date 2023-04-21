package v200909;

import com.google.api.adwords.lib.AdWordsService;
import com.google.api.adwords.lib.AdWordsServiceLogger;
import com.google.api.adwords.lib.AdWordsUser;
import com.google.api.adwords.v200909.info.ApiUsageInfo;
import com.google.api.adwords.v200909.info.ApiUsageType;
import com.google.api.adwords.v200909.info.InfoSelector;
import com.google.api.adwords.v200909.info.InfoServiceInterface;

/**
 * This example gets the total number of API units that can be used in a month
 * for a given developer token. This example must be run as the MCC user that
 * owns the developer token.
 *
 * Tags: InfoService.get
 *
 * @author api.arogal@gmail.com (Adam Rogal)
 */
public class GetTotalUsageUnitsPerMonth {

    public static void main(String[] args) {
        try {
            AdWordsServiceLogger.log();
            AdWordsUser user = new AdWordsUser();
            InfoServiceInterface infoService = user.getService(AdWordsService.V200909.INFO_SERVICE);
            InfoSelector selector = new InfoSelector();
            selector.setApiUsageType(ApiUsageType.TOTAL_USAGE_API_UNITS_PER_MONTH);
            ApiUsageInfo apiUsageInfo = infoService.get(selector);
            if (apiUsageInfo != null) {
                System.out.println("The total number of API units available per month is \"" + apiUsageInfo.getCost() + "\".");
            } else {
                System.out.println("No api usage information was found.");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
