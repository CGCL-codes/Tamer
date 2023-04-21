package com.gargoylesoftware.base.collections;

import java.text.Collator;
import java.util.Comparator;
import java.util.Locale;

/**
 * A concrete implementation of Comparator that compares two strings.  If a locale
 * is specified then the comparison will be performed using the locale specific
 * collating sequences.  If the locale is not specified then a binary comparison
 * will be performed.
 *
 * @version $Revision: 1.3 $
 * @author <a href="mailto:mbowler@GargoyleSoftware.com">Mike Bowler</a>
 */
public class StringComparator implements Comparator {

    private final Locale locale_;

    private final Collator collator_;

    private final boolean isAscending_;

    /**
     * Create a locale specific comparator.
     *
     * @param locale The locale to be used when determining sorting order.
     *        If locale is null then a binary comparison is performed.
     * @param collatorStrength The strength value to be used by the
     *        Collator.  If locale is null then this value is ignored.
     * @param isAscending True if we are sorting in ascending order, false
     *        otherwise.
     */
    public StringComparator(final Locale locale, final int collatorStrength, final boolean isAscending) {
        locale_ = locale;
        if (locale_ == null) {
            collator_ = null;
        } else {
            collator_ = Collator.getInstance(locale_);
            collator_.setStrength(collatorStrength);
        }
        isAscending_ = isAscending;
    }

    /**
     * Create a locale specific comparator.
     *
     * @param locale The locale to be used when determining sorting order.
     *        If locale is null then a binary comparison is performed.
     */
    public StringComparator(final Locale locale) {
        this(locale, Collator.PRIMARY, true);
    }

    /**
     * Compare the two strings.
     * @param object1 The first string.
     * @param object2 The second string.
     * @return a negative integer, zero, or a positive integer as the first
     * argument is less than, equal to, or greater than the second.
     */
    public int compare(final Object object1, final Object object2) {
        int rc;
        final String string1 = object1.toString();
        final String string2 = object2.toString();
        if (locale_ == null) {
            rc = string1.compareTo(string2);
        } else {
            rc = collator_.compare(string1, string2);
        }
        if (isAscending_ == false) {
            rc *= -1;
        }
        return rc;
    }
}
