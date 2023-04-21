package com.wwflgames.za.item;

public class Bandage extends StackableItem {

    public static final String ITEM_NAME = "Bandage";

    public Bandage() {
        super(ITEM_NAME, false, true);
    }

    public Bandage(int cnt) {
        this();
        setQuantity(cnt);
    }

    @Override
    public String getStackingQualifier() {
        return this.getName();
    }
}
