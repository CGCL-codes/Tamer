package v201111.creativetemplateservice;

import com.google.api.ads.dfp.lib.DfpService;
import com.google.api.ads.dfp.lib.DfpServiceLogger;
import com.google.api.ads.dfp.lib.DfpUser;
import com.google.api.ads.dfp.lib.utils.v201111.StatementBuilder;
import com.google.api.ads.dfp.v201111.CreativeTemplate;
import com.google.api.ads.dfp.v201111.CreativeTemplatePage;
import com.google.api.ads.dfp.v201111.CreativeTemplateServiceInterface;
import com.google.api.ads.dfp.v201111.CreativeTemplateType;
import com.google.api.ads.dfp.v201111.Statement;

/**
 * This example gets up to 500 system defined creative templates.
 *
 * Tags: CreativeTemplateService.getCreativeTemplatesByStatement
 *
 * @author api.arogal@gmail.com (Adam Rogal)
 */
public class GetCreativeTemplatesByStatementExample {

    public static void main(String[] args) {
        try {
            DfpServiceLogger.log();
            DfpUser user = new DfpUser();
            CreativeTemplateServiceInterface creativeTemplateService = user.getService(DfpService.V201111.CREATIVE_TEMPLATE_SERVICE);
            Statement filterStatement = new StatementBuilder("WHERE type = :creativeTemplateType LIMIT 500").putValue("creativeTemplateType", CreativeTemplateType.SYSTEM_DEFINED.toString()).toStatement();
            CreativeTemplatePage page = creativeTemplateService.getCreativeTemplatesByStatement(filterStatement);
            if (page.getResults() != null) {
                int i = page.getStartIndex();
                for (CreativeTemplate creativeTemplate : page.getResults()) {
                    System.out.println(i + ") Creative template with ID \"" + creativeTemplate.getId() + "\", name \"" + creativeTemplate.getName() + "\", and type \"" + creativeTemplate.getType() + "\" was found.");
                    i++;
                }
            }
            System.out.println("Number of results found: " + page.getTotalResultSetSize());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
