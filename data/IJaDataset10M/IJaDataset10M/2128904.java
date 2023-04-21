package net.sf.xtvdclient.xtvd.datatypes;

/**
 * A <code>ordinal-based typesafe enum</code> class that represents
 * the pre-defined values for <code>tms:starRating</code> as defined
 * in the XTVD schema.
 *
 * @author Rakesh Vidyadharan 8<sup><small>th</small></sup> March, 2004
 * @since ddclient 1.2
 *        <p/>
 *        <p>Copyright 2004, Tribune Media Services</p>
 *        <p/>
 *        $Id: StarRating.java,v 1.4 2004/03/26 15:56:16 rakesh Exp $
 */
public class StarRating implements Comparable {

    /**
   * An instance variable that stores the actual value of the
   * starRating.
   */
    private final String rating;

    /**
   * The typesafe representation of the <code>*</code> starRating.
   */
    public static final StarRating ONE_STAR = new StarRating("*");

    /**
   * The typesafe representation of the <code>*+</code> starRating.
   */
    public static final StarRating ONE_AND_HALF_STAR = new StarRating("*+");

    /**
   * The typesafe representation of the <code>**</code> starRating.
   */
    public static final StarRating TWO_STAR = new StarRating("**");

    /**
   * The typesafe representation of the <code>**+</code> starRating.
   */
    public static final StarRating TWO_AND_HALF_STAR = new StarRating("**+");

    /**
   * The typesafe representation of the <code>***</code> starRating.
   */
    public static final StarRating THREE_STAR = new StarRating("***");

    /**
   * The typesafe representation of the <code>***+</code> starRating.
   */
    public static final StarRating THREE_AND_HALF_STAR = new StarRating("***+");

    /**
   * The typesafe representation of the <code>****</code> starRating.
   */
    public static final StarRating FOUR_STAR = new StarRating("****");

    /**
   * An array of all the enumerated types.
   *
   * @since ddclient version 1.3
   */
    private static final StarRating[] RATING_ARRAY = { ONE_STAR, ONE_AND_HALF_STAR, TWO_STAR, TWO_AND_HALF_STAR, THREE_STAR, THREE_AND_HALF_STAR, FOUR_STAR };

    /**
   * Ordinal of next starRating to be created.
   */
    private static int nextOrdinal;

    /**
   * Assign an ordinal to this starRating.  Ordinal values start
   * from 1 to correspond to database primary key values.
   *
   * @see #hashCode()
   */
    private final int ordinal = ++nextOrdinal;

    /**
   * Private constructor that is used internally to initialise the
   * typesafe starRating's.
   *
   * @param rating The starRating value.
   */
    private StarRating(String rating) {
        this.rating = rating;
    }

    /**
   * Return the appropriate <code>StarRating</code> based upon the
   * string specified.  If the appropriate tv rating does not exist
   * return <code>null</code>.
   *
   * @param rating The rating value that is to be used to fetch
   *               the corresponding StarRating.
   * @return The appropriate instance if a corresponding
   *         instance is found, <code>null</code> otherwise.
   */
    public static StarRating getStarRating(String rating) {
        if (rating == null) {
            return null;
        }
        for (StarRating ratings : RATING_ARRAY) {
            if (rating.equals(ratings.rating)) {
                return ratings;
            }
        }
        return null;
    }

    /**
   * Over-ridden to return the value of {@link #rating}.
   *
   * @return String - The string representation of this class instance.
   */
    @Override
    public String toString() {
        return rating;
    }

    /**
   * Implementation of the <code>Comparable</code> interface.
   * Compares this object with the specified object for order. Returns
   * a negative integer, zero, or a positive integer as this object is
   * less than, equal to, or greater than the specified object.
   *
   * @param object The object with which this class is to
   *               be compared.  No class type checking is done.
   * @return A negative integer, zero, or a positive integer as
   *         this object is less than, equal to, or greater than the
   *         specified object.
   */
    public int compareTo(Object object) {
        return ordinal - ((StarRating) object).ordinal;
    }

    /**
   * Indicates whether some other object is "equal to" this one.
   * Returns true if the specified object is of the same class type,
   * and has the same {@link #ordinal} value.
   *
   * @param object The reference object with which to compare.
   * @return Return <code>true</code> if the match succeeds.
   */
    @Override
    public boolean equals(Object object) {
        return (this.getClass().equals(object.getClass())) && (this.ordinal == ((StarRating) object).ordinal);
    }

    /**
   * Returns a hash code value for this class.  Return the value of
   * {@link #ordinal}.
   *
   * @return  A hash code value for the object.
   */
    @Override
    public int hashCode() {
        return ordinal;
    }
}
