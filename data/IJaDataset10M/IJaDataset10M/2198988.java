package org.apache.shale.tiger.managed.config;

/**
 * <p>
 * Representation of the runtime relevant contents of a JavaServer Faces <code>&lt;managed-property&gt;</code> configuration element.
 * </p>
 */
public class ManagedPropertyConfig implements ListEntriesHolder, MapEntriesHolder, NullValueHolder {

    /** Creates a new instance of ManagedPropertyConfig. */
    public ManagedPropertyConfig() {
    }

    /**
     * <p>
     * Return <code>true</code> if the specified <code>value</code> is a value binding expression, rather than a literal value.
     * </p>
     */
    public boolean isExpression() {
        return (value != null) && value.startsWith("#{") && value.endsWith("}");
    }

    /**
     * Holds value of property name.
     */
    private String name;

    /**
     * Getter for property name.
     * 
     * @return Value of property name.
     */
    public String getName() {
        return this.name;
    }

    /**
     * Setter for property name.
     * 
     * @param name
     *            New value of property name.
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Holds value of property type.
     */
    private String type;

    /**
     * Getter for property type.
     * 
     * @return Value of property type.
     */
    public String getType() {
        return this.type;
    }

    /**
     * Setter for property type.
     * 
     * @param type
     *            New value of property type.
     */
    public void setType(String type) {
        this.type = type;
    }

    /**
     * Holds value of property value.
     */
    private String value;

    /**
     * Getter for property value.
     * 
     * @return Value of property value.
     */
    public String getValue() {
        return this.value;
    }

    /**
     * Setter for property value.
     * 
     * @param value
     *            New value of property value.
     */
    public void setValue(String value) {
        this.value = value;
    }

    /**
     * Holds value of property nullValue.
     */
    private boolean nullValue;

    /**
     * Getter for property nullValue.
     * 
     * @return Value of property nullValue.
     */
    public boolean isNullValue() {
        return this.nullValue;
    }

    /**
     * Setter for property nullValue.
     * 
     * @param nullValue
     *            New value of property nullValue.
     */
    public void setNullValue(boolean nullValue) {
        this.nullValue = nullValue;
    }

    /**
     * Holds value of property listEntries.
     */
    private ListEntriesConfig listEntries;

    /**
     * Getter for property listEntries.
     * 
     * @return Value of property listEntries.
     */
    public ListEntriesConfig getListEntries() {
        return this.listEntries;
    }

    /**
     * Setter for property listEntries.
     * 
     * @param listEntries
     *            New value of property listEntries.
     */
    public void setListEntries(ListEntriesConfig listEntries) {
        this.listEntries = listEntries;
    }

    /**
     * Holds value of property mapEntries.
     */
    private MapEntriesConfig mapEntries;

    /**
     * Getter for property mapEntries.
     * 
     * @return Value of property mapEntries.
     */
    public MapEntriesConfig getMapEntries() {
        return this.mapEntries;
    }

    /**
     * Setter for property mapEntries.
     * 
     * @param mapEntries
     *            New value of property mapEntries.
     */
    public void setMapEntries(MapEntriesConfig mapEntries) {
        this.mapEntries = mapEntries;
    }

    /**
     * <p>
     * Pretty printing toString() method.
     * </p>
     */
    public String toString() {
        StringBuffer sb = new StringBuffer("ManagedPropertyConfig");
        sb.append("[name=" + getName());
        sb.append(",type=" + getType());
        sb.append(",value=" + getValue());
        sb.append(",nullValue=" + isNullValue());
        return sb.toString();
    }
}
