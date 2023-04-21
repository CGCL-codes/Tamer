package de.jaret.util.date.iterator;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import de.jaret.util.date.JaretDate;

/**
 * Implementation of the DateIterator for hours.
 * 
 * @author kliem
 * @version $Id: HourIterator.java 828 2009-02-08 13:58:21Z kliem $
 */
public class HourIterator extends AbstractDateIterator implements DateIterator {

    /** default formatter. */
    protected IIteratorFormatter _defaultFormatter = new IIteratorFormatter() {

        /**
         * {@inheritDoc}
         */
        public String getLabel(JaretDate date, Format format) {
            if (format.equals(Format.SHORT)) {
                return date.getHours() + "h";
            } else if (format.equals(Format.MEDIUM)) {
                return NF.format(date.getHours()) + ":" + NF.format(date.getMinutes());
            } else {
                return NF.format(date.getHours()) + ":" + NF.format(date.getMinutes());
            }
        }
    };

    /** default step if none is set. */
    private static final int DEFAULT_STEP = 12;

    /** step for hours. */
    protected int _hourStep = DEFAULT_STEP;

    /** Number format for labels. */
    protected static final NumberFormat NF = new DecimalFormat();

    static {
        NF.setMaximumFractionDigits(0);
        NF.setMinimumIntegerDigits(2);
    }

    /**
     * Contructor supplying the setp.
     * 
     * @param hourStep number of hours between steps
     */
    public HourIterator(int hourStep) {
        _hourStep = hourStep;
    }

    /**
     * {@inheritDoc}
     */
    protected void advanceDate(JaretDate date) {
        date.advanceHours(_hourStep);
    }

    /**
     * {@inheritDoc}
     */
    public long getApproxStepMilliSeconds() {
        return _hourStep * 60 * 60 * 1000;
    }

    /**
     * {@inheritDoc}
     */
    protected JaretDate correctStartDate(JaretDate date) {
        date.setMinutes(0);
        date.setSeconds(0);
        date.setMilliseconds(0);
        int diff = date.getHours() % _hourStep;
        date.backHours(diff);
        return date;
    }

    /**
     * {@inheritDoc}
     */
    protected IIteratorFormatter getDefaultFormatter() {
        return _defaultFormatter;
    }
}
