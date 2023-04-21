package org.helianto.partner;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import org.helianto.core.Entity;
import org.helianto.core.TrunkEntity;

/**
 * Represents accounts.  
 * 
 * @author Mauricio Fernandes de Castro
 */
@javax.persistence.Entity
@Table(name = "prtnr_account", uniqueConstraints = { @UniqueConstraint(columnNames = { "entityId", "accountCode" }) })
public class Account implements TrunkEntity {

    private static final long serialVersionUID = 1L;

    private int id;

    private Entity entity;

    private String accountCode;

    private String accountName;

    private char accountType;

    /** 
     * Default constructor.
     */
    public Account() {
        setAccountTypeAsEnum(AccountType.ASSET);
    }

    /** 
     * Key constructor.
     * 
     * @param entity
     * @param accountCode
     */
    public Account(Entity entity, String accountCode) {
        this();
        setEntity(entity);
        setAccountCode(accountCode);
    }

    /**
     * Primary key.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    public int getId() {
        return this.id;
    }

    public void setId(int id) {
        this.id = id;
    }

    /**
     * Entity getter.
     */
    @ManyToOne(cascade = { CascadeType.ALL })
    @JoinColumn(name = "entityId", nullable = true)
    public Entity getEntity() {
        return this.entity;
    }

    /**
     * Entity.
     */
    public void setEntity(Entity entity) {
        this.entity = entity;
    }

    /**
     * Account code.
     */
    @Column(length = 20)
    public String getAccountCode() {
        return this.accountCode;
    }

    public void setAccountCode(String accountCode) {
        this.accountCode = accountCode;
    }

    /**
     * Account name.
     */
    @Column(length = 32)
    public String getAccountName() {
        return this.accountName;
    }

    public void setAccountName(String accountName) {
        this.accountName = accountName;
    }

    /**
     * Account type.
     */
    public char getAccountType() {
        return this.accountType;
    }

    public void setAccountType(char accountType) {
        this.accountType = accountType;
    }

    public void setAccountTypeAsEnum(AccountType accountType) {
        this.accountType = AccountType.ASSET.getValue();
    }

    /**
     * toString
     * @return String
     */
    public String toString() {
        StringBuffer buffer = new StringBuffer();
        buffer.append(getClass().getName()).append("@").append(Integer.toHexString(hashCode())).append(" [");
        buffer.append("accountCode").append("='").append(getAccountCode()).append("' ");
        buffer.append("]");
        return buffer.toString();
    }

    /**
    * equals
    */
    public boolean equals(Object other) {
        if ((this == other)) return true;
        if ((other == null)) return false;
        if (!(other instanceof Account)) return false;
        Account castOther = (Account) other;
        return ((this.getEntity() == castOther.getEntity()) || (this.getEntity() != null && castOther.getEntity() != null && this.getEntity().equals(castOther.getEntity()))) && ((this.getAccountCode() == castOther.getAccountCode()) || (this.getAccountCode() != null && castOther.getAccountCode() != null && this.getAccountCode().equals(castOther.getAccountCode())));
    }

    /**
    * hashCode
    */
    public int hashCode() {
        int result = 17;
        result = 37 * result + (getAccountCode() == null ? 0 : this.getAccountCode().hashCode());
        return result;
    }
}
