package jmathlib.core.graphics.properties;

import jmathlib.core.graphics.*;
import jmathlib.core.interpreter.*;

public class DoubleStructProperty extends Property {

    private String[] str;

    private double[] values;

    public DoubleStructProperty(PropertySet parent, String name, String[] str, double[] values) {
        super(parent, name);
        if (str.length != values.length) Errors.throwMathLibException("DoubleStrigProperty: size mismatch");
        this.str = str;
        this.values = values;
    }

    public void set(Object newValue) throws PropertyException {
        System.out.println("doubleStruct");
        throw new PropertyException("not implemented");
    }

    public Object get() {
        return getValues();
    }

    public double[] getValues() {
        return values;
    }

    public String toString() {
        String r = "[";
        for (int i = 0; i < values.length; i++) {
            r += " " + str[i] + "=" + values[i] + " ";
        }
        r += "]";
        return r;
    }
}
