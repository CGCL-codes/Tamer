package org.jquantlib.termstructures.volatilities;

import org.jquantlib.QL;
import org.jquantlib.daycounters.DayCounter;
import org.jquantlib.termstructures.AbstractTermStructure;
import org.jquantlib.time.BusinessDayConvention;
import org.jquantlib.time.Calendar;
import org.jquantlib.time.Date;
import org.jquantlib.time.Period;

/**
 * Volatility term structure
 * <p>
 * This abstract class defines the interface of concrete volatility structures which will be derived from this one.
 *
 * @author Richard Gomes
 */
public abstract class VolatilityTermStructure extends AbstractTermStructure {

    private final BusinessDayConvention bdc;

    /**
     * 'default' constructor
     * <p>
     * @warning term structures initialized by means of this
     *          finalructor must manage their own reference date
     *          by overriding the referenceDate() method.
     */
    public VolatilityTermStructure(final Calendar cal, final BusinessDayConvention bdc) {
        this(cal, bdc, new DayCounter());
    }

    /**
     * 'default' constructor
     * <p>
     * @warning term structures initialized by means of this
     *          finalructor must manage their own reference date
     *          by overriding the referenceDate() method.
     */
    public VolatilityTermStructure(final Calendar cal, final BusinessDayConvention bdc, final DayCounter dc) {
        super(dc);
        this.bdc = bdc;
        this.calendar = cal;
    }

    /**
     * initialize with a fixed reference date
     */
    public VolatilityTermStructure(final Date referenceDate, final Calendar cal, final BusinessDayConvention bdc) {
        this(referenceDate, cal, bdc, new DayCounter());
    }

    /**
     * initialize with a fixed reference date
     */
    public VolatilityTermStructure(final Date referenceDate, final Calendar cal, final BusinessDayConvention bdc, final DayCounter dc) {
        super(referenceDate, cal, dc);
        this.bdc = bdc;
    }

    /**
     * calculate the reference date based on the global evaluation date
     */
    public VolatilityTermStructure(final int settlementDays, final Calendar cal, final BusinessDayConvention bdc) {
        this(settlementDays, cal, bdc, new DayCounter());
    }

    /**
     * calculate the reference date based on the global evaluation date
     */
    public VolatilityTermStructure(final int settlementDays, final Calendar cal, final BusinessDayConvention bdc, final DayCounter dc) {
        super(settlementDays, cal, dc);
        this.bdc = bdc;
    }

    /**
     * The business day convention used in tenor to date conversion
     */
    public BusinessDayConvention businessDayConvention() {
        return bdc;
    }

    /**
     * Period/date conversion
     */
    public Date optionDateFromTenor(final Period p) {
        return calendar().advance(referenceDate(), p, businessDayConvention());
    }

    /**
     * The minimum strike for which the term structure can return vols
     */
    public abstract double minStrike();

    /**
     * The maximum strike for which the term structure can return vols
     */
    public abstract double maxStrike();

    /**
     * Strike-range check
     */
    protected void checkStrike(final double strike, final boolean extrapolate) {
        QL.require(extrapolate || allowsExtrapolation() || (strike >= minStrike() && strike <= maxStrike()), "strike is outside the curve domain");
    }
}
