package com.avaje.ebean.server.lib.sql;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import javax.persistence.PersistenceException;

/**
 * Holds a list of TableInfo which have the same name but could be in different
 * schema.
 */
public class TableListByName implements Serializable {

    private static final long serialVersionUID = 3237602529100109432L;

    private static final String DEFAULT_SCHEMA = "$$defaultschema";

    String onlyOneSchemaKey;

    TableInfo onlyOneTableInfo;

    HashMap<String, TableInfo> map;

    final String monitor = new String();

    private String getSchemaKey(TableInfo tableInfo) {
        String schema = tableInfo.getSchema();
        if (schema == null) {
            return DEFAULT_SCHEMA;
        } else {
            return schema.toLowerCase();
        }
    }

    private boolean isAlreadyRegistered(String schemaKey) {
        synchronized (monitor) {
            if (map == null) {
                return schemaKey.equals(onlyOneSchemaKey);
            } else {
                return map.containsKey(schemaKey);
            }
        }
    }

    private boolean isEmpty() {
        return onlyOneTableInfo == null && map == null;
    }

    public boolean contains(TableInfo tableInfo) {
        String schemaKey = getSchemaKey(tableInfo);
        return isAlreadyRegistered(schemaKey);
    }

    public boolean register(TableInfo tableInfo) {
        synchronized (monitor) {
            String schemaKey = getSchemaKey(tableInfo);
            if (isEmpty()) {
                onlyOneTableInfo = tableInfo;
                onlyOneSchemaKey = schemaKey;
                return true;
            }
            if (isAlreadyRegistered(schemaKey)) {
                return false;
            }
            if (map == null) {
                map = new HashMap<String, TableInfo>();
                map.put(onlyOneSchemaKey, onlyOneTableInfo);
                onlyOneSchemaKey = null;
                onlyOneTableInfo = null;
            }
            map.put(schemaKey, tableInfo);
            return true;
        }
    }

    /**
	 * Add all the tableInfo's to the fullList.
	 * <p>
	 * This is used to return a read only list of all the registered tables.
	 * </p>
	 */
    public void append(List<TableInfo> fullList) {
        synchronized (monitor) {
            if (map != null) {
                fullList.addAll(map.values());
            } else if (onlyOneTableInfo != null) {
                fullList.add(onlyOneTableInfo);
            } else {
            }
        }
    }

    /**
	 * Find the table given the full table name.
	 * <p>
	 * The full table name can include the catalog and schema names. If there is
	 * more than one table with the same name we need to match the schema name
	 * to find the correct one.
	 * </p>
	 * 
	 * @return returns null if no matching table is found.
	 */
    protected TableInfo find(TableSearchName name) {
        synchronized (monitor) {
            if (map == null) {
                return onlyOneTableInfo;
            }
            String schema = name.getSchema();
            if (schema == null) {
                String msg = "When searching for tableInfo for [" + name + "]";
                msg += " could not determine the schema name. Missing period? schema name?";
                throw new PersistenceException(msg);
            }
            return map.get(schema.toLowerCase());
        }
    }
}
