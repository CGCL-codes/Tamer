package de.tum.in.botl.math.implementation;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * @author Frank
 *
 */
public class Solution {

    private Map values;

    private Map multipleSolutions;

    boolean hasMultipleSolutions;

    protected Solution() {
        this.values = new HashMap();
        this.multipleSolutions = new HashMap();
        hasMultipleSolutions = false;
    }

    protected void addSolution(String varName, String value, boolean multiple) {
        values.put(varName, value);
        multipleSolutions.put(varName, new Boolean(multiple));
        if (multiple) hasMultipleSolutions = true;
    }

    public String getSolution(String varName) {
        Object res = values.get(varName);
        if (res instanceof String) return (String) res; else return null;
    }

    public boolean hasMultipleSolutions(String varName) {
        return ((Boolean) multipleSolutions.get(varName)).booleanValue();
    }

    public boolean hasMultipleSolutions() {
        return hasMultipleSolutions;
    }

    public Set getVarNames() {
        return this.values.keySet();
    }
}
