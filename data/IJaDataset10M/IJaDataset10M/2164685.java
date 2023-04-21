package net.mjrz.fm.entity.beans;

import java.math.BigDecimal;
import net.mjrz.fm.entity.FManEntityManager;

public class BudgetedAccount {

    private long id;

    private long accountId;

    private BigDecimal allocatedAmount;

    private transient BigDecimal actualAmount;

    private transient String accountName;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getAccountId() {
        return accountId;
    }

    public void setAccountId(long accountId) {
        this.accountId = accountId;
    }

    public BigDecimal getAllocatedAmount() {
        return allocatedAmount;
    }

    public void setAllocatedAmount(BigDecimal allocatedAmount) {
        this.allocatedAmount = allocatedAmount;
    }

    public BigDecimal getActualAmount() {
        return actualAmount;
    }

    public void setActualAmount(BigDecimal actualAmount) {
        this.actualAmount = actualAmount;
    }

    public String getAccountName() {
        return accountName;
    }

    public void setAccountName(String accountName) {
        this.accountName = accountName;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + (int) (accountId ^ (accountId >>> 32));
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null) return false;
        if (getClass() != obj.getClass()) return false;
        BudgetedAccount other = (BudgetedAccount) obj;
        if (accountId != other.accountId) return false;
        return true;
    }

    public String toString() {
        if (accountName == null) return String.valueOf(accountId); else return accountName;
    }
}
