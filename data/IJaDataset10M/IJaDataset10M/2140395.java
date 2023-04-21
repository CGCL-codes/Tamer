package v200909;

import com.google.api.adwords.lib.AdWordsService;
import com.google.api.adwords.lib.AdWordsServiceLogger;
import com.google.api.adwords.lib.AdWordsUser;
import com.google.api.adwords.v200909.cm.DateRange;
import com.google.api.adwords.v200909.cm.Operator;
import com.google.api.adwords.v200909.info.ApiUsageInfo;
import com.google.api.adwords.v200909.info.ApiUsageType;
import com.google.api.adwords.v200909.info.InfoSelector;
import com.google.api.adwords.v200909.info.InfoServiceInterface;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * This example retrieves the cost, in API units per operation, of the given
 * method at today's rate for a given developer token. This example must be
 * run as the MCC user that owns the developer token.
 *
 * Tags: InfoService.get
 *
 * @author api.arogal@gmail.com (Adam Rogal)
 */
public class GetMethodCost {

    public static void main(String[] args) {
        try {
            AdWordsServiceLogger.log();
            AdWordsUser user = new AdWordsUser();
            InfoServiceInterface infoService = user.getService(AdWordsService.V200909.INFO_SERVICE);
            InfoSelector selector = new InfoSelector();
            selector.setApiUsageType(ApiUsageType.METHOD_COST);
            selector.setServiceName("AdGroupService");
            selector.setMethodName("mutate");
            selector.setOperator(Operator.SET);
            selector.setDateRange(new DateRange(new SimpleDateFormat("yyyyMMdd").format(new Date()), new SimpleDateFormat("yyyyMMdd").format(new Date())));
            ApiUsageInfo apiUsageInfo = infoService.get(selector);
            if (apiUsageInfo != null) {
                System.out.println("The cost of the method in API units is \"" + apiUsageInfo.getCost() + "\".");
            } else {
                System.out.println("No api usage information was found.");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
