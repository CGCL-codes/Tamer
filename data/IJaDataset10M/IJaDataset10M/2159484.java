package org.awelements.table;

import java.util.Comparator;

public class IndexComparator implements Comparator<Integer> {

    private Comparator<Object> mElementComparator;

    public IndexComparator(Comparator<Object> elementComparator) {
        mElementComparator = elementComparator;
    }

    public int compare(Integer o1, Integer o2) {
        return 0;
    }
}
