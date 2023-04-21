package com.completex.objective.components.persistency;

import com.completex.objective.components.persistency.type.ValueStreamHelper;
import com.completex.objective.components.persistency.type.ValueStringResult;
import com.completex.objective.util.PropertyMap;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Gennady Krizhevsky
 */
public class Parameter implements Mappable {

    public static final String TAG_BINARY = "binary";

    public static final String TAG_VALUE = "value";

    public static final String TAG_COLUMN_TYPE = "columnType";

    public static final String TAG_COLUMN_TYPE_CLASS = "columnTypeClass";

    private ColumnType type = ColumnType.OBJECT;

    private Object value;

    private int jdbcType;

    private String jdbcTypeName;

    protected Parameter() {
    }

    /**
     * Creates parameter restoring its state from the map
     *
     * @param parameter map representation of this parameter
     */
    public Parameter(Map parameter) {
        fromMap(parameter);
    }

    /**
     * @param value value of this parameter.
     */
    public Parameter(Object value) {
        this.value = value;
    }

    /**
     * @param type  column type of the field this parameter corresponds to
     * @param value value of this parameter.
     */
    public Parameter(ColumnType type, Object value) {
        this.type = type;
        this.value = value;
    }

    /**
     * @param type     column type of the field this parameter corresponds to
     * @param value    value of this parameter
     * @param jdbcType one of java.sql.Types
     */
    public Parameter(ColumnType type, Object value, int jdbcType) {
        this.type = type;
        this.value = value;
        this.jdbcType = jdbcType;
    }

    /**
     * @param type         column type of the field this parameter corresponds to
     * @param value        value of this parameter
     * @param jdbcType     one of java.sql.Types
     * @param jdbcTypeName jdbc type name
     */
    public Parameter(ColumnType type, Object value, int jdbcType, String jdbcTypeName) {
        this.type = type;
        this.value = value;
        this.jdbcType = jdbcType;
        this.jdbcTypeName = jdbcTypeName;
    }

    /**
     * return column type of the field this parameter corresponds to
     *
     * @return column type of the field this parameter corresponds to
     */
    public ColumnType getType() {
        return type;
    }

    /**
     * return value of this parameter.
     *
     * @return value of this parameter.
     */
    public Object getValue() {
        return value;
    }

    /**
     * Sets value of this parameter.
     *
     * @param value value of this parameter.
     */
    protected void setValue(Object value) {
        this.value = value;
    }

    /**
     * @see com.completex.objective.components.persistency.Mappable#fromMap(java.util.Map)
     */
    public void fromMap(Map parameter) {
        PropertyMap parameterMap = PropertyMap.toPropertyMap(parameter);
        fromMap0(parameterMap);
    }

    /**
     * Restores object state from map
     *
     * @param parameterMap map representation of this object
     * @return PropertyMap wrapper of the parameter
     */
    protected PropertyMap fromMap0(PropertyMap parameterMap) {
        Object value = extractValueFromMap(parameterMap);
        String columnTypeName = parameterMap.getProperty(TAG_COLUMN_TYPE);
        this.type = ColumnType.toColumnType(columnTypeName);
        Boolean bin = parameterMap.getBooleanObj(TAG_BINARY, Boolean.FALSE, false);
        boolean binary = bin.booleanValue();
        if (value instanceof String) {
            this.value = ValueStreamHelper.string2value(binary, ((String) value), false, type, "");
        } else {
            this.value = value;
        }
        return parameterMap;
    }

    protected Object extractValueFromMap(PropertyMap parameterMap) {
        return parameterMap.get(TAG_VALUE, false);
    }

    /**
     * @see com.completex.objective.components.persistency.Mappable#toMap()
     */
    public Map toMap() {
        HashMap map = new HashMap();
        map.put(TAG_COLUMN_TYPE, type.getName());
        map.put(TAG_COLUMN_TYPE_CLASS, type.getClass().getName());
        ValueStringResult result = ValueStreamHelper.value2string(type, value);
        map.put(TAG_BINARY, Boolean.valueOf(result.isBinary()));
        map.put(TAG_VALUE, result.getValueString());
        return map;
    }

    public String toString() {
        return new StringBuffer().append(super.toString()).append("; value = ").append(value).append("; type = ").append(type).toString();
    }

    public int getJdbcType() {
        return jdbcType == 0 ? (type == null ? 0 : type.getDefaultJdbcType()) : jdbcType;
    }

    /**
     * Sets jdbcType - one of java.sql.Types types
     *
     * @param jdbcType one of java.sql.Types types
     */
    public void setJdbcType(int jdbcType) {
        this.jdbcType = jdbcType;
    }

    public String getJdbcTypeName() {
        return jdbcTypeName;
    }

    /**
     * Sets jdbcTypeName - the fully-qualified name of an SQL structured type
     *
     * @param jdbcTypeName
     */
    public void setJdbcTypeName(String jdbcTypeName) {
        this.jdbcTypeName = jdbcTypeName;
    }
}
