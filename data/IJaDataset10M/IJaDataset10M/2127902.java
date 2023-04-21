package net.sf.jfuzzydate;

import net.sf.jfuzzydate.impl.DefaultFuzzingConfiguration;
import net.sf.jfuzzydate.impl.FuzzyDateFormatterImpl;

/**
 * FuzzyDateFormat is a factory for creating instances of fuzzy time/date
 * formatters. These formatters format time/date, distances of time and
 * durations to internationalized strings.
 * 
 * @author amaasch
 */
public final class FuzzyDateFormat {

    /**
	 * Creates a fuzzy date formatter instance with the static default
	 * configuration.
	 * 
	 * @return a fuzzy date formatter instance.
	 */
    public static final FuzzyDateFormatter getInstance() {
        return new FuzzyDateFormatterImpl(DefaultFuzzingConfiguration.getInstance());
    }

    /**
	 * Creates a fuzzy date formatter instance with a given configuration.
	 * 
	 * @param config
	 *            the configuration for the date formatter.
	 * 
	 * @return a fuzzy date formatter instance.
	 */
    public static final FuzzyDateFormatter getInstance(final FuzzingConfiguration config) {
        return new FuzzyDateFormatterImpl(config);
    }
}
