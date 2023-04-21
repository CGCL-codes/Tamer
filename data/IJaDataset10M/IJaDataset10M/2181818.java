package v201203.labelservice;

import com.google.api.ads.dfp.lib.DfpService;
import com.google.api.ads.dfp.lib.DfpServiceLogger;
import com.google.api.ads.dfp.lib.DfpUser;
import com.google.api.ads.dfp.v201203.Label;
import com.google.api.ads.dfp.v201203.LabelPage;
import com.google.api.ads.dfp.v201203.LabelServiceInterface;
import com.google.api.ads.dfp.v201203.LabelType;
import com.google.api.ads.dfp.v201203.Statement;
import org.apache.commons.lang.StringUtils;
import java.util.ArrayList;
import java.util.List;

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
            LabelServiceInterface labelService = user.getService(DfpService.V201203.LABEL_SERVICE);
            LabelPage page = new LabelPage();
            Statement filterStatement = new Statement();
            int offset = 0;
            do {
                filterStatement.setQuery("LIMIT 500 OFFSET " + offset);
                page = labelService.getLabelsByStatement(filterStatement);
                if (page.getResults() != null) {
                    int i = page.getStartIndex();
                    for (Label label : page.getResults()) {
                        List<String> labelTypes = new ArrayList<String>();
                        for (LabelType labelType : label.getTypes()) {
                            labelTypes.add(labelType.toString());
                        }
                        System.out.println(i + ") Label with ID \"" + label.getId() + "\", name \"" + label.getName() + "\", and types {" + StringUtils.join(labelTypes, ",") + "} was found.");
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
