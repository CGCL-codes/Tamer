package org.tzi.use.uml.mm;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.tzi.use.util.StringUtil;

/** 
 * A Multiplicity specifies possible cardinality values for instances
 * participating in associations.
 *
 * @version     $ProjectVersion: 0.393 $
 * @author  Mark Richters
 */
public final class MMultiplicity {

    /**
     * A value representing an unlimited upper bound (that is `*').
     */
    public static final int MANY = -1;

    public static final int UNDEFINED = -2;

    /**
     * Convenience object for the multiplicity `0..*'.
     */
    public static final MMultiplicity ZERO_MANY = new MMultiplicity(0, MANY);

    /**
     * Convenience object for the multiplicity `1..*'.
     */
    public static final MMultiplicity ONE_MANY = new MMultiplicity(1, MANY);

    /**
     * Convenience object for the multiplicity `1'.
     */
    public static final MMultiplicity ONE = new MMultiplicity(1, 1);

    /**
     * Convenience object for the multiplicity `0..1'.
     */
    public static final MMultiplicity ZERO_ONE = new MMultiplicity(0, 1);

    public final class Range {

        int fLower;

        int fUpper;

        Range(int lower, int upper) {
            if (upper != MANY && upper != UNDEFINED && (lower > upper || lower < 0 || upper < 1)) throw new IllegalArgumentException("Invalid multiplicity range `" + lower + ".." + upper + "'.");
            if (lower == MANY || lower == UNDEFINED) lower = 0;
            fLower = lower;
            fUpper = upper;
        }

        public int getUpper() {
            return fUpper;
        }

        public int getLower() {
            return fLower;
        }

        public void setUpper(int upper) {
            if (MMultiplicity.getBiggerNumber(fLower, upper) == fLower) throw new IllegalArgumentException("Resulting range is invalid '" + fLower + ".." + upper + "'");
            fUpper = upper;
        }

        public void setLower(int lower) {
            if (MMultiplicity.getSmallerNumber(lower, fUpper) == fUpper) throw new IllegalArgumentException("Resulting range is invalid '" + lower + ".." + fUpper + "'");
            fLower = lower;
        }

        /**
         * Test if range contains a specified value.
         */
        public boolean contains(int n) {
            return (fLower <= n || n == MANY) && (fUpper == MANY || (n <= fUpper && n != MANY));
        }

        /**
         * Test if range contains values greater one.
         */
        public boolean isGreaterOne() {
            return fUpper > 1 || fUpper == MANY;
        }

        public String toString() {
            if (fUpper == MANY) if (fLower == 0) return "*"; else return fLower + "..*"; else if (fLower == fUpper) return Integer.toString(fUpper); else return fLower + ".." + fUpper;
        }

        public boolean contains(Range other) {
            if (this.contains(other.getLower()) && this.contains(other.getUpper())) return true;
            return false;
        }

        public boolean lowerSmallerThan(int i) {
            return this.fLower < i;
        }

        public boolean upperBiggerThan(int i) {
            if (this.fUpper == MANY) return true;
            if (i == MANY) return false;
            return this.fUpper > i;
        }
    }

    private ArrayList<Range> mRanges;

    /**
     * Creates a new multiplicity. You need to add ranges before the
     * multiplicity is actually valid.
     */
    public MMultiplicity() {
        mRanges = new ArrayList<Range>();
    }

    /**
     * Creates a multiplicity with given range.
     */
    public MMultiplicity(int lower, int upper) {
        this();
        addRange(lower, upper);
    }

    /**
     * Adds a range to this multiplicity.
     *
     * @exception IllegalArgumentException if the interval is illegal.
     */
    public void addRange(int lower, int upper) {
        mRanges.add(new Range(lower, upper));
    }

    public List<Range> getRanges() {
        return mRanges;
    }

    /**
     * Tests whether this multiplicity contains a specified value.
     */
    public boolean contains(int n) {
        Iterator<Range> it = mRanges.iterator();
        while (it.hasNext()) {
            Range r = (Range) it.next();
            if (r.contains(n)) return true;
        }
        return false;
    }

    public boolean contains(int low, int high) {
        Iterator<Range> it = mRanges.iterator();
        while (it.hasNext()) {
            Range r = (Range) it.next();
            if (r.contains(low) && r.contains(high)) return true;
        }
        return false;
    }

    public boolean contains(Range r) {
        return contains(r.getLower(), r.getUpper());
    }

    /**
     * Returns true if this multiplicity denotes a collection of
     * objects, i.e., the maximal upper range value is greater than
     * one.
     */
    public boolean isCollection() {
        Iterator<Range> it = mRanges.iterator();
        while (it.hasNext()) {
            Range r = (Range) it.next();
            if (r.isGreaterOne()) return true;
        }
        return false;
    }

    public String toString() {
        return StringUtil.fmtSeq(mRanges.iterator(), ",");
    }

    /**
     * Method for returning the range of the current multiplicity object
     * The main assumption is that there is only one range 
     * @return the range
     */
    public Range getRange() {
        return mRanges.get(0);
    }

    /**
     * Tests if this Multiplicity contains other Multiplicity by finding for each <code>Range</code> in 
     * other Multiplicity some
     * <code>Range</code> that contains it in this Multiplicity.
     * @param other - MMultiplicity object
     * @return true if <code>other</code> is contained in <code>this</code>, and false otherwise.
     */
    public boolean contains(MMultiplicity other) {
        boolean yes = true;
        for (Range r : other.mRanges) {
            yes &= this.contains(r);
        }
        return yes;
    }

    @Override
    public MMultiplicity clone() {
        MMultiplicity m = new MMultiplicity();
        for (Range r : mRanges) {
            m.addRange(r.fLower, r.fUpper);
        }
        return m;
    }

    /**
     * Returns strict range from the given two ranges.
     * Strict range is the range that conforms to both ranges 
     * (low - highest between lowers, high - lowest between highers && lower < higher)
     * @return strict range
     * @throws IllegalArgumentException if strict range is illegal.
     */
    public static Range getStrictRange(Range r1, Range r2) throws IllegalArgumentException {
        int lower, upper;
        MMultiplicity m;
        lower = (r1.lowerSmallerThan(r2.getLower())) ? r2.getLower() : r1.getLower();
        upper = (r1.upperBiggerThan(r2.getUpper())) ? r2.getUpper() : r1.getUpper();
        m = new MMultiplicity(lower, upper);
        return m.getRange();
    }

    public static int getBiggerNumber(int n1, int n2) {
        if (n1 == MANY) return n1;
        if (n2 == MANY) return n2;
        if (n1 == UNDEFINED) return n2;
        if (n2 == UNDEFINED) return n1;
        return (n1 > n2) ? n1 : n2;
    }

    public static int getSmallerNumber(int n1, int n2) {
        if (n1 == UNDEFINED) return n2;
        if (n2 == UNDEFINED) return n1;
        if (getBiggerNumber(n1, n2) == n1) return n2;
        return n1;
    }
}
