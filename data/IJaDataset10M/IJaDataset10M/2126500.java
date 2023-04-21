package org.inexact.expression;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class FuzzyAttribute {

    private String name;

    private Map<String, FuzzySet> sets;

    private double minimum;

    private double maximum;

    public FuzzyAttribute() {
        name = null;
        sets = new HashMap<String, FuzzySet>();
        minimum = maximum = 0;
    }

    public FuzzyAttribute(String name) {
        this.name = name;
        sets = new HashMap<String, FuzzySet>();
        minimum = maximum = 0;
    }

    public FuzzyAttribute(String name, double min, double max) {
        this.name = name;
        sets = new HashMap<String, FuzzySet>();
        minimum = min;
        maximum = max;
    }

    public void addFuzzySet(String name, FuzzySet set) {
        sets.put(set.getName(), set);
    }

    public void removeFuzzySet(String name) {
        sets.remove(name);
    }

    public FuzzySet getFuzzySet(String name) {
        return sets.get(name);
    }

    public String[] getFuzzySetNames() {
        Set<String> keys = sets.keySet();
        String[] names = new String[keys.size()];
        int count = 0;
        for (String name : keys) {
            names[count] = name;
            count++;
        }
        return names;
    }

    public int fuzzySetCount() {
        return sets.size();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDiscouseUniverse(double min, double max) {
        minimum = min;
        maximum = max;
    }

    public double getMinimum() {
        return minimum;
    }

    public double getMaximum() {
        return maximum;
    }
}
