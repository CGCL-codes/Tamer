package com.kescom.matrix.core.filter.stats;

import java.util.List;

public class MedianValueProperty implements IComparableSortedListProperty {

    public String getLabel() {
        return "Median";
    }

    @SuppressWarnings("unchecked")
    public Object getValue(List<Object> list) {
        if (list.isEmpty()) return null;
        int index = list.size() / 2;
        if ((list.size() % 2) == 0) return list.get(index); else {
            Object o1 = list.get(index);
            Object o2 = list.get(index + 1);
            if (o1 instanceof Number && o2 instanceof Number) {
                double v1 = ((Number) o1).doubleValue();
                double v2 = ((Number) o2).doubleValue();
                double median = (v1 + v2) / 2.0;
                return new Double(median);
            } else return o1;
        }
    }
}
