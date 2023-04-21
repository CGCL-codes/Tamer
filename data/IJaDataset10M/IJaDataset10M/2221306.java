package com.strategicgains.jbel.domain;

import java.util.ArrayList;
import java.util.List;

public class TestObject {

    private static final String ONES[] = { "zero", "one", "two", "three", "four", "five", "six", "seven", "eight", "nine" };

    private int intValue;

    private String stringValue;

    private String name;

    private TestObject parent;

    private ArrayList children;

    public TestObject(String name, int intValue, String stringValue) {
        this.name = name;
        this.intValue = intValue;
        this.stringValue = stringValue;
        this.parent = null;
        children = new ArrayList();
    }

    public static List createObjectList(int size) {
        List results = new ArrayList(size);
        for (int i = 0; i < size; ++i) {
            results.add(new TestObject(getName(i), i, String.valueOf(i)));
        }
        return results;
    }

    private static String getName(int i) {
        String value = String.valueOf(i);
        int w = Integer.valueOf(value.substring(value.length() - 1)).intValue();
        return ONES[w];
    }

    public int getIntValue() {
        return intValue;
    }

    public String getName() {
        return name;
    }

    public String getStringValue() {
        return stringValue;
    }

    public TestObject getParent() {
        return parent;
    }

    public void setParent(TestObject parent) {
        this.parent = parent;
    }

    public void addChild(TestObject child) {
        children.add(child);
        child.setParent(this);
    }

    public TestObject getChild(int index) {
        return (TestObject) children.get(index);
    }

    public int getNumberOfChildren() {
        return children.size();
    }
}
