package eu.roelbouwman.peugeotLib.model;

import java.util.Date;

public class Cost {

    protected Date date;

    protected int quantity;

    protected float amount;

    protected String description;

    protected int milage;

    protected String supplier;

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public float getAmount() {
        return amount;
    }

    public void setAmount(float amount) {
        this.amount = amount;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setMilage(int m) {
        milage = m;
    }

    public int getMilage() {
        return milage;
    }

    public String getSupplier() {
        return supplier;
    }

    public void setSupplier(String supplier) {
        this.supplier = supplier;
    }
}
