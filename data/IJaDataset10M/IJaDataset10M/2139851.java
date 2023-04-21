package org.jrman.parameters;

import org.jrman.grid.Grid;

public class UniformArrayString extends Parameter {

    private final String[] values;

    public UniformArrayString(Declaration declaration, String[] values) {
        super(declaration);
        this.values = values;
    }

    public String getValue(int index) {
        return values[index];
    }

    public Parameter linearInterpolate(float min, float max) {
        return this;
    }

    public Parameter bilinearInterpolate(float uMin, float uMax, float vMin, float vMax) {
        return this;
    }

    public void linearDice(Grid grid) {
        throw new UnsupportedOperationException("Parameter not diceable: " + declaration);
    }

    public void bilinearDice(Grid grid) {
        throw new UnsupportedOperationException("Parameter not diceable: " + declaration);
    }

    public Parameter selectValues(int[] indexes) {
        int n = declaration.getCount();
        if (n == values.length) return this;
        if (n == 1) return new UniformScalarString(declaration, values[indexes[0]]);
        String[] newValues = new String[n];
        for (int i = 0; i < n; i++) newValues[i] = values[indexes[0] + i];
        return new UniformArrayString(declaration, newValues);
    }
}
