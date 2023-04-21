package org.jaffa.applications.mylife.admin.domain;

import org.jaffa.datatypes.*;
import org.jaffa.metadata.*;
import org.jaffa.rules.RulesEngine;
import org.jaffa.persistence.Persistent;
import org.jaffa.persistence.exceptions.*;
import org.jaffa.exceptions.FrameworkException;

/**
 * Auto Generated Persistent class for the USER_ROLE table.
 * @author  Auto-Generated
 */
public class UserRole extends Persistent {

    /** Holds value of property userName. */
    private java.lang.String m_userName;

    /** Holds value of property roleName. */
    private java.lang.String m_roleName;

    /** Getter for property userName.
     * @return Value of property userName.
     */
    public java.lang.String getUserName() {
        return m_userName;
    }

    /** Setter for property userName.
     * WARNING: This is strictly for use by the Persistence Engine. A developer should never use this method. Instead, use the update(field.Name.Upper1) method.
     * @param userName New value of property userName.
     */
    public void setUserName(java.lang.String userName) {
        m_userName = userName;
    }

    /** Use this method to update the property userName.
     * This method will do nothing and simply return if the input value is the same as the current value.
     * Validation will be performed on the input value.
     * This will try to lock the underlying database row, in case CAUTIOUS locking is specified at the time of query.
     * @param userName New value of property userName.
     * @throws ValidationException if an invalid value is passed.
     * @throws UpdatePrimaryKeyException if this domain object was loaded from the database.
     * @throws ReadOnlyObjectException if a Read-Only object is updated.
     * @throws AlreadyLockedObjectException if the underlying database row is already locked by another process.
     * @throws FrameworkException Indicates some system error
     */
    public void updateUserName(java.lang.String userName) throws ValidationException, UpdatePrimaryKeyException, ReadOnlyObjectException, AlreadyLockedObjectException, FrameworkException {
        if (m_userName == null ? userName == null : m_userName.equals(userName)) return;
        if (isDatabaseOccurence()) throw new UpdatePrimaryKeyException();
        validateUserName(userName);
        super.update();
        setUserName(userName);
    }

    /** Use this method to validate a value for the property userName.
     * @param userName Value to be validated for the property userName.
     * @throws ValidationException if an invalid value is passed
     * @throws FrameworkException Indicates some system error
     */
    public void validateUserName(java.lang.String userName) throws ValidationException, FrameworkException {
        FieldValidator.validate(userName, (StringFieldMetaData) UserRoleMeta.META_USER_NAME, true);
        RulesEngine.doAllValidationsForDomainField("org.jaffa.applications.mylife.admin.domain.UserRole", "UserName", userName, this.getUOW());
    }

    /** Getter for property roleName.
     * @return Value of property roleName.
     */
    public java.lang.String getRoleName() {
        return m_roleName;
    }

    /** Setter for property roleName.
     * WARNING: This is strictly for use by the Persistence Engine. A developer should never use this method. Instead, use the update(field.Name.Upper1) method.
     * @param roleName New value of property roleName.
     */
    public void setRoleName(java.lang.String roleName) {
        m_roleName = roleName;
    }

    /** Use this method to update the property roleName.
     * This method will do nothing and simply return if the input value is the same as the current value.
     * Validation will be performed on the input value.
     * This will try to lock the underlying database row, in case CAUTIOUS locking is specified at the time of query.
     * @param roleName New value of property roleName.
     * @throws ValidationException if an invalid value is passed.
     * @throws UpdatePrimaryKeyException if this domain object was loaded from the database.
     * @throws ReadOnlyObjectException if a Read-Only object is updated.
     * @throws AlreadyLockedObjectException if the underlying database row is already locked by another process.
     * @throws FrameworkException Indicates some system error
     */
    public void updateRoleName(java.lang.String roleName) throws ValidationException, UpdatePrimaryKeyException, ReadOnlyObjectException, AlreadyLockedObjectException, FrameworkException {
        if (m_roleName == null ? roleName == null : m_roleName.equals(roleName)) return;
        if (isDatabaseOccurence()) throw new UpdatePrimaryKeyException();
        validateRoleName(roleName);
        super.update();
        setRoleName(roleName);
    }

    /** Use this method to validate a value for the property roleName.
     * @param roleName Value to be validated for the property roleName.
     * @throws ValidationException if an invalid value is passed
     * @throws FrameworkException Indicates some system error
     */
    public void validateRoleName(java.lang.String roleName) throws ValidationException, FrameworkException {
        FieldValidator.validate(roleName, (StringFieldMetaData) UserRoleMeta.META_ROLE_NAME, true);
        RulesEngine.doAllValidationsForDomainField("org.jaffa.applications.mylife.admin.domain.UserRole", "RoleName", roleName, this.getUOW());
    }

    /** This returns the diagnostic information.
    * @return the diagnostic information.
    */
    public String toString() {
        StringBuffer buf = new StringBuffer();
        buf.append("<UserRole>");
        buf.append("<userName>");
        if (m_userName != null) buf.append(m_userName);
        buf.append("</userName>");
        buf.append("<roleName>");
        if (m_roleName != null) buf.append(m_roleName);
        buf.append("</roleName>");
        buf.append(super.toString());
        buf.append("</UserRole>");
        return buf.toString();
    }
}
