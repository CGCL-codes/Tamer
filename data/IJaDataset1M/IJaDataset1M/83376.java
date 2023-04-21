package org.avaje.ebean.server.deploy;

/**
 * Used hold meta data when a bean property is overridden.
 * <p>
 * Typically this is for Embedded Beans.
 * </p>
 */
public class BeanPropertyOverride {

    final String dbColumn;

    final String dbTableAlias;

    final String sqlFormulaSelect;

    final String sqlFormulaJoin;

    public BeanPropertyOverride(String dbColumn, String tableAlias) {
        this(dbColumn, tableAlias, null, null);
    }

    public BeanPropertyOverride(String dbColumn, String tableAlias, String sqlFormulaSelect, String sqlFormulaJoin) {
        this.dbColumn = dbColumn;
        this.dbTableAlias = tableAlias;
        this.sqlFormulaSelect = sqlFormulaSelect;
        this.sqlFormulaJoin = sqlFormulaJoin;
    }

    public String getDbColumn() {
        return dbColumn;
    }

    public String getDbTableAlias() {
        return dbTableAlias;
    }

    public String getDbFullName() {
        return dbTableAlias + "." + dbColumn;
    }

    public String getSqlFormulaSelect() {
        return sqlFormulaSelect;
    }

    public String getSqlFormulaJoin() {
        return sqlFormulaJoin;
    }
}
