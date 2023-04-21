package v201203.lineitemservice;

import com.google.api.ads.dfp.lib.DfpService;
import com.google.api.ads.dfp.lib.DfpServiceLogger;
import com.google.api.ads.dfp.lib.DfpUser;
import com.google.api.ads.dfp.v201203.Statement;
import com.google.api.ads.dfp.v201203.LineItemPage;
import com.google.api.ads.dfp.v201203.LineItemServiceInterface;
import com.google.api.ads.dfp.v201203.LineItemSummary;

/**
 * This example gets all line items. To create line items, run
 * CreateLineItemsExample.java.
 *
 * Tags: LineItemService.getLineItemsByStatement
 *
 * @author api.arogal@gmail.com (Adam Rogal)
 */
public class GetAllLineItemsExample {

    public static void main(String[] args) {
        try {
            DfpServiceLogger.log();
            DfpUser user = new DfpUser();
            LineItemServiceInterface lineItemService = user.getService(DfpService.V201203.LINEITEM_SERVICE);
            LineItemPage page = new LineItemPage();
            Statement filterStatement = new Statement();
            int offset = 0;
            do {
                filterStatement.setQuery("LIMIT 500 OFFSET " + offset);
                page = lineItemService.getLineItemsByStatement(filterStatement);
                if (page.getResults() != null) {
                    int i = page.getStartIndex();
                    for (LineItemSummary lineItem : page.getResults()) {
                        System.out.println(i + ") Line item with ID \"" + lineItem.getId() + "\", belonging to order ID \"" + lineItem.getOrderId() + "\", and named \"" + lineItem.getName() + "\" was found.");
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
