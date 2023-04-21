package edu.jas.root;

import java.util.List;
import java.util.ArrayList;
import edu.jas.arith.BigDecimal;
import edu.jas.arith.BigRational;
import edu.jas.arith.Rational;
import edu.jas.structure.ElemFactory;
import edu.jas.structure.RingElem;
import edu.jas.structure.GcdRingElem;
import edu.jas.structure.RingFactory;

/**
 * RealAlgebraicNumber root tuple.
 * @param <C> coefficient type.
 * @author Heinz Kredel
 */
public class RealRootTuple<C extends GcdRingElem<C> & Rational> {

    /**
     * Tuple of RealAlgebraicNumbers.
     */
    public final List<RealAlgebraicNumber<C>> tuple;

    /**
     * Constructor.
     * @param t list of roots.
     */
    public RealRootTuple(List<RealAlgebraicNumber<C>> t) {
        if (t == null) {
            throw new IllegalArgumentException("null tuple not allowed");
        }
        tuple = t;
    }

    /**
     * String representation of tuple.
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return tuple.toString();
    }

    /**
     * Get a scripting compatible string representation.
     * @return script compatible representation for this Rectangle.
     */
    public String toScript() {
        StringBuffer sb = new StringBuffer("[");
        boolean first = true;
        for (RealAlgebraicNumber<C> r : tuple) {
            if (first) {
                first = false;
            } else {
                sb.append(",");
            }
            sb.append(r.toScript());
        }
        return sb.toString();
    }

    /**
     * Clone this.
     * @see java.lang.Object#clone()
     */
    @Override
    public RealRootTuple<C> clone() {
        return new RealRootTuple<C>(new ArrayList<RealAlgebraicNumber<C>>(tuple));
    }

    /**
     * Comparison with any other object.
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    @SuppressWarnings("unchecked")
    public boolean equals(Object b) {
        if (!(b instanceof RealRootTuple)) {
            return false;
        }
        RealRootTuple<C> a = null;
        try {
            a = (RealRootTuple<C>) b;
        } catch (ClassCastException e) {
        }
        return tuple.equals(a.tuple);
    }

    /**
     * Hash code for this Rectangle.
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {
        return tuple.hashCode();
    }

    /**
     * Rational approximation of each coordinate.
     * @return list of coordinate points.
     */
    public List<BigRational> getRational() {
        List<BigRational> center = new ArrayList<BigRational>(tuple.size());
        for (RealAlgebraicNumber<C> rr : tuple) {
            BigRational r = rr.getRational();
            center.add(r);
        }
        return center;
    }

    /**
     * Decimal approximation of each coordinate.
     * @return list of coordinate points.
     */
    public List<BigDecimal> decimalMagnitude() {
        List<BigDecimal> center = new ArrayList<BigDecimal>(tuple.size());
        for (RealAlgebraicNumber<C> rr : tuple) {
            BigDecimal r = rr.decimalMagnitude();
            center.add(r);
        }
        return center;
    }

    /**
     * Rational Length.
     * @return max |v_i|;
     */
    public BigRational rationalLength() {
        BigRational len = new BigRational();
        for (RealAlgebraicNumber<C> rr : tuple) {
            BigRational r = rr.ring.root.rationalLength();
            int s = len.compareTo(r);
            if (s < 0) {
                len = r;
            }
        }
        return len;
    }

    /**
     * Signum.
     * @return ?;
     */
    public int signum() {
        int s = 0;
        for (RealAlgebraicNumber<C> rr : tuple) {
            int rs = rr.signum();
            if (rs != 0) {
                s = rs;
            }
        }
        return s;
    }
}
