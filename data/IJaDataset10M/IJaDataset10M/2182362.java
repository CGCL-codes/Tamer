package com.butterfill.opb.webdemo.data;

import com.butterfill.opb.data.*;
import com.butterfill.opb.groups.*;
import com.butterfill.opb.timing.*;
import com.butterfill.opb.util.*;

/**
 * File created from the PL/SQL package specification
 * property_group.
 */
public interface PropertyGroup extends OpbGroupable, OpbTimingEventPublisher, OpbActiveCacheableEntity {

    /**
     * Resets all field values to their initial values.
     */
    void opbClearState();

    /**
     * Returns the value of rowId.
     * @return The value of rowId.
     */
    String getRowId();

    /**
     * Returns the value of groupName.
     * @return The value of groupName.
     */
    String getGroupName();

    /**
     * Sets the value of groupName.
     * @param a The new value for groupName.
     */
    void setGroupName(String a);

    /**
     * Returns the value of groupNameDataSourceValue.
     * This is the last value returned by the data source for groupName.
     * @return The value of groupNameDataSourceValue.
     */
    String getGroupNameDataSourceValue();

    /**
     * Returns true if the value of groupName 
     * is different to the value that was loaded from the data source,
     * false otherwise.
     * @return true if groupName has changed since it was loaded.
     */
    boolean getGroupNameChanged();

    /**
     * Returns the value of singleValuePerKey.
     * @return The value of singleValuePerKey.
     */
    String getSingleValuePerKey();

    /**
     * Sets the value of singleValuePerKey.
     * @param a The new value for singleValuePerKey.
     */
    void setSingleValuePerKey(String a);

    /**
     * Returns the value of singleValuePerKeyDataSourceValue.
     * This is the last value returned by the data source for singleValuePerKey.
     * @return The value of singleValuePerKeyDataSourceValue.
     */
    String getSingleValuePerKeyDataSourceValue();

    /**
     * Returns true if the value of singleValuePerKey 
     * is different to the value that was loaded from the data source,
     * false otherwise.
     * @return true if singleValuePerKey has changed since it was loaded.
     */
    boolean getSingleValuePerKeyChanged();

    /**
     * Returns the value of locked.
     * @return The value of locked.
     */
    String getLocked();

    /**
     * Sets the value of locked.
     * @param a The new value for locked.
     */
    void setLocked(String a);

    /**
     * Returns the value of lockedDataSourceValue.
     * This is the last value returned by the data source for locked.
     * @return The value of lockedDataSourceValue.
     */
    String getLockedDataSourceValue();

    /**
     * Returns true if the value of locked 
     * is different to the value that was loaded from the data source,
     * false otherwise.
     * @return true if locked has changed since it was loaded.
     */
    boolean getLockedChanged();

    /**
     * Returns the value of groupDescription.
     * @return The value of groupDescription.
     */
    String getGroupDescription();

    /**
     * Sets the value of groupDescription.
     * @param a The new value for groupDescription.
     */
    void setGroupDescription(String a);

    /**
     * Returns the value of groupDescriptionDataSourceValue.
     * This is the last value returned by the data source for groupDescription.
     * @return The value of groupDescriptionDataSourceValue.
     */
    String getGroupDescriptionDataSourceValue();

    /**
     * Returns true if the value of groupDescription 
     * is different to the value that was loaded from the data source,
     * false otherwise.
     * @return true if groupDescription has changed since it was loaded.
     */
    boolean getGroupDescriptionChanged();

    /**
     * Returns the value of forceDelete.
     * @return The value of forceDelete.
     */
    Boolean getForceDelete();

    /**
     * Sets the value of forceDelete.
     * @param a The new value for forceDelete.
     */
    void setForceDelete(Boolean a);

    /**
     * 
     * Calls the database function ins.
     * @throws OpbDataAccessException
     *   If we fail to make the database call.
     */
    String ins(String pGroupName, String pSingleValuePerKey, String pGroupDescription) throws OpbDataAccessException;

    /**
     * Calls ins using mapped parameters.
     * <ul>
     * <li>pGroupName is mapped to groupName</li>
     * <li>pSingleValuePerKey is mapped to singleValuePerKey</li>
     * <li>pGroupDescription is mapped to groupDescription</li>
     * </ul>
     * @throws OpbDataAccessException
     *   If we fail to make the database call.
     */
    String ins() throws OpbDataAccessException;

    /**
     * 
     * Calls the database function del.
     * @throws OpbDataAccessException
     *   If we fail to make the database call.
     */
    String del(String pRowId, Boolean pForce, String pOldGroupName, String pOldSingleValuePerKey, String pOldLocked, String pOldGroupDescription) throws OpbDataAccessException;

    /**
     * Calls del using mapped parameters.
     * <ul>
     * <li>pRowId is mapped to rowId</li>
     * <li>pForce is mapped to forceDelete</li>
     * <li>pOldGroupName is mapped to groupNameDataSourceValue</li>
     * <li>pOldSingleValuePerKey is mapped to singleValuePerKeyDataSourceValue</li>
     * <li>pOldLocked is mapped to lockedDataSourceValue</li>
     * <li>pOldGroupDescription is mapped to groupDescriptionDataSourceValue</li>
     * </ul>
     * @throws OpbDataAccessException
     *   If we fail to make the database call.
     */
    String del() throws OpbDataAccessException;

    /**
     * 
     * Calls the database function upd.
     * @throws OpbDataAccessException
     *   If we fail to make the database call.
     */
    String upd(String pRowId, String pOldGroupName, String pOldSingleValuePerKey, String pSingleValuePerKey, String pOldLocked, String pLocked, String pOldGroupDescription, String pGroupDescription) throws OpbDataAccessException;

    /**
     * Calls upd using mapped parameters.
     * <ul>
     * <li>pRowId is mapped to rowId</li>
     * <li>pOldGroupName is mapped to groupNameDataSourceValue</li>
     * <li>pOldSingleValuePerKey is mapped to singleValuePerKeyDataSourceValue</li>
     * <li>pSingleValuePerKey is mapped to singleValuePerKey</li>
     * <li>pOldLocked is mapped to lockedDataSourceValue</li>
     * <li>pLocked is mapped to locked</li>
     * <li>pOldGroupDescription is mapped to groupDescriptionDataSourceValue</li>
     * <li>pGroupDescription is mapped to groupDescription</li>
     * </ul>
     * @throws OpbDataAccessException
     *   If we fail to make the database call.
     */
    String upd() throws OpbDataAccessException;

    /**
     * 
     * Calls the database function get_property_values.
     * @throws OpbDataAccessException
     *   If we fail to make the database call.
     */
    java.util.List<PropertyValue> getPropertyValues(String pOldGroupName) throws OpbDataAccessException;

    /**
     * Calls getPropertyValues using mapped parameters.
     * <ul>
     * <li>pOldGroupName is mapped to groupNameDataSourceValue</li>
     * </ul>
     * @throws OpbDataAccessException
     *   If we fail to make the database call.
     */
    java.util.List<PropertyValue> getPropertyValues() throws OpbDataAccessException;
}
