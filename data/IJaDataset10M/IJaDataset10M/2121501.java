package edu.ecnu.aspect;

public class BankAccount {

    private String accountId = null;

    public BankAccount(String accountId) {
        this.accountId = accountId;
    }

    public String getAccountId() {
        return accountId;
    }

    public void setAccountId(String accountId) {
        this.accountId = accountId;
    }

    @Pointcut(name = "doAudit", advice = "before")
    public void deposit(float amount) {
        System.out.println("Deposit: " + amount);
    }

    @Pointcut(name = "doAudit", advice = "before")
    public void transferFund(float amount, String targetAccountId) {
        System.out.println("Transfer " + amount + " to " + targetAccountId);
    }

    public float getBalance() {
        float balance = 0;
        return balance;
    }
}
