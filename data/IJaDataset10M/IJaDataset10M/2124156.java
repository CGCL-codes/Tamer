package com.germinus.xpression.cms.model.summaries;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import com.germinus.util.Bag;
import com.germinus.xpression.cms.contents.service.DateSummaryService;
import com.germinus.xpression.cms.model.ChronologicalMonth;
import com.germinus.xpression.cms.model.ChronologicalMonths;
import com.germinus.xpression.cms.util.ManagerRegistry;
import com.germinus.xpression.cms.worlds.World;

public class WorldDateSummary extends SpaceDateSummary {

    private List<DateSummary> worldEntries;

    private DateSummaryService dateSummaryService;

    private Map<ChronologicalMonth, DateSummary> byChronologicalEntry;

    private WorldSpace summarySpace;

    public WorldDateSummary(World world) {
        this.summarySpace = new WorldSpace(world);
        init();
    }

    public WorldDateSummary(World world, ChronologicalMonths chronologicalEntries) {
        this.summarySpace = new WorldSpace(world);
        createDateSummary(world, chronologicalEntries);
    }

    private void createDateSummary(World world, ChronologicalMonths chronologicalEntries) {
        this.worldEntries = DateSummary.fromChronologicalEntries(chronologicalEntries, summarySpace.space());
        getDateSummaryService().deleteSummary(summarySpace.space());
        getDateSummaryService().createDateSummary(worldEntries);
        initChronologicalBag();
    }

    public List<DateSummary> getWorldEntries() {
        return worldEntries;
    }

    public void addDiff(Bag<ChronologicalMonth> entries) {
        String space = summarySpace.space();
        for (ChronologicalMonth chronologicalEntry : entries.keySet()) {
            long count = entries.getCount(chronologicalEntry);
            if (this.byChronologicalEntry.containsKey(chronologicalEntry)) {
                DateSummary dateSummary = byChronologicalEntry.get(chronologicalEntry);
                dateSummary.addCount(count);
            } else if (count > 0) {
                DateSummary newDateSummary = new DateSummary(chronologicalEntry.getYear(), chronologicalEntry.getMonth(), count, space);
                worldEntries.add(newDateSummary);
                byChronologicalEntry.put(chronologicalEntry, newDateSummary);
            }
        }
        dateSummaryService.storeSummaries(worldEntries);
    }

    private void init() {
        if (existsSummary()) {
            this.worldEntries = listBySpace();
            initChronologicalBag();
        } else throw new SummaryNotGeneratedException(summarySpace.space());
    }

    private void initChronologicalBag() {
        this.byChronologicalEntry = new HashMap<ChronologicalMonth, DateSummary>();
        for (DateSummary dateSummary : worldEntries) {
            this.byChronologicalEntry.put(new ChronologicalMonth(dateSummary.getYear(), dateSummary.getMonth()), dateSummary);
        }
    }

    private List<DateSummary> listBySpace() {
        return getDateSummaryService().listBySpace(summarySpace.space());
    }

    private boolean existsSummary() {
        return getDateSummaryService().countBySpace(summarySpace.space()) > 0;
    }

    public static boolean existsSummary(World world) {
        return ((DateSummaryService) ManagerRegistry.getScribeBean("dateSummaryService")).countBySpace(WorldSpace.space(world)) > 0;
    }

    public DateSummary byYearMonth(Integer year, Integer month) {
        return getDateSummaryService().loadBySpaceYearMonth(summarySpace.space(), year, month);
    }

    public DateSummary byChronologicalMonth(ChronologicalMonth month) {
        return getDateSummaryService().loadBySpaceYearMonth(summarySpace.space(), month.getYear(), month.getMonth());
    }

    public DateSummaryService getDateSummaryService() {
        if (dateSummaryService == null) {
            dateSummaryService = (DateSummaryService) ManagerRegistry.getScribeBean("dateSummaryService");
        }
        return dateSummaryService;
    }

    public void setDateSummaryService(DateSummaryService dateSummaryService) {
        this.dateSummaryService = dateSummaryService;
    }
}
