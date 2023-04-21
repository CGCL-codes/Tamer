package com.mvu.banana.common;

import org.fest.assertions.ObjectAssert;
import org.joda.time.DateTime;
import java.util.Date;
import static org.fest.assertions.Assertions.assertThat;

/**
 */
public class DateAssert extends ObjectAssert {

    private DateTime date;

    public DateAssert(Date date) {
        super(date);
        this.date = new DateTime(date);
    }

    public DateAssert hasDay(int day) {
        assertThat(date.dayOfMonth().get()).isEqualTo(day);
        return this;
    }

    public DateAssert hasMonth(int month) {
        assertThat(date.monthOfYear().get()).isEqualTo(month);
        return this;
    }

    public DateAssert hasYear(int year) {
        assertThat(date.year().get()).isEqualTo(year);
        return this;
    }
}
