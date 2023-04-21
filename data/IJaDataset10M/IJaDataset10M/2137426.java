package org.reward4j.model;

/**
 * A very simple implementation of the {@link Amount} interface.
 * 
 * @author hillger.t
 */
public class SimpleAmount implements Amount {

    private double amount = 0d;

    @Override
    public double getAmount() {
        return amount;
    }

    @Override
    public void setAmount(double amount) {
        this.amount = amount;
    }
}
