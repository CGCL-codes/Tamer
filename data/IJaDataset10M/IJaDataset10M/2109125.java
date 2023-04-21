package com.tms.webservices.applications.datatypes;

/**
 * A <code>ordinal-based typesafe enum</code> class that represents
 * the pre-defined values for <code>tms:tvRatings</code> as defined
 * in the XTVD schema.
 *
 * @author Rakesh Vidyadharan 01<sup><small>st</small></sup> March, 2004
 * @since ddclient 1.2
 *
 * <p>Copyright 2004, Tribune Media Services</p>
 *
 * $Id: TvRatings.java,v 1.1.1.1 2005/07/19 04:28:18 shawndr Exp $
 */
public class TvRatings extends AbstractDataType {

    /**
   * An instance variable that stores the actual value of the
   * tvRating.
   */
    private final String rating;

    /**
   * The typesafe representation of the <code>TV-Y</code> tvRating.
   */
    public static final TvRatings TVY = new TvRatings("TV-Y");

    /**
   * The typesafe representation of the <code>TV-Y7</code> tvRating.
   */
    public static final TvRatings TVY7 = new TvRatings("TV-Y7");

    /**
   * The typesafe representation of the <code>TV-G</code> tvRating.
   */
    public static final TvRatings TVG = new TvRatings("TV-G");

    /**
   * The typesafe representation of the <code>TV-PG</code> tvRating.
   */
    public static final TvRatings TVPG = new TvRatings("TV-PG");

    /**
   * The typesafe representation of the <code>TV-14</code> tvRating.
   */
    public static final TvRatings TV14 = new TvRatings("TV-14");

    /**
   * The typesafe representation of the <code>TV-MA</code> tvRating.
   */
    public static final TvRatings TVMA = new TvRatings("TV-MA");

    /**
   * An array of all the enumerated types.
   *
   * @since ddclient version 1.3
   */
    private static final TvRatings[] RATINGS_ARRAY = { TVY, TVY7, TVG, TVPG, TV14, TVMA };

    /**
   * Ordinal of next tvRating to be created.
   */
    private static int nextOrdinal = 0;

    /**
   * Assign an ordinal to this tvRating.  Ordinal values start from 1
   * to correspond to database primary key values.
   *
   * @see #hashCode()
   */
    private final int ordinal = ++nextOrdinal;

    /**
   * Private constructor that is used internally to initialise the
   * typesafe tvRating's.
   *
   * @param String rating - The tvRating value.
   */
    private TvRatings(String rating) {
        this.rating = rating;
    }

    /**
   * Return the appropriate <code>TvRatings</code> based upon the
   * string specified.  If the appropriate tv rating does not exist
   * return <code>null</code>.
   *
   * @param String rating - The rating value that is to be used to fetch
   *   the corresponding TvRatings.
   * @return TvRatings - The appropriate instance if a corresponding
   *   instance is found, <code>null</code> otherwise.
   */
    public static final TvRatings getTvRating(String rating) {
        if (rating == null) return null;
        for (int i = 0; i < RATINGS_ARRAY.length; ++i) {
            if (rating.equals(RATINGS_ARRAY[i].rating)) {
                return RATINGS_ARRAY[i];
            }
        }
        return null;
    }

    /**
   * Over-ridden to return the value of {@link #rating}.
   *
   * @return String - The string representation of this class instance.
   */
    public String toString() {
        return rating;
    }

    /**
   * Implementation of the <code>Comparable</code> interface.
   * Compares this object with the specified object for order. Returns
   * a negative integer, zero, or a positive integer as this object is
   * less than, equal to, or greater than the specified object.
   *
   * @param Object object - The object with which this class is to
   *   be compared.  No class type checking is done.
   * @return int - A negative integer, zero, or a positive integer as
   *   this object is less than, equal to, or greater than the
   *   specified object.
   */
    public int compareTo(Object object) {
        return ordinal - ((TvRatings) object).ordinal;
    }

    /**
   * Indicates whether some other object is "equal to" this one.
   * Returns true if the specified object is of the same class type,
   * and has the same {@link #ordinal} value.
   *
   * @param Object object - The reference object with which to compare.
   * @return boolean - Return <code>true</code> if the match succeeds.
   */
    public boolean equals(Object object) {
        return (this.getClass().equals(object.getClass())) && (this.ordinal == ((TvRatings) object).ordinal);
    }

    /**
   * Returns a hash code value for this class.  Return the value of
   * {@link #ordinal}.
   *
   * @return int - A hash code value for the object.
   */
    public int hashCode() {
        return ordinal;
    }
}
