package org.research.sdbx.datetime;

import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

/**
 * TimeInstance is mutable and not thread-safe.
 * @author Saad.Khawaja
 *
 */
public interface TimeInstance {

    public abstract TimeInstance addYear(int amount);

    public abstract TimeInstance addMonth(int amount);

    public abstract TimeInstance addWeek(int amount);

    public abstract TimeInstance addDayOfMonth(int amount);

    public abstract TimeInstance addHourOfDay(int amount);

    public abstract TimeInstance addMinute(int amount);

    public abstract TimeInstance addSecond(int amount);

    public abstract TimeInstance addMillisecond(int amount);

    public abstract int getYear();

    public abstract Month getMonth();

    public abstract int getWeekOfMonth();

    public abstract int getWeekOfYear();

    public abstract int getDayOfMonth();

    public abstract DayOfWeek getDayOfWeek();

    public abstract int getHourOfDay();

    public abstract int getHour();

    public abstract int getMinute();

    public abstract int getSecond();

    public abstract int getMillisecond();

    public abstract TimeZone getTimeZone();

    public abstract TimePrecision getTimePrecision();

    public abstract long getTimeInMillis();

    public abstract TimeInstance setYear();

    public abstract TimeInstance setMonth(Month month);

    public abstract TimeInstance setWeekOfYear();

    public abstract TimeInstance setWeekOfMonth();

    public abstract TimeInstance setDayOfMonth();

    public abstract TimeInstance setHourOfDay();

    public abstract TimeInstance setHour();

    public abstract TimeInstance setMinute();

    public abstract TimeInstance setSecond();

    public abstract TimeInstance setMillisecond();

    public abstract TimeInstance setTimeZone(TimeZone timeZone);

    public abstract TimeInstance setTimePrecision(TimePrecision timePrecision);

    public abstract TimeDifference getTimeDifference(TimeInstance timeInstance);

    public boolean before(TimeInstance arg);

    public boolean before(Date date);

    public boolean before(Calendar calendar);

    public boolean before(TimeRange timeRange);

    public boolean after(TimeInstance timeInstance);

    public boolean after(Date date);

    public boolean after(Calendar calendar);

    public boolean after(TimeRange timeRange);

    public boolean equals(Object object);

    public boolean equals(TimeInstance timeInstance, TimePrecision precision);

    public boolean between(TimeRange timeRange);

    public boolean betweenInclusive(TimeRange timeRange);

    public boolean beforeOrOnStart(TimeRange timeRange);

    public boolean betweenOrOnStart(TimeRange timeRange);

    public boolean betweenOrOnEnd(TimeRange timeRange);
}
