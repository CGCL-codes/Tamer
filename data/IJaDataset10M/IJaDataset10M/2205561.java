package com.medcentrex.interfaces;

import java.lang.String;
import java.lang.Integer;
import java.util.Collection;
import java.util.Vector;

public class ReconcileBean {

    private PeopleEntityData person;

    private Register_TransactionEntityData register_transaction;

    private Collection claims;

    private double usedmoney = 0;

    public ReconcileBean() {
    }

    public PeopleEntityData getPerson() {
        return person;
    }

    public void setPerson(PeopleEntityData Person) {
        person = Person;
    }

    public Register_TransactionEntityData getRegister_Transaction() {
        return register_transaction;
    }

    public void setRegister_Transaction(Register_TransactionEntityData Register_Transaction) {
        register_transaction = Register_Transaction;
    }

    public Collection getClaims() {
        return claims == null ? (Collection) new Vector() : claims;
    }

    public void setClaims(String[] Claims) {
        claims = (Collection) new Vector();
        for (int i = 0; i < Claims.length; i++) {
            claims.add(Claims[i]);
        }
    }

    public void setClaims(Collection Claims) {
        claims = Claims;
    }

    public double getUsedMoney() {
        return usedmoney;
    }

    public void addUsedMoney(double Money) {
        usedmoney += Money;
    }
}
