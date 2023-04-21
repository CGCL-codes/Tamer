package v201203.thirdpartyslotservice;

import com.google.api.ads.dfp.lib.DfpService;
import com.google.api.ads.dfp.lib.DfpServiceLogger;
import com.google.api.ads.dfp.lib.DfpUser;
import com.google.api.ads.dfp.lib.utils.v201203.StatementBuilder;
import com.google.api.ads.dfp.v201203.ArchiveThirdPartySlots;
import com.google.api.ads.dfp.v201203.Statement;
import com.google.api.ads.dfp.v201203.ThirdPartySlot;
import com.google.api.ads.dfp.v201203.ThirdPartySlotPage;
import com.google.api.ads.dfp.v201203.ThirdPartySlotServiceInterface;
import com.google.api.ads.dfp.v201203.ThirdPartySlotStatus;
import com.google.api.ads.dfp.v201203.UpdateResult;
import org.apache.commons.lang.StringUtils;
import java.util.ArrayList;
import java.util.List;

/**
 * This example archives all active third party slots for a company. To
 * determine which third party slots exist, 
 * run GetAllThirdPartySlotsExample.java.
 * 
 * Tags: ThirdPartSlotService.getThirdPartySlotsByStatement
 * Tags: ThirdPartSlotService.performThirdPartySlotsAction
 * 
 * @author api.paulrashidi@gmail.com (Paul Rashidi)
 */
public class ArchiveThirdPartySlotsExample {

    public static void main(String[] args) {
        try {
            DfpServiceLogger.log();
            DfpUser user = new DfpUser();
            ThirdPartySlotServiceInterface thirdpartySlotService = user.getService(DfpService.V201203.THIRD_PARTY_SLOT_SERVICE);
            Long companyId = Long.parseLong("INSERT_COMPANY_ID_HERE");
            String statementText = "WHERE status = :status AND " + "companyId = :companyId LIMIT 500";
            Statement filterStatement = new StatementBuilder("").putValue("status", ThirdPartySlotStatus.ACTIVE.toString()).putValue("companyId", companyId).toStatement();
            ThirdPartySlotPage page = new ThirdPartySlotPage();
            int offset = 0;
            int i = 0;
            List<Long> thirdPartySlotIds = new ArrayList<Long>();
            do {
                filterStatement.setQuery(statementText + " OFFSET " + offset);
                page = thirdpartySlotService.getThirdPartySlotsByStatement(filterStatement);
                if (page.getResults() != null) {
                    for (ThirdPartySlot thirdPartySlot : page.getResults()) {
                        System.out.println("Third party slot with ID \"" + thirdPartySlot.getId() + "\" will be archived.");
                        thirdPartySlotIds.add(thirdPartySlot.getId());
                    }
                }
                offset += 500;
            } while (offset < page.getTotalResultSetSize());
            System.out.println("Number of third party slots to be archived: " + thirdPartySlotIds.size());
            if (thirdPartySlotIds.size() > 0) {
                filterStatement.setQuery("WHERE id IN (" + StringUtils.join(thirdPartySlotIds, ",") + ")");
                ArchiveThirdPartySlots action = new ArchiveThirdPartySlots();
                UpdateResult result = thirdpartySlotService.performThirdPartySlotAction(action, filterStatement);
                if (result != null && result.getNumChanges() > 0) {
                    System.out.println("Number of third party slots archived: " + result.getNumChanges());
                } else {
                    System.out.println("No third party slots were archived.");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
