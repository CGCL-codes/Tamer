package edu.uiuc.ncsa.security.storage.sql.internals;

/**
 * Top-level class for modeling SQL tables that have a single, unique key and can be modelled as
 * maps of java objects. Each row in the table corresponds to a java
 * object. The primary key is the key in the map.
 * This class represents the bridge between java and SQL.
 * Normally you must only implement the {@link #createColumnDescriptors()}
 * method to get all creation (yes! management too!!),
 * inserts and updates automagically taken care of.
 * <p>Created by Jeff Gaynor<br>
 * on Apr 13, 2010 at  2:04:54 PM
 */
public abstract class Table {

    public Table() {
    }

    public String getFQTablename() {
        return fqTablename(getTablename());
    }

    public String getTablename() {
        return tablename;
    }

    public void setTablename(String tablename) {
        this.tablename = tablename;
    }

    String tablename;

    protected ColumnDescriptors columnDescriptor;

    /**
     * Invoked to create all the descriptors for this table.
     */
    public abstract void createColumnDescriptors();

    public ColumnDescriptors getColumnDescriptor() {
        if (columnDescriptor == null) {
            columnDescriptor = new ColumnDescriptors();
            createColumnDescriptors();
        }
        return columnDescriptor;
    }

    /**
     * The schema and prefix are not part of the table's information, actually, but are needed to
     * create its fully qualified name in context. Hence they must be supplied.
     *
     * @param schema
     * @param tablenamePrefix
     */
    public Table(String schema, String tablenamePrefix, String tablename) {
        this.schema = schema;
        this.tablenamePrefix = tablenamePrefix;
        this.tablename = tablename;
    }

    public String getPrimaryKeyColumnName() {
        return getColumnDescriptor().getPrimaryKey().getName();
    }

    public ColumnTypeTranslator getColumnTypeTranslator() {
        if (columnTypeTranslator == null) {
            columnTypeTranslator = new ColumnTypeTranslator() {
            };
        }
        return columnTypeTranslator;
    }

    public void setColumnTypeTranslator(ColumnTypeTranslator columnTypeTranslator) {
        this.columnTypeTranslator = columnTypeTranslator;
    }

    ColumnTypeTranslator columnTypeTranslator;

    public String createTableStatement() {
        String x = "CREATE TABLE " + getFQTablename() + "(";
        boolean isFirst = true;
        for (ColumnDescriptorEntry cde : getColumnDescriptor()) {
            String temp = cde.getName() + " " + getColumnTypeTranslator().toSQL(cde) + " " + (cde.isNullable() ? "" : "NOT NULL");
            if (isFirst) {
                x = x + temp;
                isFirst = false;
            } else {
                x = x + "," + temp;
            }
        }
        x = x + ")";
        return x;
    }

    /**
     * @return
     */
    public String createRegisterStatement() {
        String x = null;
        boolean isFirst = true;
        for (ColumnDescriptorEntry cde : getColumnDescriptor()) {
            if (isFirst) {
                x = cde.getName();
                isFirst = false;
            } else {
                x = x + ", " + cde.getName();
            }
        }
        return x;
    }

    public String getTablenamePrefix() {
        return tablenamePrefix;
    }

    public String getSchema() {
        return schema;
    }

    String schema;

    public void setTablenamePrefix(String tablenamePrefix) {
        this.tablenamePrefix = tablenamePrefix;
    }

    String tablenamePrefix = "a";

    protected String fqTablename(String name) {
        String prefix = getTablenamePrefix();
        if ((prefix != null && prefix.length() != 0)) {
            name = prefix + "_" + name;
        }
        if (getSchema() != null && getSchema().length() != 0) {
            return getSchema() + "." + name;
        }
        return name;
    }

    /**
     * An update statement has all columns, except, the primary key, which is used to create the where clause.
     *
     * @return
     */
    public String createUpdateStatement() {
        String out = "UPDATE " + getFQTablename() + " SET ";
        boolean isFirst = true;
        for (ColumnDescriptorEntry cde : getColumnDescriptor()) {
            if (!cde.isPrimaryKey()) {
                out = out + (isFirst ? "" : ", ") + cde.getName() + "=?";
                if (isFirst) {
                    isFirst = false;
                }
            }
        }
        out = out + " WHERE " + getPrimaryKeyColumnName() + "=?";
        return out;
    }

    /**
     * Creates a new insert statement with every column.
     *
     * @return
     */
    public String createInsertStatement() {
        String out = "insert into " + getFQTablename() + "(" + createRegisterStatement() + ") values (";
        String qmarks = "";
        for (int i = 0; i < getColumnDescriptor().size(); i++) {
            qmarks = qmarks + "?" + (i + 1 == getColumnDescriptor().size() ? "" : ", ");
        }
        out = out + qmarks + ")";
        return out;
    }
}
