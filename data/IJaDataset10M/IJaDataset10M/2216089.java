package org.jquantlib.indexes;

import org.jquantlib.currencies.Europe.EURCurrency;
import org.jquantlib.daycounters.Thirty360;
import org.jquantlib.quotes.Handle;
import org.jquantlib.termstructures.YieldTermStructure;
import org.jquantlib.time.BusinessDayConvention;
import org.jquantlib.time.Period;
import org.jquantlib.time.TimeUnit;
import org.jquantlib.time.calendars.Target;

/**
 * 
 * EuriborSwapIfrFixB index base class
 * Euribor Swap indexes fixed by ISDA in cooperation with
 * Reuters and Intercapital Brokers at 11am Frankfurt.
 * Annual 30/360 vs 6M Euribor, 1Y vs 3M Euribor.
 * Reuters page ISDAFIX2 or EURSFIXB=.
 * 
 * Further info can be found at <http://www.isda.org/fix/isdafix.html> or Reuters page ISDAFIX.
 * 
 * @author Tim Blackler
 */
public class EuriborSwapIsdaFixB extends SwapIndex {

    public EuriborSwapIsdaFixB(final Period tenor) {
        this(tenor, new Handle<YieldTermStructure>());
    }

    public EuriborSwapIsdaFixB(final Period tenor, final Handle<YieldTermStructure> h) {
        super("EuriborSwapIsdaFixB", tenor, 2, new EURCurrency(), new Target(), new Period(1, TimeUnit.Years), BusinessDayConvention.ModifiedFollowing, new Thirty360(Thirty360.Convention.BondBasis), tenor.gt(new Period(1, TimeUnit.Years)) ? new Euribor(new Period(6, TimeUnit.Months), h) : new Euribor(new Period(3, TimeUnit.Months), h));
    }
}
