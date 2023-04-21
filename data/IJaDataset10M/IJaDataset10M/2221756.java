package jaxlib.util;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Collection;
import java.util.Comparator;
import javax.annotation.Nullable;
import jaxlib.col.ObjectArray;
import jaxlib.col.XCollections;

/**
 * @author  jw
 * @since   JaXLib 1.0
 * @version $Id: CompoundComparator.java 2773 2010-01-24 02:08:47Z joerg_wassmer $
 */
public class CompoundComparator<T> extends AbstractComparator<T> implements Serializable {

    /**
   * @since JaXLib 1.0
   */
    private static final long serialVersionUID = 1L;

    public static <T> Comparator<T> create(@Nullable final Comparator<? super T>... comparators) {
        if (comparators == null) return null; else {
            final int len = comparators.length;
            if (len == 0) return (Comparator) Comparators.ALWAYS_EQUAL; else if (len == 1) return (Comparator) comparators[0]; else return new CompoundComparator<T>(comparators);
        }
    }

    public static <T> Comparator<T> create(@Nullable final Collection<Comparator<? super T>> comparators) {
        if (comparators == null) return null; else {
            final int len = comparators.size();
            if (len == 0) return (Comparator) Comparators.ALWAYS_EQUAL; else if (len == 1) return (Comparator) XCollections.first(comparators); else return new CompoundComparator<T>(comparators.toArray(new Comparator[len]));
        }
    }

    private final Comparator<? super T>[] comparators;

    private transient ObjectArray<Comparator<? super T>> comparatorsView;

    public CompoundComparator(final Comparator<? super T>... comparators) {
        super();
        CheckArg.notNull(comparators, "comparators");
        this.comparators = comparators;
    }

    @Override
    public int compare(final T a, final T b) {
        if (a != b) {
            for (int i = 0; i < this.comparators.length; i++) {
                final Comparator comp = this.comparators[i];
                final int cmp = (comp != null) ? comp.compare(a, b) : ((Comparable) a).compareTo(b);
                if (cmp != 0) return cmp;
            }
        }
        return 0;
    }

    @Override
    public boolean equals(final Object o) {
        return (o == this) || ((o instanceof CompoundComparator) && Arrays.equals(this.comparators, ((CompoundComparator) o).comparators));
    }

    @Override
    public int hashCode() {
        return Arrays.hashCode(this.comparators);
    }

    public final ObjectArray<Comparator<? super T>> getComparators() {
        if (this.comparatorsView == null) this.comparatorsView = ObjectArray.readOnly(this.comparators);
        return this.comparatorsView;
    }

    @Override
    public String toString() {
        return super.toString() + "(" + Arrays.toString(this.comparators) + ")";
    }
}
