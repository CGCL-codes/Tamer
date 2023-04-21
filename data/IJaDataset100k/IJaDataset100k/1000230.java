package com.dsrsc.impl.model;

import com.dsrsc.model.Inventory;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Lothy
 */
public class PlayerInventory implements Inventory {

    public static final int DEFAULT_SIZE = 30;

    private final int maxSize;

    private final List<InvItem> items;

    public PlayerInventory() {
        this(DEFAULT_SIZE);
    }

    public PlayerInventory(int maxSize) {
        items = new ArrayList<InvItem>();
        this.maxSize = maxSize;
    }

    public int add(InvItem item) {
        return 0;
    }

    public int remove(InvItem item) {
        return 0;
    }

    public int remove(InvItem item, int amount) {
        return 0;
    }

    public int size() {
        return 0;
    }

    public InvItem get(InvItem item) {
        return null;
    }

    public List<InvItem> getItems() {
        return null;
    }

    public boolean canHold(InvItem item) {
        return false;
    }

    public boolean contains(InvItem item) {
        return false;
    }

    public boolean full() {
        return false;
    }
}
