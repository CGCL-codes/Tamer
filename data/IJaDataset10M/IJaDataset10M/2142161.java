package org.exteca.web.search.model;

import java.util.Comparator;

/**
 *  Used to compare <code>Concept</code>s with respect to the frequency.
 */
public class ConceptComparator implements Comparator {

    /**
     *
     *  Compares two Concepts checking the frequency
     *
     */
    public int compare(Object o1, Object o2) {
        Concept c1 = (Concept) o1;
        Concept c2 = (Concept) o2;
        return c1.compareTo(c2);
    }

    /**
     *  Determines if Comparator is equal to another object
     */
    public boolean equals(Object o) {
        return false;
    }
}
