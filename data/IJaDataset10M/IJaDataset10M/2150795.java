package chapter2;

/***
 * Exercise 2.2.9 Sedgewick and Wayne: Algorithms
 * @author Andreas
 *
 */
public class Exercise_2_2_9 {

    public static void sort(Comparable a[], Comparable[] aux, int lo, int hi) {
        if (hi <= lo) return;
        int mid = lo + (hi - lo) / 2;
        sort(a, aux, lo, mid);
        sort(a, aux, mid + 1, hi);
        merge(a, aux, lo, mid, hi);
    }

    private static void merge(Comparable[] a, Comparable[] aux, int lo, int mid, int hi) {
        int i = lo, j = mid + 1;
        for (int k = lo; k <= hi; k++) {
            aux[k] = a[k];
        }
        for (int k = lo; k <= hi; k++) {
            if (i > mid) a[k] = aux[j++]; else if (j > hi) a[k] = aux[i++]; else if (less(aux[j], aux[i])) a[k] = aux[j++]; else a[k] = aux[i++];
        }
    }

    private static boolean less(Comparable v, Comparable w) {
        return (v.compareTo(w) < 0);
    }

    public static void sort(Comparable[] a) {
        Comparable[] aux = new Comparable[a.length];
        sort(a, aux, 0, a.length - 1);
    }

    public static void main(String[] args) {
        int N = Integer.parseInt("10");
        Double[] a = new Double[N];
        for (int i = 0; i < N; i++) {
            a[i] = Math.random();
        }
        sort(a);
        for (int i = 0; i < N; i++) {
            System.out.println(a[i]);
        }
    }
}
