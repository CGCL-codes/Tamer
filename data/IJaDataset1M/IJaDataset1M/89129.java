package com.eptica.ias.models.accounts.accountrole;

import java.io.Serializable;
import java.lang.Comparable;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.CompareToBuilder;

/**
 * Composite primary key class for AccountRoleModel
 */
public class AccountRolePk implements Serializable, Comparable {

    private static final long serialVersionUID = 1L;

    private String accountId = null;

    private Integer roleId = null;

    /**
     * Set the accountId 
     *
     * @param accountId the accountId to set
     */
    public void setAccountId(String accountId) {
        this.accountId = accountId;
    }

    /**
     * Returns this accountId value.
     *
     * @return the accountId
     */
    public String getAccountId() {
        return accountId;
    }

    /**
     * Helper method to know if the primary key has been set
     */
    public boolean hasAccountId() {
        return accountId != null && accountId.length() > 0;
    }

    /**
     * @see #hasAccountId()
     */
    public boolean getHasAccountId() {
        return hasAccountId();
    }

    /**
     * Set the roleId 
     *
     * @param roleId the roleId to set
     */
    public void setRoleId(Integer roleId) {
        this.roleId = roleId;
    }

    /**
     * Returns this roleId value.
     *
     * @return the roleId
     */
    public Integer getRoleId() {
        return roleId;
    }

    /**
     * Helper method to know if the primary key has been set
     */
    public boolean hasRoleId() {
        return roleId != null;
    }

    /**
     * @see #hasRoleId()
     */
    public boolean getHasRoleId() {
        return hasRoleId();
    }

    /**
     * Helper method to determine if this instance is considered empty, that is<br>
     * if all the non primary key columns are null.
     */
    public boolean isEmpty() {
        return (accountId == null || accountId.length() == 0) && roleId == null;
    }

    /**
     * Please read discussion about object identity at <a href="http://www.hibernate.org/109.html">http://www.hibernate.org/109.html</a>
     *
     * @param object the object to check again the current instance
     * @return true if equals, false otherwise
     * @see java.lang.Object#equals(Object)
     */
    public boolean equals(Object object) {
        if (this == object) {
            return true;
        }
        if ((object == null) || (object.getClass() != this.getClass())) {
            return false;
        }
        AccountRolePk other = (AccountRolePk) object;
        return new EqualsBuilder().append(getAccountId(), other.getAccountId()).append(getRoleId(), other.getRoleId()).isEquals();
    }

    /**
     * When two objects are equals, their hashcode must be equal too
     *
     * @see #equals(Object)
     * @see java.lang.Object#hashCode()
     * @return the object hashcode
     */
    public int hashCode() {
        return new HashCodeBuilder(17, 37).append(getAccountId()).append(getRoleId()).toHashCode();
    }

    /**
     * compare this object to the passed instance
     *
     * @param object the object to compare with
     * @return a integer
     */
    public int compareTo(Object object) {
        AccountRolePk obj = (AccountRolePk) object;
        return new CompareToBuilder().append(getAccountId(), obj.getAccountId()).append(getRoleId(), obj.getRoleId()).toComparison();
    }

    /**
     * String representation of the current $modelClass
     *
     * @see java.lang.Object#toString()
     */
    public String toString() {
        StringBuilder ret = new StringBuilder(128);
        ret.append("account_role.account_id=[").append(getAccountId()).append("] ");
        ret.append("account_role.role_id=[").append(getRoleId()).append("] ");
        return ret.toString();
    }

    public String toDisplayString() {
        StringBuilder ret = new StringBuilder(128);
        if (getAccountId() != null) ret.append(getAccountId()).append(" ");
        if (getRoleId() != null) ret.append(getRoleId()).append(" ");
        return ret.toString();
    }
}
