package com.schinzer.fin.basic;

/**
 * @author cschinzer
 *
 */
public class PaymentTransaction extends Transaction {

    private String counterpart;

    @Override
    public float getBalance() {
        return this.getAmount();
    }

    public void setAmount(String type, int amount) throws TransactionTypeUndefinedException {
        if (type.equals("H")) {
            this.setAmount(amount);
        } else if (type.equals("S")) {
            this.setAmount(-amount);
        } else {
            throw new TransactionTypeUndefinedException("Transaction type is undefined for type PaymentTransaction: " + type);
        }
    }

    public String getCounterpart() {
        return counterpart;
    }

    public void setCounterpart(String counterpart) {
        this.counterpart = counterpart;
    }
}
