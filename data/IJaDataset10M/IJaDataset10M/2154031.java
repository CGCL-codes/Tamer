package com.daffodilwoods.daffodildb.server.serversystem.chainedcolumn;

import com.daffodilwoods.database.general.*;

public class ChainedColumnInfo {

    QualifiedIdentifier tableName;

    String columnName;

    public ChainedColumnInfo(QualifiedIdentifier tableName0, String columnName0) {
        tableName = tableName0;
        columnName = columnName0;
    }

    public boolean equals(Object cci0) {
        ChainedColumnInfo cci = (ChainedColumnInfo) cci0;
        return tableName.equals(cci.tableName) && columnName.equalsIgnoreCase(cci.columnName);
    }

    public int hashCode() {
        return 13 * tableName.hashCode() + 17 * columnName.hashCode();
    }

    public QualifiedIdentifier getTableName() {
        return tableName;
    }

    public String toString() {
        return " [CCI [tableName = " + tableName + " ] [ columnName = " + columnName + " ] ]";
    }
}
