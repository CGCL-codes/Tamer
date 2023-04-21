package v201008;

import com.google.api.adwords.lib.AdWordsService;
import com.google.api.adwords.lib.AdWordsServiceLogger;
import com.google.api.adwords.lib.AdWordsUser;
import com.google.api.adwords.v201008.cm.Paging;
import com.google.api.adwords.v201008.mcm.Alert;
import com.google.api.adwords.v201008.mcm.AlertPage;
import com.google.api.adwords.v201008.mcm.AlertQuery;
import com.google.api.adwords.v201008.mcm.AlertSelector;
import com.google.api.adwords.v201008.mcm.AlertServiceInterface;
import com.google.api.adwords.v201008.mcm.AlertSeverity;
import com.google.api.adwords.v201008.mcm.AlertType;
import com.google.api.adwords.v201008.mcm.ClientSpec;
import com.google.api.adwords.v201008.mcm.FilterSpec;
import com.google.api.adwords.v201008.mcm.TriggerTimeSpec;

/**
 * This example gets all alerts for all clients of an MCC account. The effective
 * user (clientEmail, clientCustomerId, or authToken) must be an MCC user to
 * get results.
 *
 * Tags: AlertService.get
 *
 * @author api.arogal@gmail.com (Adam Rogal)
 */
public class GetAllAlerts {

    public static void main(String[] args) {
        try {
            AdWordsServiceLogger.log();
            AdWordsUser user = new AdWordsUser();
            AlertServiceInterface alertService = user.getService(AdWordsService.V201008.ALERT_SERVICE);
            AlertQuery alertQuery = new AlertQuery();
            alertQuery.setClientSpec(ClientSpec.ALL);
            alertQuery.setFilterSpec(FilterSpec.ALL);
            alertQuery.setTypes(new AlertType[] { AlertType.ACCOUNT_BUDGET_BURN_RATE, AlertType.ACCOUNT_BUDGET_ENDING, AlertType.ACCOUNT_ON_TARGET, AlertType.CAMPAIGN_ENDED, AlertType.CAMPAIGN_ENDING, AlertType.CREDIT_CARD_EXPIRING, AlertType.DECLINED_PAYMENT, AlertType.KEYWORD_BELOW_MIN_CPC, AlertType.MANAGER_LINK_PENDING, AlertType.MISSING_BANK_REFERENCE_NUMBER, AlertType.PAYMENT_NOT_ENTERED, AlertType.TV_ACCOUNT_BUDGET_ENDING, AlertType.TV_ACCOUNT_ON_TARGET, AlertType.TV_ZERO_DAILY_SPENDING_LIMIT, AlertType.USER_INVITE_ACCEPTED, AlertType.USER_INVITE_PENDING, AlertType.ZERO_DAILY_SPENDING_LIMIT });
            alertQuery.setSeverities(new AlertSeverity[] { AlertSeverity.GREEN, AlertSeverity.YELLOW, AlertSeverity.RED });
            alertQuery.setTriggerTimeSpec(TriggerTimeSpec.ALL_TIME);
            AlertSelector selector = new AlertSelector();
            selector.setQuery(alertQuery);
            selector.setPaging(new Paging(0, 100));
            AlertPage page = alertService.get(selector);
            if (page.getEntries() != null && page.getEntries().length > 0) {
                for (Alert alert : page.getEntries()) {
                    System.out.printf("Alert of type '%s' and severity '%s' for account '%d' was found.\n", alert.getAlertType(), alert.getAlertSeverity(), alert.getClientCustomerId());
                }
            } else {
                System.out.println("No alerts were found.\n");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
