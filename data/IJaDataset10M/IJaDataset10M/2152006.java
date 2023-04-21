package org.middleheaven.quantity.time;

public abstract class TimeZone {

    public static TimeZone getDefault() {
        return TimeContext.getTimeContext().getDefaultTimeZone();
    }

    public static TimeZone getGMTTimeZone(int hours) {
        return TimeContext.getTimeContext().getTimeZoneTable().getTimeZone("GMT" + hours + ":00");
    }

    public static TimeZone getTimeZone(String reference) {
        return TimeContext.getTimeContext().getTimeZoneTable().getTimeZone(reference);
    }

    public final Period getRawOffsetPeriod() {
        return Period.miliseconds(getRawOffset());
    }

    public abstract long getRawOffset();

    public abstract long getOffset(long universalTime);

    public abstract java.util.TimeZone toTimeZone();
}
