package org.nomadpim.core.util.date;

import org.joda.time.DateTime;
import org.nomadpim.core.FilterAsserts;
import org.nomadpim.core.util.filter.IFilter;

public abstract class DateFilterTest {

    protected IFilter<DateTime> filter;

    protected void assertAccept(DateTime date) {
        FilterAsserts.assertAccept(filter, date);
    }

    protected void assertAccept(int ms) {
        assertAccept(new DateTime(ms));
    }

    protected void assertAccept(int year, int month, int day, int hour, int minute, int second, int ms) {
        assertAccept(new DateTime(year, month, day, hour, minute, second, ms));
    }

    protected void assertReject(DateTime date) {
        FilterAsserts.assertReject(filter, date);
    }

    protected void assertReject(int ms) {
        assertReject(new DateTime(ms));
    }

    protected void assertReject(int year, int month, int day, int hour, int minute, int second, int ms) {
        assertReject(new DateTime(year, month, day, hour, minute, second, ms));
    }
}
