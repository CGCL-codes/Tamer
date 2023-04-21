package org.chires.stat;

import javax.management.j2ee.statistics.BoundaryStatistic;

/**
 * @author Greg Hinkle (ghinkle@users.sourceforge.net), Sep 12, 2004
 * @version $Revision: 1.1.1.1 $($Author: ghinkl $ / $Date: 2004/11/12 03:47:39 $)
 */
public class ChiresBoundaryStatistic extends ChiresStatistic implements BoundaryStatistic {

    long upperBound;

    long lowerBound;

    public long getUpperBound() {
        return upperBound;
    }

    public void setUpperBound(long upperBound) {
        this.upperBound = upperBound;
    }

    public long getLowerBound() {
        return lowerBound;
    }

    public void setLowerBound(long lowerBound) {
        this.lowerBound = lowerBound;
    }

    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ChiresBoundaryStatistic)) return false;
        if (!super.equals(o)) return false;
        final ChiresBoundaryStatistic chiresBoundaryStatistic = (ChiresBoundaryStatistic) o;
        if (lowerBound != chiresBoundaryStatistic.lowerBound) return false;
        if (upperBound != chiresBoundaryStatistic.upperBound) return false;
        return true;
    }

    public int hashCode() {
        int result = super.hashCode();
        result = 29 * result + (int) (upperBound ^ (upperBound >>> 32));
        result = 29 * result + (int) (lowerBound ^ (lowerBound >>> 32));
        return result;
    }
}
