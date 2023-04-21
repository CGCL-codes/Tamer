package com.hibernate;

import java.util.Map;

public class AccountGroup {

    private long id;

    private String name;

    private Map<String, Account> accounts;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Map<String, Account> getAccounts() {
        return accounts;
    }

    public void setAccounts(Map<String, Account> accounts) {
        this.accounts = accounts;
    }

    public int getAccountCount() {
        return this.accounts.size();
    }

    public double getBalance() {
        double bal = 0;
        for (Account acc : this.getAccounts().values()) {
            bal += acc.getBalance();
        }
        return bal;
    }
}
