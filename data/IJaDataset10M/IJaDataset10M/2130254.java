package net.chrisrichardson.bankingExample.domain;

import java.util.Date;

public class TransferTransaction implements BankingTransaction {

    private int id = -1;

    private Account fromAccount;

    private Account toAccount;

    private double amount;

    private Date date;

    TransferTransaction() {
    }

    public int getId() {
        return id;
    }

    public TransferTransaction(Account fromAccount, Account toAccount, double amount, Date date) {
        this.fromAccount = fromAccount;
        this.toAccount = toAccount;
        this.amount = amount;
        this.date = date;
    }

    public double getAmount() {
        return amount;
    }

    public Date getDate() {
        return date;
    }

    public Account getFromAccount() {
        return fromAccount;
    }

    public Account getToAccount() {
        return toAccount;
    }
}
