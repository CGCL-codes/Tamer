package net.mjrz.fm.entity.beans;

import java.math.BigDecimal;
import java.text.NumberFormat;
import java.util.Date;

/**
 * @author Mjrz contact@mjrz.net
 *
 */
public class Account implements FManEntity, Cloneable, Comparable<Account> {

    private long accountId;

    private long ownerId;

    private int accountType;

    private int accountParentType;

    private int status;

    private String accountName;

    private String accountNotes;

    private BigDecimal startingBalance;

    private BigDecimal currentBalance;

    private Date startDate;

    private BigDecimal highBalance;

    private Date highBalanceDate;

    private Long categoryId;

    private String accountNumber;

    public String getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }

    public long getAccountId() {
        return accountId;
    }

    public void setAccountId(long accountId) {
        this.accountId = accountId;
    }

    public long getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(long ownerId) {
        this.ownerId = ownerId;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getAccountName() {
        return accountName;
    }

    public void setAccountName(String accountName) {
        this.accountName = accountName;
    }

    public String getAccountNotes() {
        return accountNotes;
    }

    public void setAccountNotes(String accountNotes) {
        this.accountNotes = accountNotes;
    }

    public int getAccountType() {
        return accountType;
    }

    public void setAccountType(int accountType) {
        this.accountType = accountType;
    }

    public int getAccountParentType() {
        return accountParentType;
    }

    public void setAccountParentType(int accountParentType) {
        this.accountParentType = accountParentType;
    }

    public BigDecimal getCurrentBalance() {
        return currentBalance;
    }

    public void setCurrentBalance(BigDecimal currentBalance) {
        this.currentBalance = currentBalance;
        if (highBalance == null) {
            highBalance = new BigDecimal(0d);
        }
        if (highBalance.compareTo(currentBalance) < 0) {
            highBalance = currentBalance;
            highBalanceDate = new Date();
        }
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public BigDecimal getStartingBalance() {
        return startingBalance;
    }

    public void setStartingBalance(BigDecimal startingBalance) {
        this.startingBalance = startingBalance;
    }

    public BigDecimal getHighBalance() {
        return highBalance;
    }

    public void setHighBalance(BigDecimal highBalance) {
        this.highBalance = highBalance;
    }

    public Date getHighBalanceDate() {
        return highBalanceDate;
    }

    public void setHighBalanceDate(Date highBalanceDate) {
        this.highBalanceDate = highBalanceDate;
    }

    public Long getCategoryId() {
        if (categoryId == null) return Long.valueOf(accountType);
        return categoryId;
    }

    public void setCategoryId(Long categoryId) {
        this.categoryId = categoryId;
    }

    public String toString() {
        return accountName.toString();
    }

    public final Object clone() throws CloneNotSupportedException {
        try {
            return super.clone();
        } catch (CloneNotSupportedException e) {
            return null;
        }
    }

    public int hashCode() {
        int hash = 1;
        hash = hash * 31 + accountName.hashCode();
        hash = hash * 31 + (accountNotes == null ? 0 : accountNotes.hashCode());
        return hash;
    }

    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Account)) {
            return false;
        }
        Account otherAcct = (Account) o;
        return (accountId == otherAcct.accountId);
    }

    @Override
    public int compareTo(Account o) {
        return accountName.compareTo(o.getAccountName());
    }

    @Override
    public String getPKColumnName() {
        return "accountId";
    }

    @Override
    public Object getPK() {
        return null;
    }

    @Override
    public void setPK(Object pk) {
        setAccountId((Long) pk);
    }
}
