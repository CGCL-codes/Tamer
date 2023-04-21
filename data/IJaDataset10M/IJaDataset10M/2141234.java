package playground.dressler.Interval;

import playground.dressler.ea_flow.BreadCrumb;
import playground.dressler.ea_flow.PathStep;

public class VertexIntervalWithCost extends VertexInterval {

    /**
	 * relative gives time t cost = t + cost;
	 * absolute gives time t cost = cost;
	 * for an interval of length 1, relative cost 0 is the same as absolute cost lowbound 
	 */
    public boolean costIsRelative = true;

    public int cost = 0;

    public VertexIntervalWithCost() {
        super();
    }

    public VertexIntervalWithCost(final int l, final int r) {
        super(l, r);
    }

    /**
	 * construct an VertexInterval from l to r with the settings of other
	 * the Predecessor will be shifted to the new value of l
	 * @param l lowbound
	 * @param r highbound
	 * @param other Interval to copy settings from
	 */
    public VertexIntervalWithCost(final int l, final int r, final VertexIntervalWithCost other) {
        super(l, r);
        this.reachable = other.reachable;
        this.scanned = other.scanned;
        this._pred = other._pred;
        this._succ = other._succ;
        this.cost = other.cost;
        this.costIsRelative = other.costIsRelative;
    }

    /**
	 * creates a VertexInterval instance as a copy of an Interval
	 * not reachable, not scanned, no predecessor
	 * @param j Interval to copy
	 */
    public VertexIntervalWithCost(final Interval j) {
        super(j.getLowBound(), j.getHighBound());
    }

    public VertexIntervalWithCost(final VertexIntervalWithCost j) {
        super(j.getLowBound(), j.getHighBound());
        this.reachable = j.reachable;
        this.scanned = j.scanned;
        this._pred = j._pred;
        this._succ = j._succ;
        this.cost = j.cost;
        this.costIsRelative = j.costIsRelative;
    }

    /**
	 * Should the other VertexInterval be replaced with this?
	 * It is assumed that this is reachable! Why else would you call this function? 
	 * Note that in intricate situations of being better "here and there",
	 * the method has to be called on subintervals after the first returned interval again.
	 * CAVE: This version that understands costs is not compatible with the mixed search!     
	 * @param other a VertexInterval
	 * @return null if not, and the first subinterval of other that should be replaced otherwise 
	 */
    @Override
    public Interval isBetterThan(final VertexInterval other) {
        VertexIntervalWithCost temp = (VertexIntervalWithCost) other;
        boolean isbetter = false;
        int l = Math.max(this._l, temp._l);
        int r = Math.min(this._r, temp._r);
        if (l >= r) return null;
        if (!temp.reachable) {
            isbetter = true;
        } else {
            if (this.costIsRelative == temp.costIsRelative) {
                if (this.cost < temp.cost) {
                    isbetter = true;
                }
            } else {
                if (this.costIsRelative) {
                    int better_r = temp.cost - this.cost;
                    r = Math.min(r, better_r);
                    isbetter = true;
                } else {
                    int better_l = this.cost - temp.cost + 1;
                    l = Math.max(l, better_l);
                    isbetter = true;
                }
            }
        }
        if (isbetter) {
            if (l < r) {
                return new Interval(l, r);
            }
        }
        return null;
    }

    /**
	 * Set the fields of the VertexInterval reachable true, scanned false, and the _pred to pred
	 * This performs no checks at all!
	 * Note that this is not suitable for the Reverse search anymore! 
	 * @param pred which is set as predecessor. It is never shifted anymore.
	 */
    public void setArrivalAttributesForward(final BreadCrumb pred) {
        this.scanned = false;
        this.reachable = true;
        this._pred = pred;
    }

    /**
	 * Set the fields of the VertexInterval reachable true, scanned false, and the _pred to pred
	 * This performs no checks at all!
	 * @param succ which is set as successor. It is never shifted anymore.
	 */
    public void setArrivalAttributesReverse(final BreadCrumb succ) {
        this.scanned = false;
        this.reachable = true;
        this._succ = succ;
    }

    /**
	 * Set the fields of the VertexInterval to the one given.
	 * Predecessor or Successor are only updated if they are not null. 
	 * @param other The VertexInterval from which the settings are copied
	 * @return if there is an unusual reason to scan again ... with costs, this is not checked and simply returns true all the time!  
	 */
    @Override
    public boolean setArrivalAttributes(final VertexInterval other) {
        if (!(other instanceof VertexIntervalWithCost)) return super.setArrivalAttributes(other);
        VertexIntervalWithCost temp = (VertexIntervalWithCost) other;
        this.scanned = temp.scanned;
        this.reachable = temp.reachable;
        if (temp._pred != null) this._pred = temp._pred;
        if (temp._succ != null) this._succ = temp._succ;
        this.costIsRelative = temp.costIsRelative;
        this.cost = temp.cost;
        return true;
    }

    /**
	 * Can this VertexInterval be combined with other?
	 * Times or interval bounds are not checked
	 * except for intervals of length 1, where relative and absolute costs behave the same
	 * Note that the results are not transitive, that is, check only two at a time an join them afterwards!
	 * Do not scan an entire list for the largest joinable part.  
	 * @param other VertexInterval to compare to
	 * @return true iff the intervalls agree on their arrival properties
	 */
    @Override
    public boolean continuedBy(final VertexInterval o) {
        throw new UnsupportedOperationException("Cannot continue VIs at the moment.");
    }

    @Override
    public VertexIntervalWithCost splitAt(int t) {
        Interval j = super.splitAt(t);
        VertexIntervalWithCost k = new VertexIntervalWithCost(j.getLowBound(), j.getHighBound(), this);
        return k;
    }

    /**
	 * Does this VertexIntervalWithCost match the given cost?
	 * Or could it be represented as such?
	 * @param c the constant part
	 * @param rel if the cost is relative (increasing with slope 1) 
	 */
    public boolean isSameCost(int c, boolean rel) {
        if (this.costIsRelative == rel) {
            return cost == c;
        } else {
            if (this.length() != 1) {
                return false;
            } else {
                if (rel) {
                    return c == this.cost - this._l;
                } else {
                    return c == this._l + this.cost;
                }
            }
        }
    }

    public int getAbsoluteCost(int t) {
        if (!this.costIsRelative) {
            return this.cost;
        } else {
            return t + this.cost;
        }
    }

    @Override
    public VertexIntervalWithCost getIntervalCopy() {
        VertexIntervalWithCost result = new VertexIntervalWithCost(this._l, this._r, this);
        return result;
    }

    public String toString() {
        return super.toString() + " cost: " + this.cost + " cost is relative: " + this.costIsRelative;
    }
}
