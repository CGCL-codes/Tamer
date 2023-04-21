package edu.java.texbooks.scjp.threads.test13;

public class Account {

    private int balance = 50;

    public Account(int balance) {
        this.balance = balance;
    }

    public Account() {
        this(50);
    }

    public int getBalance() {
        return balance;
    }

    public void withdraw(int amount) {
        balance = balance - amount;
    }
}
