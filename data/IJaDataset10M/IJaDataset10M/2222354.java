package org.jquantlib.indexes.ibor;

import org.jquantlib.currencies.America.USDCurrency;
import org.jquantlib.daycounters.Actual360;
import org.jquantlib.quotes.Handle;
import org.jquantlib.termstructures.AbstractYieldTermStructure;
import org.jquantlib.termstructures.YieldTermStructure;
import org.jquantlib.time.Date;
import org.jquantlib.time.calendars.UnitedStates;

/**
 * base class for all BBA LIBOR indexes but the EUR, O/N, and S/N ones
 * <p>
 * LIBOR fixed by BBA.
 *
 * @see <a href="http://www.bba.org.uk/bba/jsp/polopoly.jsp?d=225&a=1414">http://www.bba.org.uk/bba/jsp/polopoly.jsp?d=225&a=1414</a>
 */
public class DailyTenorUSDLibor extends DailyTenorLibor {

    public DailyTenorUSDLibor(final int settlementDays) {
        this(settlementDays, new Handle<YieldTermStructure>(new AbstractYieldTermStructure() {

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

    public DailyTenorUSDLibor(final int settlementDays, final Handle<YieldTermStructure> h) {
        super("USDLibor", settlementDays, new USDCurrency(), new UnitedStates(UnitedStates.Market.SETTLEMENT), new Actual360(), h);
    }
}
