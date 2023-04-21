package org.ddsteps.samples.stringcomparator;

/**
 * A class containing a simple method that can be tested.
 * 
 * @author Adam Skogman
 * @author Bj�rn Granvik
 * @version $Id: StringComparator.java,v 1.2 2006/01/02 14:38:08 bjorngranvik Exp $
 */
public class StringComparator {

    /**
     * 
     */
    public StringComparator() {
        super();
    }

    /**
     * This method will compare two string for case insensitive equality.
     * 
	 * Note! Running for the first time you should get two errors:
	 * - row 8 "a not equal to A"
	 * - row 9 "nullpointerException".
     * 
     * Use the compareCorrectly method found below to get a fixed version.
     * 
     * @param one The first string
     * @param two The seccond string
     * @return True if they are the same, regardless of case.
     */
    public boolean compare(String one, String two) {
        return one.equals(two);
    }

    /**
     * Correct version of compare.
     * This method will check for null and case insensitive equals.
     * 
     * @param one The first string
     * @param two The seccond string
     * @return True if they are the same, regardless of case.
     */
    public boolean compareCorrectly(String one, String two) {
        if (one == null && two == null) {
            return true;
        }
        if (one == null && two != null) {
            return two.equalsIgnoreCase(one);
        }
        return one.equalsIgnoreCase(two);
    }
}
