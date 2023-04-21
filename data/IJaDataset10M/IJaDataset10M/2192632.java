package org.jquantlib.cashflow;

import org.jquantlib.daycounters.DayCounter;
import org.jquantlib.indexes.SwapIndex;
import org.jquantlib.time.Date;
import org.jquantlib.util.PolymorphicVisitor;
import org.jquantlib.util.Visitor;

/**
 * CMS coupon class
 * @author Richard Gomes
 *
 * @warning This class does not perform any date adjustment,
 *          i.e., the start and end date passed upon finalruction
 *          should be already rolled to a business day.
 */
public class CmsCoupon extends FloatingRateCoupon {

    protected SwapIndex swapIndex_;

    public CmsCoupon(final Date paymentDate, final double nominal, final Date startDate, final Date endDate, final int fixingDays, final SwapIndex index) {
        this(paymentDate, nominal, startDate, endDate, fixingDays, index, 1.0);
    }

    public CmsCoupon(final Date paymentDate, final double nominal, final Date startDate, final Date endDate, final int fixingDays, final SwapIndex index, final double gearing) {
        this(paymentDate, nominal, startDate, endDate, fixingDays, index, gearing, 0.0);
    }

    public CmsCoupon(final Date paymentDate, final double nominal, final Date startDate, final Date endDate, final int fixingDays, final SwapIndex index, final double gearing, final double spread) {
        this(paymentDate, nominal, startDate, endDate, fixingDays, index, gearing, spread, new Date(), new Date());
    }

    public CmsCoupon(final Date paymentDate, final double nominal, final Date startDate, final Date endDate, final int fixingDays, final SwapIndex index, final double gearing, final double spread, final Date refPeriodStart, final Date refPeriodEnd) {
        this(paymentDate, nominal, startDate, endDate, fixingDays, index, gearing, spread, refPeriodStart, refPeriodEnd, new DayCounter());
    }

    public CmsCoupon(final Date paymentDate, final double nominal, final Date startDate, final Date endDate, final int fixingDays, final SwapIndex index, final double gearing, final double spread, final Date refPeriodStart, final Date refPeriodEnd, final DayCounter dayCounter) {
        this(paymentDate, nominal, startDate, endDate, fixingDays, index, gearing, spread, refPeriodStart, refPeriodEnd, dayCounter, false);
    }

    public CmsCoupon(final Date paymentDate, final double nominal, final Date startDate, final Date endDate, final int fixingDays, final SwapIndex index, final double gearing, final double spread, final Date refPeriodStart, final Date refPeriodEnd, final DayCounter dayCounter, final boolean isInArrears) {
        super(paymentDate, nominal, startDate, endDate, fixingDays, index, gearing, spread, refPeriodStart, refPeriodEnd, dayCounter, isInArrears);
    }

    public SwapIndex swapIndex() {
        return swapIndex_;
    }

    @Override
    public void accept(final PolymorphicVisitor pv) {
        final Visitor<CmsCoupon> v = (pv != null) ? pv.visitor(this.getClass()) : null;
        if (v != null) {
            v.visit(this);
        } else {
            super.accept(pv);
        }
    }
}
