package com.guig.maths.language;

import com.guig.maths.Maths;

public class Constante implements IValue {

    private Double value;

    public Double getValue() {
        return value;
    }

    public void setValue(Double value) {
        this.value = value;
    }

    public static IOperande scan(String mask, String maths, Maths maths2) {
        IOperande cc = null;
        int size = 0;
        while (size < maths.length() && (maths.charAt(size) == '1' || maths.charAt(size) == '2' || maths.charAt(size) == '3' || maths.charAt(size) == '4' || maths.charAt(size) == '5' || maths.charAt(size) == '6' || maths.charAt(size) == '7' || maths.charAt(size) == '8' || maths.charAt(size) == '9' || maths.charAt(size) == '0' || maths.charAt(size) == '.')) size++;
        if (size != 0) {
            cc = new Constante();
            ((Constante) cc).setValue(Double.valueOf(maths.substring(0, size)));
        } else cc = X.scan(mask, maths, maths2);
        return cc;
    }

    public boolean isInteger() {
        return value.equals(Double.valueOf(value.intValue() + ""));
    }

    public String toString() {
        if (isInteger()) return value.intValue() + "";
        return value + "";
    }

    public Double compute() {
        return getValue();
    }

    public void update(IValue x) {
    }

    public String arbo(int rang) {
        return toString();
    }
}
