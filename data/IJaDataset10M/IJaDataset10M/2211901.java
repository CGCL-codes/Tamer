package org.jquantlib.indexes.ibor;

import org.jquantlib.currencies.Europe.CHFCurrency;
import org.jquantlib.daycounters.Actual360;
import org.jquantlib.indexes.IborIndex;
import org.jquantlib.quotes.Handle;
import org.jquantlib.termstructures.AbstractYieldTermStructure;
import org.jquantlib.termstructures.YieldTermStructure;
import org.jquantlib.time.BusinessDayConvention;
import org.jquantlib.time.Date;
import org.jquantlib.time.Period;
import org.jquantlib.time.calendars.Switzerland;

/**
 * Zurich Interbank Offered Rate. 
 * This is the rate fixed in Zurich by BBA. Use CHFLibor if
 * you're interested in the London fixing by BBA.
 * 
 * TODO check settlement days, end-of-month adjustment, and day-count convention.
 * 
 */
public class Zibor extends IborIndex {

    public Zibor(final Period tenor) {
        this(tenor, new Handle<YieldTermStructure>(new AbstractYieldTermStructure() {

            @Override
            protected double discountImpl(final double t) {
                throw new UnsupportedOperationException();
            }

            @Override
            public Date maxDate() {
                throw new UnsupportedOperationException();
            }
        }));
    }

    public Zibor(final Period tenor, final Handle<YieldTermStructure> h) {
        super("Zibor", tenor, 2, new CHFCurrency(), new Switzerland(), BusinessDayConvention.ModifiedFollowing, false, new Actual360(), h);
    }
}
