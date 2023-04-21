package org.berlin.lang.octane.type;

public class ONumber extends OBaseType {

    private double value;

    public ONumber(final double val) {
        this.value = val;
    }

    @Override
    public Object getValue() {
        return this.value;
    }

    @Override
    public int getType() {
        return TypeConstants.NUMBER;
    }
}
