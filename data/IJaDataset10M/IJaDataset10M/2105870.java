package org.maverickdbms.basic;

public class mvDoubleMath implements mvMathInterface {

    public mvString ABS(mvString result, mvConstantString a) {
        result.set(Math.abs(a.getDouble()));
        return result;
    }

    public mvString add(mvString result, mvConstantString a, mvConstantString b) {
        result.set(a.getDouble() + b.getDouble());
        return result;
    }

    public mvString COS(mvString result, mvConstantString a) {
        result.set(Math.cos(a.getDouble()));
        return result;
    }

    public mvString divide(mvString result, mvConstantString a, mvConstantString b) {
        result.set(a.getDouble() / b.getDouble());
        return result;
    }

    public boolean GE(mvConstantString a, mvConstantString b) {
        return (a.getDouble() >= b.getDouble());
    }

    public boolean GT(mvConstantString a, mvConstantString b) {
        return (a.getDouble() > b.getDouble());
    }

    public boolean LE(mvConstantString a, mvConstantString b) {
        return (a.getDouble() <= b.getDouble());
    }

    public mvString LN(mvString result, mvConstantString a) {
        result.set(Math.log(a.getDouble()));
        return result;
    }

    public boolean LT(mvConstantString a, mvConstantString b) {
        return (a.getDouble() < b.getDouble());
    }

    public mvString multiply(mvString result, mvConstantString a, mvConstantString b) {
        result.set(a.getDouble() * b.getDouble());
        return result;
    }

    public mvString RND(mvString result, mvConstantString range) {
        result.set(Math.random() * range.getDouble());
        return result;
    }

    public mvString SIN(mvString result, mvConstantString a) {
        result.set(Math.sin(a.getDouble()));
        return result;
    }

    public mvString SQRT(mvString result, mvConstantString a) {
        result.set(Math.sqrt(a.getDouble()));
        return result;
    }

    public mvString subtract(mvString result, mvConstantString a, mvConstantString b) {
        result.set(a.getDouble() - b.getDouble());
        return result;
    }

    public mvString TAN(mvString result, mvConstantString a) {
        result.set(Math.tan(a.getDouble()));
        return result;
    }
}
