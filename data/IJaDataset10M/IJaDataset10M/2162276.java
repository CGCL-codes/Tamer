package com.completex.objective.components.persistency;

import com.completex.objective.components.persistency.meta.ModelFilter;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Under construction 
 *
 * @author Gennady Krizhevsky
 */
public class UserDefinedTypeMetaModel implements ModelConsts {

    public static final ModelFilter.NullModelFilter NULL_MODEL_FILTER = new ModelFilter.NullModelFilter();

    private Map tables = new LinkedHashMap();

    private Map modelByObjectId = new LinkedHashMap();

    private ModelFilter filter = NULL_MODEL_FILTER;

    public UserDefinedTypeMetaModel() {
    }

    public UserDefinedTypeMetaModel(ModelFilter filter) {
        setFilter(filter);
    }

    public UserDefinedTypeMetaTable getColumnById(String objectId) {
        return (UserDefinedTypeMetaTable) modelByObjectId.get(objectId);
    }

    public UserDefinedTypeMetaTable getTypeTable(String key) {
        return (UserDefinedTypeMetaTable) tables.get(key);
    }

    public boolean containsObjectId(String objectId) {
        return modelByObjectId.containsKey(objectId);
    }

    public void addColumn(String key, UserDefinedTypeMetaTable table) {
        String tableName = table.getName();
        if (filter.isAllowed(tableName)) {
            tables.put(key, table);
            table.setModelByObjectId(modelByObjectId);
        }
    }

    public Iterator typeTableNameIterator() {
        return tables.keySet().iterator();
    }

    public final Map toInternalMap() {
        final Map modelMap = new LinkedHashMap();
        final Map tableMap = new LinkedHashMap();
        modelMap.put(TABLES_TAG, tableMap);
        for (Iterator it = tables.keySet().iterator(); it.hasNext(); ) {
            String tableName = (String) it.next();
            if (filter.isAllowed(tableName)) {
                UserDefinedTypeMetaTable table = (UserDefinedTypeMetaTable) tables.get(tableName);
                tableMap.put(table.getName(), table.toInternalMap());
            }
        }
        return modelMap;
    }

    public final Map toExternalMap() {
        final Map modelMap = new LinkedHashMap();
        final Map tableMap = new LinkedHashMap();
        modelMap.put(TABLES_TAG, tableMap);
        for (Iterator it = tables.keySet().iterator(); it.hasNext(); ) {
            String tableName = (String) it.next();
            UserDefinedTypeMetaTable table = (UserDefinedTypeMetaTable) tables.get(tableName);
            tableMap.put(table.getName(), table.toExternalMap());
        }
        return modelMap;
    }

    /**
     * This method must be called before fromInternalMap since it populates
     * aliases map.
     *
     * @param modelMap  model map
     */
    public final void fromExternalMap(Map modelMap) {
        Map tablesMap = (Map) modelMap.get(TABLES_TAG);
        for (Iterator it = tablesMap.keySet().iterator(); it.hasNext(); ) {
            String tableKey = (String) it.next();
            Map tableMap = (Map) tablesMap.get(tableKey);
            String tableName = (String) tableMap.get(TABLE_NAME_TAG);
            if (filter.isAllowed(tableName)) {
                UserDefinedTypeMetaTable table = (UserDefinedTypeMetaTable) tables.get(tableName);
                if (table != null) {
                    table.fromExternalMap(tableMap);
                    tables.put(tableName, table);
                }
            }
        }
    }

    public final void fromInternalMap(Map modelMap) {
        Map tablesMap = (Map) modelMap.get(TABLES_TAG);
        for (Iterator it = tablesMap.keySet().iterator(); it.hasNext(); ) {
            String tableKey = (String) it.next();
            Map tableMap = (Map) tablesMap.get(tableKey);
            String tableName = (String) tableMap.get(TABLE_NAME_TAG);
            if (filter.isAllowed(tableName)) {
                UserDefinedTypeMetaTable table = (UserDefinedTypeMetaTable) tables.get(tableName);
                if (table == null) {
                    table = new UserDefinedTypeMetaTable(tableName);
                }
                table.fromInternalMap(tableMap);
                tables.put(tableName, table);
            }
        }
    }

    public boolean hasFilter() {
        return filter != NULL_MODEL_FILTER;
    }

    public void setFilter(ModelFilter filter) {
        if (filter != null) {
            this.filter = filter;
        }
    }

    public ModelFilter getFilter() {
        return filter;
    }
}
