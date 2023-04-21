package jgp.algorithm;

import jgp.interfaces.RandomAccessable;
import jgp.predicate.BinaryPredicate;

public class InsertionSorter {

    private RandomAccessable vec = null;

    private BinaryPredicate lessThan = null;

    public InsertionSorter(RandomAccessable v, BinaryPredicate less) {
        vec = v;
        lessThan = less;
    }

    public void sort(int first, int last) {
        for (int i = first; i <= last; ++i) {
            Object obj = vec.getElementAt(i);
            int j;
            for (j = first; j < i; ++j) if (lessThan.execute(obj, vec.getElementAt(j))) {
                int from = i - 1;
                int to = i;
                while (to > j) vec.setElementAt(to--, vec.getElementAt(from--));
                break;
            }
            vec.setElementAt(j, obj);
        }
    }

    public void sort() {
        sort(0, vec.getSize() - 1);
    }
}
