package net.caece.pri.hibernate;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

/** 
 *        @hibernate.class
 *         table="pri_user"
 *     
*/
public class User implements Serializable {

    /** identifier field */
    private Long userId;

    /** nullable persistent field */
    private String account;

    /** nullable persistent field */
    private String password;

    /** nullable persistent field */
    private String userName;

    /** nullable persistent field */
    private String operateBy;

    /** nullable persistent field */
    private Date operateAt;

    /** persistent field */
    private net.caece.pri.hibernate.Org org;

    /** persistent field */
    private List duties;

    /** full constructor */
    public User(String account, String password, String userName, String operateBy, Date operateAt, net.caece.pri.hibernate.Org org, List duties) {
        this.account = account;
        this.password = password;
        this.userName = userName;
        this.operateBy = operateBy;
        this.operateAt = operateAt;
        this.operateAt = operateAt;
        this.org = org;
        this.duties = duties;
    }

    /** default constructor */
    public User() {
    }

    /** minimal constructor */
    public User(net.caece.pri.hibernate.Org org, List duties) {
        this.org = org;
        this.duties = duties;
    }

    /** 
     *            @hibernate.id
     *             generator-class="native"
     *             type="java.lang.Long"
     *             column="userid"
     *         
     */
    public Long getUserId() {
        return this.userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    /** 
     *            @hibernate.property
     *             column="account"
     *             length="30"
     *         
     */
    public String getAccount() {
        return this.account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    /** 
     *            @hibernate.property
     *             column="passwd"
     *             length="8"
     *         
     */
    public String getPassword() {
        return this.password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    /** 
     *            @hibernate.property
     *             column="username"
     *             length="30"
     *         
     */
    public String getUserName() {
        return this.userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    /** 
     *            @hibernate.property
     *             column="operateby"
     *             length="30"
     *         
     */
    public String getOperateBy() {
        return this.operateBy;
    }

    public void setOperateBy(String operateBy) {
        this.operateBy = operateBy;
    }

    /** 
     *            @hibernate.property
     *             column="operateat"
     *             length="29"
     *         
     */
    public Date getOperateAt() {
        return this.operateAt;
    }

    public void setOperateAt(Date operateAt) {
        this.operateAt = operateAt;
    }

    /** 
     *            @hibernate.many-to-one
     *             not-null="true"
     *            @hibernate.column name="orgid"         
     *         
     */
    public net.caece.pri.hibernate.Org getOrg() {
        return this.org;
    }

    public void setOrg(net.caece.pri.hibernate.Org org) {
        this.org = org;
    }

    /** 
     *            @hibernate.list
     *             lazy="true"
     *             inverse="true"
     *             cascade="none"
     *            @hibernate.collection-key
     *             column="userid"
     *            @hibernate.collection-one-to-many
     *             class="net.caece.pri.hibernate.Duty"
     *         
     */
    public List getDuties() {
        return this.duties;
    }

    public void setDuties(List duties) {
        this.duties = duties;
    }

    public String toString() {
        return new ToStringBuilder(this).append("userId", getUserId()).toString();
    }

    public boolean equals(Object other) {
        if (!(other instanceof User)) return false;
        User castOther = (User) other;
        return new EqualsBuilder().append(this.getUserId(), castOther.getUserId()).isEquals();
    }

    public int hashCode() {
        return new HashCodeBuilder().append(getUserId()).toHashCode();
    }
}
