package com.ddc.drivers.fmpxml;

import java.util.Map;
import java.util.HashMap;

public class FmField {

    /** Where this field lives. */
    private FmTable table;

    /** The column name in the database. */
    private String columnName;

    /** The alias in the SQL command, or <code>null</code> if no alias was specified. */
    private String alias;

    /** The type of field.  This may be modified after the field is created. */
    private FmFieldType type;

    /** Whether the field can be assigned NULL. */
    private boolean isNullable = true;

    /** Whether the field is read-only. */
    private boolean readOnly = false;

    /** Creates an FMField without any metadata.
	 * This method is usually called by SqlCommand, during parsing of SQL query strings.
	 * Fields created using this constructor have a type of <code>null</code> and isNullable set to <code>true</code>,
	 * these values should be set later during parsing the XML header response from a filemaker query.
	 * @param table The table
	 * @param name The column name
	 * @param alias The alias, or null if no alias is used.
	 */
    public FmField(FmTable table, String name, String alias) {
        this(table, name, alias, null, true);
    }

    /**
	 * Complete constructor for FmField.
	 * @param table The table
	 * @param name The column name
	 * @param alias The alias, or null
	 * @param type The type of field
	 * @param isNullable Whether null values are allowed.
	 */
    public FmField(FmTable table, String name, String alias, FmFieldType type, boolean isNullable) {
        this.table = table;
        this.columnName = stripFieldPrefix(name);
        this.alias = alias;
        this.type = type;
        this.isNullable = isNullable;
    }

    /**
	 * Complete constructor for FmField.
	 * @param table
	 * @param name
	 * @param alias
	 * @param type
	 * @param isNullable
	 * @param readOnly
	 */
    public FmField(FmTable table, String name, String alias, FmFieldType type, boolean isNullable, boolean readOnly) {
        this.table = table;
        this.columnName = stripFieldPrefix(name);
        this.alias = alias;
        this.type = type;
        this.isNullable = isNullable;
        this.readOnly = readOnly;
    }

    /**
	 * Returns the table containing this field.
	 */
    public FmTable getTable() {
        return table;
    }

    public void setTable(FmTable table) {
        this.table = table;
    }

    /**
	 * The name of the column in the Filemaker table.
	 */
    public String getColumnName() {
        return columnName;
    }

    /**
	 * Returns the alias if one was used for this FmField, otherwise returns the columnName.
	 * This should never return null.
	 */
    public String getAlias() {
        return alias == null ? columnName : alias;
    }

    public void setAlias(String alias) {
        this.alias = alias;
    }

    public boolean isNullable() {
        return isNullable;
    }

    public void setNullable(boolean nullable) {
        isNullable = nullable;
    }

    public FmFieldType getType() {
        return type;
    }

    public void setType(FmFieldType type) {
        this.type = type;
    }

    public boolean isReadOnly() {
        return readOnly;
    }

    public void setReadOnly(boolean readOnly) {
        this.readOnly = readOnly;
    }

    public String toString() {
        return "FmField{" + "table=" + table + ", columnName='" + columnName + "'" + "}";
    }

    /** Equals and hashcode methods do not look at field types isNulllable, or table name only the column name. for now */
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof FmField)) {
            return false;
        }
        final FmField field = (FmField) o;
        if (columnName != null ? !columnName.equals(field.columnName) : field.columnName != null) {
            return false;
        }
        return true;
    }

    /** Equals and hashcode methods do not look at field types isNulllable, or table name only the column name. for now */
    public int hashCode() {
        return (columnName != null ? columnName.hashCode() : 0);
    }

    public String stripFieldPrefix(String in) {
        String myReturn = "";
        int pPos = in.indexOf(".", 0);
        if (pPos > 0) {
            myReturn = in.substring(pPos + 1);
        } else {
            myReturn = in;
        }
        return myReturn;
    }
}
