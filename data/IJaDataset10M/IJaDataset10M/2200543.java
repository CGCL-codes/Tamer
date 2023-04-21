package org.orekit.time;

import java.io.Serializable;
import java.util.Comparator;

/** Comparator for {@link TimeStamped} instance.
 * <p>This comparator is implemented as a singleton.</p>
 * @see AbsoluteDate
 * @see TimeStamped
 * @author Luc Maisonobe
 * @version $Revision: 1818 $ $Date: 2008-08-07 11:12:39 +0200 (jeu 07 aoû 2008) $
 */
public class ChronologicalComparator implements Comparator<TimeStamped>, Serializable {

    /** Serializable UID. */
    private static final long serialVersionUID = -602997163791160921L;

    /** Private constructor for singleton.
     */
    private ChronologicalComparator() {
    }

    /** Get the unique instance.
     * @return the unique instance
     */
    public static ChronologicalComparator getInstance() {
        return LazyHolder.INSTANCE;
    }

    /** Compare two time-stamped instances.
     * @param timeStamped1 first time-stamped instance
     * @param timeStamped2 second time-stamped instance
     * @return a negative integer, zero, or a positive integer as the first
     * instance is before, simultaneous, or after the second one.
     */
    public int compare(final TimeStamped timeStamped1, final TimeStamped timeStamped2) {
        return timeStamped1.getDate().compareTo(timeStamped2.getDate());
    }

    /** Holder for the ChronologicalComparator frame singleton. */
    private static class LazyHolder {

        /** Unique instance. */
        private static final ChronologicalComparator INSTANCE = new ChronologicalComparator();

        /** Private constructor.
         * <p>This class is a utility class, it should neither have a public
         * nor a default constructor. This private constructor prevents
         * the compiler from generating one automatically.</p>
         */
        private LazyHolder() {
        }
    }
}
