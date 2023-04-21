package com.google.ical.iter;

import com.google.ical.util.Predicate;
import com.google.ical.values.DateValue;

/**
 * factory for predicates used to test whether a recurrence is over.
 *
 * @author mikesamuel+svn@gmail.com (Mike Samuel)
 */
final class Conditions {

    /** constructs a condition that fails after passing count dates. */
    static Predicate<DateValue> countCondition(final int count) {
        return new Predicate<DateValue>() {

            int count_ = count;

            public boolean apply(DateValue _) {
                return --count_ >= 0;
            }

            @Override
            public String toString() {
                return "CountCondition:" + count_;
            }
        };
    }

    /**
   * constructs a condition that passes for every date on or before until.
   * @param until non null.
   */
    static Predicate<DateValue> untilCondition(final DateValue until) {
        return new Predicate<DateValue>() {

            public boolean apply(DateValue date) {
                return date.compareTo(until) <= 0;
            }

            @Override
            public String toString() {
                return "UntilCondition:" + until;
            }
        };
    }

    private Conditions() {
    }
}
