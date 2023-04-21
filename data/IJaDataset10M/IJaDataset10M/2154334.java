package org.gwtoolbox.commons.collections.client;

import java.util.Comparator;

/**
 * Compares 2 values of an enum. The comparison is done either by the ordinal of the values or by the values (names)
 * themselves.
 *
 * @author Uri Boness
 */
public class EnumComparator<T extends Enum<T>> implements Comparator<T> {

    private final boolean compareByOrdinal;

    /**
     * Constructs a new EnumComparator which compares the enums based on their oridnals.
     *
     * @see #EnumComparator(boolean)
     */
    public EnumComparator() {
        this(true);
    }

    /**
     * Constructs a new EnumComparator.
     *
     * @param compareByOrdinal When {@code true} the values will be compared by their ordinals, otherwise they will be
     *        compared by the enum values (i.e. names).
     */
    public EnumComparator(boolean compareByOrdinal) {
        this.compareByOrdinal = compareByOrdinal;
    }

    /**
     * @inheritDocs
     */
    public int compare(T o1, T o2) {
        return compareByOrdinal ? compareByOrdinal(o1, o2) : compareByValue(o1, o2);
    }

    private int compareByOrdinal(T o1, T o2) {
        return o1.ordinal() - o2.ordinal();
    }

    private int compareByValue(T o1, T o2) {
        return o1.name().compareTo(o2.name());
    }
}
