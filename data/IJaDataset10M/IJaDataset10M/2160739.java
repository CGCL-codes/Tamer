package com.butterfill.opb.webdemo.data;

import com.butterfill.opb.data.*;
import com.butterfill.opb.groups.*;
import com.butterfill.opb.timing.*;
import com.butterfill.opb.util.*;

/**
 * File created from the PL/SQL package specification
 * permissions.
 */
public interface Permissions extends OpbGroupable, OpbTimingEventPublisher, OpbActiveDataObject, OpbEntity {

    /**
     * Resets all field values to their initial values.
     */
    void opbClearState();

    /**
     * Returns the value of permission.
     * @return The value of permission.
     */
    String getPermission();

    /**
     * Sets the value of permission.
     * @param a The new value for permission.
     */
    void setPermission(String a);

    /**
     * Returns the value of permissionDataSourceValue.
     * This is the last value returned by the data source for permission.
     * @return The value of permissionDataSourceValue.
     */
    String getPermissionDataSourceValue();

    /**
     * Returns true if the value of permission 
     * is different to the value that was loaded from the data source,
     * false otherwise.
     * @return true if permission has changed since it was loaded.
     */
    boolean getPermissionChanged();

    /**
     * Returns the value of description.
     * @return The value of description.
     */
    String getDescription();

    /**
     * Sets the value of description.
     * @param a The new value for description.
     */
    void setDescription(String a);

    /**
     * Returns the value of descriptionDataSourceValue.
     * This is the last value returned by the data source for description.
     * @return The value of descriptionDataSourceValue.
     */
    String getDescriptionDataSourceValue();

    /**
     * Returns true if the value of description 
     * is different to the value that was loaded from the data source,
     * false otherwise.
     * @return true if description has changed since it was loaded.
     */
    boolean getDescriptionChanged();

    /**
     * Returns the value of status.
     * @return The value of status.
     */
    Long getStatus();

    /**
     * Sets the value of status.
     * @param a The new value for status.
     */
    void setStatus(Long a);

    /**
     * Returns the value of statusDataSourceValue.
     * This is the last value returned by the data source for status.
     * @return The value of statusDataSourceValue.
     */
    Long getStatusDataSourceValue();

    /**
     * Returns true if the value of status 
     * is different to the value that was loaded from the data source,
     * false otherwise.
     * @return true if status has changed since it was loaded.
     */
    boolean getStatusChanged();

    /**
     * Returns the value of permissionSearchString.
     * @return The value of permissionSearchString.
     */
    String getPermissionSearchString();

    /**
     * Sets the value of permissionSearchString.
     * @param a The new value for permissionSearchString.
     */
    void setPermissionSearchString(String a);

    /**
     * Returns the value of permissionSearchStringDataSourceValue.
     * This is the last value returned by the data source for permissionSearchString.
     * @return The value of permissionSearchStringDataSourceValue.
     */
    String getPermissionSearchStringDataSourceValue();

    /**
     * Returns true if the value of permissionSearchString 
     * is different to the value that was loaded from the data source,
     * false otherwise.
     * @return true if permissionSearchString has changed since it was loaded.
     */
    boolean getPermissionSearchStringChanged();

    /**
     * Returns the value of descriptionSearchString.
     * @return The value of descriptionSearchString.
     */
    String getDescriptionSearchString();

    /**
     * Sets the value of descriptionSearchString.
     * @param a The new value for descriptionSearchString.
     */
    void setDescriptionSearchString(String a);

    /**
     * Returns the value of descriptionSearchStringDataSourceValue.
     * This is the last value returned by the data source for descriptionSearchString.
     * @return The value of descriptionSearchStringDataSourceValue.
     */
    String getDescriptionSearchStringDataSourceValue();

    /**
     * Returns true if the value of descriptionSearchString 
     * is different to the value that was loaded from the data source,
     * false otherwise.
     * @return true if descriptionSearchString has changed since it was loaded.
     */
    boolean getDescriptionSearchStringChanged();

    /**
     * Returns the value of statusSearchValue.
     * @return The value of statusSearchValue.
     */
    Long getStatusSearchValue();

    /**
     * Sets the value of statusSearchValue.
     * @param a The new value for statusSearchValue.
     */
    void setStatusSearchValue(Long a);

    /**
     * Returns the value of statusSearchValueDataSourceValue.
     * This is the last value returned by the data source for statusSearchValue.
     * @return The value of statusSearchValueDataSourceValue.
     */
    Long getStatusSearchValueDataSourceValue();

    /**
     * Returns true if the value of statusSearchValue 
     * is different to the value that was loaded from the data source,
     * false otherwise.
     * @return true if statusSearchValue has changed since it was loaded.
     */
    boolean getStatusSearchValueChanged();

    /**
     * 
     * Calls the database function get_permissions.
     * @throws OpbDataAccessException
     *   If we fail to make the database call.
     */
    java.util.List<Permission> getPermissions(String pPermissionSearchString, String pDescriptionSearchString, Long pStatusSearchValue) throws OpbDataAccessException;

    /**
     * Calls getPermissions using mapped parameters.
     * <ul>
     * <li>pPermissionSearchString is mapped to permissionSearchString</li>
     * <li>pDescriptionSearchString is mapped to descriptionSearchString</li>
     * <li>pStatusSearchValue is mapped to statusSearchValue</li>
     * </ul>
     * @throws OpbDataAccessException
     *   If we fail to make the database call.
     */
    java.util.List<Permission> getPermissions() throws OpbDataAccessException;

    /**
     * 
     * Calls the database function get_permission_sets.
     * @throws OpbDataAccessException
     *   If we fail to make the database call.
     */
    java.util.List<Permission> getPermissionSets(String pPermissionSearchString, String pDescriptionSearchString, Long pStatusSearchValue) throws OpbDataAccessException;

    /**
     * Calls getPermissionSets using mapped parameters.
     * <ul>
     * <li>pPermissionSearchString is mapped to permissionSearchString</li>
     * <li>pDescriptionSearchString is mapped to descriptionSearchString</li>
     * <li>pStatusSearchValue is mapped to statusSearchValue</li>
     * </ul>
     * @throws OpbDataAccessException
     *   If we fail to make the database call.
     */
    java.util.List<Permission> getPermissionSets() throws OpbDataAccessException;
}
