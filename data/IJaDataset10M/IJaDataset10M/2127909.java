package org.libreplan.business.workingday;

import static org.libreplan.business.workingday.EffortDuration.seconds;
import static org.libreplan.business.workingday.EffortDuration.zero;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;
import org.apache.commons.lang.Validate;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.hibernate.validator.NotNull;
import org.joda.time.DateTime;
import org.joda.time.Days;
import org.joda.time.LocalDate;
import org.libreplan.business.calendars.entities.Capacity;

/**
 * <p>
 * Instances of this class represent values so immutable objects are used. In
 * order to do modifications new instances must be created.
 * </p>
 * <p>
 * A date type that represents a point inside a working day. This doesn't
 * translate directly to a concrete DateTime because the working day can start
 * at an arbitrary hour.
 * </p>
 * <p>
 * It represents the instant at which {@link #effortDuration} has been elapsed
 * in the working day specified by the field {@link #date}. Since the amount of
 * time for a working day is measured with an {@link EffortDuration}, to
 * indicate the point inside a working day an {@link EffortDuration} is
 * specified.
 * </p>
 * <p>
 * For example, a IntraDayDate with a date such as 23/07/2010 and an effort
 * duration of 2 hours represents the moment on 23/07/2010 at which 2 hours of
 * the working day had been elapsed. Normally this object can't be converted to
 * a precise {@link DateTime} due to not knowing the timetable of the workers.
 * Nevertheless, this object is useful anyway in order to know how many effort
 * is left at the working day.
 * </p>
 *
 * @see PartialDay
 * @see LocalDate
 * @see EffortDuration
 * @author Óscar González Fernández
 *
 */
public class IntraDayDate implements Comparable<IntraDayDate> {

    public static IntraDayDate min(IntraDayDate... dates) {
        Validate.noNullElements(dates);
        return Collections.min(Arrays.asList(dates));
    }

    public static IntraDayDate max(IntraDayDate... dates) {
        Validate.noNullElements(dates);
        return Collections.max(Arrays.asList(dates));
    }

    public static IntraDayDate startOfDay(LocalDate date) {
        return create(date, zero());
    }

    public static IntraDayDate create(LocalDate date, EffortDuration effortDuration) {
        return new IntraDayDate(date, effortDuration);
    }

    private LocalDate date;

    private EffortDuration effortDuration;

    /**
     * Default constructor for hibernate do not use!
     */
    public IntraDayDate() {
    }

    private IntraDayDate(LocalDate date, EffortDuration effortDuration) {
        Validate.notNull(date);
        Validate.notNull(effortDuration);
        this.date = date;
        this.effortDuration = effortDuration;
    }

    @NotNull
    public LocalDate getDate() {
        return date;
    }

    /**
     * <p>
     * The duration elapsed projected to a day allocated with one resource per
     * day.
     * </p>
     * <p>
     * So, for example, if 8 hours are elapsed and the resources per day are 2,
     * this value should be 4. If 4 hours are elapsed and the resources per day
     * are 0.5, this value should be 8.
     * <p>
     * More generally, this value is the effort applied on {@link #date} divided
     * by the resources per day being applied.
     * </p>
     * <p>
     * This amount is useful in order to determine how much spare room for work
     * is left on {@link #date}.
     * </p>
     *
     * @return that duration
     */
    public EffortDuration getEffortDuration() {
        return effortDuration == null ? EffortDuration.zero() : effortDuration;
    }

    public boolean isStartOfDay() {
        return getEffortDuration().isZero();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof IntraDayDate) {
            IntraDayDate other = (IntraDayDate) obj;
            return this.date.equals(other.date) && this.getEffortDuration().equals(other.getEffortDuration());
        }
        return false;
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(this.date).append(this.getEffortDuration()).toHashCode();
    }

    public boolean areSameDay(Date date) {
        return areSameDay(LocalDate.fromDateFields(date));
    }

    public boolean areSameDay(LocalDate date) {
        return this.date.equals(date);
    }

    public DateTime toDateTimeAtStartOfDay() {
        return this.date.toDateTimeAtStartOfDay();
    }

    @Override
    public int compareTo(IntraDayDate other) {
        int result = date.compareTo(other.date);
        if (result == 0) {
            result = getEffortDuration().compareTo(other.getEffortDuration());
        }
        return result;
    }

    /**
     * Return the day which is the exclusive end given this {@link IntraDayDate}
     * @return
     */
    public LocalDate asExclusiveEnd() {
        if (isStartOfDay()) {
            return getDate();
        }
        return getDate().plusDays(1);
    }

    public int compareTo(LocalDate other) {
        int result = this.date.compareTo(other);
        if (result != 0) {
            return result;
        }
        return isStartOfDay() ? 0 : 1;
    }

    /**
     * It's an interval of {@link IntraDayDate}. It must not elapse more than
     * one day. It allows to represent a subinterval of the working day. It
     * allows to know how much effort can be spent in this day.
     */
    public static class PartialDay {

        public static PartialDay wholeDay(LocalDate date) {
            return new PartialDay(IntraDayDate.startOfDay(date), IntraDayDate.startOfDay(date.plusDays(1)));
        }

        private final IntraDayDate start;

        private final IntraDayDate end;

        public PartialDay(IntraDayDate start, IntraDayDate end) {
            Validate.notNull(start);
            Validate.notNull(end);
            Validate.isTrue(end.compareTo(start) >= 0);
            Validate.isTrue(start.areSameDay(end.getDate()) || (start.areSameDay(end.getDate().minusDays(1)) && end.getEffortDuration().isZero()));
            this.start = start;
            this.end = end;
        }

        public IntraDayDate getStart() {
            return start;
        }

        public IntraDayDate getEnd() {
            return end;
        }

        public LocalDate getDate() {
            return start.getDate();
        }

        /**
         * <p>
         * Limits the standard duration that can be worked in a day taking into
         * account this day duration.
         * </p>
         * <ul>
         * <li>
         * If the day is whole then the duration returned is all.</li>
         * <li>If the day has an end and a no zero start, the result will be:
         * <code>max(duration, end) - start</code></li>
         * <li>If the day has a no zero start that must be discounted from the
         * duration</li>
         * <li>If the day has an end, the duration must not surpass this end</li>
         * </ul>
         *
         * @param standardWorkingDayDuration
         * @return a duration that can be employed taking into consideration
         *         this day
         */
        public EffortDuration limitWorkingDay(EffortDuration standardWorkingDayDuration) {
            if (isWholeDay()) {
                return standardWorkingDayDuration;
            }
            EffortDuration alreadyElapsedInDay = start.getEffortDuration();
            if (alreadyElapsedInDay.compareTo(standardWorkingDayDuration) >= 0) {
                return zero();
            }
            EffortDuration durationLimitedByEnd = standardWorkingDayDuration;
            if (!end.getEffortDuration().isZero()) {
                durationLimitedByEnd = EffortDuration.min(end.getEffortDuration(), standardWorkingDayDuration);
            }
            return durationLimitedByEnd.minus(alreadyElapsedInDay);
        }

        public Capacity limitCapacity(Capacity capacity) {
            if (capacity.getAllowedExtraEffort() == null) {
                EffortDuration effort = limitWorkingDay(capacity.getStandardEffort());
                return Capacity.create(effort).overAssignableWithoutLimit(capacity.isOverAssignableWithoutLimit());
            }
            EffortDuration allEffort = capacity.getStandardEffort().plus(capacity.getAllowedExtraEffort());
            EffortDuration limited = limitWorkingDay(allEffort);
            EffortDuration newStandard = EffortDuration.min(limited, capacity.getStandardEffort());
            return Capacity.create(newStandard).withAllowedExtraEffort(EffortDuration.min(limited.minus(newStandard))).overAssignableWithoutLimit(capacity.isOverAssignableWithoutLimit());
        }

        private boolean isWholeDay() {
            return start.getEffortDuration().isZero() && end.getEffortDuration().isZero();
        }

        @Override
        public String toString() {
            return Arrays.toString(new Object[] { start, end });
        }
    }

    interface IterationPredicate {

        public boolean hasNext(IntraDayDate current);

        public IntraDayDate limitNext(IntraDayDate nextDay);
    }

    public static class UntilEnd implements IterationPredicate {

        private final IntraDayDate endExclusive;

        public UntilEnd(IntraDayDate endExclusive) {
            this.endExclusive = endExclusive;
        }

        @Override
        public final boolean hasNext(IntraDayDate current) {
            return hasNext(current.compareTo(endExclusive) < 0);
        }

        protected boolean hasNext(boolean currentDateIsLessThanEnd) {
            return currentDateIsLessThanEnd;
        }

        @Override
        public IntraDayDate limitNext(IntraDayDate nextDay) {
            return min(nextDay, endExclusive);
        }
    }

    /**
     * Returns an on demand {@link Iterable} that gives all the days from
     * <code>this</code> to end
     *
     * @param endExclusive
     * @return an on demand iterable
     */
    public Iterable<PartialDay> daysUntil(final IntraDayDate endExclusive) {
        Validate.isTrue(compareTo(endExclusive) <= 0);
        return daysUntil(new UntilEnd(endExclusive));
    }

    public int numberOfDaysUntil(IntraDayDate end) {
        Validate.isTrue(compareTo(end) <= 0);
        Days daysBetween = Days.daysBetween(getDate(), end.getDate());
        if (getEffortDuration().compareTo(end.getEffortDuration()) <= 0) {
            return daysBetween.getDays();
        } else {
            return daysBetween.getDays() - 1;
        }
    }

    public Iterable<PartialDay> daysUntil(final UntilEnd predicate) {
        return new Iterable<IntraDayDate.PartialDay>() {

            @Override
            public Iterator<PartialDay> iterator() {
                return createIterator(IntraDayDate.this, predicate);
            }
        };
    }

    public static List<PartialDay> toList(Iterable<PartialDay> days) {
        List<PartialDay> result = new ArrayList<IntraDayDate.PartialDay>();
        for (PartialDay each : days) {
            result.add(each);
        }
        return result;
    }

    private static Iterator<PartialDay> createIterator(final IntraDayDate start, final IterationPredicate predicate) {
        return new Iterator<IntraDayDate.PartialDay>() {

            private IntraDayDate current = start;

            @Override
            public boolean hasNext() {
                return predicate.hasNext(current);
            }

            @Override
            public PartialDay next() {
                if (!hasNext()) {
                    throw new NoSuchElementException();
                }
                IntraDayDate start = current;
                current = calculateNext(current);
                return new PartialDay(start, current);
            }

            private IntraDayDate calculateNext(IntraDayDate date) {
                IntraDayDate nextDay = IntraDayDate.startOfDay(date.date.plusDays(1));
                return predicate.limitNext(nextDay);
            }

            @Override
            public void remove() {
                throw new UnsupportedOperationException();
            }
        };
    }

    @Override
    public String toString() {
        return Arrays.toString(new Object[] { date, effortDuration });
    }

    public IntraDayDate nextDayAtStart() {
        return IntraDayDate.startOfDay(getDate().plusDays(1));
    }

    public IntraDayDate previousDayAtStart() {
        return IntraDayDate.startOfDay(getDate().minusDays(1));
    }

    /**
     * @return the next day or the same day if this {@link IntraDayDate} has no
     *         duration.
     */
    public LocalDate roundUp() {
        return asExclusiveEnd();
    }

    /**
     * @return A date resulting of striping this {@link IntraDayDate} of its
     *         duration
     */
    public LocalDate roundDown() {
        return date;
    }

    /**
     * Calculates a new {@link IntraDayDate} adding {@link EffortDuration
     * effort} to {@link IntraDayDate this}. It considers the provided
     * {@link ResourcesPerDay resourcesPerDay}, so if the resources per day is
     * big the effort taken will be less. The date will stay the same, i.e. the
     * returned {@link IntraDayDate} is on the same day.
     *
     * @param resourcesPerDay
     * @param effort
     * @return a new {@link IntraDayDate}
     */
    public IntraDayDate increaseBy(ResourcesPerDay resourcesPerDay, EffortDuration effort) {
        EffortDuration newEnd = this.getEffortDuration().plus(calculateProportionalDuration(resourcesPerDay, effort));
        return IntraDayDate.create(getDate(), newEnd);
    }

    private EffortDuration calculateProportionalDuration(ResourcesPerDay resourcesPerDay, EffortDuration effort) {
        int seconds = effort.getSeconds();
        BigDecimal end = new BigDecimal(seconds).divide(resourcesPerDay.getAmount(), RoundingMode.HALF_UP);
        return seconds(end.intValue());
    }

    /**
     * The same as
     * {@link IntraDayDate#increaseBy(ResourcesPerDay, EffortDuration)} but
     * decreasing the effort. The date will stay the same, i.e. the returned
     * {@link IntraDayDate} is on the same day.
     *
     * @see IntraDayDate#increaseBy(ResourcesPerDay, EffortDuration)
     * @param resourcesPerDay
     * @param effort
     * @return a new {@link IntraDayDate}
     */
    public IntraDayDate decreaseBy(ResourcesPerDay resourcesPerDay, EffortDuration effort) {
        EffortDuration proportionalDuration = calculateProportionalDuration(resourcesPerDay, effort);
        if (getEffortDuration().compareTo(proportionalDuration) > 0) {
            return IntraDayDate.create(getDate(), getEffortDuration().minus(proportionalDuration));
        } else {
            return IntraDayDate.startOfDay(getDate());
        }
    }

    public static IntraDayDate convert(LocalDate date, IntraDayDate morePreciseAlternative) {
        LocalDate morePreciseDate = morePreciseAlternative.getDate();
        if (morePreciseDate.equals(date)) {
            return morePreciseAlternative;
        }
        return startOfDay(date);
    }
}
