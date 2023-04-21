package org.demis.elf.bankAccountEntry;

import java.util.Collection;
import java.util.HashSet;
import java.io.Serializable;
import org.apache.commons.lang.StringUtils;
import org.demis.elf.bankAccount.*;

public class BankAccountEntry implements Serializable {

    private static final org.apache.commons.logging.Log logger = org.apache.commons.logging.LogFactory.getLog(BankAccountEntry.class);

    private java.lang.String bankAccountEntryId = null;

    private java.sql.Timestamp entryDate = null;

    private java.lang.Double entryAmount = null;

    private java.lang.String entryPaye = null;

    private java.lang.String entryMemo = null;

    private BankAccount bankAccount = null;

    public BankAccountEntry() {
    }

    /**
     * Helper method to know whether the primary key is set or not,
     * @return true if the primary key is set, false otherwise
     * TODO : multiple column primary key
     */
    public boolean hasPrimaryKey() {
        return hasBankAccountEntryId();
    }

    public java.lang.String getBankAccountEntryId() {
        return bankAccountEntryId;
    }

    public void setBankAccountEntryId(java.lang.String bankAccountEntryId) {
        this.bankAccountEntryId = bankAccountEntryId;
    }

    public boolean hasBankAccountEntryId() {
        return StringUtils.isNotEmpty(getBankAccountEntryId());
    }

    public java.sql.Timestamp getEntryDate() {
        return entryDate;
    }

    public void setEntryDate(java.sql.Timestamp entryDate) {
        this.entryDate = entryDate;
    }

    public boolean hasEntryDate() {
        return entryDate != null;
    }

    public java.lang.Double getEntryAmount() {
        return entryAmount;
    }

    public void setEntryAmount(java.lang.Double entryAmount) {
        this.entryAmount = entryAmount;
    }

    public boolean hasEntryAmount() {
        return entryAmount != null;
    }

    public java.lang.String getEntryPaye() {
        return entryPaye;
    }

    public void setEntryPaye(java.lang.String entryPaye) {
        this.entryPaye = entryPaye;
    }

    public boolean hasEntryPaye() {
        return StringUtils.isNotEmpty(getEntryPaye());
    }

    public java.lang.String getEntryMemo() {
        return entryMemo;
    }

    public void setEntryMemo(java.lang.String entryMemo) {
        this.entryMemo = entryMemo;
    }

    public boolean hasEntryMemo() {
        return StringUtils.isNotEmpty(getEntryMemo());
    }

    public void setBankAccount(BankAccount bankAccount) {
        this.bankAccount = bankAccount;
    }

    public BankAccount getBankAccount() {
        return bankAccount;
    }

    public boolean hasBankAccount() {
        return bankAccount != null;
    }

    public String toString() {
        String result = "";
        result += bankAccountEntryId;
        return result;
    }

    private boolean __firstTimeEqualsOrHashCodeUse__ = false;

    private java.rmi.dgc.VMID __idUseInEqualsOrHashCode__ = null;

    /**
      * If onject have no primary key at the first equals or hashCode call,
      * they use a unique id.
      */
    private void setEqualsAndHashcodeId() {
        if (!__firstTimeEqualsOrHashCodeUse__) {
            __firstTimeEqualsOrHashCodeUse__ = true;
            if (!hasPrimaryKey()) {
                __idUseInEqualsOrHashCode__ = new java.rmi.dgc.VMID();
            }
        }
    }

    /**
      * Indicates whether some other object is "equal to" this one.
      * @see java.lang.Object#equals(Object)
      * @return true if the equals, false otherwise
      */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (!(obj instanceof BankAccountEntry)) {
            return false;
        }
        BankAccountEntry other = (BankAccountEntry) obj;
        setEqualsAndHashcodeId();
        if (__idUseInEqualsOrHashCode__ != null) {
            return __idUseInEqualsOrHashCode__.equals(other.__idUseInEqualsOrHashCode__);
        } else {
            boolean result = true;
            result = result && getBankAccountEntryId().equals(other.getBankAccountEntryId());
            return result;
        }
    }

    @Override
    public int hashCode() {
        setEqualsAndHashcodeId();
        if (__idUseInEqualsOrHashCode__ != null) {
            return __idUseInEqualsOrHashCode__.hashCode();
        } else {
            if (hasPrimaryKey()) {
                return getBankAccountEntryId().hashCode();
            } else {
                return super.hashCode();
            }
        }
    }
}
