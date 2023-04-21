package datadog.db.model;

import java.sql.Types;
import java.util.List;

/**
 * Model of a column in a relational database.
 *
 * @author Justin Tomich
 * @version $Id: Column.java 178 2006-03-12 01:36:50Z tomichj $
 */
public class Column {

    /**
     * indicates the type is not set for this column
     */
    public static int TYPE_NOT_SET = -1;

    /**
     * Table containing this column.
     */
    private final Table table;

    /**
     * Name of column. Does not include table name.
     */
    private final String name;

    /**
     * Datatype of column.
     *
     * @see java.sql.Types
     */
    private int type = TYPE_NOT_SET;

    private boolean notNull;

    private boolean primaryKey;

    private boolean unique;

    private boolean autoIncrement = false;

    /**
     * @param name of column, not qualified.
     */
    public Column(String name, Table table) {
        if (name.indexOf(".") > -1) {
            name = name.substring(name.indexOf(".") + 1);
        }
        this.name = name;
        this.table = table;
        table.addColumn(this);
    }

    /**
     * No-arg constructor for testing only.
     */
    Column() {
        table = null;
        name = null;
    }

    /**
     * @return column name, not qualified.
     */
    public String getName() {
        return name;
    }

    /**
     * @return column name, fully qualified in <code>table.column</code> format
     */
    public String getFullName() {
        StringBuffer buffante = new StringBuffer();
        buffante.append(table.getName());
        buffante.append(".");
        buffante.append(name);
        return buffante.toString();
    }

    public Table getTable() {
        return table;
    }

    public String getTableName() {
        return table.getName();
    }

    public void setType(int java_sql_type) {
        this.type = java_sql_type;
    }

    public int getType() {
        return type;
    }

    /**
     * Has the type been set yet?
     */
    public boolean isTypeSet() {
        return (type != TYPE_NOT_SET);
    }

    /**
     * Used in building SQL - should this value be quoted?
     */
    public boolean isStringType() {
        if (type == Types.VARCHAR) return true;
        if (type == Types.CHAR) return true;
        return false;
    }

    public void setNotNull() {
        this.notNull = true;
    }

    public boolean isNotNull() {
        return notNull;
    }

    public void setPrimaryKey() {
        this.primaryKey = true;
        this.notNull = true;
    }

    public boolean isPrimaryKey() {
        return primaryKey;
    }

    public void setUnique() {
        this.unique = true;
    }

    public boolean isUnique() {
        return unique;
    }

    public void addReferrer(ForeignKey fk) {
        throw new UnsupportedOperationException("addReferrer not supported");
    }

    public List getReferrers() {
        throw new UnsupportedOperationException("getReferrers not supported");
    }

    public String toStringDetail() {
        StringBuffer sb = new StringBuffer();
        sb.append(this.getClass());
        sb.append("\n");
        sb.append("name: ");
        sb.append(getFullName());
        sb.append("\n");
        sb.append("type: ");
        sb.append(type);
        sb.append("\n");
        sb.append("primaryKey: ");
        sb.append(primaryKey);
        sb.append("\n");
        sb.append("notNull: ");
        sb.append(notNull);
        sb.append("\n");
        sb.append("unique: ");
        sb.append(unique);
        sb.append("\n");
        return sb.toString();
    }

    public String toString() {
        return "Column{" + "fullName='" + getFullName() + "'" + "}";
    }
}
