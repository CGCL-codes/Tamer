package org.jmetis.kernel.comparator;

import java.util.Comparator;

/**
 * {@code IgnoreCaseComparator} compares two strings lexicographically, ignoring
 * case differences.
 * 
 * @author era
 * 
 * @see java.lang.String#compareToIgnoreCase(String)
 */
public class IgnoreCaseComparator implements Comparator<String> {

    private static Comparator<String> DEFAULT_INSTANCE;

    /**
	 * Constructs a new {@code IgnoreCaseComparator} instance.
	 * 
	 * @see org.jmetis.kernel.comparator.IgnoreCaseComparator#defaultInstance()
	 */
    public IgnoreCaseComparator() {
        super();
    }

    /**
	 * Returns the default {@code IgnoreCaseComparator} instance.
	 * 
	 * @return the default {@code IgnoreCaseComparator} instance.
	 */
    public static Comparator<String> defaultInstance() {
        if (IgnoreCaseComparator.DEFAULT_INSTANCE == null) {
            IgnoreCaseComparator.DEFAULT_INSTANCE = new IgnoreCaseComparator();
        }
        return IgnoreCaseComparator.DEFAULT_INSTANCE;
    }

    /**
	 * Compares two strings lexicographically, ignoring case differences.
	 * 
	 * @see java.util.Comparator#compare(java.lang.Object, java.lang.Object)
	 */
    public int compare(String firstValue, String secondValue) {
        return firstValue.compareToIgnoreCase(secondValue);
    }
}
