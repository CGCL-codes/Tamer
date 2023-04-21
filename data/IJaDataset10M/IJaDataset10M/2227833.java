package org.borlis.mvpsimplecrm.server.logic.org.borlis.base.core;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class Account {

    private String bank_id;

    private String account_number;

    private BigDecimal balance;

    private Collection<AbstractHuman> humans = new ArrayList<AbstractHuman>();

    private Map<Account, AbstractHuman> accountHumanMap = new ConcurrentHashMap<Account, AbstractHuman>();

    private Map<Bank, AbstractHuman> bankHumanMap = new ConcurrentHashMap<Bank, AbstractHuman>();

    public void setHumans(Collection<AbstractHuman> humans) {
        this.humans = humans;
    }

    public Collection<AbstractHuman> getHumans() {
        return this.humans;
    }

    public void setAccountHumanMap(Map<Account, AbstractHuman> accountHumanMap) {
        this.accountHumanMap = accountHumanMap;
    }

    public Map<Account, AbstractHuman> getAccountHumanMap() {
        return this.accountHumanMap;
    }

    public void setBankHumanMap(Map<Bank, AbstractHuman> bankHumanMap) {
        this.bankHumanMap = bankHumanMap;
    }

    public Map<Bank, AbstractHuman> getBankHumanMap() {
        return this.bankHumanMap;
    }

    public Account() {
    }

    public Account(String bank, String number, BigDecimal balance) {
        this.account_number = number;
        this.bank_id = bank;
        this.balance = balance;
    }

    public String getBank_id() {
        return bank_id;
    }

    public void setBank_id(String bank_id) {
        this.bank_id = bank_id;
    }

    public String getAccount_number() {
        return account_number;
    }

    public void setAccount_number(String account_number) {
        this.account_number = account_number;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }
}
