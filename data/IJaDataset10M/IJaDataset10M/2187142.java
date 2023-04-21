package org.jquantlib.termstructures.yieldcurves;

import org.jquantlib.QL;
import org.jquantlib.daycounters.Actual365Fixed;
import org.jquantlib.daycounters.DayCounter;
import org.jquantlib.termstructures.AbstractYieldTermStructure;
import org.jquantlib.termstructures.TermStructure;
import org.jquantlib.time.Calendar;
import org.jquantlib.time.Date;
import org.jquantlib.time.calendars.Target;

/**
 * Forward-rate term structure
 * <p>
 * This abstract class acts as an adapter to TermStructure allowing the programmer to implement only method
 * <code>forwardImpl(double)</code> in derived classes. Zero yields and discounts are calculated from forwards. Rates are assumed to be
 * annual continuous compounding.
 *
 * @see TermStructure documentation for issues regarding constructors.
 *
 * @author Richard Gomes
 */
public abstract class ForwardRateStructure extends AbstractYieldTermStructure {

    /**
	 * @see TermStructure documentation for issues regarding constructors.
	 *
	 * @param dc
	 */
    protected ForwardRateStructure() {
        this(new Actual365Fixed());
        QL.validateExperimentalMode();
    }

    /**
	 * @see TermStructure documentation for issues regarding constructors.
	 *
	 * @param dc
	 */
    protected ForwardRateStructure(final DayCounter dc) {
        super(dc);
        QL.validateExperimentalMode();
    }

    /**
	 * @see TermStructure documentation for issues regarding constructors.
	 *
	 * @param refDate
	 * @param cal
	 * @param dc
	 */
    protected ForwardRateStructure(final Date refDate, final Calendar cal) {
        this(refDate, cal, new Actual365Fixed());
        QL.validateExperimentalMode();
    }

    /**
	 * @see TermStructure documentation for issues regarding constructors.
	 *
	 * @param refDate
	 * @param cal
	 * @param dc
	 */
    protected ForwardRateStructure(final Date refDate, final DayCounter dc) {
        this(refDate, new Target(), dc);
        QL.validateExperimentalMode();
    }

    /**
	 * @see TermStructure documentation for issues regarding constructors.
	 *
	 * @param refDate
	 * @param cal
	 * @param dc
	 */
    protected ForwardRateStructure(final Date refDate) {
        this(refDate, new Target(), new Actual365Fixed());
        QL.validateExperimentalMode();
    }

    /**
	 * @see TermStructure documentation for issues regarding constructors.
	 *
	 * @param refDate
	 * @param cal
	 * @param dc
	 */
    protected ForwardRateStructure(final Date refDate, final Calendar cal, final DayCounter dc) {
        super(refDate, cal, dc);
        QL.validateExperimentalMode();
    }

    /**
	 * @see TermStructure documentation for issues regarding constructors.
	 *
	 * @param settlementDays
	 * @param cal
	 * @param dc
	 */
    protected ForwardRateStructure(final int settlementDays, final Calendar cal) {
        super(settlementDays, cal, new Actual365Fixed());
        QL.validateExperimentalMode();
    }

    /**
	 * @see TermStructure documentation for issues regarding constructors.
	 *
	 * @param settlementDays
	 * @param cal
	 * @param dc
	 */
    protected ForwardRateStructure(final int settlementDays, final Calendar cal, final DayCounter dc) {
        super(settlementDays, cal, dc);
        QL.validateExperimentalMode();
    }

    /**
	 * Instantaneous forward-rate calculation
	 */
    protected abstract double forwardImpl(double t);

    /**
	 * Returns the zero yield rate for the given date calculating it from the instantaneous forward rate.
	 *
	 * @note This is just a default, highly inefficient and possibly wildly inaccurate implementation.
	 * Derived classes should implement their own zeroYield method.
	 */
    protected double zeroYieldImpl(final double t) {
        if (t == 0.0) {
            return forwardImpl(0.0);
        }
        double sum = 0.5 * forwardImpl(0.0);
        final int n = 1000;
        final double dt = t / n;
        for (double i = dt; i < t; i += dt) {
            sum += forwardImpl(i);
        }
        sum += 0.5 * forwardImpl(t);
        return sum * dt / t;
    }

    @Override
    public double discountImpl(final double t) {
        final double r = zeroYieldImpl(t);
        return Math.exp(-r * t);
    }
}
