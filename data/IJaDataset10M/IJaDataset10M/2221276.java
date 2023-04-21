package com.google.doclava;

public class Sorter implements Comparable {

    public String label;

    public Object data;

    public Sorter(String l, Object d) {
        label = l;
        data = d;
    }

    public int compareTo(Object other) {
        return label.compareToIgnoreCase(((Sorter) other).label);
    }
}
