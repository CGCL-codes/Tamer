package com.trackerdogs.testing;

public abstract class Test {

    public void setUp() {
    }

    public void tearDown() {
    }

    /**********************************************************************
     * Each equals method should be reflexive, symmetric, transitive,
     * consistent and return false with non-null vs null value 
     * comparisation.
     *
     * This method tests those equivalences.
     * Use in code:
     * <code>
     *  // val1, val2, val3 different values
     *	String eq = equalsEquivalence(val1, val2, val3); 
     *	if(eq != null) {
     *	    return eq;
     *	}
     * </code>
     */
    public String equalsEquivalence(Object x, Object y, Object z) {
        String error;
        error = equalsEquivalenceSingle(x, y, z);
        if (error != null) return error;
        error = equalsEquivalenceSingle(z, x, y);
        if (error != null) return error;
        error = equalsEquivalenceSingle(z, y, x);
        if (error != null) return error;
        error = equalsEquivalenceSingle(x, x, y);
        if (error != null) return error;
        error = equalsEquivalenceSingle(z, y, z);
        if (error != null) return error;
        error = equalsEquivalenceSingle(x, z, z);
        if (error != null) return error;
        error = equalsEquivalenceSingle(x, x, x);
        if (error != null) return error;
        error = equalsEquivalenceSingle(y, y, y);
        if (error != null) return error;
        error = equalsEquivalenceSingle(z, z, z);
        if (error != null) return error;
        return null;
    }

    private String equalsEquivalenceSingle(Object x, Object y, Object z) {
        if (!x.equals(x)) {
            return "not reflexive";
        }
        if (!((x.equals(y) && y.equals(x)) || (!x.equals(y) && !y.equals(x)))) {
            return "not symmetric";
        }
        if (!((x.equals(y) && y.equals(z) && x.equals(z)) || !(x.equals(y) && y.equals(z)))) {
            return "not transitive";
        }
        boolean startEq = x.equals(y);
        for (int i = 0; i < 5; i++) {
            if (x.equals(y) != startEq) {
                return "not consistent";
            }
        }
        if (x != null && x.equals(null)) {
            return "non-null reference error";
        }
        return null;
    }
}
