package org.argouml.language.sql;

/**
 * Represents a column definition.
 * 
 * @author drahmann
 */
public class ColumnDefinition {

    private String datatype;

    private Object defaultValue;

    private String name;

    private Boolean nullable;

    /**
     * Creates a new column definition.
     * 
     */
    public ColumnDefinition() {
        super();
    }

    /**
     * Creates a new column definition with the given attributes.
     * 
     * @param datatype
     * @param name
     * @param nullable
     */
    public ColumnDefinition(String datatype, String name, Boolean nullable) {
        this();
        this.datatype = datatype;
        this.name = name;
        this.nullable = nullable;
    }

    /**
     * 
     * @return The datatype of the column definition.
     */
    public String getDatatype() {
        return datatype;
    }

    /**
     * 
     * @return The default Value of the column definition.
     */
    public Object getDefaultValue() {
        return defaultValue;
    }

    /**
     * 
     * @return The name of the column.
     */
    public String getName() {
        return name;
    }

    /**
     * 
     * @return Whether this column is NULLable
     */
    public Boolean getNullable() {
        return nullable;
    }

    /**
     * Set the datatype of the column.
     * 
     * @param datatype
     */
    public void setDatatype(String datatype) {
        this.datatype = datatype;
    }

    /**
     * Set the default value of the column.
     * 
     * @param defaultValue
     */
    public void setDefaultValue(Object defaultValue) {
        this.defaultValue = defaultValue;
    }

    /**
     * Set the name of the column.
     * 
     * @param name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Set whether this column is NULLable.
     * 
     * @param nullable
     */
    public void setNullable(Boolean nullable) {
        this.nullable = nullable;
    }
}
