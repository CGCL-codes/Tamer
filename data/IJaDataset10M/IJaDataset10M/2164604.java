package com.zeromessenger.entity;

import java.util.Vector;

public class BuddyList {

    private Vector v;

    public BuddyList() {
        v = new Vector();
    }

    public void addGroupToList(BuddyGroup gp) {
        v.add(gp);
    }

    public void removeGroupFromList(BuddyGroup gp) {
        v.remove(gp);
    }

    public Vector GetBuddyList() {
        return v;
    }

    public void printBuddyList() {
        System.out.println("Printing buddy list");
        for (int i = 0; i < v.size(); i++) {
            ((BuddyGroup) v.get(i)).printBuddyInGroup();
        }
    }
}
