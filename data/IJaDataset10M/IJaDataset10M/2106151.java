package com.noahsloan.nutils.streams.clocked;

/**
 * Echos measurements to other calculators. The primary calculator answers all
 * queries. Useful when you tracking a download with several parts. Add a
 * listener to each individual part's transfer to get the overall rate.
 * 
 * @author noah
 * 
 */
public class EchoRateCalculator implements RateCalculator {

    private final RateCalculator primary;

    private final RateCalculator[] listeners;

    /**
     * 
     * @param primary
     * @param listeners
     */
    public EchoRateCalculator(final RateCalculator primary, final RateCalculator... listeners) {
        super();
        this.primary = primary;
        this.listeners = listeners;
    }

    /**
     * Closes the primary calculator.
     */
    public void close() {
        primary.close();
    }

    /**
     * Gets the rate from the primary calculator.
     */
    public double getRate() {
        return primary.getRate();
    }

    /**
     * Stores the measurement in all the calculators.
     */
    public long storeMeasurement(long count) {
        for (RateCalculator calc : listeners) {
            calc.storeMeasurement(count);
        }
        return primary.storeMeasurement(count);
    }
}
