package org.gbif.checklistbank.service.impl;

import org.gbif.checklistbank.model.Checklist;
import org.gbif.checklistbank.model.NameUsageFull;
import org.gbif.checklistbank.model.rowmapper.rs.NameUsageFullRowMapper;
import org.gbif.checklistbank.model.voc.ChecklistType;
import org.gbif.checklistbank.model.voc.SearchType;
import org.gbif.checklistbank.model.voc.SortOrder;
import org.gbif.checklistbank.service.NameUsageService;
import org.gbif.checklistbank.utils.SqlStatement;
import org.gbif.ecat.voc.Rank;
import org.gbif.ecat.voc.TaxonomicStatus;
import java.util.List;
import java.util.Set;
import javax.annotation.Nullable;
import com.google.inject.Inject;
import com.google.inject.Singleton;

@Singleton
public class NameUsageFullServicePgSql extends CRUDBaseServicePgSql<NameUsageFull> implements NameUsageService<NameUsageFull> {

    @Inject
    private NameUsageServiceSupportPgSql support;

    private final NameUsageFullRowMapper selectRowMapper;

    protected static final int MAX_LIST_RECORDS = 10000;

    public NameUsageFullServicePgSql() {
        super("name_usage", new NameUsageFullRowMapper(), false);
        this.selectRowMapper = new NameUsageFullRowMapper();
    }

    @Override
    public NameUsageFull get(Integer usageId) {
        return queryForObject(selectRowMapper.sqlWhereId(), rowMapper, usageId);
    }

    @Override
    public List<NameUsageFull> getByRank(Integer checklistId, Rank rank, Integer page, Integer pagesize, SortOrder sortOrder) {
        return support.getByRank(selectRowMapper, checklistId, rank, page, pagesize, sortOrder);
    }

    @Override
    public List<NameUsageFull> getChildren(Integer usageId, Integer page, Integer pagesize, SortOrder sortOrder) {
        return support.getChildren(selectRowMapper, usageId, page, pagesize, sortOrder);
    }

    @Override
    public List<NameUsageFull> getDescendantsByRank(Integer usageId, Rank rank, Integer page, Integer pagesize, SortOrder sortOrder) {
        return support.getDescendantsByRank(selectRowMapper, usageId, rank, page, pagesize, sortOrder);
    }

    @Override
    public List<NameUsageFull> getRoot(Checklist checklist, Integer page, Integer pagesize, SortOrder sortOrder) {
        return support.getRoot(selectRowMapper, checklist, page, pagesize, sortOrder);
    }

    @Override
    public List<NameUsageFull> getSynonyms(Integer usageId, Integer page, Integer pagesize, SortOrder sortOrder, TaxonomicStatus... status) {
        return support.getSynonyms(selectRowMapper, usageId, page, pagesize, sortOrder, status);
    }

    @Override
    public List<NameUsageFull> listByNubId(Integer nubId) {
        SqlStatement sql = selectRowMapper.sql();
        sql.where = "nu.nub_fk=?";
        return queryForList(sql, rowMapper, nubId);
    }

    @Override
    public void removeAll(Integer... checklistId) {
        support.removeAll(checklistId);
    }

    @Override
    public List<NameUsageFull> search(@Nullable Integer nameId, @Nullable Integer nubId, @Nullable String nameString, @Nullable SearchType searchType, @Nullable Integer checklistId, @Nullable Set<ChecklistType> checklistTypes, @Nullable TaxonomicStatus status, @Nullable Rank rank, @Nullable Integer page, @Nullable Integer pagesize, @Nullable SortOrder sortOrder) {
        return support.search(selectRowMapper, nameId, nubId, nameString, searchType, checklistId, checklistTypes, status, rank, page, pagesize, sortOrder);
    }

    @Override
    public int searchCount(@Nullable Integer nameId, @Nullable Integer nubId, @Nullable String nameString, @Nullable SearchType searchType, @Nullable Integer checklistId, @Nullable Set<ChecklistType> checklistTypes, @Nullable TaxonomicStatus status, @Nullable Rank rank) {
        return support.searchCount(nameId, nubId, nameString, searchType, checklistId, checklistTypes, status, rank);
    }
}
