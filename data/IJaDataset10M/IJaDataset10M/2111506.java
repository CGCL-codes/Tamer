package org.gwtoolbox.widget.client.data;

import java.util.Comparator;
import java.util.List;

/**
 * @author Uri Boness
 */
public class SortSpecComparator<T extends Record> implements Comparator<T> {

    private final SortSpec sortSpec;

    public SortSpecComparator(SortSpec sortSpec) {
        this.sortSpec = sortSpec;
    }

    public int compare(T t1, T t2) {
        List<FieldSort> sorts = sortSpec.getSorts();
        for (FieldSort sort : sorts) {
            String fieldName = sort.getFieldName();
            int result = compareField(t1, t2, fieldName, sort.isAsc());
            if (result != 0) {
                return result;
            }
        }
        return 0;
    }

    private int compareField(T t1, T t2, String fieldName, boolean asc) {
        Object v1 = t1.getValue(fieldName);
        Object v2 = t2.getValue(fieldName);
        if (v1 instanceof Comparable && v2 instanceof Comparable) {
            int result = ((Comparable) v1).compareTo(v2);
            if (!asc) {
                return -1 * result;
            }
        }
        return 0;
    }
}
