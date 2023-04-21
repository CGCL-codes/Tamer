package verjinxer.util;

/**
 *
 * @author Sven Rahmann
 */
public final class Sorter {

    public Sorter(Sortable ss) {
        s = ss;
    }

    private final Sortable s;

    /** given a Sortable, build a heap starting at node i. There a n nodes in total. */
    private void heapify(int p, final int n) {
        for (int r, l = (p << 1) + 1; l < n; p = l, l = (p << 1) + 1) {
            if ((r = l + 1) < n && s.compare(l, r) < 0) l = r;
            if (s.compare(p, l) < 0) s.swap(p, l); else break;
        }
    }

    private void phase1() {
        final int n = s.length();
        for (int p = n / 2; p >= 0; p--) heapify(p, n);
    }

    private void phase2() {
        for (int n = s.length(); --n > 0; ) {
            s.swap(0, n);
            heapify(0, n);
        }
    }

    public Sortable heapsort() {
        phase1();
        phase2();
        return s;
    }

    /*************************************************************************/
    private int partition(int l, int r) {
        int m = (l + r) / 2;
        while (l <= r) {
            while (s.compare(m, r) < 0) r--;
            while (s.compare(l, m) < 0) l++;
            if (l < r) {
                if (l == m) m = r; else if (r == m) m = l;
                s.swap(l++, r--);
            } else return r;
        }
        return r;
    }

    private void quicksortRec(final int l, final int r) {
        if (l < r) {
            final int m = partition(l, r);
            quicksortRec(m + 1, r);
            quicksortRec(l, m);
        }
    }

    public Sortable quicksort() {
        quicksortRec(0, s.length() - 1);
        return s;
    }
}
