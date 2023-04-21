package v201111.labelservice;

import com.google.api.ads.dfp.lib.DfpService;
import com.google.api.ads.dfp.lib.DfpServiceLogger;
import com.google.api.ads.dfp.lib.DfpUser;
import com.google.api.ads.dfp.lib.utils.v201111.StatementBuilder;
import com.google.api.ads.dfp.v201111.Label;
import com.google.api.ads.dfp.v201111.LabelPage;
import com.google.api.ads.dfp.v201111.LabelServiceInterface;
import com.google.api.ads.dfp.v201111.LabelType;
import com.google.api.ads.dfp.v201111.Statement;

/**
 * This example updates the descriptions of all labels that are competitive
 * exclusion by updating its description up to the first 500. To determine which
 * labels exist, run GetAllLabelsExample.java. This feature is only available to
 * DFP premium solution networks.
 *
 * Tags: LabelService.getLabelsByStatement, LabelService.updateLabels
 *
 * @author api.arogal@gmail.com (Adam Rogal)
 */
public class UpdateLabelsExample {

    public static void main(String[] args) {
        try {
            DfpServiceLogger.log();
            DfpUser user = new DfpUser();
            LabelServiceInterface labelService = user.getService(DfpService.V201111.LABEL_SERVICE);
            Statement filterStatement = new StatementBuilder("WHERE type = :type and isActive = true LIMIT 500").putValue("type", LabelType.COMPETITIVE_EXCLUSION.toString()).toStatement();
            LabelPage page = labelService.getLabelsByStatement(filterStatement);
            if (page.getResults() != null) {
                Label[] labels = page.getResults();
                for (Label label : labels) {
                    label.setDescription("These labels are still competiting with each other.");
                }
                labels = labelService.updateLabels(labels);
                if (labels != null) {
                    for (Label label : labels) {
                        System.out.println("A label with ID \"" + label.getId() + "\" and name \"" + label.getName() + "\" was updated.");
                    }
                } else {
                    System.out.println("No labels updated.");
                }
            } else {
                System.out.println("No labels found to update.");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
