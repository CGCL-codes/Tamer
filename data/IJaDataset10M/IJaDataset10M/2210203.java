package org.gbif.checklistbank.utils;

import org.gbif.checklistbank.model.Checklist;
import org.gbif.checklistbank.model.NameUsage;
import org.gbif.checklistbank.model.NameUsageSimple;
import org.gbif.checklistbank.service.ChecklistImportService;
import org.gbif.checklistbank.service.ChecklistService;
import org.gbif.checklistbank.service.IdentifierService;
import org.gbif.checklistbank.service.NameUsageService;
import org.gbif.checklistbank.service.impl.ChecklistServicePgSqlIT;
import org.gbif.ecat.voc.Rank;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import javax.annotation.Nullable;
import com.google.inject.Inject;

/**
 * @author markus
 */
public abstract class ImportBaseTest extends InjectingTestBase {

    @Inject
    protected ChecklistImportService importService;

    @Inject
    protected ChecklistService checklistService;

    @Inject
    protected NameUsageService<NameUsageSimple> usageService;

    @Inject
    protected NameUsageService<NameUsage> usageRawService;

    @Inject
    protected IdentifierService identifierService;

    protected NameUsageSimple getUsageBySourceId(int checklistId, String sourceId) {
        return usageService.get(identifierService.getUsageId(checklistId, sourceId));
    }

    protected NameUsageSimple getNubUsageBySourceId(int checklistId, String sourceId) {
        return usageService.get(getUsageBySourceId(checklistId, sourceId).getNubId());
    }

    protected List<NameUsageSimple> getUsagesByName(String name, @Nullable Integer checklistId, @Nullable Rank rank) {
        return usageService.search(null, null, name, null, checklistId, null, null, rank, null, 100, null);
    }

    protected List<NameUsageSimple> getUsagesByNameId(int nameId, @Nullable Integer checklistId) {
        return usageService.search(nameId, null, null, null, checklistId, null, null, null, null, 100, null);
    }

    protected NameUsage getUsageRawBySourceId(int checklistId, String sourceId) {
        return usageRawService.get(identifierService.getUsageId(checklistId, sourceId));
    }

    protected List<Checklist> init(int... checklistId) {
        List<Checklist> result = new ArrayList<Checklist>();
        importService.initDatabase();
        for (int cid : checklistId) {
            Checklist c;
            if (cid > 99) {
                c = ChecklistServicePgSqlIT.newTestChecklist(cid, UUID.randomUUID());
                checklistService.insert(c);
            } else {
                c = checklistService.get(cid);
            }
            importService.importChecklist(c, true, true);
            result.add(c);
        }
        return result;
    }
}
