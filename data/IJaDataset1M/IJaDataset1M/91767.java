package com.angel.dao.generic.jdbc.engine.impl;

import com.angel.dao.generic.jdbc.engine.EngineSintaxQuery;

/**
 * @author William
 *
 */
public class MySQLSintaxQuery implements EngineSintaxQuery {

    public String buildDeleteTableQuery(String tableName) {
        return "delete from " + tableName;
    }

    public String[] buildDisableTableContraintsQuery(String tableName) {
        return new String[] { "ALTER TABLE " + tableName + " DISABLE KEYS", "SET FOREIGN_KEY_CHECKS = 0" };
    }

    public String buildDropDatabaseQuery(String databaseName) {
        return "drop database " + databaseName;
    }

    public String buildDropTableQuery(String tableName) {
        return "drop table " + tableName;
    }

    public String[] buildEnableTableContraintsQuery(String tableName) {
        return new String[] { "ALTER TABLE " + tableName + " ENABLE KEYS", "SET FOREIGN_KEY_CHECKS = 1" };
    }

    public String[] buildDisableTableContraintsQuery(String tableName, String[] constraintsNames) {
        return new String[] { "ALTER TABLE " + tableName + " DISABLE KEYS", "SET FOREIGN_KEY_CHECKS = 0" };
    }
}
