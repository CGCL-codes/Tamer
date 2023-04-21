package org.deri.iris.terms.concrete;

import java.net.URI;
import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeConstants;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;
import org.deri.iris.api.terms.ITerm;
import org.deri.iris.api.terms.concrete.IGYear;

/**
 * <p>
 * Simple implementation of the IGYear.
 * </p>
 * <p>
 * This class represents a year. <br/><b>ATTENTION:</b> At the moment it is
 * not possible to represent BC dates
 * </p>
 * <p>
 * $Id$
 * </p>
 * @author Richard Pöttler (richard dot poettler at deri dot at)
 * @version $Revision$
 */
public class GYear implements IGYear {

    /** Factory used to create the xml durations. */
    private static final DatatypeFactory FACTORY;

    /** The inner calendar object. */
    private final XMLGregorianCalendar date;

    static {
        DatatypeFactory tmp = null;
        try {
            tmp = DatatypeFactory.newInstance();
        } catch (DatatypeConfigurationException e) {
            throw new IllegalArgumentException("Couldn't create the factory for the year", e);
        }
        FACTORY = tmp;
    }

    /**
	 * Creates a new year. The zimezone will be set to GMT.
	 * @param year the year
	 */
    public GYear(final int year) {
        this(year, 0, 0);
    }

    /**
	 * Creates a new year within the given timezone.
	 * @param year the year
	 * @param tzHour the timezone hours (relative to GMT)
	 * @param tzMinute the timezone minutes (relative to GMT)
	 * @throws IllegalArgumentException if the tzHour and tzMinute
	 * wheren't both positive, or negative
	 */
    public GYear(final int year, final int tzHour, final int tzMinute) {
        if (((tzHour < 0) && (tzMinute > 0)) || ((tzHour > 0) && (tzMinute < 0))) {
            throw new IllegalArgumentException("Both, the timezone hours and " + "minutes must be negative, or positive, but were " + tzHour + " and " + tzMinute);
        }
        date = FACTORY.newXMLGregorianCalendarDate(year, DatatypeConstants.FIELD_UNDEFINED, DatatypeConstants.FIELD_UNDEFINED, tzHour * 60 + tzMinute);
    }

    public int compareTo(ITerm o) {
        if (o == null) {
            return 1;
        }
        GYear gy = (GYear) o;
        return getYear() - gy.getValue();
    }

    public boolean equals(final Object obj) {
        if (!(obj instanceof IGYear)) {
            return false;
        }
        IGYear gy = (IGYear) obj;
        return gy.getYear() == getYear();
    }

    public int getYear() {
        return date.getYear();
    }

    public int hashCode() {
        return date.hashCode();
    }

    public String toString() {
        return date.toString();
    }

    public boolean isGround() {
        return true;
    }

    public Integer getValue() {
        return date.getYear();
    }

    public URI getDatatypeIRI() {
        return URI.create("http://www.w3.org/2001/XMLSchema#gYear");
    }

    public String toCanonicalString() {
        return date.toString();
    }
}
