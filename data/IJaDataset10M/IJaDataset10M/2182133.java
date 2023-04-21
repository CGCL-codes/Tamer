package org.carlmanaster.allelogram.model;

public class Bin implements Comparable<Bin> {

    private Double lo;

    private Double hi;

    public Bin(double lo, double hi) {
        this.lo = lo;
        this.hi = hi;
    }

    public boolean contains(double x) {
        if (x < lo) return false;
        if (x >= hi) return false;
        return true;
    }

    public boolean contains(Allele allele) {
        return contains(allele.getAdjustedValue());
    }

    public boolean equals(Object obj) {
        if (obj == null) return false;
        if (obj == this) return true;
        if (!(obj instanceof Bin)) return false;
        Bin that = (Bin) obj;
        if (!this.lo.equals(that.lo)) return false;
        if (!this.hi.equals(that.hi)) return false;
        return true;
    }

    public int hashCode() {
        int hash = 1;
        hash = hash * 31 + lo.hashCode();
        hash = hash * 31 + hi.hashCode();
        return hash;
    }

    public String toString() {
        return String.format("(%1.1f, %1.1f)", lo, hi);
    }

    public String toTabDelimitedString() {
        return String.format("%1.1f\t%1.1f", lo, hi);
    }

    public double getLow() {
        return lo;
    }

    public double getHigh() {
        return hi;
    }

    public void setLow(double d) {
        lo = d;
    }

    public void setHigh(double d) {
        hi = d;
    }

    private double width() {
        return hi - lo;
    }

    private double center() {
        return (hi + lo) / 2;
    }

    public double fit(double d) {
        return Math.abs(d - center()) / (width() / 2);
    }

    public double fit(Allele allele) {
        return fit(allele.getAdjustedValue());
    }

    public int compareTo(Bin that) {
        return (int) (this.lo - that.lo);
    }
}
