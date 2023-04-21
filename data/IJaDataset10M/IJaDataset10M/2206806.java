package v201111.labelservice;

import com.google.api.ads.dfp.lib.DfpService;
import com.google.api.ads.dfp.lib.DfpServiceLogger;
import com.google.api.ads.dfp.lib.DfpUser;
import com.google.api.ads.dfp.v201111.Label;
import com.google.api.ads.dfp.v201111.LabelPage;
import com.google.api.ads.dfp.v201111.LabelServiceInterface;
import com.google.api.ads.dfp.v201111.Statement;

/**
 * This example gets all labels. To create labels, run CreateLabelsExample.java.
 * This feature is only available to DFP premium solution networks.
 *
 * Tags: LabelService.getLabelsByStatement
 *
 * @author api.arogal@gmail.com (Adam Rogal)
 */
public class GetAllLabelsExample {

    public static void main(String[] args) {
        try {
            DfpServiceLogger.log();
            DfpUser user = new DfpUser();
            LabelServiceInterface labelService = user.getService(DfpService.V201111.LABEL_SERVICE);
            LabelPage page = new LabelPage();
            Statement filterStatement = new Statement();
            int offset = 0;
            do {
                filterStatement.setQuery("LIMIT 500 OFFSET " + offset);
                page = labelService.getLabelsByStatement(filterStatement);
                if (page.getResults() != null) {
                    int i = page.getStartIndex();
                    for (Label label : page.getResults()) {
                        System.out.println(i + ") Label with ID \"" + label.getId() + "\", name \"" + label.getName() + "\", and type \"" + label.getType() + "\" was found.");
                        i++;
                    }
                }
                offset += 500;
            } while (offset < page.getTotalResultSetSize());
            System.out.println("Number of results found: " + page.getTotalResultSetSize());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
