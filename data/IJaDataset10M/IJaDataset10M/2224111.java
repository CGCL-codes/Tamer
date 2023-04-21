package org.jquantlib.model.volatility;

import org.jquantlib.lang.iterators.Iterables;
import org.jquantlib.math.IntervalPrice;
import org.jquantlib.time.Date;
import org.jquantlib.time.TimeSeries;

/**
 * Garman-Klass volatility model
 * <p>
 * This class implements a concrete volatility model based on high low formulas using the method of
 * Garman and Klass in their paper "On the Estimation of the Security Price from Historical Data" at
 * http://www.fea.com/resources/pdf/a_estimation_of_security_price.pdf
 * <p>
 * Volatilities are assumed to be expressed on an annual basis.
 *
 * @author Anand Mani
 */
public abstract class GarmanKlassAbstract implements LocalVolatilityEstimator<IntervalPrice> {

    private final double yearFraction;

    public GarmanKlassAbstract(final double y) {
        this.yearFraction = y;
    }

    @Override
    public TimeSeries<Double> calculate(final TimeSeries<IntervalPrice> quotes) {
        final TimeSeries<Double> retval = new TimeSeries<Double>(Double.class);
        for (final Date date : Iterables.unmodifiableIterable(quotes.navigableKeySet())) {
            final IntervalPrice curr = quotes.get(date);
            retval.put(date, Math.sqrt(Math.abs(calculatePoint(curr)) / yearFraction));
        }
        return retval;
    }

    public double getYearFraction() {
        return yearFraction;
    }

    protected abstract double calculatePoint(final IntervalPrice p);
}
