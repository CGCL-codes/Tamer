package pcgen.util;

import java.util.Comparator;

/**
 * A <code>Comparator</code> to compare objects as
 * <code>String</code>s ignoring case.  This is particularly useful
 * for applications such as maintaining a sorted
 * <code>JComboBoxEx</code> and the like.
 *
 * @author <a href="mailto:binkley@alumni.rice.edu">B. K. Oxley (binkley)</a>
 * @version $Revision: 1.6 $
 */
public final class StringIgnoreCaseComparator implements Comparator {

    /** Constructs a <code>StringIgnoreCaseComparator</code>. */
    public StringIgnoreCaseComparator() {
    }

    /** {@inheritDoc} */
    public int compare(Object o1, Object o2) {
        return ((o1 == null) ? "" : o1.toString()).compareToIgnoreCase((o2 == null) ? "" : o2.toString());
    }
}
