package com.ibook;

import java.util.List;

/**
 *
 *
 *
 *
 **/
public class Cart {

    private List<Cheese> cheeses;

    private Address billingAddress = null;

    public Address getBillingAddress() {
        return billingAddress;
    }

    public void setBillingAddress(Address billingAddress) {
        this.billingAddress = billingAddress;
    }

    public List<Cheese> getCheeses() {
        return cheeses;
    }

    public void setCheeses(List<Cheese> cheeses) {
        this.cheeses = cheeses;
    }

    public void add(Cheese selected) {
        this.cheeses.add(selected);
    }

    public float getTotal() {
        float total = 0f;
        for (Cheese cheese : this.cheeses) {
            total += cheese.getPrice();
        }
        return total;
    }
}
