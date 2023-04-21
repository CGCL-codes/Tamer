package com.jeantessier.classreader;

public class GroupData {

    private String name;

    private int size;

    private int count;

    public GroupData(String name, int size) {
        this.name = name;
        this.size = size;
        this.count = 0;
    }

    public String getName() {
        return name;
    }

    public int getSize() {
        return size;
    }

    public int getCount() {
        return count;
    }

    public void incrementCount() {
        count++;
    }

    public String toString() {
        StringBuffer result = new StringBuffer();
        result.append("Group \"" + getName() + "\" (" + getCount() + "/" + getSize() + ")");
        return result.toString();
    }
}
