package xal.tools.math;

/**
 * Represents closed interval <i>I</i> of the real line.  Thus,
 * <i>I</i> has the form <i>I</i> = [<i>x</i><sub>0</sub>,<i>x</i><sub>1</sub>]
 * where <i>x</i><sub>0</sub> &le; <i>x</i><sub>1</sub> are real numbers.
 * 
 * @author Christopher K. Allen
 *
 */
public class ClosedInterval extends Interval {

    /** Serialization identifier */
    private static final long serialVersionUID = 1L;

    /** 
     * Default constructor - creates a new instance of Interval with one point, 
     * the origin.
     */
    public ClosedInterval() {
        super();
    }

    /**
     * Initializing constructor - creates a single point (zero length) interval
     * given by the value of the argument <code>pt</code>.
     * @param pt
     */
    public ClosedInterval(double pt) {
        super(pt, pt);
    }

    /**
     * Initializing constructor - create a new open interval with specified 
     * endpoints.
     *
     *  @param  min     left endpoint
     *  @param  max     right endpoint
     */
    public ClosedInterval(double min, double max) {
        super(min, max);
    }

    /**
     * Copy constructor - create a new open interval initialized to the argument.
     *  
     * @param   I       interval to copy
     */
    public ClosedInterval(Interval I) {
        super(I);
    }

    /**
     * Is point a member of the closed interval
     *
     * @param  x       point to test for membership
     *
     * @return         true if x is in interval
     */
    public boolean membership(double x) {
        return (getMaxEndPt() >= x) && (getMinEndPt() <= x);
    }

    ;

    /**
     * Is there a nonzero intersection between this interval
     * and the argument.  
     * 
     * @param  I   interval to be tested
     * @return     true if the intervals intersect
     */
    public boolean intersect(ClosedInterval I) {
        return (this.getMaxEndPt() >= I.getMinEndPt()) || (this.getMinEndPt() <= I.getMaxEndPt());
    }

    /**
     * Test whether the given interval is a sub-interval of this
     * interval.
     *
     * @param I         Interval for test
     * @return          True if <i>I</i> is contained in this, false otherwise
     * 
     * @since  Jul 17, 2008
     * @author Christopher K. Allen
     */
    public boolean contains(ClosedInterval I) {
        return (this.getMaxEndPt() >= I.getMaxEndPt() && this.getMinEndPt() <= I.getMinEndPt());
    }

    /**
     * Test whether or given interval is equal to this interval
     * (as intervals, not objects).
     *
     * @param  I       interval object to be checked for equality
     *
     * @return         true if both objects are equal as intervals
     */
    public boolean equals(ClosedInterval I) {
        return (this.getMinEndPt() == I.getMinEndPt()) && (this.getMaxEndPt() == I.getMaxEndPt());
    }

    ;

    /**
     * Return the contents of the interval as a <code>String</code>.
     * 
     * return      string representation of interval
     * 
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return "[" + getMinEndPt() + "," + getMaxEndPt() + "]";
    }
}
