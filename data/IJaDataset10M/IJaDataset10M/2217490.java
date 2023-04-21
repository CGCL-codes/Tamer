package com.guig.maths.language;

import com.guig.maths.Maths;

public class X implements IValue, IVariable {

    private Double value;

    public Double getValue() {
        return value;
    }

    public void setValue(Double value) {
        this.value = value;
    }

    public IOperande scan(String maths, boolean g) {
        if (maths.charAt(0) == 'x') {
            value = 0.0;
            return this;
        }
        return null;
    }

    public static IOperande scan(String mask, String maths, Maths maths2) {
        IOperande cc = null;
        if (maths.charAt(0) == 'x') {
            cc = new X();
            ((X) cc).setValue(0.0);
            return cc;
        } else cc = Sinus.scan(mask, maths, maths2);
        return cc;
    }

    public String toString() {
        return "x";
    }

    public Double compute() {
        return getValue();
    }

    public void update(IValue x) {
        if (x instanceof X) value = x.getValue();
    }

    public String arbo(int rang) {
        return "x";
    }
}
