package jamm.backend;

import java.util.Comparator;

/**
 * Compares aliases by their names, ignoring case.  Note: This is
 * inconsistent with equals().
 */
public class AliasNameComparator implements Comparator {

    /**
     * Compares two <code>AliasInfo</code> objects by their name,
     * ignoring case.
     *
     * @param o1 First object to compare.  Must be an
     * <code>AliasInfo</code> object
     * @param o2 Second object to compare.  Must be an
     * <code>AliasInfo</code> object
     * @return a negative integer, zero, or a positive integer as the
     * first argument is less than, equal to, or greater than the
     * second
     * @see AliasInfo
     */
    public int compare(Object o1, Object o2) {
        AliasInfo alias1 = (AliasInfo) o1;
        AliasInfo alias2 = (AliasInfo) o2;
        return alias1.getName().compareToIgnoreCase(alias2.getName());
    }
}
