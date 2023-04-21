package jmortgage.payment;

import java.math.BigDecimal;
import java.math.RoundingMode;
import org.apache.commons.lang.builder.ToStringBuilder;

/**
 * The default implementation of <tt>PmtCalculator</tt>. This object calculates
 * a mortgage payment for the United States. This object is immutable, so its
 * thread safety is guaranteed.
 * @since 1.0
 * @author David Armstrong
 */
public final class DefaultPmtCalculator implements PmtCalculator {

    private final double loanAmt;

    private final double interestRate;

    private final double intervalInterestRate;

    private final int years;

    private final int pmtCt;

    private final Interval interval;

    /**
     * Creates a new instance with the specified interval, loan amount, interest
     * rate, and years. This constructor is available for the benefit of
     * {@link jmortgage.JMortgageFactory}, which uses reflection to build
     * objects. Reflection will not find constructors that take primitives as
     * arguments.
     * @param interval The <tt>Interval</tt> value
     * @param loanAmt The loan amount value
     * @param interestRate The interest rate value
     * @param years The number of years in the mortgage term
     */
    public DefaultPmtCalculator(final Interval interval, final Double loanAmt, final Double interestRate, final Integer years) {
        this(interval, loanAmt.doubleValue(), interestRate.doubleValue(), years.intValue());
    }

    /**
     * Creates a new instance with the specified interval, loan amount, interest
     * rate, and years.
     * @param interval The <tt>Interval</tt> value
     * @param loanAmt The loan amount value
     * @param interestRate The interest rate value
     * @param years The number of years in the mortgage term
     */
    public DefaultPmtCalculator(Interval interval, double loanAmt, double interestRate, int years) {
        if (loanAmt < 0.0) {
            throw new IllegalArgumentException("Loan amount must be greater than 0.");
        }
        if (interestRate < 0.0 || interestRate > 99) {
            throw new IllegalArgumentException("Interest rate must be between 0 and 99.");
        }
        if (years < 0) {
            throw new IllegalArgumentException("Mortgage term in years must be greater than 0.");
        }
        if (interval.equals(Interval.Yearly) || interval.equals(Interval.Onetime)) {
            throw new IllegalArgumentException("Illegal payment interval. Valid payment intervals are monthly, bi-weekly, and weekly.");
        }
        this.loanAmt = loanAmt;
        this.interestRate = interestRate;
        this.intervalInterestRate = this.interestRate / (interval.pmtsPerYear() * 100);
        this.years = years;
        this.interval = interval;
        this.pmtCt = years * interval.pmtsPerYear();
    }

    /**
     * Calculates and returns the payment amount rounded.
     * @return <tt>double</tt> payment amount rounded
     */
    public double calcPmt() {
        return new BigDecimal(calcPmtUnrounded()).setScale(2, RoundingMode.HALF_EVEN).doubleValue();
    }

    /**
     * Calculates and returns the payment amount unrounded. This method is used
     * by implementations of {@link jmortgage.amortization.FixedAmortizationBuilder}
     * when building an amortization schedule
     * @return <tt>double</tt> payment amount unrounded
     */
    public double calcPmtUnrounded() {
        double mthlyIntRate = interestRate / (12 * 100);
        double pwer = Math.pow(1 + mthlyIntRate, -(years * 12));
        double pmt = loanAmt * (mthlyIntRate / (1 - pwer));
        if (interval.equals(Interval.Biweekly)) pmt /= 2; else if (interval.equals(Interval.Weekly)) pmt /= 4;
        return pmt;
    }

    /**
     * Gets the loan amount to be used when calculating the payment amount.
     * @return <tt>double</tt> loan amount
     */
    public double getLoanAmt() {
        return loanAmt;
    }

    /**
     * Gets the interest rate to be used when calculating the payment amount.
     * @return <tt>double</tt> interest rate amount
     */
    public double getInterestRate() {
        return interestRate;
    }

    /**
     * Gets the interval interest rate, which is used by an implementation of
     * <tt>FixedAmortizationBuilder</tt> when building an amortization schedule.
     * @return <tt>double</tt> interval interest rate amount
     */
    public double getIntervalInterestRate() {
        return intervalInterestRate;
    }

    /**
     * Gets the years of the mortgage term to be used when calculating the
     * payment amount
     * @return <tt>int</tt> years amount
     */
    public int getYears() {
        return years;
    }

    /**
     * Gets the payment count, which is used by an implementation of
     * <tt>FixedAmortizationBuilder</tt> when building an amortization schedule.
     * @return <tt>int</tt> payment count value
     */
    public int getPmtCt() {
        return pmtCt;
    }

    /**
     * Gets the interval value to be used when calculating the payment amount.
     * @return <tt>Interval</tt> interval value
     */
    public Interval getInterval() {
        return interval;
    }

    /**
     * Returns a new <tt>DefaultPmtCalculator</tt> object with the passed-in
     * loan amount value and the same interval, interest rate, and years values
     * as the object on which <tt>setLoanAmt</tt> was called.
     * @param loanAmt The new loan amount
     * @return <tt>PmtCalculator</tt> A new instance of <tt>DefaultPmtCalculator</tt>
     */
    public PmtCalculator setLoanAmt(double loanAmt) {
        return new DefaultPmtCalculator(this.interval, loanAmt, this.interestRate, this.years);
    }

    /**
     * Returns a new <tt>DefaultPmtCalculator</tt> object with the passed-in
     * interest rate value and the same interval, loan amount, and years values
     * as the object on which <tt>setInterestRate</tt> was called.
     * @param interestRate The new interest rate
     * @return <tt>PmtCalculator</tt> A new instance of <tt>DefaultPmtCalculator</tt>
     */
    public PmtCalculator setInterestRate(double interestRate) {
        return new DefaultPmtCalculator(this.interval, this.loanAmt, interestRate, this.years);
    }

    /**
     * Returns a new <tt>DefaultPmtCalculator</tt> object with the passed-in
     * years value and the same interval, loan amount, and interest rate values
     * as the object on which <tt>setYears</tt> was called.
     * @param years The new years value
     * @return <tt>PmtCalculator</tt> A new instance of <tt>DefaultPmtCalculator</tt>
     */
    public PmtCalculator setYears(int years) {
        return new DefaultPmtCalculator(this.interval, this.loanAmt, this.interestRate, years);
    }

    /**
     * Returns a new <tt>DefaultPmtCalculator</tt> object with the passed-in
     * interval value and the same loan amount, interest rate, and years values
     * as the object on which <tt>setInterval</tt> was called.
     * @param interval The new <tt>Interval</tt> value
     * @return <tt>PmtCalculator</tt> A new instance of <tt>DefaultPmtCalculator</tt>
     */
    public PmtCalculator setInterval(Interval interval) {
        return new DefaultPmtCalculator(interval, this.loanAmt, this.interestRate, this.years);
    }

    /**
     * Returns a String representation of this <tt>DefaultPmtCalculator</tt>
     * @return a String representation
     */
    @Override
    public String toString() {
        return (new ToStringBuilder(this).append("loanAmt", loanAmt).append("interestRate", interestRate).append("mthlyInterestRate", intervalInterestRate).append("years", years).append("pmtCt", pmtCt).append("interval", interval).toString());
    }
}
