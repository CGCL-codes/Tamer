package de.thinrichs.accounts;

import java.util.Iterator;
import java.util.Set;
import java.util.TreeSet;

public class Account {

    private String name;

    private Integer number;

    private double amountBooked;

    private double amountNotBooked;

    private Set<Account> subAccounts;

    public Account(int number, String name) {
        this(new Integer(number), name);
    }

    public Account(Integer number, String name) {
        this.subAccounts = new TreeSet<Account>();
        this.name = name;
        this.number = number;
        this.amountBooked = new Double(0);
        this.amountNotBooked = new Double(0);
    }

    public double plusAmountNotBooked(double amountToAdd) {
        amountNotBooked += amountToAdd;
        return getAmountNotBooked();
    }

    public double plusAmountBooked(double amountToAdd, Transaction action) {
        if (action.isAccountAssignee(this) && action.isAccountOriginator(this)) {
            amountBooked += amountToAdd;
        }
        return getAmountBooked();
    }

    public void addSubAccount(Account subAccount) {
        this.subAccounts.add(subAccount);
    }

    public double getAmountBooked() {
        Iterator<Account> it = this.subAccounts.iterator();
        double amountBookedWithSubAccounts = 0;
        while (it.hasNext()) {
            amountBookedWithSubAccounts += it.next().getAmountBooked();
        }
        return amountBooked + amountBookedWithSubAccounts;
    }

    public double getAmountNotBooked() {
        Iterator<Account> it = this.subAccounts.iterator();
        double amountNotBookedWithSubAccounts = 0;
        while (it.hasNext()) {
            Account currentSubAccount = it.next();
            amountNotBookedWithSubAccounts += currentSubAccount.getAmountNotBooked();
        }
        return amountNotBooked += amountNotBookedWithSubAccounts;
    }

    public boolean equals(Object o) {
        if (!(o instanceof Account)) {
            return false;
        }
        Account other = (Account) o;
        return other.getAccountNumber().equals(getAccountNumber());
    }

    public String toString() {
        return getAccountNumber().toString() + ": " + getAccountName();
    }

    public int hashCode() {
        return getAccountNumber().intValue();
    }

    public String getAccountName() {
        return name;
    }

    public Integer getAccountNumber() {
        return number;
    }
}
