package com.googlecode.pondskum.timer;

import com.googlecode.pondskum.config.Config;
import com.googlecode.pondskum.util.NumericUtil;
import com.googlecode.pondskum.util.NumericUtilImpl;

public final class TimerDelay implements RepeatFrequency {

    /**
     * The minimum delay allowed is 10 minutes.
     */
    private static final int TEN_MINUTES = 10;

    private static final int MINUTE_IN_A_MILLISECOND = 60 * 1000;

    private int frequencyInMinutes;

    private NumericUtil numericUtil;

    public TimerDelay(final Config config) {
        numericUtil = new NumericUtilImpl();
        calculateFrequency(config);
    }

    private void calculateFrequency(final Config config) {
        String intervalProperty = config.getRepeatFrequencyInMinutes();
        frequencyInMinutes = getAMinimumOfTenMinutesForTheDelay(intervalProperty);
    }

    private int getAMinimumOfTenMinutesForTheDelay(final String intervalProperty) {
        return Math.max(TEN_MINUTES, numericUtil.getNumber(intervalProperty));
    }

    @Override
    public int getFrequencyInMinutes() {
        return frequencyInMinutes;
    }

    @Override
    public int getFrequencyInMilliSeconds() {
        return frequencyInMinutes * MINUTE_IN_A_MILLISECOND;
    }
}
