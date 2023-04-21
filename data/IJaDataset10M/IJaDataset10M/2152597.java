package org.aiotrade.math.timeseries;

import java.util.HashSet;
import java.util.Set;
import java.util.TimeZone;
import org.aiotrade.util.serialization.BeansDocument;
import org.aiotrade.util.serialization.DeserializationConstructor;
import org.aiotrade.util.serialization.JavaDocument;
import org.w3c.dom.Element;

/**
 * Class combining Unit and nUnits.
 * Try to implement a Primitive-like type.
 * Use final modifies to define a lightweight class.
 *
 * This class is better to be treated as a <b>value</b> class, so, using:
 *   <code>freq = anotherFreq.clone();</code>
 * instead of:
 *   <code>freq = anotherFreq;</code>
 * is always more safe.
 *
 * @author Caoyuan Deng
 */
public final class Frequency implements Cloneable, Comparable<Frequency> {

    public static final Frequency SELF_DEFINED = new Frequency(Unit.Second, 0);

    public static final Frequency ONE_SEC = new Frequency(Unit.Second, 1);

    public static final Frequency TWO_SECS = new Frequency(Unit.Second, 2);

    public static final Frequency THREE_SECS = new Frequency(Unit.Second, 3);

    public static final Frequency FOUR_SECS = new Frequency(Unit.Second, 3);

    public static final Frequency FIVE_SECS = new Frequency(Unit.Second, 5);

    public static final Frequency FIFTEEN_SECS = new Frequency(Unit.Second, 15);

    public static final Frequency THIRTY_SECS = new Frequency(Unit.Second, 30);

    public static final Frequency ONE_MIN = new Frequency(Unit.Minute, 1);

    public static final Frequency TWO_MINS = new Frequency(Unit.Minute, 2);

    public static final Frequency THREE_MINS = new Frequency(Unit.Minute, 3);

    public static final Frequency FOUR_MINS = new Frequency(Unit.Minute, 3);

    public static final Frequency FIVE_MINS = new Frequency(Unit.Minute, 5);

    public static final Frequency FIFTEEN_MINS = new Frequency(Unit.Minute, 15);

    public static final Frequency THIRTY_MINS = new Frequency(Unit.Minute, 30);

    public static final Frequency ONE_HOUR = new Frequency(Unit.Hour, 1);

    public static final Frequency DAILY = new Frequency(Unit.Day, 1);

    public static final Frequency TWO_DAYS = new Frequency(Unit.Day, 2);

    public static final Frequency THREE_DAYS = new Frequency(Unit.Day, 3);

    public static final Frequency FOUR_DAYS = new Frequency(Unit.Day, 4);

    public static final Frequency FIVE_DAYS = new Frequency(Unit.Day, 5);

    public static final Frequency WEEKLY = new Frequency(Unit.Week, 1);

    public static final Frequency MONTHLY = new Frequency(Unit.Month, 1);

    public static final Frequency THREE_MONTHS = new Frequency(Unit.Month, 3);

    public static final Frequency ONE_YEAR = new Frequency(Unit.Year, 1);

    private static Set<Frequency> PREDEFINED;

    public final Unit unit;

    public final int nUnits;

    public final long interval;

    /** @TODO temporary solution */
    private static final int offsetToUTC = TimeZone.getDefault().getRawOffset() - TimeZone.getTimeZone("UTC").getRawOffset();

    @DeserializationConstructor
    public Frequency(Unit unit, int nUnits) {
        this.unit = unit;
        this.nUnits = nUnits;
        this.interval = unit.getInterval() * nUnits;
    }

    public static Set<Frequency> predefined() {
        if (PREDEFINED == null) {
            PREDEFINED = new HashSet<Frequency>();
            PREDEFINED.add(ONE_MIN);
            PREDEFINED.add(TWO_MINS);
            PREDEFINED.add(THREE_MINS);
            PREDEFINED.add(FOUR_MINS);
            PREDEFINED.add(FIVE_MINS);
            PREDEFINED.add(FIFTEEN_MINS);
            PREDEFINED.add(THIRTY_MINS);
            PREDEFINED.add(DAILY);
            PREDEFINED.add(TWO_DAYS);
            PREDEFINED.add(THREE_DAYS);
            PREDEFINED.add(FOUR_DAYS);
            PREDEFINED.add(FIVE_DAYS);
            PREDEFINED.add(WEEKLY);
            PREDEFINED.add(MONTHLY);
        }
        return PREDEFINED;
    }

    public final Unit getUnit() {
        return unit;
    }

    public final int getNUnits() {
        return nUnits;
    }

    /**
     * return interval in milliseconds
     */
    public final long getInterval() {
        return interval;
    }

    public final long nextTime(long fromTime) {
        return unit.timeAfterNUnits(fromTime, nUnits);
    }

    public final long previousTime(long fromTime) {
        return unit.timeAfterNUnits(fromTime, -nUnits);
    }

    public final long timeAfterNFreqs(long fromTime, int nFreqs) {
        return unit.timeAfterNUnits(fromTime, nUnits * nFreqs);
    }

    public final int nFreqsBetween(long fromTime, long toTime) {
        return unit.nUnitsBetween(fromTime, toTime) / nUnits;
    }

    /**
     * round time to freq's begin 0
     * @param time time in milliseconds from the epoch (1 January 1970 0:00 UTC)
     */
    public final long round(long time) {
        return ((time + offsetToUTC) / interval) * interval - offsetToUTC;
    }

    public final boolean sameInterval(long timeA, long timeB) {
        return round(timeA) == round(timeB);
    }

    public final String getName() {
        if (nUnits == 1) {
            switch(unit) {
                case Hour:
                    return "Hourly";
                case Day:
                    return "Daily";
                case Week:
                    return "Weekly";
                case Month:
                    return "Monthly";
                case Year:
                    return "Yearly";
                default:
            }
        }
        StringBuilder sb = new StringBuilder(10).append(nUnits).append(unit.getCompactDescription());
        if (nUnits > 1) {
            sb.append("s");
        }
        return sb.toString();
    }

    public final boolean equals(Object o) {
        if (o instanceof Frequency) {
            final Frequency another = (Frequency) o;
            if (another.unit == this.unit && another.nUnits == this.nUnits) {
                return true;
            }
        }
        return false;
    }

    public final Frequency clone() {
        try {
            return (Frequency) super.clone();
        } catch (CloneNotSupportedException ex) {
            ex.printStackTrace();
        }
        return null;
    }

    public final int compareTo(Frequency another) {
        if (this.unit.ordinal() < another.unit.ordinal()) {
            return -1;
        } else if (this.unit.ordinal() > another.unit.ordinal()) {
            return 1;
        } else {
            return (this.nUnits < another.nUnits ? -1 : (this.nUnits == another.nUnits ? 0 : 1));
        }
    }

    public final int hashCode() {
        return (int) interval;
    }

    public final String toString() {
        return getName();
    }

    public Element writeToBean(BeansDocument doc) {
        final Element bean = doc.createBean(this);
        doc.valueConstructorArgOfBean(bean, 0, getUnit());
        doc.valueConstructorArgOfBean(bean, 1, getNUnits());
        return bean;
    }

    public String writeToJava(String id) {
        return JavaDocument.create(id, Frequency.class, getUnit(), getNUnits());
    }
}
