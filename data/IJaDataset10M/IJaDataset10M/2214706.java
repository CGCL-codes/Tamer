package org.goda.time.convert;

import org.goda.time.Chronology;
import org.goda.time.PeriodType;
import org.goda.time.ReadWritablePeriod;

/**
 * PeriodConverter defines how an object is converted to a time period.
 *
 * @author Stephen Colebourne
 * @author Brian S O'Neill
 * @since 1.0
 */
public interface PeriodConverter extends Converter {

    /**
     * Extracts duration values from an object of this converter's type, and
     * sets them into the given ReadWritableDuration.
     *
     * @param period  the period to modify
     * @param object  the object to convert, must not be null
     * @param chrono  the chronology to use, must not be null
     * @throws ClassCastException if the object is invalid
     */
    void setInto(ReadWritablePeriod period, Object object, Chronology chrono);

    /**
     * Selects a suitable period type for the given object.
     *
     * @param object  the object to examine, must not be null
     * @return the period type, never null
     * @throws ClassCastException if the object is invalid
     */
    PeriodType getPeriodType(Object object);
}
