package org.gbif.checklistbank.service.impl;

import org.gbif.checklistbank.model.Dataset;
import org.gbif.checklistbank.model.rowmapper.rs.DatasetRowMapper;
import org.gbif.checklistbank.service.DatasetService;
import org.gbif.checklistbank.utils.SqlStatement;
import org.gbif.ecat.utils.LimitedMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import org.apache.commons.lang3.StringUtils;

@Singleton
public class DatasetServicePgSql extends CRUDBaseServicePgSql<Dataset> implements DatasetService {

    private final String INSERT_SQL = "insert into " + TABLE + " (registry_key,checklist_fk,dataset_id,dataset_name,title,description,home_url) values (?,?,?,?,?,?,?)";

    private final String UPDATE_SQL = "update " + TABLE + " set (registry_key,checklist_fk,dataset_id,dataset_name,title,description,home_url) = (?,?,?,?,?,?,?) where id=?";

    private final Map<String, Dataset> datasetCache = new LimitedMap<String, Dataset>(250);

    @Inject
    public DatasetServicePgSql() {
        super("dataset", new DatasetRowMapper(), true);
    }

    private String buildCacheKey(Integer checklistId, String datasetId, String datasetName) {
        String lookup = null;
        if (!StringUtils.isBlank(datasetId)) {
            lookup = checklistId + "-+DSID+-" + datasetId;
        } else if (!StringUtils.isBlank(datasetName)) {
            lookup = checklistId + "-+DSN+-" + datasetName;
        } else {
            return null;
        }
        return lookup;
    }

    @Override
    public Integer datasetStringToId(Integer checklistId, String datasetId, String datasetName) {
        if (checklistId == null) {
            throw new IllegalArgumentException("checklistId required");
        }
        String lookup = buildCacheKey(checklistId, datasetId, datasetName);
        if (lookup == null) {
            return null;
        }
        if (datasetCache.containsKey(lookup)) {
            return datasetCache.get(lookup).getId();
        } else {
            SqlStatement sql = sql();
            String param = null;
            if (!StringUtils.isBlank(datasetId)) {
                sql.where = "dataset_id=? and checklist_fk=" + checklistId;
                param = datasetId;
            } else {
                sql.where = "dataset_name=? and checklist_fk=" + checklistId;
                param = datasetName;
            }
            Dataset ds = queryForObject(sql, rowMapper, param);
            if (ds == null) {
                ds = new Dataset();
                ds.setChecklistId(checklistId);
                ds.setDatasetId(datasetId);
                ds.setDatasetName(datasetName);
                insert(ds);
            }
            datasetCache.put(lookup, ds);
            return ds.getId();
        }
    }

    @Override
    public Dataset get(UUID resourceKey) {
        if (resourceKey == null) {
            return null;
        }
        SqlStatement sql = sql();
        sql.where = "registry_key = ?";
        return queryForObject(sql, rowMapper, resourceKey.toString().toLowerCase());
    }

    @Override
    protected Object[] getInsertParameters(Dataset ds) {
        return new Object[] { ds.getRegistryKey() == null ? null : ds.getRegistryKey().toString(), ds.getChecklistId(), ds.getDatasetId(), ds.getDatasetName(), ds.getTitle(), ds.getDescription(), ds.getHomeUrl() };
    }

    @Override
    protected String getInsertSql() {
        return INSERT_SQL;
    }

    @Override
    protected Object[] getUpdateParameters(Dataset ds) {
        return new Object[] { ds.getRegistryKey() == null ? null : ds.getRegistryKey().toString(), ds.getChecklistId(), ds.getDatasetId(), ds.getDatasetName(), ds.getTitle(), ds.getDescription(), ds.getHomeUrl(), ds.getId() };
    }

    @Override
    protected String getUpdateSql() {
        return UPDATE_SQL;
    }

    @Override
    public List<Dataset> list(Integer checklistId) {
        SqlStatement sql = sql();
        if (checklistId != null) {
            sql.where = "checklist_fk=" + checklistId;
        }
        return queryForList(sql, rowMapper);
    }

    @Override
    public void remove(Integer id) {
        if (id != null) {
            Dataset ds = get(id);
            datasetCache.remove(buildCacheKey(ds.getChecklistId(), ds.getDatasetId(), ds.getDatasetName()));
            super.remove(id);
        }
    }
}
