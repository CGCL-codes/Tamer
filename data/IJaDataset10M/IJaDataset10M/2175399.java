package org.tzi.use.gui.main.sorting;

/**
 *
 * @author <a href="mailto:gutsche@tzi.de">Fabian Gutsche</a>
 * @version $ProjectVersion: 0.393 $
 */
public class CompareUtilImpl implements CompareUtil {

    /**
     * Basic comparison operation to compare two integers
     * @param firstInt first integer
     * @param secondInt second integer
     * @return a negative integer, zero, or a positive integer as the
     *         first argument is less than, equal to, or greater than the
     *         second.
     */
    public int compareInt(final int firstInt, final int secondInt) {
        return new Integer(firstInt).compareTo(new Integer(secondInt));
    }

    /**
     * Basic comparison operation to compare two strings
     * @param firstString first string
     * @param secondString second string
     * @return a negative integer, zero, or a positive integer as the
     *         first argument is less than, equal to, or greater than the
     *         second.
     */
    public int compareString(final String firstString, final String secondString) {
        return firstString.compareToIgnoreCase(secondString);
    }
}
