package com.ham.mud.characters;

/**
 * Created by hlucas on Jun 29, 2011 at 9:41:13 AM
 */
public class Experience {

    int count;

    public Experience(int count) {
        this.count = count;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public void add(int amount) {
        this.count += amount;
    }
}
