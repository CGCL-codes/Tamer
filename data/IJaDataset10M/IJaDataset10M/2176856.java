package org.rscdaemon.ls.model;

public class Item {

    private int id;

    private int amount;

    public Item(int id, int amount) {
        this.id = id;
        this.amount = amount;
    }

    public int getID() {
        return id;
    }

    public int getAmount() {
        return amount;
    }
}
