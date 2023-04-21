package com.ibm.wala.logic;

public class DoubleConstant extends NumberConstant {

    private final double val;

    protected DoubleConstant(double val) {
        this.val = val;
    }

    public static DoubleConstant make(double val) {
        return new DoubleConstant(val);
    }

    @Override
    public Number getVal() {
        return val;
    }

    @Override
    public String toString() {
        return String.valueOf(val);
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        long temp;
        temp = Double.doubleToLongBits(val);
        result = prime * result + (int) (temp ^ (temp >>> 32));
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null) return false;
        if (getClass() != obj.getClass()) return false;
        final DoubleConstant other = (DoubleConstant) obj;
        if (Double.doubleToLongBits(val) != Double.doubleToLongBits(other.val)) return false;
        return true;
    }
}
