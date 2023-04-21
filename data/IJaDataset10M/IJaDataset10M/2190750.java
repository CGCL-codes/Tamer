package org.tamacat.dao.meta;

import java.util.Collection;

public interface Table {

    String getSchemaName();

    String getTableName();

    String getTableOrAliasName();

    String getTableNameWithSchema();

    Collection<Column> getPrimaryKeys();

    Collection<Column> getColumns();

    Table registerColumn(Column... columns);

    Column find(String columnName);
}
