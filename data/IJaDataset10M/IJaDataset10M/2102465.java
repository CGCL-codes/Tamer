package org.gbif.checklistbank.service.impl;

import org.gbif.checklistbank.model.ClbStatistics;
import org.gbif.checklistbank.service.ChecklistService;
import org.gbif.checklistbank.service.ClbStatisticsService;
import org.gbif.ecat.voc.NameType;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;
import com.google.inject.Inject;
import com.google.inject.Singleton;

@Singleton
public class ClbStatisticsServicePgSql extends PgSqlBaseService implements ClbStatisticsService {

    class UpdateStatsTask extends TimerTask {

        @Override
        public void run() {
            refreshStatistics();
        }
    }

    private final Timer timer = new Timer();

    private ClbStatistics stats = null;

    @Inject
    ChecklistService checklistService;

    public ClbStatisticsServicePgSql() {
        super();
        this.timer.scheduleAtFixedRate(new UpdateStatsTask(), new Date(), 1000 * 60 * 60 * 24);
    }

    @Override
    public ClbStatistics getStatistics() {
        if (stats == null) {
            refreshStatistics();
        }
        return stats;
    }

    @Override
    public ClbStatistics refreshStatistics() {
        if (checklistService == null) {
            return null;
        }
        if (stats == null) {
            stats = new ClbStatistics();
        }
        stats.setLastRefresh(System.currentTimeMillis());
        stats.setChecklists(checklistService.list());
        stats.setNumNameStrings(queryForInt("select count(id) from name_string where type!=" + NameType.blacklisted.ordinal()));
        stats.setNumCanonicalNameStrings(queryForInt("select count(id) from name_string where canonical_name_fk=id"));
        stats.setNumCanonicalsMono(0);
        stats.setNumCanonicalsBi(0);
        stats.setNumCanonicalsTri(0);
        stats.setNumHomonyms(0);
        stats.setNumHomonymsGeneric(0);
        stats.setNumUnparsedNameStrings(queryForInt("select (select count(*) from name_string) - (select count(*) from parsed_name)"));
        stats.setNumUserNameLists(queryForInt("select count(id) from uploaded_list"));
        stats.setNumUserNames(queryForInt("select count(id) from list_name"));
        return stats;
    }
}
