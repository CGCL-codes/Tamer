package com.centraview.account.expense;

import java.io.Serializable;

public class ExpensePK implements Serializable {

    private int expenseId;

    private String dataSource;

    public int hashCode() {
        String aggregate = this.expenseId + this.dataSource;
        return (aggregate.hashCode());
    }

    public boolean equals(Object obj) {
        if (obj == null || !(obj instanceof ExpensePK)) {
            return false;
        } else if ((((ExpensePK) obj).getId() == this.expenseId) && ((ExpensePK) obj).getDataSource() == this.dataSource) {
            return true;
        } else {
            return false;
        }
    }

    public String toString() {
        return ("ExpensePK: ExpenseID: " + expenseId + ", dataSource: " + dataSource);
    }

    public ExpensePK(int pkId, String ds) {
        this.expenseId = pkId;
        this.dataSource = ds;
    }

    public int getId() {
        return this.expenseId;
    }

    public String getDataSource() {
        return this.dataSource;
    }

    public ExpensePK() {
    }
}
