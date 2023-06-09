package net.sourceforge.toscanaj.model.manyvaluedcontext.types;

import net.sourceforge.toscanaj.model.manyvaluedcontext.AttributeValue;
import net.sourceforge.toscanaj.model.manyvaluedcontext.ScaleColumn;

public class NumericalType extends TypeImplementation {

    private double minValue;

    private double maxValue;

    private int numOfDecimals;

    public NumericalType(String name) {
        super(name);
    }

    public void addValueGroup(ScaleColumn column, String id) {
        if (column instanceof NumericalValueGroup) {
            scale.addColumn(column, id);
            return;
        }
        throw new RuntimeException("Wrong value group type");
    }

    public void setMinimumValue(double minValue) {
        this.minValue = minValue;
    }

    public void setMaximumValue(double maxValue) {
        this.maxValue = maxValue;
    }

    public void setNumberOfDecimals(int numOfDecimals) {
        this.numOfDecimals = numOfDecimals;
    }

    public double getMinimumValue() {
        return minValue;
    }

    public double getMaximumValue() {
        return maxValue;
    }

    public int getNumOfDecimals() {
        return numOfDecimals;
    }

    public boolean isValidValue(AttributeValue valueToTest) {
        if (!(valueToTest instanceof NumericalValue)) {
            return false;
        }
        NumericalValue numVal = (NumericalValue) valueToTest;
        double value = numVal.getValue();
        if (value >= getMinimumValue() && value <= getMaximumValue()) {
            return true;
        }
        return false;
    }

    public AttributeValue[] getValueRange() {
        return null;
    }
}
