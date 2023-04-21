package desmoj.core.simulator;

import java.util.concurrent.TimeUnit;

/**
 * TimeOperations is an utility class that provides arithmetic operations for
 * the time classes TimeInstant and TimeSpan. It also holds the time settings,
 * i.e. the granularity (epsilon) and the reference time unit.
 * 
 * @version DESMO-J, Ver. 2.3.4beta copyright (c) 2012
 * @author Felix Klueckmann
 * 
 *         Licensed under the Apache License, Version 2.0 (the "License"); you
 *         may not use this file except in compliance with the License. You may
 *         obtain a copy of the License at
 *         http://www.apache.org/licenses/LICENSE-2.0
 * 
 *         Unless required by applicable law or agreed to in writing, software
 *         distributed under the License is distributed on an "AS IS" BASIS,
 *         WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or
 *         implied. See the License for the specific language governing
 *         permissions and limitations under the License.
 * 
 */
public final class TimeOperations {

    /**
	 * The granularity of simulation time. Default is MICROSECONDS.
	 */
    private static TimeUnit epsilon = TimeUnit.MICROSECONDS;

    /**
	 * The reference time unit specifying what is meant by the simulation time
	 * step of 1 in statements without an explicit declaration of a time unit
	 * like in <code>new TimeSpan(5)</code>.
	 */
    private static TimeUnit referenceUnit = TimeUnit.SECONDS;

    /**
	 * The point in simulation time when the experiment has started.
	 */
    private static TimeInstant startTime;

    /**
     * The TimeFormatter to use for printing TimeInstants in this environment.
     */
    private static TimeFormatter myTimeFormatter = getDefaultTimeFormatter();

    private TimeOperations() {
    }

    /**
	 * Returns a new TimeSpan object representing the sum of the given TimeSpan
	 * objects. Note that the simulation will stop immediately if the resulting
	 * sum is larger than Long.MAX_VALUE-1 (in the unit of epsilon).
	 * 
	 * @return TimeSpan : A new TimeSpan as the sum of the TimeSpan parameters
	 * @param a
	 *            TimeSpan : TimeSpan a
	 * @param b
	 *            TimeSpan : TimeSpan b
	 */
    public static TimeSpan add(TimeSpan a, TimeSpan b) {
        if (Long.MAX_VALUE - a.getTimeInEpsilon() - b.getTimeInEpsilon() < 1) {
            throw (new desmoj.core.exception.SimAbortedException(new desmoj.core.report.ErrorMessage(null, "Can't add TimeSpan objects! Simulation aborted.", "Class : TimeOperations  Methode : add(TimeSpan a, TimeSpan b)", "the resulting sum is too big. ", "Can only create TimeSpan objects which are smaller than Long.MAX_VALUE (in the TimeUnit of epsilon).", null)));
        }
        return new TimeSpan(a.getTimeInEpsilon() + b.getTimeInEpsilon(), epsilon);
    }

    /**
     * Returns a new Time Instant determined from the instant passed to this
     * method plus the time span passed to method.
     * In other words, this method determines the instant that is a given
     * span after the instant provided.
     * Note that the simulation will stop immediately if the resulting
     * instant is larger than <code>Long.MAX_VALUE-1</code> (in the unit of epsilon).
     * The preferred time zone of the result is equal to the preferred time zone
     * of parameter <code>b</code>.
     * 
     * @return TimeSpan : A new TimeInstant determined from given TimeInstant plus
     *            the given TimeSpan
	 * @param span
	 *            TimeSpan : The TimeSpan to add...
	 * @param instant
	 *            TimeInstant : ...to this TimeInstant.
	 */
    public static TimeInstant add(TimeSpan span, TimeInstant instant) {
        return TimeOperations.add(instant, span);
    }

    /**
     * Returns a new Time Instant determined from the instant passed to this
     * method plus the time span passed to method.
     * In other words, this method determines the instant that is a given
     * span after the instant provided.
     * Note that the simulation will stop immediately if the resulting
     * instant is larger than <code>Long.MAX_VALUE-1</code> (in the unit of epsilon).
     * The preferred time zone of the result is equal to the preferred time zone
     * of instant passed to this method.
     * 
     * @return TimeSpan : A new TimeInstant determined from given TimeInstant plus
     *            the given TimeSpan
	 * @param instant
	 *            TimeInstant : The TimeInstant, to which...
	 * @param span
	 *            TimeSpan : ...this TimeSpan will be added
	 */
    public static TimeInstant add(TimeInstant instant, TimeSpan span) {
        if (instant.getTimeInEpsilon() > 0) {
            if (Long.MAX_VALUE - span.getTimeInEpsilon() - instant.getTimeInEpsilon() < 1) {
                throw (new desmoj.core.exception.SimAbortedException(new desmoj.core.report.ErrorMessage(null, "Can't add TimeSpan and TimeInstant objects! Simulation aborted.", "Class : TimeOperations  Methode : add(TimeSpan a, TimeInstant b)", "the resulting sum is too big. ", "Can only create TimeInstant objects which are before Long.MAX_VALUE (in the TimeUnit of epsilon).", null)));
            }
        }
        TimeInstant result = new TimeInstant(span.getTimeInEpsilon() + instant.getTimeInEpsilon(), epsilon);
        result.setPreferredTimeZone(instant.getPreferredTimeZone());
        return result;
    }

    /**
     * Returns a new Time Instant determined from the instant passed to this
     * method minus the time span passed to method.
     * In other words, this method determines the instant that is a given
     * span before the instant provided.
     * Note that the simulation will stop immediately if the resulting
     * instant is smaller than 0 (in the unit of epsilon).
     * The preferred time zone of the result is equal to the preferred time zone
     * of instant passed to this method.
     * 
     * @return TimeSpan : A new TimeInstant determined from given TimeInstant minus
     *            the given TimeSpan
     * @param instant
     *            TimeInstant : The TimeInstant, from which...
     * @param span
     *            TimeSpan : ...this TimeSpan will be subtracted
     */
    public static TimeInstant subtract(TimeInstant instant, TimeSpan span) {
        if (instant.getTimeInEpsilon() > 0) {
            if (instant.getTimeInEpsilon() - span.getTimeInEpsilon() < 0) {
                throw (new desmoj.core.exception.SimAbortedException(new desmoj.core.report.ErrorMessage(null, "Can't subtract TimeInstant and  TimeSpan objects! Simulation aborted.", "Class : TimeOperations  Methode : subtract(TimeInstant a, TimeSpan b)", "the resulting smaller than 0.", "Can only create TimeInstant objects which are non-negative (in the TimeUnit of epsilon).", null)));
            }
        }
        TimeInstant result = new TimeInstant(instant.getTimeInEpsilon() - span.getTimeInEpsilon(), epsilon);
        result.setPreferredTimeZone(instant.getPreferredTimeZone());
        return result;
    }

    /**
	 * Returns a new TimeSpan object representing the absolute difference of the
	 * given TimeSpan objects.
	 * 
	 * @return TimeSpan : A new TimeSpan as the absolute difference of the
	 *         TimeSpan parameters
	 * @param a
	 *            TimeSpan : TimeSpan a
	 * @param b
	 *            TimeSpan : TimeSpan b
	 */
    public static TimeSpan diff(TimeSpan a, TimeSpan b) {
        if (TimeSpan.isShorter(a, b)) {
            return new TimeSpan(b.getTimeInEpsilon() - a.getTimeInEpsilon(), epsilon);
        }
        return new TimeSpan(a.getTimeInEpsilon() - b.getTimeInEpsilon(), epsilon);
    }

    /**
	 * Returns a new TimeSpan object representing the absolute difference of the
	 * given TimeInstant objects, i.e. the span of time between the two instants
	 * of time. Note that the simulation will stop immediately if the resulting
	 * sum is larger than <code>Long.MAX_VALUE-1</code> (in the unit of epsilon).
	 * 
	 * @return TimeSpan : A new TimeSpan as the difference of the TimeSpan
	 *         parameters
	 * @param a
	 *            TimeInstant : TimeInstant a
	 * @param b
	 *            TimeInstant : TimeInstant b
	 */
    public static TimeSpan diff(TimeInstant a, TimeInstant b) {
        if (TimeInstant.isAfter(a, b)) {
            if (b.getTimeInEpsilon() < 0) {
                if (Long.MAX_VALUE - 1 + b.getTimeInEpsilon() < a.getTimeInEpsilon()) {
                    throw (new desmoj.core.exception.SimAbortedException(new desmoj.core.report.ErrorMessage(null, "Can't subtract TimeInstant objects! Simulation aborted.", "Class : TimeOperations  Methode : diff(TimeSpan a, TimeInstant b)", "the resulting difference is too big. ", "Can only create TimeSpan objects which are smaller than Long.MAX_VALUE (in the TimeUnit of epsilon).", null)));
                }
            }
            return new TimeSpan(a.getTimeInEpsilon() - b.getTimeInEpsilon(), epsilon);
        } else {
            if (a.getTimeInEpsilon() < 0) {
                if (Long.MAX_VALUE + a.getTimeInEpsilon() < b.getTimeInEpsilon()) {
                    throw (new desmoj.core.exception.SimAbortedException(new desmoj.core.report.ErrorMessage(null, "Can't subtract TimeInstant objects! Simulation aborted.", "Class : TimeOperations  Methode : diff(TimeSpan a, TimeInstant b)", "the resulting difference is too big. ", "Can only create TimeSpan objects which are smaller than Long.MAX_VALUE (in the TimeUnit of epsilon).", null)));
                }
            }
            return new TimeSpan(b.getTimeInEpsilon() - a.getTimeInEpsilon(), epsilon);
        }
    }

    /**
	 * Returns a new TimeSpan object representing the product of the given
	 * TimeSpan and the factor of type double. Note that the simulation will
	 * stop immediately if the resulting product is larger than <code>Long.MAX_VALUE-1</code>
	 * (in the unit of epsilon).
	 * 
	 * @return TimeSpan : A new TimeSpan as the product of span and factor
	 * @param span
	 *            TimeSpan : The span of time
	 * @param factor
	 *            double : The scalar factor
	 */
    public static TimeSpan multiply(TimeSpan span, double factor) {
        if (factor > 1) {
            if (Long.MAX_VALUE / factor < span.getTimeInEpsilon()) {
                throw (new desmoj.core.exception.SimAbortedException(new desmoj.core.report.ErrorMessage(null, "Can't multiply TimeSpan and double value! Simulation aborted.", "Class : TimeOperations  Methode : multiply(TimeSpan span, double factor)", "the resulting product is too big. ", "Can only create TimeSpan objects which are shorter than Long.MAX_VALUE (in the TimeUnit of epsilon).", null)));
            }
        }
        return new TimeSpan((long) (span.getTimeInEpsilon() * factor), epsilon);
    }

    /**
	 * Returns a new TimeSpan object representing the product of the given
	 * TimeSpan and the factor of type double. Note that the simulation will
	 * stop immediately if the resulting product is larger than <code>Long.MAX_VALUE-1</code>
	 * (in the unit of epsilon).
	 * 
	 * @return TimeSpan : A new TimeSpan as the product of span and factor
	 * @param factor
	 *            double : The scalar factor
	 * @param span
	 *            TimeSpan : The span of time
	 */
    public static TimeSpan multiply(double factor, TimeSpan span) {
        return TimeOperations.multiply(span, factor);
    }

    /**
	 * Returns a new TimeSpan object representing the quotient of the given
	 * TimeSpan objects.
	 * 
	 * @return TimeSpan : A new TimeSpan as the quotient of dividend and divisor
	 * @param dividend
	 *            TimeSpan : The dividend
	 * @param divisor
	 *            TimeSpan : The divisor
	 */
    public static double divide(TimeSpan dividend, TimeSpan divisor) {
        if (divisor.getTimeInEpsilon() == 0) {
            throw (new desmoj.core.exception.SimAbortedException(new desmoj.core.report.ErrorMessage(null, "Can't divide TimeSpan values! Simulation aborted.", "Class : TimeOperations  Methode : divide(TimeSpan dividend, TimeSpan divisor)", "Cannot devide by zero.", "Never try to devide by zero.", null)));
        }
        return ((double) dividend.getTimeInEpsilon() / (double) divisor.getTimeInEpsilon());
    }

    /**
	 * Returns a new TimeSpan object representing the quotient of the given
	 * TimeSpan and the divisor of type double. Note that the simulation will
	 * stop immediately if the resulting quotient is larger than
	 * Long.MAX_VALUE-1 (in the unit of epsilon).
	 * 
	 * @return TimeSpan : A new TimeSpan as the quotient of divident and divisor
	 * @param dividend
	 *            TimeSpan : The dividend
	 * @param divisor
	 *            double : The divisor
	 */
    public static TimeSpan divide(TimeSpan dividend, double divisor) {
        if (divisor <= 0) {
            throw (new desmoj.core.exception.SimAbortedException(new desmoj.core.report.ErrorMessage(null, "Can't divide TimeSpan and double value! Simulation aborted.", "Class : TimeOperations  Methode : mdivide(TimeSpan dividend, double divisor)", "Cannot devide by zero.", "Never try to devide by zero.", null)));
        }
        if (divisor < 1) {
            if (Long.MAX_VALUE * divisor < dividend.getTimeInEpsilon()) {
                throw (new desmoj.core.exception.SimAbortedException(new desmoj.core.report.ErrorMessage(null, "Can't divide TimeSpan and double value! Simulation aborted.", "Class : TimeOperations  Methode : mdivide(TimeSpan dividend, double divisor)", "the resulting quotient is too big. ", "Can only create TimeSpan objects which are shorter than Long.MAX_VALUE (in the TimeUnit of epsilon).", null)));
            }
        }
        return new TimeSpan((long) (dividend.getTimeInEpsilon() / divisor), epsilon);
    }

    /**
	 * Returns the epsilon value representing the granularity of simulation time
	 * for this experiment.
	 * 
	 * @return TimeUnit : The granularity of simulation time
	 */
    public static TimeUnit getEpsilon() {
        return epsilon;
    }

    /**
     * Returns the smallest distinguishable TimeSpan.
     * 
     * @return TimeSpan : The smallest distinguishable TimeSpan, i.e. one interval
     *       of the epsilon unit 
     */
    public static TimeSpan getEpsilonSpan() {
        return new TimeSpan(1, epsilon);
    }

    /**
	 * Returns the reference time unit specifying what is meant by the
	 * simulation time step of 1 in statements without an explicit declaration
	 * of a time unit like in <code>new TimeSpan(5)</code>.
	 * 
	 * 
	 * @return the reference time unit
	 * 
	 */
    static TimeUnit getReferenceUnit() {
        return referenceUnit;
    }

    /**
	 * Sets the epsilon value representing the granularity of simulation time to
	 * the given TimeUnit parameter. This is a package private method for
	 * internal framework use only.
	 * 
	 * @param epsilon
	 *            TimeUnit : The granularity of simulation time, i.e. the
	 *            smallest distinguishable span of simulation time.
	 */
    static void setEpsilon(TimeUnit epsilon) {
        TimeOperations.epsilon = epsilon;
    }

    /**
	 * Sets the reference time unit specifying what is meant by the simulation
	 * time step of 1 in statements without an explicit declaration of a time
	 * unit like in <code>new TimeSpan(5)</code>. This is a package private
	 * method for internal framework use only.
	 * 
	 * 
	 * @param referenceUnit
	 *            the reference time unit
	 * 
	 */
    static void setReferenceUnit(TimeUnit referenceUnit) {
        TimeOperations.referenceUnit = referenceUnit;
    }

    /**
	 * Formats the given instant of time according to the timeFormatter. This is
	 * a package private method for internal framework use only.
	 * 
	 * @param instant
	 *            the instant of time to be formatted
	 * 
	 */
    static String formatTimeInstant(TimeInstant instant) {
        return myTimeFormatter.buildTimeString(instant);
    }

    /**
	 * Formats the given span of time according to the timeFormatter. This is a
	 * package private method for internal framework use only.
	 * 
	 * @param span
	 *            the span of time to be formatted
	 * 
	 */
    static String formatTimeSpan(TimeSpan span) {
        return myTimeFormatter.buildTimeString(span);
    }

    /**
	 * Returns the time Formatter. This is a package private method for internal
	 * framework use only.
	 */
    public static TimeFormatter getTimeFormatter() {
        return myTimeFormatter;
    }

    /**
	 * Sets the time Formatter. This is a package private method for internal
	 * framework use only.
	 * 
	 * @param myTimeFormatter
	 *            the Time Formatter
	 */
    static void setTimeFormatter(TimeFormatter myTimeFormatter) {
        TimeOperations.myTimeFormatter = myTimeFormatter;
    }

    /**
     * Sets the TimeFormatter used by this environment to a 
     * <code>SingleUnitTimeFormatter</code>, using the current reference 
     * unit and four floating point digits. This is a package private method for
     * internal framework use only.
     */
    static TimeFormatter getDefaultTimeFormatter() {
        return new SingleUnitTimeFormatter(referenceUnit, epsilon, 4, false);
    }

    /**
	 * Returns the TimeInstant when the experiment has started.
	 * 
	 * @return TimeInstant : The point in simulation time, the experiment has
	 *         started.
	 */
    public static TimeInstant getStartTime() {
        return startTime;
    }

    static void setStartTime(TimeInstant startTime) {
        TimeOperations.startTime = startTime;
    }
}
