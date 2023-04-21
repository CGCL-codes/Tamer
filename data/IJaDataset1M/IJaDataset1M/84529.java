package fr.thomascoffin.mocaf;

import org.jetbrains.annotations.NotNull;

/**
 * NullableComparator implementation that uses Comparable values.
 *
 * @see NullableComparator
 *      <p/>
 *      <p/>
 *      This class is released by Thomas Coffin (thomas.coffin@gmail.com) under the <a href="http://www.gnu.org/copyleft/lesser.html" target="_blank">LGPL License</a>
 *      as a component of the <a href="http://code.google.com/p/mocaf" target="_blank">mocaf project</a>
 *      <p/>
 *      (c) Thomas Coffin 2008.
 */
public class NullableComparableComparator<T extends Comparable<T>> extends NullableComparator<T> {

    public NullableComparableComparator(boolean nullIsLessThanEverything) {
        super(nullIsLessThanEverything);
    }

    public NullableComparableComparator() {
        super();
    }

    protected int compareNotNull(@NotNull T o1, @NotNull T o2) {
        return o1.compareTo(o2);
    }
}
