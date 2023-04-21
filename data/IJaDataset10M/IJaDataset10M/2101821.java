package net.sf.salmon.util;

import java.util.*;

/**
 * <p>
 * An algorithm implementation of the well-known quick sort algorithm
 * </p>
 * 
 * @author Cedric Shih
 * @version 1.0
 */
public class QuickSort implements SortAlgorithm {

    private int MODEL;

    private static final int COMPARATOR = 0;

    private static final int COMPARABLE = 1;

    private static final int TOSTRING = 3;

    private Random random;

    private Comparator comp;

    public QuickSort() {
        random = new Random();
        comp = null;
    }

    public QuickSort(Comparator c) {
        random = new Random();
        comp = null;
        setComparator(c);
    }

    private void qsort(Vector seq, int leftbound, int rightbound) throws UncomparableException {
        if (seq.size() >= 2 && leftbound < rightbound) {
            swap(seq, ((int) (random.nextFloat() * (float) (rightbound - leftbound)) + leftbound), rightbound);
            Object pivot = seq.elementAt(rightbound);
            int leftindex = leftbound;
            int rightindex = rightbound - 1;
            while (leftindex <= rightindex) {
                if (MODEL == 0) {
                    for (; (leftindex <= rightindex && comp.compare(seq.elementAt(leftindex), pivot) <= 0); leftindex++) {
                    }
                    for (; (rightindex >= leftindex && comp.compare(seq.elementAt(rightindex), pivot) >= 0); rightindex--) {
                    }
                } else if (MODEL == 1) {
                    for (; (leftindex <= rightindex && ((Comparable) seq.elementAt(leftindex)).compareTo((Comparable) pivot) <= 0); leftindex++) {
                    }
                    for (; (rightindex >= leftindex && ((Comparable) seq.elementAt(rightindex)).compareTo((Comparable) pivot) >= 0); rightindex--) {
                    }
                } else {
                    for (; (leftindex <= rightindex && seq.elementAt(leftindex).toString().compareTo(pivot.toString()) <= 0); leftindex++) {
                    }
                    for (; (rightindex >= leftindex && seq.elementAt(rightindex).toString().compareTo(pivot.toString()) >= 0); rightindex--) {
                    }
                }
                if (leftindex < rightindex) {
                    swap(seq, leftindex, rightindex);
                }
            }
            swap(seq, leftindex, rightbound);
            qsort(seq, leftbound, leftindex - 1);
            qsort(seq, leftindex + 1, rightbound);
        }
    }

    public void setComparator(Comparator c) {
        comp = c;
    }

    public Vector sort(Vector seq) throws UncomparableException {
        if (comp != null) {
            MODEL = 0;
        } else if (seq.firstElement() instanceof Comparable) {
            MODEL = 1;
        } else {
            MODEL = 3;
        }
        Vector clone = (Vector) seq.clone();
        qsort(clone, 0, clone.size() - 1);
        return clone;
    }

    private void swap(Vector seq, int i, int j) {
        Object obj = seq.elementAt(j);
        seq.setElementAt(seq.elementAt(i), j);
        seq.setElementAt(obj, i);
    }
}
