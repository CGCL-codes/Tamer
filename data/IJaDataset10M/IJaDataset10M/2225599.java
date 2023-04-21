package com.obdobion.algebrain;

import java.util.Hashtable;

/**
 * @author  degreefc
 */
public class TestEquationsSupport implements EquationSupport {

    private Hashtable variables;

    public TestEquationsSupport() {
        super();
        setVariables(new Hashtable());
    }

    public void setVariables(java.util.Hashtable newVariables) {
        variables = newVariables;
    }

    public java.util.Hashtable getVariables() {
        return variables;
    }

    @SuppressWarnings("unchecked")
    public Hashtable resolveRate(String tableName, java.sql.Date baseDate, double tableKey) throws EquException {
        Hashtable rates = new Hashtable();
        rates.put(new Double(0), new Double(1));
        return rates;
    }

    public double resolveRate(String tableId, java.sql.Date effectiveDate, String key1, String key2, String key3, String key4, String key5) throws EquException {
        return 1D;
    }

    public Object resolveVariable(String variableName, java.sql.Date baseDate) throws EquException {
        if (getVariables().get(variableName) instanceof String) return (String) getVariables().get(variableName); else return (Double) getVariables().get(variableName);
    }
}
