package jscript.expression.operator;

import jscript.expression.*;

public class OpUnary extends OperatorReal {

    public OpUnary(Expression value) {
        super(value, value);
    }

    protected double operate(double d1, double d2) {
        return -d2;
    }

    protected float operate(float f1, float f2) {
        return -f2;
    }

    protected int operate(int i1, int i2) {
        return -i2;
    }

    protected long operate(long l1, long l2) {
        return -l2;
    }

    public String toString() {
        return "operator -";
    }
}
