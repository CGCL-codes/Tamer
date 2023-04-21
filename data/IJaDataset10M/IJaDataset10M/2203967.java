package com.butterfill.opb.webdemo.data;

import com.butterfill.opb.*;
import com.butterfill.opb.data.*;
import com.butterfill.opb.groups.*;
import com.butterfill.opb.timing.*;
import com.butterfill.opb.util.*;
import com.butterfill.opb.plsql.util.*;
import java.util.logging.*;

/**
 * File created from the PL/SQL package specification
 * property_value.
 */
public class PropertyValueImpl implements PropertyValue {

    /**
     * The name of this class.
     */
    public static final String CLASS_NAME = PropertyValueImpl.class.getName();

    /**
     * The logger of this class.
     */
    private static final Logger logger = Logger.getLogger(CLASS_NAME);

    /**
     * Creates a new instance of PropertyValueImpl.
     */
    public PropertyValueImpl() {
        logger.entering(CLASS_NAME, "PropertyValueImpl()");
    }

    /**
     * Returns a String representation of this instance and it's values.
     * @return A String representation of this PropertyValueImpl.
     */
    @Override
    public String toString() {
        return com.butterfill.opb.util.OpbToStringHelper.toString(this);
    }

    /**
     * The group mananger map to be used by this PropertyValueImpl.
     */
    private OpbGroupManagerMap opbGroupManagerMap;

    /**
     * Sets the group manager map to be used by this PropertyValueImpl.
     * @param map The group manager map to use.
     */
    public void setGroupManagerMap(final OpbGroupManagerMap map) {
        this.opbGroupManagerMap = map;
    }

    /**
     * Returns the group manager map used by this PropertyValueImpl.
     * @return The group manager map used by this instance.
     */
    public OpbGroupManagerMap getGroupManagerMap() {
        return opbGroupManagerMap;
    }

    /**
     * The event timer provider to be used by this PropertyValueImpl.
     */
    private OpbEventTimerProvider opbEventTimerProvider;

    /**
     * Sets the event timer to be used by this PropertyValueImpl.
     * @param provider The event timer to use.
     */
    public void setOpbEventTimerProvider(final OpbEventTimerProvider provider) {
        this.opbEventTimerProvider = provider;
    }

    /**
     * The data object source to be used by this PropertyValueImpl.
     */
    private OpbDataObjectSource opbDataObjectSource;

    /**
     * Sets the data object source to be used by this PropertyValueImpl.
     * @param source The data object source to use.
     */
    public void setOpbDataObjectSource(final OpbDataObjectSource source) {
        this.opbDataObjectSource = source;
    }

    /**
     * The connection provider to be used by this PropertyValueImpl.
     */
    private OpbConnectionProvider opbConnectionProvider;

    /**
     * Sets the connection provider to be used by this PropertyValueImpl.
     * @param provider The connection provider to use.
     */
    public void setOpbConnectionProvider(final OpbConnectionProvider provider) {
        this.opbConnectionProvider = provider;
    }

    /**
     * Resets all field values to their initial values.
     */
    public void opbClearState() {
        final String methodName = "opbClearState()";
        logger.entering(CLASS_NAME, methodName);
        opbId = null;
        rowId = null;
        valueContainsCr = null;
        groupName = null;
        groupNameDataSourceValue = null;
        key = null;
        keyDataSourceValue = null;
        propertyDescription = null;
        propertyDescriptionDataSourceValue = null;
        sortOrder = null;
        sortOrderDataSourceValue = null;
        value = null;
        valueDataSourceValue = null;
    }

    /**
     * The id of this PropertyValueImpl. 
     * Set by opbLoad(ResultSet).
     */
    private OpbId opbId;

    /**
     * Returns the id of this PropertyValueImpl.
     * This ID is created using the field(s):
     * <ul>
     * <li>rowId</li>
     * </ul>
     * This method will return null if opbLoad(ResultSet) has not been called.
     * 
     * @return The ID of this instance.
     */
    public OpbId getOpbId() {
        return opbId;
    }

    /**
     * Resets all field values to their initial values by calling 
     * opbClearState() and then sets all field values using values taken from 
     * the current row in resultSet.
     * <br/>
     * This method will look for the following fields in resultSet;
     * <ul>
     * <li>row_id is <em>mandatory</em></li>
     * <li>value_contains_cr is <em>mandatory</em></li>
     * <li>group_name is <em>mandatory</em></li>
     * <li>key is <em>mandatory</em></li>
     * <li>property_description is <em>mandatory</em></li>
     * <li>sort_order is <em>mandatory</em></li>
     * <li>value is <em>mandatory</em></li>
     * </ul>
     * 
     * @param resultSet The result set from which this instance should be loaded.
     * @throws OpbDataAccessException If we fail to load this instance.
     */
    public void opbLoad(final java.sql.ResultSet resultSet) throws OpbDataAccessException {
        final String methodName = "opbLoad(ResultSet)";
        logger.entering(CLASS_NAME, methodName);
        opbClearState();
        OpbAssert.notNull(logger, CLASS_NAME, methodName, "resultSet", resultSet);
        try {
            rowId = OpbSqlHelper.getValue(rowId, resultSet, "row_id", true);
            valueContainsCr = OpbSqlHelper.getValue(valueContainsCr, resultSet, "value_contains_cr", true);
            groupName = OpbSqlHelper.getValue(groupName, resultSet, "group_name", true);
            groupNameDataSourceValue = groupName;
            key = OpbSqlHelper.getValue(key, resultSet, "key", true);
            keyDataSourceValue = key;
            propertyDescription = OpbSqlHelper.getValue(propertyDescription, resultSet, "property_description", true);
            propertyDescriptionDataSourceValue = propertyDescription;
            sortOrder = OpbSqlHelper.getValue(sortOrder, resultSet, "sort_order", true);
            sortOrderDataSourceValue = sortOrder;
            value = OpbSqlHelper.getValue(value, resultSet, "value", true);
            valueDataSourceValue = value;
            opbId = new OpbId(rowId);
        } catch (Exception ex) {
            OpbExceptionHelper.throwException(new OpbDataAccessException("failed to load", ex), logger, CLASS_NAME, methodName);
        } finally {
            logger.exiting(CLASS_NAME, methodName);
        }
    }

    /**
     * Derived from an opb-package field.
     */
    private String rowId = null;

    /**
     * Returns the value of rowId.
     * @return The value of rowId.
     */
    public String getRowId() {
        return rowId;
    }

    /**
     * Sets the value of rowId.
     * @param a The new value for rowId.
     */
    private void setRowId(final String a) {
        this.rowId = a;
    }

    /**
     * Derived from an opb-package field.
     */
    private Boolean valueContainsCr = null;

    /**
     * Returns the value of valueContainsCr.
     * @return The value of valueContainsCr.
     */
    public Boolean getValueContainsCr() {
        return valueContainsCr;
    }

    /**
     * Sets the value of valueContainsCr.
     * @param a The new value for valueContainsCr.
     */
    private void setValueContainsCr(final Boolean a) {
        this.valueContainsCr = a;
    }

    /**
     * Derived from an opb-package field.
     */
    private String groupName = null;

    /**
     * Returns the value of groupName.
     * @return The value of groupName.
     */
    public String getGroupName() {
        return groupName;
    }

    /**
     * Sets the value of groupName.
     * @param a The new value for groupName.
     */
    public void setGroupName(final String a) {
        this.groupName = a;
    }

    /**
     * Derived from a read-write opb-package field.
     */
    private String groupNameDataSourceValue = null;

    /**
     * Returns the value of groupNameDataSourceValue.
     * This is the last value returned by the data source for groupName.
     * @return The value of groupNameDataSourceValue.
     */
    public String getGroupNameDataSourceValue() {
        return groupNameDataSourceValue;
    }

    /**
     * Returns true if the value of groupName 
     * is different to the value that was loaded from the data source,
     * false otherwise.
     * @return true if groupName has changed since it was loaded.
     */
    public boolean getGroupNameChanged() {
        return !OpbComparisonHelper.isEqual(groupName, groupNameDataSourceValue);
    }

    /**
     * Derived from an opb-package field.
     */
    private String key = null;

    /**
     * Returns the value of key.
     * @return The value of key.
     */
    public String getKey() {
        return key;
    }

    /**
     * Sets the value of key.
     * @param a The new value for key.
     */
    public void setKey(final String a) {
        this.key = a;
    }

    /**
     * Derived from a read-write opb-package field.
     */
    private String keyDataSourceValue = null;

    /**
     * Returns the value of keyDataSourceValue.
     * This is the last value returned by the data source for key.
     * @return The value of keyDataSourceValue.
     */
    public String getKeyDataSourceValue() {
        return keyDataSourceValue;
    }

    /**
     * Returns true if the value of key 
     * is different to the value that was loaded from the data source,
     * false otherwise.
     * @return true if key has changed since it was loaded.
     */
    public boolean getKeyChanged() {
        return !OpbComparisonHelper.isEqual(key, keyDataSourceValue);
    }

    /**
     * Derived from an opb-package field.
     */
    private String propertyDescription = null;

    /**
     * Returns the value of propertyDescription.
     * @return The value of propertyDescription.
     */
    public String getPropertyDescription() {
        return propertyDescription;
    }

    /**
     * Sets the value of propertyDescription.
     * @param a The new value for propertyDescription.
     */
    public void setPropertyDescription(final String a) {
        this.propertyDescription = a;
    }

    /**
     * Derived from a read-write opb-package field.
     */
    private String propertyDescriptionDataSourceValue = null;

    /**
     * Returns the value of propertyDescriptionDataSourceValue.
     * This is the last value returned by the data source for propertyDescription.
     * @return The value of propertyDescriptionDataSourceValue.
     */
    public String getPropertyDescriptionDataSourceValue() {
        return propertyDescriptionDataSourceValue;
    }

    /**
     * Returns true if the value of propertyDescription 
     * is different to the value that was loaded from the data source,
     * false otherwise.
     * @return true if propertyDescription has changed since it was loaded.
     */
    public boolean getPropertyDescriptionChanged() {
        return !OpbComparisonHelper.isEqual(propertyDescription, propertyDescriptionDataSourceValue);
    }

    /**
     * Derived from an opb-package field.
     */
    private String sortOrder = null;

    /**
     * Returns the value of sortOrder.
     * @return The value of sortOrder.
     */
    public String getSortOrder() {
        return sortOrder;
    }

    /**
     * Sets the value of sortOrder.
     * @param a The new value for sortOrder.
     */
    public void setSortOrder(final String a) {
        this.sortOrder = a;
    }

    /**
     * Derived from a read-write opb-package field.
     */
    private String sortOrderDataSourceValue = null;

    /**
     * Returns the value of sortOrderDataSourceValue.
     * This is the last value returned by the data source for sortOrder.
     * @return The value of sortOrderDataSourceValue.
     */
    public String getSortOrderDataSourceValue() {
        return sortOrderDataSourceValue;
    }

    /**
     * Returns true if the value of sortOrder 
     * is different to the value that was loaded from the data source,
     * false otherwise.
     * @return true if sortOrder has changed since it was loaded.
     */
    public boolean getSortOrderChanged() {
        return !OpbComparisonHelper.isEqual(sortOrder, sortOrderDataSourceValue);
    }

    /**
     * Derived from an opb-package field.
     */
    private String value = null;

    /**
     * Returns the value of value.
     * @return The value of value.
     */
    public String getValue() {
        return value;
    }

    /**
     * Sets the value of value.
     * @param a The new value for value.
     */
    public void setValue(final String a) {
        this.value = a;
    }

    /**
     * Derived from a read-write opb-package field.
     */
    private String valueDataSourceValue = null;

    /**
     * Returns the value of valueDataSourceValue.
     * This is the last value returned by the data source for value.
     * @return The value of valueDataSourceValue.
     */
    public String getValueDataSourceValue() {
        return valueDataSourceValue;
    }

    /**
     * Returns true if the value of value 
     * is different to the value that was loaded from the data source,
     * false otherwise.
     * @return true if value has changed since it was loaded.
     */
    public boolean getValueChanged() {
        return !OpbComparisonHelper.isEqual(value, valueDataSourceValue);
    }

    /**
     * 
     * Calls the database function del.
     * @throws OpbDataAccessException
     *   If we fail to make the database call.
     */
    public String del(final String pRowId, final String pOldGroupName, final String pOldKey, final String pOldPropertyDescription, final String pOldSortOrder, final String pOldValue) throws OpbDataAccessException {
        final String methodName = "del(String, String, String, String, String, String)";
        logger.entering(CLASS_NAME, methodName);
        String result = null;
        OpbPlsqlCallHelper opbCallHelper = new OpbPlsqlCallHelper(logger, CLASS_NAME, methodName, opbEventTimerProvider, opbConnectionProvider, "BEGIN ? := property_value.del(?, ?, ?, ?, ?, ?); END;", "DbCall:property_value#del(VARCHAR2, VARCHAR2, VARCHAR2, VARCHAR2, VARCHAR2, VARCHAR2)");
        opbCallHelper.registerOutParameter(1, java.sql.Types.VARCHAR);
        opbCallHelper.setObject(2, java.sql.Types.VARCHAR, pRowId);
        opbCallHelper.setObject(3, java.sql.Types.VARCHAR, pOldGroupName);
        opbCallHelper.setObject(4, java.sql.Types.VARCHAR, pOldKey);
        opbCallHelper.setObject(5, java.sql.Types.VARCHAR, pOldPropertyDescription);
        opbCallHelper.setObject(6, java.sql.Types.VARCHAR, pOldSortOrder);
        opbCallHelper.setObject(7, java.sql.Types.VARCHAR, pOldValue);
        opbCallHelper.execute();
        result = opbCallHelper.get(String.class, 1);
        opbDataObjectSource.clearCached(PropertyValue.class, getOpbId());
        opbCallHelper.callComplete();
        logger.exiting(CLASS_NAME, methodName);
        return result;
    }

    /**
     * Calls del using mapped parameters.
     * <ul>
     * <li>pRowId is mapped to rowId</li>
     * <li>pOldGroupName is mapped to groupNameDataSourceValue</li>
     * <li>pOldKey is mapped to keyDataSourceValue</li>
     * <li>pOldPropertyDescription is mapped to propertyDescriptionDataSourceValue</li>
     * <li>pOldSortOrder is mapped to sortOrderDataSourceValue</li>
     * <li>pOldValue is mapped to valueDataSourceValue</li>
     * </ul>
     * @throws OpbDataAccessException
     *   If we fail to make the database call.
     */
    public String del() throws OpbDataAccessException {
        final String methodName = "del()";
        logger.entering(CLASS_NAME, methodName);
        String result = del(getRowId(), getGroupNameDataSourceValue(), getKeyDataSourceValue(), getPropertyDescriptionDataSourceValue(), getSortOrderDataSourceValue(), getValueDataSourceValue());
        return result;
    }

    /**
     * 
     * Calls the database function ins.
     * @throws OpbDataAccessException
     *   If we fail to make the database call.
     */
    public String ins(final String pGroupName, final String pKey, final String pPropertyDescription, final String pSortOrder, final String pValue) throws OpbDataAccessException {
        final String methodName = "ins(String, String, String, String, String)";
        logger.entering(CLASS_NAME, methodName);
        String result = null;
        OpbPlsqlCallHelper opbCallHelper = new OpbPlsqlCallHelper(logger, CLASS_NAME, methodName, opbEventTimerProvider, opbConnectionProvider, "BEGIN ? := property_value.ins(?, ?, ?, ?, ?); END;", "DbCall:property_value#ins(VARCHAR2, VARCHAR2, VARCHAR2, VARCHAR2, VARCHAR2)");
        opbCallHelper.registerOutParameter(1, java.sql.Types.VARCHAR);
        opbCallHelper.setObject(2, java.sql.Types.VARCHAR, pGroupName);
        opbCallHelper.setObject(3, java.sql.Types.VARCHAR, pKey);
        opbCallHelper.setObject(4, java.sql.Types.VARCHAR, pPropertyDescription);
        opbCallHelper.setObject(5, java.sql.Types.VARCHAR, pSortOrder);
        opbCallHelper.setObject(6, java.sql.Types.VARCHAR, pValue);
        opbCallHelper.execute();
        result = opbCallHelper.get(String.class, 1);
        opbDataObjectSource.invalidateCached(PropertyValue.class, getOpbId());
        opbCallHelper.callComplete();
        logger.exiting(CLASS_NAME, methodName);
        return result;
    }

    /**
     * Calls ins using mapped parameters.
     * <ul>
     * <li>pGroupName is mapped to groupName</li>
     * <li>pKey is mapped to key</li>
     * <li>pPropertyDescription is mapped to propertyDescription</li>
     * <li>pSortOrder is mapped to sortOrder</li>
     * <li>pValue is mapped to value</li>
     * </ul>
     * @throws OpbDataAccessException
     *   If we fail to make the database call.
     */
    public String ins() throws OpbDataAccessException {
        final String methodName = "ins()";
        logger.entering(CLASS_NAME, methodName);
        String result = ins(getGroupName(), getKey(), getPropertyDescription(), getSortOrder(), getValue());
        return result;
    }

    /**
     * 
     * Calls the database function upd.
     * @throws OpbDataAccessException
     *   If we fail to make the database call.
     */
    public String upd(final String pRowId, final String pOldGroupName, final String pOldKey, final String pKey, final String pOldPropertyDescription, final String pPropertyDescription, final String pOldSortOrder, final String pSortOrder, final String pOldValue, final String pValue) throws OpbDataAccessException {
        final String methodName = "upd(String, String, String, String, String, String, String, String, String, String)";
        logger.entering(CLASS_NAME, methodName);
        String result = null;
        OpbPlsqlCallHelper opbCallHelper = new OpbPlsqlCallHelper(logger, CLASS_NAME, methodName, opbEventTimerProvider, opbConnectionProvider, "BEGIN ? := property_value.upd(?, ?, ?, ?, ?, ?, ?, ?, ?, ?); END;", "DbCall:property_value#upd(VARCHAR2, VARCHAR2, VARCHAR2, VARCHAR2, VARCHAR2, VARCHAR2, VARCHAR2, VARCHAR2, VARCHAR2, VARCHAR2)");
        opbCallHelper.registerOutParameter(1, java.sql.Types.VARCHAR);
        opbCallHelper.setObject(2, java.sql.Types.VARCHAR, pRowId);
        opbCallHelper.setObject(3, java.sql.Types.VARCHAR, pOldGroupName);
        opbCallHelper.setObject(4, java.sql.Types.VARCHAR, pOldKey);
        opbCallHelper.setObject(5, java.sql.Types.VARCHAR, pKey);
        opbCallHelper.setObject(6, java.sql.Types.VARCHAR, pOldPropertyDescription);
        opbCallHelper.setObject(7, java.sql.Types.VARCHAR, pPropertyDescription);
        opbCallHelper.setObject(8, java.sql.Types.VARCHAR, pOldSortOrder);
        opbCallHelper.setObject(9, java.sql.Types.VARCHAR, pSortOrder);
        opbCallHelper.setObject(10, java.sql.Types.VARCHAR, pOldValue);
        opbCallHelper.setObject(11, java.sql.Types.VARCHAR, pValue);
        opbCallHelper.execute();
        result = opbCallHelper.get(String.class, 1);
        opbDataObjectSource.invalidateCached(PropertyValue.class, getOpbId());
        opbCallHelper.callComplete();
        logger.exiting(CLASS_NAME, methodName);
        return result;
    }

    /**
     * Calls upd using mapped parameters.
     * <ul>
     * <li>pRowId is mapped to rowId</li>
     * <li>pOldGroupName is mapped to groupNameDataSourceValue</li>
     * <li>pOldKey is mapped to keyDataSourceValue</li>
     * <li>pKey is mapped to key</li>
     * <li>pOldPropertyDescription is mapped to propertyDescriptionDataSourceValue</li>
     * <li>pPropertyDescription is mapped to propertyDescription</li>
     * <li>pOldSortOrder is mapped to sortOrderDataSourceValue</li>
     * <li>pSortOrder is mapped to sortOrder</li>
     * <li>pOldValue is mapped to valueDataSourceValue</li>
     * <li>pValue is mapped to value</li>
     * </ul>
     * @throws OpbDataAccessException
     *   If we fail to make the database call.
     */
    public String upd() throws OpbDataAccessException {
        final String methodName = "upd()";
        logger.entering(CLASS_NAME, methodName);
        String result = upd(getRowId(), getGroupNameDataSourceValue(), getKeyDataSourceValue(), getKey(), getPropertyDescriptionDataSourceValue(), getPropertyDescription(), getSortOrderDataSourceValue(), getSortOrder(), getValueDataSourceValue(), getValue());
        return result;
    }
}
