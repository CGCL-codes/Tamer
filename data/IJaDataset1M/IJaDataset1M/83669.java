package com.avaje.ebean.server.lib.sql;

/**
 * Used to search for a given table in the dictionary.
 * <p>
 * Parses out the catalog, schema and table names.
 * </p>
 */
class TableSearchName {

    final String fullTableName;

    final String catalog;

    final String schema;

    final String tableName;

    /**
	 * Parses out the catalog, schema and table names.
	 */
    TableSearchName(String fullTableName) {
        this.fullTableName = fullTableName;
        int dotPos0 = fullTableName.indexOf('.');
        int dotPos1 = -1;
        if (dotPos0 > 0) {
            dotPos1 = fullTableName.indexOf('.', dotPos0 + 1);
        }
        if (dotPos1 > -1) {
            catalog = fullTableName.substring(0, dotPos0);
            schema = fullTableName.substring(dotPos0 + 1, dotPos1);
            tableName = fullTableName.substring(dotPos1 + 1);
        } else if (dotPos0 > -1) {
            catalog = null;
            schema = fullTableName.substring(0, dotPos0);
            tableName = fullTableName.substring(dotPos0 + 1);
        } else {
            catalog = null;
            schema = null;
            tableName = fullTableName;
        }
    }

    public String toString() {
        return fullTableName;
    }

    String getSchema() {
        return schema;
    }

    String getTableName() {
        return tableName;
    }
}
