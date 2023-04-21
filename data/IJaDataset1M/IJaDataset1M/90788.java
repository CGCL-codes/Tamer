package org.torweg.pulse.util.time;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;
import javax.persistence.Basic;
import javax.persistence.Embeddable;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 * represents a duration in time.
 * 
 * @author Thomas Weber
 * @version $Revision: 2093 $
 */
@XmlRootElement(name = "duration")
@XmlAccessorType(XmlAccessType.PROPERTY)
@Embeddable
public final class Duration implements Comparable<TimeSpan>, IHasDuration, Serializable {

    /**
	 * serialVersionUID.
	 */
    private static final long serialVersionUID = -7767710131035673016L;

    /**
	 * the start date of the duration.
	 */
    @Basic
    private Long startDate;

    /**
	 * the end date of the duration.
	 */
    @Basic
    private Long endDate;

    /**
	 * creates a {@code Duration} starting now and lasting <tt>zero</tt>
	 * milliseconds.
	 */
    public Duration() {
        super();
        this.endDate = System.currentTimeMillis();
        this.startDate = this.endDate;
    }

    /**
	 * creates a new {@code Duration} lasting for the given time span string and
	 * <tt>now</tt> as the reference date.
	 * 
	 * @param ts
	 *            the time span as a string
	 */
    public Duration(final String ts) {
        super();
        this.startDate = System.currentTimeMillis();
        this.endDate = this.startDate + new TimeSpan(ts).getMilliseconds();
        normalize();
    }

    /**
	 * creates a new {@code Duration} lasting for the given time span.
	 * <p>
	 * If the time span is negative, the duration's start date is
	 * <tt>now - time span</tt>. Otherwise the start date is <tt>now</tt>.
	 * </p>
	 * 
	 * @param ts
	 *            the time span
	 */
    public Duration(final TimeSpan ts) {
        super();
        this.startDate = System.currentTimeMillis();
        this.endDate = this.startDate + ts.getMilliseconds();
        normalize();
    }

    /**
	 * creates a new {@code Duration} lasting for the given time span and the
	 * given reference date.
	 * 
	 * @param d
	 *            the reference date
	 * @param ts
	 *            the time span
	 */
    public Duration(final Date d, final String ts) {
        super();
        this.startDate = d.getTime();
        this.endDate = this.startDate + new TimeSpan(ts).getMilliseconds();
        normalize();
    }

    /**
	 * creates a new {@code Duration} lasting for the given time span and the
	 * given reference date.
	 * 
	 * @param d
	 *            the reference date
	 * @param ts
	 *            the time span
	 */
    public Duration(final Date d, final TimeSpan ts) {
        super();
        this.startDate = d.getTime();
        this.endDate = this.startDate + ts.getMilliseconds();
        normalize();
    }

    /**
	 * creates a new {@code Duration} with a positive {@code TimeSpan} , making
	 * the less recent {@code Date} the reference date.
	 * 
	 * @param a
	 *            boundary date a
	 * @param b
	 *            boundary date b
	 */
    public Duration(final Date a, final Date b) {
        super();
        this.startDate = a.getTime();
        this.endDate = b.getTime();
        normalize();
    }

    /**
	 * creates a new {@code Duration} with a positive {@code TimeSpan} , making
	 * the less recent {@code Calendar} the reference date.
	 * 
	 * @param a
	 *            boundary {@code Calendar} a
	 * @param b
	 *            boundary {@code Calendar} b
	 */
    public Duration(final Calendar a, final Calendar b) {
        super();
        this.startDate = a.getTimeInMillis();
        this.endDate = b.getTimeInMillis();
        normalize();
    }

    /**
	 * creates a new {@code Duration} with a positive {@code TimeSpan} , making
	 * the less recent time-stamp the reference date.
	 * 
	 * @param a
	 *            boundary time-stamp a
	 * @param b
	 *            boundary time-stamp b
	 */
    public Duration(final long a, final long b) {
        super();
        this.startDate = a;
        this.endDate = b;
        normalize();
    }

    /**
	 * Creates a new {@code Duration} from the given {@code Duration}.
	 * 
	 * @param duration
	 *            the {@code Duration}
	 */
    public Duration(final Duration duration) {
        super();
        this.startDate = duration.getStartMillis();
        this.endDate = duration.getEndMillis();
    }

    /**
	 * used to normalise the {@code Duration}.
	 */
    private void normalize() {
        if (this.startDate > this.endDate) {
            long end = this.endDate;
            this.endDate = this.startDate;
            this.startDate = end;
        }
    }

    /**
	 * returns the start date.
	 * 
	 * @return the start date
	 */
    @XmlAttribute(name = "start-date", required = true)
    public Date getStartDate() {
        if (this.endDate >= 0) {
            return new Date(this.startDate);
        }
        return new Date(this.startDate + this.endDate);
    }

    /**
	 * returns the UTC start date.
	 * 
	 * @return the UTC start date
	 */
    @XmlAttribute(name = "start-date-utc-string")
    public String getStartDateUTCString() {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS+00:00", Locale.getDefault());
        format.setTimeZone(TimeZone.getTimeZone("UTC"));
        return format.format(getStartDate());
    }

    /**
	 * Returns the milliseconds-value of the start of the {@code Duration}.
	 * 
	 * @return <tt>startDate</tt>
	 */
    @XmlElement(name = "start-millis")
    public long getStartMillis() {
        return this.startDate;
    }

    /**
	 * Sets the given value as <tt>startDate</tt>.
	 * 
	 * @param millis
	 *            the value to set.
	 * 
	 * @throws IllegalArgumentException
	 *             if the given milliseconds lie after or are equal to the
	 *             <tt>endDate</tt>
	 */
    public void setStartDateMillis(final long millis) {
        if (millis >= this.endDate) {
            throw new IllegalArgumentException("The value for the start date must not be larger than or equal to " + "the end date of the duration.");
        }
        this.endDate = millis;
    }

    /**
	 * returns the end date.
	 * 
	 * @return the end date
	 */
    @XmlAttribute(name = "end-date")
    public Date getEndDate() {
        return new Date(this.endDate);
    }

    /**
	 * returns the UTC end date.
	 * 
	 * @return the UTC end date
	 */
    @XmlAttribute(name = "end-date-utc-string")
    public String getEndDateUTCString() {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS+00:00", Locale.getDefault());
        format.setTimeZone(TimeZone.getTimeZone("UTC"));
        return format.format(getEndDate());
    }

    /**
	 * Returns the milliseconds-value of the end of the {@code Duration}.
	 * 
	 * @return <tt>endDate</tt>
	 */
    @XmlElement(name = "end-millis")
    public long getEndMillis() {
        return this.endDate;
    }

    /**
	 * Sets the given value as <tt>endDate</tt>.
	 * 
	 * @param millis
	 *            the value to set.
	 * 
	 * @throws IllegalArgumentException
	 *             if the given milliseconds lie before or are equal to the
	 *             <tt>startDate</tt>
	 */
    public void setEndDateMillis(final long millis) {
        if (millis <= this.startDate) {
            throw new IllegalArgumentException("The value for the end date must not be smaller than or equal to " + "the start date of the duration.");
        }
        this.endDate = millis;
    }

    /**
	 * returns the reference date.
	 * 
	 * @return the reference date
	 */
    @XmlTransient
    public Date getReferenceDate() {
        return new Date(this.startDate);
    }

    /**
	 * returns the milliseconds.
	 * 
	 * @return the milliseconds
	 */
    @XmlTransient
    public long getMilliseconds() {
        return this.endDate - this.startDate;
    }

    /**
	 * returns the {@code TimeSpan} of the {@code Duration}.
	 * 
	 * @return the time span
	 */
    @XmlTransient
    public TimeSpan getTimeSpan() {
        return new TimeSpan(this.endDate - this.startDate);
    }

    /**
	 * sets the milliseconds for the time span.
	 * 
	 * @param ms
	 *            the time span
	 */
    public void setMilliseconds(final long ms) {
        this.endDate = this.startDate + ms;
        normalize();
    }

    /**
	 * returns the string value.
	 * 
	 * @return the string value
	 */
    @XmlAttribute(name = "span")
    public String getSpan() {
        return new TimeSpan(this.endDate - this.startDate).toString();
    }

    /**
	 * sets the time span.
	 * 
	 * @param s
	 *            the time span string
	 */
    public void setSpan(final String s) {
        this.endDate = this.startDate + new TimeSpan(s).getMilliseconds();
        normalize();
    }

    /**
	 * adds a given time span to this time span.
	 * 
	 * @param s
	 *            the time span to add
	 * @return this time span plus the given time span
	 */
    public TimeSpan add(final TimeSpan s) {
        setMilliseconds(this.endDate + s.getMilliseconds());
        return new TimeSpan(this.endDate - this.startDate);
    }

    /**
	 * subtracts a given time span from this time span.
	 * 
	 * @param s
	 *            the time span to add
	 * @return this time span minus the given time span
	 */
    public TimeSpan subtract(final TimeSpan s) {
        setMilliseconds(this.endDate - s.getMilliseconds());
        return new TimeSpan(this.endDate - this.startDate);
    }

    /**
	 * returns a new {@code Date} with the time span added to the {@code Date}.
	 * 
	 * @param d
	 *            the date
	 * @return the computed date
	 */
    public Date addTo(final Date d) {
        return new Date(d.getTime() + getMilliseconds());
    }

    /**
	 * returns a new {@code Date} with the time span subtracted from the
	 * {@code Date}.
	 * 
	 * @param d
	 *            the date
	 * @return the computed date
	 */
    public Date subtractFrom(final Date d) {
        return new Date(d.getTime() - getMilliseconds());
    }

    /**
	 * @return the hash code
	 */
    @Override
    public int hashCode() {
        int hc = this.endDate.hashCode();
        hc = hc * 31 + this.startDate.hashCode();
        return hc;
    }

    /**
	 * checks whether the given object is equal to the duration.
	 * 
	 * @param obj
	 *            the object to check against
	 * @return {@code true}, if and only if both start and end dates are equal
	 */
    @Override
    public boolean equals(final Object obj) {
        if ((obj != null) && obj.getClass().equals(this.getClass())) {
            Duration d = (Duration) obj;
            return (this.getStartDate().equals(d.getStartDate()) && this.getEndDate().equals(d.getEndDate()));
        }
        return false;
    }

    /**
	 * compare the time spans of the duration with the given time span.
	 * 
	 * @param o
	 *            the time span to compare with
	 * @return a negative integer, zero, or a positive integer as this time span
	 *         is less than, equal to, or greater than the specified time span.
	 * @see java.lang.Comparable#compareTo(java.lang.Object)
	 */
    public int compareTo(final TimeSpan o) {
        return new TimeSpan(this.endDate - this.startDate).compareTo(o);
    }

    /**
	 * returns whether a given date is contained in the duration.
	 * 
	 * @param date
	 *            the date
	 * @return {@code true}, if and only if the given date is within the
	 *         duration. Otherwise {@code false}.
	 */
    public boolean contains(final Date date) {
        if (date == null) {
            return false;
        }
        return contains(date.getTime());
    }

    /**
	 * returns whether a given time stamp is contained in the duration.
	 * 
	 * @param time
	 *            the time stamp
	 * @return {@code true}, if and only if the given time stamp is within the
	 *         duration. Otherwise {@code false}.
	 */
    public boolean contains(final long time) {
        return (time >= getStartDate().getTime()) && (time <= getEndDate().getTime());
    }

    /**
	 * Checks if the given duration is within or equal the duration.
	 * 
	 * @param duration
	 *            the {@code Duration}
	 * 
	 * @return {@code true}, if and only if the given {@code Duration} is within
	 *         or equal the duration, {@code false} otherwise
	 */
    public boolean contains(final Duration duration) {
        if (duration == null) {
            return false;
        }
        return (duration.getStartMillis() >= getStartMillis() && duration.getEndMillis() <= getEndMillis());
    }

    /**
	 * Returns a new {@code Duration} with the values of the {@code Duration}.
	 * 
	 * @return a new {@code Duration} with the values of the {@code Duration}
	 * 
	 * @see org.torweg.pulse.util.time.IHasDuration#getDuration()
	 */
    public Duration getDuration() {
        return new Duration(this);
    }

    /**
	 * Applies the given offset to the start/end time stamp of {@code Duration}
	 * if the offset is not {@code null}.
	 * 
	 * @param offset
	 *            the offset to scroll by
	 * 
	 * @return the modified {@code Duration}
	 */
    public Duration applyOffset(final Long offset) {
        if (offset == null) {
            return this;
        }
        this.startDate += offset;
        this.endDate += offset;
        return this;
    }

    /**
	 * Modifies the start/end time stamp of the {@code Duration} to match the
	 * start/end of the given {@code Period}.
	 * 
	 * @param period
	 *            {@code Period}
	 * 
	 * @return the modified {@code Duration}
	 */
    public Duration levelBoundaries(final Period period) {
        if (!period.equals(Period.UNDEFINED)) {
            this.startDate = Period.Level.level(this.startDate, period, Period.Level.START).getTimeInMillis();
            this.endDate = Period.Level.level(this.endDate, period, Period.Level.END).getTimeInMillis();
        }
        return this;
    }

    /**
	 * Returns the {@code Period} represented by the {@code Duration}.
	 * 
	 * @return the {@code Period} the {@code Duration} represents
	 */
    public Period getPeriod() {
        return Period.valueof(this);
    }

    /**
	 * @return a string-representation of the {@code Duration}
	 */
    @Override
    @SuppressWarnings("deprecation")
    public String toString() {
        return "{" + super.toString() + "[" + getStartDate().toGMTString() + " - " + getEndDate().toGMTString() + ", " + getSpan() + "]}";
    }
}
