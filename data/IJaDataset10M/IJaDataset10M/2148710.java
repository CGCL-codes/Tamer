package v201111.thirdpartyslotservice;

import com.google.api.ads.dfp.lib.DfpService;
import com.google.api.ads.dfp.lib.DfpServiceLogger;
import com.google.api.ads.dfp.lib.DfpUser;
import com.google.api.ads.dfp.lib.utils.v201111.StatementBuilder;
import com.google.api.ads.dfp.v201111.Statement;
import com.google.api.ads.dfp.v201111.ThirdPartySlot;
import com.google.api.ads.dfp.v201111.ThirdPartySlotPage;
import com.google.api.ads.dfp.v201111.ThirdPartySlotServiceInterface;
import com.google.api.ads.dfp.v201111.ThirdPartySlotStatus;

/**
 * This example gets archived third party slots. The statement retrieves up to
 * the maximum page size limit of 500. To create third party slots, run
 * CreateThirdPartySlotExample.java.
 * 
 * Tags: ThirdPartySlotService.getThirdPartySlotsByStatement
 * 
 * @author api.paulrashidi@gmail.com (Paul Rashidi)
 */
public class GetThirdPartySlotsByStatementExample {

    public static void main(String[] args) {
        try {
            DfpServiceLogger.log();
            DfpUser user = new DfpUser();
            ThirdPartySlotServiceInterface thirdPartySlotService = user.getService(DfpService.V201111.THIRD_PARTY_SLOT_SERVICE);
            ThirdPartySlotPage page = new ThirdPartySlotPage();
            Statement filterStatement = new StatementBuilder("WHERE status = :status LIMIT 500 ").putValue("status", ThirdPartySlotStatus.ARCHIVED.toString()).toStatement();
            page = thirdPartySlotService.getThirdPartySlotsByStatement(filterStatement);
            if (page.getResults() != null) {
                int i = page.getStartIndex();
                for (ThirdPartySlot thirdPartySlot : page.getResults()) {
                    System.out.println(i + ") Third party slot with ID \"" + thirdPartySlot.getId() + "\" was found.");
                    i++;
                }
            }
            System.out.println("Number of results found: " + page.getTotalResultSetSize());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
