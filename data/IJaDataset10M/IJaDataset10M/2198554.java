package net.sf.xtvdclient.xtvd.datatypes;

/**
 * A <code>ordinal-based typesafe enum</code> class that represents
 * the pre-defined values for <code>tms:movieAdvisoriess</code> as defined
 * in the XTVD schema.
 *
 * @author Rakesh Vidyadharan 26<sup><small>th</small></sup> February, 2004
 * @since ddclient 1.2
 *        <p/>
 *        <p>Copyright 2004, Tribune Media Services</p>
 *        <p/>
 *        $Id: MovieAdvisories.java,v 1.4 2004/03/26 15:56:16 rakesh Exp $
 */
public class MovieAdvisories implements Comparable {

    /**
   * An instance variable that stores the actual value of the
   * movieAdvisories.
   */
    private final String advisory;

    /**
   * The typesafe representation of the <code>Adult Situations</code>
   * movieAdvisories.
   */
    public static final MovieAdvisories ADULT_SITUATIONS = new MovieAdvisories("Adult Situations");

    /**
   * The typesafe representation of the <code>Adolescentes y Adultos</code>
   * movieAdvisories.
   */
    public static final MovieAdvisories ADOLESCENTES_Y_ADULTOS = new MovieAdvisories("Adolescentes y Adultos");

    /**
   * The typesafe representation of the <code>Adultos</code>
   * movieAdvisories.
   */
    public static final MovieAdvisories ADULTOS = new MovieAdvisories("Adultos");

    /**
   * The typesafe representation of the <code>Brief Nudity</code>
   * movieAdvisories.
   */
    public static final MovieAdvisories BRIEF_NUDITY = new MovieAdvisories("Brief Nudity");

    /**
   * The typesafe representation of the <code>Graphic Language</code>
   * movieAdvisories.
   */
    public static final MovieAdvisories GRAPHIC_LANGUAGE = new MovieAdvisories("Graphic Language");

    /**
   * The typesafe representation of the <code>Graphic Violence</code>
   * movieAdvisories.
   */
    public static final MovieAdvisories GRAPHIC_VIOLENCE = new MovieAdvisories("Graphic Violence");

    /**
   * The typesafe representation of the <code>Language</code>
   * movieAdvisories.
   */
    public static final MovieAdvisories LANGUAGE = new MovieAdvisories("Language");

    /**
   * The typesafe representation of the <code>Mild Violence</code>
   * movieAdvisories.
   */
    public static final MovieAdvisories MILD_VIOLENCE = new MovieAdvisories("Mild Violence");

    /**
   * The typesafe representation of the <code>Nudity</code>
   * movieAdvisories.
   */
    public static final MovieAdvisories NUDITY = new MovieAdvisories("Nudity");

    /**
   * The typesafe representation of the <code>Publico General</code>
   * movieAdvisories.
   */
    public static final MovieAdvisories PUBLICO_GENERAL = new MovieAdvisories("Publico General");

    /**
   * The typesafe representation of the <code>Rape</code>
   * movieAdvisories.
   */
    public static final MovieAdvisories RAPE = new MovieAdvisories("Rape");

    /**
   * The typesafe representation of the <code>Strong Sexual
   * Content</code> movieAdvisories.
   */
    public static final MovieAdvisories STRONG_SEXUAL_CONTENT = new MovieAdvisories("Strong Sexual Content");

    /**
   * The typesafe representation of the <code>Violence</code>
   * movieAdvisories.
   */
    public static final MovieAdvisories VIOLENCE = new MovieAdvisories("Violence");

    /**
   * An array of all the enumerated types.
   *
   * @since ddclient version 1.3
   */
    private static final MovieAdvisories[] ADVISORIES_ARRAY = { ADULT_SITUATIONS, ADOLESCENTES_Y_ADULTOS, ADULTOS, BRIEF_NUDITY, GRAPHIC_LANGUAGE, GRAPHIC_VIOLENCE, LANGUAGE, MILD_VIOLENCE, NUDITY, PUBLICO_GENERAL, RAPE, STRONG_SEXUAL_CONTENT, VIOLENCE };

    /**
   * Ordinal of next movieAdvisories to be created.  Ordinal values
   * start from <code>1</code> to match database primary key values.
   */
    private static int nextOrdinal = 0;

    /**
   * Assign an ordinal to this movieAdvisories.
   *
   * @see #hashCode()
   */
    private final int ordinal = ++nextOrdinal;

    /**
   * Private constructor that is used internally to initialise the
   * typesafe movieAdvisory's.
   *
   * @param advisory The movieAdvisories value.
   */
    private MovieAdvisories(String advisory) {
        this.advisory = advisory;
    }

    /**
   * Return the appropriate <code>MovieAdvisory</code> based upon the
   * string specified.  If the appropriate movie advisory does not exist
   * return <code>null</code>.
   *
   * @param advisory The advisory value that is to be used
   *               to fetch the corresponding MovieAdvisories.
   * @return MovieAdvisories - The appropriate instance if a
   *         corresponding instance is found, <code>null</code> otherwise.
   */
    public static MovieAdvisories getMovieAdvisory(String advisory) {
        if (advisory == null) {
            return null;
        }
        for (MovieAdvisories advisories : ADVISORIES_ARRAY) {
            if (advisory.equals(advisories.advisory)) {
                return advisories;
            }
        }
        return null;
    }

    /**
   * Over-ridden to return the value of {@link #advisory}.
   *
   * @return String - The string representation of this class instance.
   */
    @Override
    public String toString() {
        return advisory;
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
        return ordinal - ((MovieAdvisories) object).ordinal;
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
        return (this.getClass().equals(object.getClass())) && (this.ordinal == ((MovieAdvisories) object).ordinal);
    }

    /**
   * Returns a hash code value for this class.  Return the value of
   * {@link #ordinal}.
   *
   * @return int - A hash code value for the object.
   */
    @Override
    public int hashCode() {
        return ordinal;
    }
}
