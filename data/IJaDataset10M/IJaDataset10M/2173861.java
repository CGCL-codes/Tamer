package org.jquantlib.exercise;

import java.util.Arrays;
import java.util.List;
import org.jquantlib.QL;
import org.jquantlib.lang.annotation.QualityAssurance;
import org.jquantlib.lang.annotation.QualityAssurance.Quality;
import org.jquantlib.lang.annotation.QualityAssurance.Version;
import org.jquantlib.time.Date;

/**
 * A Bermudan option can only be exercised at a set of fixed dates.
 *
 * @author Richard Gomes
 */
@QualityAssurance(quality = Quality.Q3_DOCUMENTATION, version = Version.V097, reviewers = { "Richard Gomes" })
public class BermudanExercise extends EarlyExercise {

    /**
	 * Constructs a BermudanExercise with a list of exercise dates and the default payoff
	 *
	 * @note In the very special case when the list of dates contains only one date, the BermudanExercise behaves
	 * like an EuropeanExercise.
	 *
	 * @note When there's a single expiry date, this constructor assumes that <i>there will be no payoff at expiry date</i>.
	 * If this is not the desired behavior, use {@link BermudanExercise#BermudanExercise(List, boolean)} instead.
	 *
	 * @param dates is a list of exercise dates. If the list contains only one date, a BermundanExercise behaves like an EuropeanExercise.
	 * @throws IllegalArgumentException if the list is null or empty
	 *
	 * @see EuropeanExercise
	 * @see BermudanExercise#BermudanExercise(List, boolean)
	 */
    public BermudanExercise(final Date[] dates) {
        this(dates, false);
    }

    /**
	 * Constructs a BermudanExercise with a list of exercise dates and the default payoff
	 *
	 * @note In the very special case when the list of dates contains only one date, the BermudanExercise behaves
	 * like an EuropeanExercise.
	 *
	 * @param dates is a list of exercise dates. If the list contains only one date, a BermundanExercise behaves like an EuropeanExercise.
	 * @param payoffAtExpiry is <code>true</code> if payoffs are expected to happen on exercise dates
	 * @throws IllegalArgumentException if the list is null or empty
	 *
	 * @see EuropeanExercise
	 */
    public BermudanExercise(final Date[] dates, final boolean payoffAtExpiry) {
        super(Exercise.Type.Bermudan, payoffAtExpiry);
        QL.require(dates != null && dates.length > 0, "empty exercise dates");
        if (dates.length == 1) {
            super.type = Exercise.Type.European;
            super.payoffAtExpiry = false;
        }
        for (final Date date : dates) {
            super.dates.add(date);
        }
        Arrays.sort(dates);
    }
}
