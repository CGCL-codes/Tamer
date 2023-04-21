package v201109;

import com.google.api.adwords.lib.AdWordsService;
import com.google.api.adwords.lib.AdWordsServiceLogger;
import com.google.api.adwords.lib.AdWordsUser;
import com.google.api.adwords.v201109.cm.ReportDefinition;
import com.google.api.adwords.v201109.cm.ReportDefinitionPage;
import com.google.api.adwords.v201109.cm.ReportDefinitionSelector;
import com.google.api.adwords.v201109.cm.ReportDefinitionServiceInterface;

/**
 * This example gets all report definitions. To add a report definition, run
 * AddKeywordsPerformanceReportDefinition.java.
 *
 * Tags: ReportDefinitionService.get
 *
 * @author api.arogal@gmail.com (Adam Rogal)
 */
public class GetAllReportDefinitions {

    public static void main(String[] args) {
        try {
            AdWordsServiceLogger.log();
            AdWordsUser user = new AdWordsUser();
            ReportDefinitionServiceInterface reportDefinitionService = user.getService(AdWordsService.V201109.REPORT_DEFINITION_SERVICE);
            ReportDefinitionSelector selector = new ReportDefinitionSelector();
            ReportDefinitionPage page = reportDefinitionService.get(selector);
            if (page.getEntries() != null) {
                for (ReportDefinition reportDefinition : page.getEntries()) {
                    System.out.println("ReportDefinition with name \"" + reportDefinition.getReportName() + "\" and id \"" + reportDefinition.getId() + "\" was found.");
                }
            } else {
                System.out.println("No report definitions were found.");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
