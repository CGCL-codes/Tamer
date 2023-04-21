package com.metanology.mde.core.metaModel.datatypes;

public class UnlimitedInteger {

    /**
     * The unlimited instance of UnlimitedInteger.
     * The value is -1.
     */
    private static UnlimitedInteger UNLIMITED = new UnlimitedInteger(-1);

    /**
     * The positive integer value.
     */
    private int value;

    /**
	 *  Get value.
	 */
    public int getValue() {
        return this.value;
    }

    /**
	 *  Set value.
	 */
    public void setValue(int newVal) {
        this.value = newVal;
    }

    public UnlimitedInteger(int value) {
    }

    public boolean equals(Object o) {
        return false;
    }

    public Object cloneObject(java.util.Map parameters, Object clone) {
        if (clone == null) clone = new UnlimitedInteger(UNLIMITED.getValue());
        java.util.Map objMap = (java.util.Map) parameters.get("model.element");
        if (objMap.get(this) != null) return objMap.get(this);
        ((UnlimitedInteger) clone).value = value;
        objMap.put(this, clone);
        return clone;
    }
}
