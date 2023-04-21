package co.edu.unal.ungrid.util;

public class SortHelper {

    private SortHelper() {
    }

    /**
	 * Sorts the elements of the specified array in ascending order.
	 * After sorting, a[0] &lt;= a[1] &lt;= a[2] &lt;= ....
	 * @param a the array to be sorted.
	 */
    public static void quickSort(byte[] a) {
        int n = a.length;
        if (n < NSMALL_SORT) {
            insertionSort(a, 0, n - 1);
        } else {
            int[] m = new int[2];
            quickSort(a, 0, n - 1, m);
        }
    }

    /**
	 * Sorts indices of the elements of the specified array in ascending order.
	 * After sorting, a[i[0]] &lt;= a[i[1]] &lt;= a[i[2]] &lt;= ....
	 * @param a the array.
	 * @param i the indices to be sorted.
	 */
    public static void quickIndexSort(byte[] a, int[] i) {
        int n = a.length;
        if (n < NSMALL_SORT) {
            insertionSort(a, i, 0, n - 1);
        } else {
            int[] m = new int[2];
            quickSort(a, i, 0, n - 1, m);
        }
    }

    /**
	 * Partially sorts the elements of the specified array in ascending order.
	 * After partial sorting, the element a[k] with specified index k has the 
	 * value it would have if the array were completely sorted. That is,
	 * a[0:k-1] &lt;= a[k] &lt;= a[k:n-1], where n is the length of a.
	 * @param k the index.
	 * @param a the array to be partially sorted.
	 */
    public static void quickPartialSort(int k, byte[] a) {
        int n = a.length;
        int p = 0;
        int q = n - 1;
        int[] m = (n > NSMALL_SORT) ? new int[2] : null;
        while (q - p >= NSMALL_SORT) {
            m[0] = p;
            m[1] = q;
            quickPartition(a, m);
            if (k < m[0]) {
                q = m[0] - 1;
            } else if (k > m[1]) {
                p = m[1] + 1;
            } else {
                return;
            }
        }
        insertionSort(a, p, q);
    }

    /**
	 * Partially sorts indices of the elements of the specified array.
	 * After partial sorting, the element i[k] with specified index k has the 
	 * value it would have if the indices were completely sorted. That is, 
	 * a[i[0:k-1]] &lt;= a[i[k]] &lt;= a[i[k:n-1]], where n is the length of i.
	 * @param k the index.
	 * @param a the array.
	 * @param i the indices to be partially sorted.
	 */
    public static void quickPartialIndexSort(int k, byte[] a, int[] i) {
        int n = i.length;
        int p = 0;
        int q = n - 1;
        int[] m = (n > NSMALL_SORT) ? new int[2] : null;
        while (q - p >= NSMALL_SORT) {
            m[0] = p;
            m[1] = q;
            quickPartition(a, i, m);
            if (k < m[0]) {
                q = m[0] - 1;
            } else if (k > m[1]) {
                p = m[1] + 1;
            } else {
                return;
            }
        }
        insertionSort(a, i, p, q);
    }

    /**
	 * Sorts the elements of the specified array in ascending order.
	 * After sorting, a[0] &lt;= a[1] &lt;= a[2] &lt;= ....
	 * @param a the array to be sorted.
	 */
    public static void quickSort(short[] a) {
        int n = a.length;
        if (n < NSMALL_SORT) {
            insertionSort(a, 0, n - 1);
        } else {
            int[] m = new int[2];
            quickSort(a, 0, n - 1, m);
        }
    }

    /**
	 * Sorts indices of the elements of the specified array in ascending order.
	 * After sorting, a[i[0]] &lt;= a[i[1]] &lt;= a[i[2]] &lt;= ....
	 * @param a the array.
	 * @param i the indices to be sorted.
	 */
    public static void quickIndexSort(short[] a, int[] i) {
        int n = a.length;
        if (n < NSMALL_SORT) {
            insertionSort(a, i, 0, n - 1);
        } else {
            int[] m = new int[2];
            quickSort(a, i, 0, n - 1, m);
        }
    }

    /**
	 * Partially sorts the elements of the specified array in ascending order.
	 * After partial sorting, the element a[k] with specified index k has the 
	 * value it would have if the array were completely sorted. That is,
	 * a[0:k-1] &lt;= a[k] &lt;= a[k:n-1], where n is the length of a.
	 * @param k the index.
	 * @param a the array to be partially sorted.
	 */
    public static void quickPartialSort(int k, short[] a) {
        int n = a.length;
        int p = 0;
        int q = n - 1;
        int[] m = (n > NSMALL_SORT) ? new int[2] : null;
        while (q - p >= NSMALL_SORT) {
            m[0] = p;
            m[1] = q;
            quickPartition(a, m);
            if (k < m[0]) {
                q = m[0] - 1;
            } else if (k > m[1]) {
                p = m[1] + 1;
            } else {
                return;
            }
        }
        insertionSort(a, p, q);
    }

    /**
	 * Partially sorts indices of the elements of the specified array.
	 * After partial sorting, the element i[k] with specified index k has the 
	 * value it would have if the indices were completely sorted. That is, 
	 * a[i[0:k-1]] &lt;= a[i[k]] &lt;= a[i[k:n-1]], where n is the length of i.
	 * @param k the index.
	 * @param a the array.
	 * @param i the indices to be partially sorted.
	 */
    public static void quickPartialIndexSort(int k, short[] a, int[] i) {
        int n = i.length;
        int p = 0;
        int q = n - 1;
        int[] m = (n > NSMALL_SORT) ? new int[2] : null;
        while (q - p >= NSMALL_SORT) {
            m[0] = p;
            m[1] = q;
            quickPartition(a, i, m);
            if (k < m[0]) {
                q = m[0] - 1;
            } else if (k > m[1]) {
                p = m[1] + 1;
            } else {
                return;
            }
        }
        insertionSort(a, i, p, q);
    }

    /**
	 * Sorts the elements of the specified array in ascending order.
	 * After sorting, a[0] &lt;= a[1] &lt;= a[2] &lt;= ....
	 * @param a the array to be sorted.
	 */
    public static void quickSort(int[] a) {
        int n = a.length;
        if (n < NSMALL_SORT) {
            insertionSort(a, 0, n - 1);
        } else {
            int[] m = new int[2];
            quickSort(a, 0, n - 1, m);
        }
    }

    /**
	 * Sorts indices of the elements of the specified array in ascending order.
	 * After sorting, a[i[0]] &lt;= a[i[1]] &lt;= a[i[2]] &lt;= ....
	 * @param a the array.
	 * @param i the indices to be sorted.
	 */
    public static void quickIndexSort(int[] a, int[] i) {
        int n = a.length;
        if (n < NSMALL_SORT) {
            insertionSort(a, i, 0, n - 1);
        } else {
            int[] m = new int[2];
            quickSort(a, i, 0, n - 1, m);
        }
    }

    /**
	 * Partially sorts the elements of the specified array in ascending order.
	 * After partial sorting, the element a[k] with specified index k has the 
	 * value it would have if the array were completely sorted. That is,
	 * a[0:k-1] &lt;= a[k] &lt;= a[k:n-1], where n is the length of a.
	 * @param k the index.
	 * @param a the array to be partially sorted.
	 */
    public static void quickPartialSort(int k, int[] a) {
        int n = a.length;
        int p = 0;
        int q = n - 1;
        int[] m = (n > NSMALL_SORT) ? new int[2] : null;
        while (q - p >= NSMALL_SORT) {
            m[0] = p;
            m[1] = q;
            quickPartition(a, m);
            if (k < m[0]) {
                q = m[0] - 1;
            } else if (k > m[1]) {
                p = m[1] + 1;
            } else {
                return;
            }
        }
        insertionSort(a, p, q);
    }

    /**
	 * Partially sorts indices of the elements of the specified array.
	 * After partial sorting, the element i[k] with specified index k has the 
	 * value it would have if the indices were completely sorted. That is, 
	 * a[i[0:k-1]] &lt;= a[i[k]] &lt;= a[i[k:n-1]], where n is the length of i.
	 * @param k the index.
	 * @param a the array.
	 * @param i the indices to be partially sorted.
	 */
    public static void quickPartialIndexSort(int k, int[] a, int[] i) {
        int n = i.length;
        int p = 0;
        int q = n - 1;
        int[] m = (n > NSMALL_SORT) ? new int[2] : null;
        while (q - p >= NSMALL_SORT) {
            m[0] = p;
            m[1] = q;
            quickPartition(a, i, m);
            if (k < m[0]) {
                q = m[0] - 1;
            } else if (k > m[1]) {
                p = m[1] + 1;
            } else {
                return;
            }
        }
        insertionSort(a, i, p, q);
    }

    /**
	 * Sorts the elements of the specified array in ascending order.
	 * After sorting, a[0] &lt;= a[1] &lt;= a[2] &lt;= ....
	 * @param a the array to be sorted.
	 */
    public static void quickSort(long[] a) {
        int n = a.length;
        if (n < NSMALL_SORT) {
            insertionSort(a, 0, n - 1);
        } else {
            int[] m = new int[2];
            quickSort(a, 0, n - 1, m);
        }
    }

    /**
	 * Sorts indices of the elements of the specified array in ascending order.
	 * After sorting, a[i[0]] &lt;= a[i[1]] &lt;= a[i[2]] &lt;= ....
	 * @param a the array.
	 * @param i the indices to be sorted.
	 */
    public static void quickIndexSort(long[] a, int[] i) {
        int n = a.length;
        if (n < NSMALL_SORT) {
            insertionSort(a, i, 0, n - 1);
        } else {
            int[] m = new int[2];
            quickSort(a, i, 0, n - 1, m);
        }
    }

    /**
	 * Partially sorts the elements of the specified array in ascending order.
	 * After partial sorting, the element a[k] with specified index k has the 
	 * value it would have if the array were completely sorted. That is,
	 * a[0:k-1] &lt;= a[k] &lt;= a[k:n-1], where n is the length of a.
	 * @param k the index.
	 * @param a the array to be partially sorted.
	 */
    public static void quickPartialSort(int k, long[] a) {
        int n = a.length;
        int p = 0;
        int q = n - 1;
        int[] m = (n > NSMALL_SORT) ? new int[2] : null;
        while (q - p >= NSMALL_SORT) {
            m[0] = p;
            m[1] = q;
            quickPartition(a, m);
            if (k < m[0]) {
                q = m[0] - 1;
            } else if (k > m[1]) {
                p = m[1] + 1;
            } else {
                return;
            }
        }
        insertionSort(a, p, q);
    }

    /**
	 * Partially sorts indices of the elements of the specified array.
	 * After partial sorting, the element i[k] with specified index k has the 
	 * value it would have if the indices were completely sorted. That is, 
	 * a[i[0:k-1]] &lt;= a[i[k]] &lt;= a[i[k:n-1]], where n is the length of i.
	 * @param k the index.
	 * @param a the array.
	 * @param i the indices to be partially sorted.
	 */
    public static void quickPartialIndexSort(int k, long[] a, int[] i) {
        int n = i.length;
        int p = 0;
        int q = n - 1;
        int[] m = (n > NSMALL_SORT) ? new int[2] : null;
        while (q - p >= NSMALL_SORT) {
            m[0] = p;
            m[1] = q;
            quickPartition(a, i, m);
            if (k < m[0]) {
                q = m[0] - 1;
            } else if (k > m[1]) {
                p = m[1] + 1;
            } else {
                return;
            }
        }
        insertionSort(a, i, p, q);
    }

    /**
	 * Sorts the elements of the specified array in ascending order.
	 * After sorting, a[0] &lt;= a[1] &lt;= a[2] &lt;= ....
	 * @param a the array to be sorted.
	 */
    public static void quickSort(float[] a) {
        int n = a.length;
        if (n < NSMALL_SORT) {
            insertionSort(a, 0, n - 1);
        } else {
            int[] m = new int[2];
            quickSort(a, 0, n - 1, m);
        }
    }

    /**
	 * Sorts indices of the elements of the specified array in ascending order.
	 * After sorting, a[i[0]] &lt;= a[i[1]] &lt;= a[i[2]] &lt;= ....
	 * @param a the array.
	 * @param i the indices to be sorted.
	 */
    public static void quickIndexSort(float[] a, int[] i) {
        int n = a.length;
        if (n < NSMALL_SORT) {
            insertionSort(a, i, 0, n - 1);
        } else {
            int[] m = new int[2];
            quickSort(a, i, 0, n - 1, m);
        }
    }

    /**
	 * Partially sorts the elements of the specified array in ascending order.
	 * After partial sorting, the element a[k] with specified index k has the 
	 * value it would have if the array were completely sorted. That is,
	 * a[0:k-1] &lt;= a[k] &lt;= a[k:n-1], where n is the length of a.
	 * @param k the index.
	 * @param a the array to be partially sorted.
	 */
    public static void quickPartialSort(int k, float[] a) {
        int n = a.length;
        int p = 0;
        int q = n - 1;
        int[] m = (n > NSMALL_SORT) ? new int[2] : null;
        while (q - p >= NSMALL_SORT) {
            m[0] = p;
            m[1] = q;
            quickPartition(a, m);
            if (k < m[0]) {
                q = m[0] - 1;
            } else if (k > m[1]) {
                p = m[1] + 1;
            } else {
                return;
            }
        }
        insertionSort(a, p, q);
    }

    /**
	 * Partially sorts indices of the elements of the specified array.
	 * After partial sorting, the element i[k] with specified index k has the 
	 * value it would have if the indices were completely sorted. That is, 
	 * a[i[0:k-1]] &lt;= a[i[k]] &lt;= a[i[k:n-1]], where n is the length of i.
	 * @param k the index.
	 * @param a the array.
	 * @param i the indices to be partially sorted.
	 */
    public static void quickPartialIndexSort(int k, float[] a, int[] i) {
        int n = i.length;
        int p = 0;
        int q = n - 1;
        int[] m = (n > NSMALL_SORT) ? new int[2] : null;
        while (q - p >= NSMALL_SORT) {
            m[0] = p;
            m[1] = q;
            quickPartition(a, i, m);
            if (k < m[0]) {
                q = m[0] - 1;
            } else if (k > m[1]) {
                p = m[1] + 1;
            } else {
                return;
            }
        }
        insertionSort(a, i, p, q);
    }

    /**
	 * Sorts the elements of the specified array in ascending order.
	 * After sorting, a[0] &lt;= a[1] &lt;= a[2] &lt;= ....
	 * @param a the array to be sorted.
	 */
    public static void quickSort(double[] a) {
        int n = a.length;
        if (n < NSMALL_SORT) {
            insertionSort(a, 0, n - 1);
        } else {
            int[] m = new int[2];
            quickSort(a, 0, n - 1, m);
        }
    }

    /**
	 * Sorts indices of the elements of the specified array in ascending order.
	 * After sorting, a[i[0]] &lt;= a[i[1]] &lt;= a[i[2]] &lt;= ....
	 * @param a the array.
	 * @param i the indices to be sorted.
	 */
    public static void quickIndexSort(double[] a, int[] i) {
        int n = a.length;
        if (n < NSMALL_SORT) {
            insertionSort(a, i, 0, n - 1);
        } else {
            int[] m = new int[2];
            quickSort(a, i, 0, n - 1, m);
        }
    }

    /**
	 * Partially sorts the elements of the specified array in ascending order.
	 * After partial sorting, the element a[k] with specified index k has the 
	 * value it would have if the array were completely sorted. That is,
	 * a[0:k-1] &lt;= a[k] &lt;= a[k:n-1], where n is the length of a.
	 * @param k the index.
	 * @param a the array to be partially sorted.
	 */
    public static void quickPartialSort(int k, double[] a) {
        int n = a.length;
        int p = 0;
        int q = n - 1;
        int[] m = (n > NSMALL_SORT) ? new int[2] : null;
        while (q - p >= NSMALL_SORT) {
            m[0] = p;
            m[1] = q;
            quickPartition(a, m);
            if (k < m[0]) {
                q = m[0] - 1;
            } else if (k > m[1]) {
                p = m[1] + 1;
            } else {
                return;
            }
        }
        insertionSort(a, p, q);
    }

    /**
	 * Partially sorts indices of the elements of the specified array.
	 * After partial sorting, the element i[k] with specified index k has the 
	 * value it would have if the indices were completely sorted. That is, 
	 * a[i[0:k-1]] &lt;= a[i[k]] &lt;= a[i[k:n-1]], where n is the length of i.
	 * @param k the index.
	 * @param a the array.
	 * @param i the indices to be partially sorted.
	 */
    public static void quickPartialIndexSort(int k, double[] a, int[] i) {
        int n = i.length;
        int p = 0;
        int q = n - 1;
        int[] m = (n > NSMALL_SORT) ? new int[2] : null;
        while (q - p >= NSMALL_SORT) {
            m[0] = p;
            m[1] = q;
            quickPartition(a, i, m);
            if (k < m[0]) {
                q = m[0] - 1;
            } else if (k > m[1]) {
                p = m[1] + 1;
            } else {
                return;
            }
        }
        insertionSort(a, i, p, q);
    }

    private static final int NSMALL_SORT = 7;

    private static final int NLARGE_SORT = 40;

    private static int med3(byte[] a, int i, int j, int k) {
        return a[i] < a[j] ? (a[j] < a[k] ? j : a[i] < a[k] ? k : i) : (a[j] > a[k] ? j : a[i] > a[k] ? k : i);
    }

    private static int med3(byte[] a, int[] i, int j, int k, int l) {
        return a[i[j]] < a[i[k]] ? (a[i[k]] < a[i[l]] ? k : a[i[j]] < a[i[l]] ? l : j) : (a[i[k]] > a[i[l]] ? k : a[i[j]] > a[i[l]] ? l : j);
    }

    private static void swap(byte[] a, int i, int j) {
        byte ai = a[i];
        a[i] = a[j];
        a[j] = ai;
    }

    private static void swap(byte[] a, int i, int j, int n) {
        while (n > 0) {
            byte ai = a[i];
            a[i++] = a[j];
            a[j++] = ai;
            --n;
        }
    }

    private static void insertionSort(byte[] a, int p, int q) {
        for (int i = p; i <= q; ++i) for (int j = i; j > p && a[j - 1] > a[j]; --j) swap(a, j, j - 1);
    }

    private static void insertionSort(byte[] a, int[] i, int p, int q) {
        for (int j = p; j <= q; ++j) for (int k = j; k > p && a[i[k - 1]] > a[i[k]]; --k) swap(i, k, k - 1);
    }

    private static void quickSort(byte[] a, int p, int q, int[] m) {
        if (q - p <= NSMALL_SORT) {
            insertionSort(a, p, q);
        } else {
            m[0] = p;
            m[1] = q;
            quickPartition(a, m);
            int r = m[0];
            int s = m[1];
            if (p < r - 1) quickSort(a, p, r - 1, m);
            if (q > s + 1) quickSort(a, s + 1, q, m);
        }
    }

    private static void quickSort(byte[] a, int[] i, int p, int q, int[] m) {
        if (q - p <= NSMALL_SORT) {
            insertionSort(a, i, p, q);
        } else {
            m[0] = p;
            m[1] = q;
            quickPartition(a, i, m);
            int r = m[0];
            int s = m[1];
            if (p < r - 1) quickSort(a, i, p, r - 1, m);
            if (q > s + 1) quickSort(a, i, s + 1, q, m);
        }
    }

    private static void quickPartition(byte[] x, int[] m) {
        int p = m[0];
        int q = m[1];
        int n = q - p + 1;
        int k = (p + q) / 2;
        if (n > NSMALL_SORT) {
            int j = p;
            int l = q;
            if (n > NLARGE_SORT) {
                int s = n / 8;
                j = med3(x, j, j + s, j + 2 * s);
                k = med3(x, k - s, k, k + s);
                l = med3(x, l - 2 * s, l - s, l);
            }
            k = med3(x, j, k, l);
        }
        byte y = x[k];
        int a = p, b = p;
        int c = q, d = q;
        while (true) {
            while (b <= c && x[b] <= y) {
                if (x[b] == y) swap(x, a++, b);
                ++b;
            }
            while (c >= b && x[c] >= y) {
                if (x[c] == y) swap(x, c, d--);
                --c;
            }
            if (b > c) break;
            swap(x, b, c);
            ++b;
            --c;
        }
        int r = Math.min(a - p, b - a);
        int s = Math.min(d - c, q - d);
        int t = q + 1;
        swap(x, p, b - r, r);
        swap(x, b, t - s, s);
        m[0] = p + (b - a);
        m[1] = q - (d - c);
    }

    private static void quickPartition(byte[] x, int[] i, int[] m) {
        int p = m[0];
        int q = m[1];
        int n = q - p + 1;
        int k = (p + q) / 2;
        if (n > NSMALL_SORT) {
            int j = p;
            int l = q;
            if (n > NLARGE_SORT) {
                int s = n / 8;
                j = med3(x, i, j, j + s, j + 2 * s);
                k = med3(x, i, k - s, k, k + s);
                l = med3(x, i, l - 2 * s, l - s, l);
            }
            k = med3(x, i, j, k, l);
        }
        byte y = x[i[k]];
        int a = p, b = p;
        int c = q, d = q;
        while (true) {
            while (b <= c && x[i[b]] <= y) {
                if (x[i[b]] == y) swap(i, a++, b);
                ++b;
            }
            while (c >= b && x[i[c]] >= y) {
                if (x[i[c]] == y) swap(i, c, d--);
                --c;
            }
            if (b > c) break;
            swap(i, b, c);
            ++b;
            --c;
        }
        int r = Math.min(a - p, b - a);
        int s = Math.min(d - c, q - d);
        int t = q + 1;
        swap(i, p, b - r, r);
        swap(i, b, t - s, s);
        m[0] = p + (b - a);
        m[1] = q - (d - c);
    }

    private static int med3(short[] a, int i, int j, int k) {
        return a[i] < a[j] ? (a[j] < a[k] ? j : a[i] < a[k] ? k : i) : (a[j] > a[k] ? j : a[i] > a[k] ? k : i);
    }

    private static int med3(short[] a, int[] i, int j, int k, int l) {
        return a[i[j]] < a[i[k]] ? (a[i[k]] < a[i[l]] ? k : a[i[j]] < a[i[l]] ? l : j) : (a[i[k]] > a[i[l]] ? k : a[i[j]] > a[i[l]] ? l : j);
    }

    private static void swap(short[] a, int i, int j) {
        short ai = a[i];
        a[i] = a[j];
        a[j] = ai;
    }

    private static void swap(short[] a, int i, int j, int n) {
        while (n > 0) {
            short ai = a[i];
            a[i++] = a[j];
            a[j++] = ai;
            --n;
        }
    }

    private static void insertionSort(short[] a, int p, int q) {
        for (int i = p; i <= q; ++i) for (int j = i; j > p && a[j - 1] > a[j]; --j) swap(a, j, j - 1);
    }

    private static void insertionSort(short[] a, int[] i, int p, int q) {
        for (int j = p; j <= q; ++j) for (int k = j; k > p && a[i[k - 1]] > a[i[k]]; --k) swap(i, k, k - 1);
    }

    private static void quickSort(short[] a, int p, int q, int[] m) {
        if (q - p <= NSMALL_SORT) {
            insertionSort(a, p, q);
        } else {
            m[0] = p;
            m[1] = q;
            quickPartition(a, m);
            int r = m[0];
            int s = m[1];
            if (p < r - 1) quickSort(a, p, r - 1, m);
            if (q > s + 1) quickSort(a, s + 1, q, m);
        }
    }

    private static void quickSort(short[] a, int[] i, int p, int q, int[] m) {
        if (q - p <= NSMALL_SORT) {
            insertionSort(a, i, p, q);
        } else {
            m[0] = p;
            m[1] = q;
            quickPartition(a, i, m);
            int r = m[0];
            int s = m[1];
            if (p < r - 1) quickSort(a, i, p, r - 1, m);
            if (q > s + 1) quickSort(a, i, s + 1, q, m);
        }
    }

    private static void quickPartition(short[] x, int[] m) {
        int p = m[0];
        int q = m[1];
        int n = q - p + 1;
        int k = (p + q) / 2;
        if (n > NSMALL_SORT) {
            int j = p;
            int l = q;
            if (n > NLARGE_SORT) {
                int s = n / 8;
                j = med3(x, j, j + s, j + 2 * s);
                k = med3(x, k - s, k, k + s);
                l = med3(x, l - 2 * s, l - s, l);
            }
            k = med3(x, j, k, l);
        }
        short y = x[k];
        int a = p, b = p;
        int c = q, d = q;
        while (true) {
            while (b <= c && x[b] <= y) {
                if (x[b] == y) swap(x, a++, b);
                ++b;
            }
            while (c >= b && x[c] >= y) {
                if (x[c] == y) swap(x, c, d--);
                --c;
            }
            if (b > c) break;
            swap(x, b, c);
            ++b;
            --c;
        }
        int r = Math.min(a - p, b - a);
        int s = Math.min(d - c, q - d);
        int t = q + 1;
        swap(x, p, b - r, r);
        swap(x, b, t - s, s);
        m[0] = p + (b - a);
        m[1] = q - (d - c);
    }

    private static void quickPartition(short[] x, int[] i, int[] m) {
        int p = m[0];
        int q = m[1];
        int n = q - p + 1;
        int k = (p + q) / 2;
        if (n > NSMALL_SORT) {
            int j = p;
            int l = q;
            if (n > NLARGE_SORT) {
                int s = n / 8;
                j = med3(x, i, j, j + s, j + 2 * s);
                k = med3(x, i, k - s, k, k + s);
                l = med3(x, i, l - 2 * s, l - s, l);
            }
            k = med3(x, i, j, k, l);
        }
        short y = x[i[k]];
        int a = p, b = p;
        int c = q, d = q;
        while (true) {
            while (b <= c && x[i[b]] <= y) {
                if (x[i[b]] == y) swap(i, a++, b);
                ++b;
            }
            while (c >= b && x[i[c]] >= y) {
                if (x[i[c]] == y) swap(i, c, d--);
                --c;
            }
            if (b > c) break;
            swap(i, b, c);
            ++b;
            --c;
        }
        int r = Math.min(a - p, b - a);
        int s = Math.min(d - c, q - d);
        int t = q + 1;
        swap(i, p, b - r, r);
        swap(i, b, t - s, s);
        m[0] = p + (b - a);
        m[1] = q - (d - c);
    }

    private static int med3(int[] a, int i, int j, int k) {
        return a[i] < a[j] ? (a[j] < a[k] ? j : a[i] < a[k] ? k : i) : (a[j] > a[k] ? j : a[i] > a[k] ? k : i);
    }

    private static int med3(int[] a, int[] i, int j, int k, int l) {
        return a[i[j]] < a[i[k]] ? (a[i[k]] < a[i[l]] ? k : a[i[j]] < a[i[l]] ? l : j) : (a[i[k]] > a[i[l]] ? k : a[i[j]] > a[i[l]] ? l : j);
    }

    private static void swap(int[] a, int i, int j) {
        int ai = a[i];
        a[i] = a[j];
        a[j] = ai;
    }

    private static void swap(int[] a, int i, int j, int n) {
        while (n > 0) {
            int ai = a[i];
            a[i++] = a[j];
            a[j++] = ai;
            --n;
        }
    }

    private static void insertionSort(int[] a, int p, int q) {
        for (int i = p; i <= q; ++i) {
            for (int j = i; j > p && a[j - 1] > a[j]; --j) {
                swap(a, j, j - 1);
            }
        }
    }

    private static void insertionSort(int[] a, int[] i, int p, int q) {
        for (int j = p; j <= q; ++j) {
            for (int k = j; k > p && a[i[k - 1]] > a[i[k]]; --k) {
                swap(i, k, k - 1);
            }
        }
    }

    private static void quickSort(int[] a, int p, int q, int[] m) {
        if (q - p <= NSMALL_SORT) {
            insertionSort(a, p, q);
        } else {
            m[0] = p;
            m[1] = q;
            quickPartition(a, m);
            int r = m[0];
            int s = m[1];
            if (p < r - 1) quickSort(a, p, r - 1, m);
            if (q > s + 1) quickSort(a, s + 1, q, m);
        }
    }

    private static void quickSort(int[] a, int[] i, int p, int q, int[] m) {
        if (q - p <= NSMALL_SORT) {
            insertionSort(a, i, p, q);
        } else {
            m[0] = p;
            m[1] = q;
            quickPartition(a, i, m);
            int r = m[0];
            int s = m[1];
            if (p < r - 1) quickSort(a, i, p, r - 1, m);
            if (q > s + 1) quickSort(a, i, s + 1, q, m);
        }
    }

    private static void quickPartition(int[] x, int[] m) {
        int p = m[0];
        int q = m[1];
        int n = q - p + 1;
        int k = (p + q) / 2;
        if (n > NSMALL_SORT) {
            int j = p;
            int l = q;
            if (n > NLARGE_SORT) {
                int s = n / 8;
                j = med3(x, j, j + s, j + 2 * s);
                k = med3(x, k - s, k, k + s);
                l = med3(x, l - 2 * s, l - s, l);
            }
            k = med3(x, j, k, l);
        }
        int y = x[k];
        int a = p, b = p;
        int c = q, d = q;
        while (true) {
            while (b <= c && x[b] <= y) {
                if (x[b] == y) swap(x, a++, b);
                ++b;
            }
            while (c >= b && x[c] >= y) {
                if (x[c] == y) swap(x, c, d--);
                --c;
            }
            if (b > c) break;
            swap(x, b, c);
            ++b;
            --c;
        }
        int r = Math.min(a - p, b - a);
        int s = Math.min(d - c, q - d);
        int t = q + 1;
        swap(x, p, b - r, r);
        swap(x, b, t - s, s);
        m[0] = p + (b - a);
        m[1] = q - (d - c);
    }

    private static void quickPartition(int[] x, int[] i, int[] m) {
        int p = m[0];
        int q = m[1];
        int n = q - p + 1;
        int k = (p + q) / 2;
        if (n > NSMALL_SORT) {
            int j = p;
            int l = q;
            if (n > NLARGE_SORT) {
                int s = n / 8;
                j = med3(x, i, j, j + s, j + 2 * s);
                k = med3(x, i, k - s, k, k + s);
                l = med3(x, i, l - 2 * s, l - s, l);
            }
            k = med3(x, i, j, k, l);
        }
        int y = x[i[k]];
        int a = p, b = p;
        int c = q, d = q;
        while (true) {
            while (b <= c && x[i[b]] <= y) {
                if (x[i[b]] == y) swap(i, a++, b);
                ++b;
            }
            while (c >= b && x[i[c]] >= y) {
                if (x[i[c]] == y) swap(i, c, d--);
                --c;
            }
            if (b > c) break;
            swap(i, b, c);
            ++b;
            --c;
        }
        int r = Math.min(a - p, b - a);
        int s = Math.min(d - c, q - d);
        int t = q + 1;
        swap(i, p, b - r, r);
        swap(i, b, t - s, s);
        m[0] = p + (b - a);
        m[1] = q - (d - c);
    }

    private static int med3(long[] a, int i, int j, int k) {
        return a[i] < a[j] ? (a[j] < a[k] ? j : a[i] < a[k] ? k : i) : (a[j] > a[k] ? j : a[i] > a[k] ? k : i);
    }

    private static int med3(long[] a, int[] i, int j, int k, int l) {
        return a[i[j]] < a[i[k]] ? (a[i[k]] < a[i[l]] ? k : a[i[j]] < a[i[l]] ? l : j) : (a[i[k]] > a[i[l]] ? k : a[i[j]] > a[i[l]] ? l : j);
    }

    private static void swap(long[] a, int i, int j) {
        long ai = a[i];
        a[i] = a[j];
        a[j] = ai;
    }

    private static void swap(long[] a, int i, int j, int n) {
        while (n > 0) {
            long ai = a[i];
            a[i++] = a[j];
            a[j++] = ai;
            --n;
        }
    }

    private static void insertionSort(long[] a, int p, int q) {
        for (int i = p; i <= q; ++i) {
            for (int j = i; j > p && a[j - 1] > a[j]; --j) {
                swap(a, j, j - 1);
            }
        }
    }

    private static void insertionSort(long[] a, int[] i, int p, int q) {
        for (int j = p; j <= q; ++j) {
            for (int k = j; k > p && a[i[k - 1]] > a[i[k]]; --k) {
                swap(i, k, k - 1);
            }
        }
    }

    private static void quickSort(long[] a, int p, int q, int[] m) {
        if (q - p <= NSMALL_SORT) {
            insertionSort(a, p, q);
        } else {
            m[0] = p;
            m[1] = q;
            quickPartition(a, m);
            int r = m[0];
            int s = m[1];
            if (p < r - 1) quickSort(a, p, r - 1, m);
            if (q > s + 1) quickSort(a, s + 1, q, m);
        }
    }

    private static void quickSort(long[] a, int[] i, int p, int q, int[] m) {
        if (q - p <= NSMALL_SORT) {
            insertionSort(a, i, p, q);
        } else {
            m[0] = p;
            m[1] = q;
            quickPartition(a, i, m);
            int r = m[0];
            int s = m[1];
            if (p < r - 1) quickSort(a, i, p, r - 1, m);
            if (q > s + 1) quickSort(a, i, s + 1, q, m);
        }
    }

    private static void quickPartition(long[] x, int[] m) {
        int p = m[0];
        int q = m[1];
        int n = q - p + 1;
        int k = (p + q) / 2;
        if (n > NSMALL_SORT) {
            int j = p;
            int l = q;
            if (n > NLARGE_SORT) {
                int s = n / 8;
                j = med3(x, j, j + s, j + 2 * s);
                k = med3(x, k - s, k, k + s);
                l = med3(x, l - 2 * s, l - s, l);
            }
            k = med3(x, j, k, l);
        }
        long y = x[k];
        int a = p, b = p;
        int c = q, d = q;
        while (true) {
            while (b <= c && x[b] <= y) {
                if (x[b] == y) swap(x, a++, b);
                ++b;
            }
            while (c >= b && x[c] >= y) {
                if (x[c] == y) swap(x, c, d--);
                --c;
            }
            if (b > c) break;
            swap(x, b, c);
            ++b;
            --c;
        }
        int r = Math.min(a - p, b - a);
        int s = Math.min(d - c, q - d);
        int t = q + 1;
        swap(x, p, b - r, r);
        swap(x, b, t - s, s);
        m[0] = p + (b - a);
        m[1] = q - (d - c);
    }

    private static void quickPartition(long[] x, int[] i, int[] m) {
        int p = m[0];
        int q = m[1];
        int n = q - p + 1;
        int k = (p + q) / 2;
        if (n > NSMALL_SORT) {
            int j = p;
            int l = q;
            if (n > NLARGE_SORT) {
                int s = n / 8;
                j = med3(x, i, j, j + s, j + 2 * s);
                k = med3(x, i, k - s, k, k + s);
                l = med3(x, i, l - 2 * s, l - s, l);
            }
            k = med3(x, i, j, k, l);
        }
        long y = x[i[k]];
        int a = p, b = p;
        int c = q, d = q;
        while (true) {
            while (b <= c && x[i[b]] <= y) {
                if (x[i[b]] == y) swap(i, a++, b);
                ++b;
            }
            while (c >= b && x[i[c]] >= y) {
                if (x[i[c]] == y) swap(i, c, d--);
                --c;
            }
            if (b > c) break;
            swap(i, b, c);
            ++b;
            --c;
        }
        int r = Math.min(a - p, b - a);
        int s = Math.min(d - c, q - d);
        int t = q + 1;
        swap(i, p, b - r, r);
        swap(i, b, t - s, s);
        m[0] = p + (b - a);
        m[1] = q - (d - c);
    }

    private static int med3(float[] a, int i, int j, int k) {
        return a[i] < a[j] ? (a[j] < a[k] ? j : a[i] < a[k] ? k : i) : (a[j] > a[k] ? j : a[i] > a[k] ? k : i);
    }

    private static int med3(float[] a, int[] i, int j, int k, int l) {
        return a[i[j]] < a[i[k]] ? (a[i[k]] < a[i[l]] ? k : a[i[j]] < a[i[l]] ? l : j) : (a[i[k]] > a[i[l]] ? k : a[i[j]] > a[i[l]] ? l : j);
    }

    private static void swap(float[] a, int i, int j) {
        float ai = a[i];
        a[i] = a[j];
        a[j] = ai;
    }

    private static void swap(float[] a, int i, int j, int n) {
        while (n > 0) {
            float ai = a[i];
            a[i++] = a[j];
            a[j++] = ai;
            --n;
        }
    }

    private static void insertionSort(float[] a, int p, int q) {
        for (int i = p; i <= q; ++i) for (int j = i; j > p && a[j - 1] > a[j]; --j) swap(a, j, j - 1);
    }

    private static void insertionSort(float[] a, int[] i, int p, int q) {
        for (int j = p; j <= q; ++j) for (int k = j; k > p && a[i[k - 1]] > a[i[k]]; --k) swap(i, k, k - 1);
    }

    private static void quickSort(float[] a, int p, int q, int[] m) {
        if (q - p <= NSMALL_SORT) {
            insertionSort(a, p, q);
        } else {
            m[0] = p;
            m[1] = q;
            quickPartition(a, m);
            int r = m[0];
            int s = m[1];
            if (p < r - 1) quickSort(a, p, r - 1, m);
            if (q > s + 1) quickSort(a, s + 1, q, m);
        }
    }

    private static void quickSort(float[] a, int[] i, int p, int q, int[] m) {
        if (q - p <= NSMALL_SORT) {
            insertionSort(a, i, p, q);
        } else {
            m[0] = p;
            m[1] = q;
            quickPartition(a, i, m);
            int r = m[0];
            int s = m[1];
            if (p < r - 1) quickSort(a, i, p, r - 1, m);
            if (q > s + 1) quickSort(a, i, s + 1, q, m);
        }
    }

    private static void quickPartition(float[] x, int[] m) {
        int p = m[0];
        int q = m[1];
        int n = q - p + 1;
        int k = (p + q) / 2;
        if (n > NSMALL_SORT) {
            int j = p;
            int l = q;
            if (n > NLARGE_SORT) {
                int s = n / 8;
                j = med3(x, j, j + s, j + 2 * s);
                k = med3(x, k - s, k, k + s);
                l = med3(x, l - 2 * s, l - s, l);
            }
            k = med3(x, j, k, l);
        }
        float y = x[k];
        int a = p, b = p;
        int c = q, d = q;
        while (true) {
            while (b <= c && x[b] <= y) {
                if (x[b] == y) swap(x, a++, b);
                ++b;
            }
            while (c >= b && x[c] >= y) {
                if (x[c] == y) swap(x, c, d--);
                --c;
            }
            if (b > c) break;
            swap(x, b, c);
            ++b;
            --c;
        }
        int r = Math.min(a - p, b - a);
        int s = Math.min(d - c, q - d);
        int t = q + 1;
        swap(x, p, b - r, r);
        swap(x, b, t - s, s);
        m[0] = p + (b - a);
        m[1] = q - (d - c);
    }

    private static void quickPartition(float[] x, int[] i, int[] m) {
        int p = m[0];
        int q = m[1];
        int n = q - p + 1;
        int k = (p + q) / 2;
        if (n > NSMALL_SORT) {
            int j = p;
            int l = q;
            if (n > NLARGE_SORT) {
                int s = n / 8;
                j = med3(x, i, j, j + s, j + 2 * s);
                k = med3(x, i, k - s, k, k + s);
                l = med3(x, i, l - 2 * s, l - s, l);
            }
            k = med3(x, i, j, k, l);
        }
        float y = x[i[k]];
        int a = p, b = p;
        int c = q, d = q;
        while (true) {
            while (b <= c && x[i[b]] <= y) {
                if (x[i[b]] == y) swap(i, a++, b);
                ++b;
            }
            while (c >= b && x[i[c]] >= y) {
                if (x[i[c]] == y) swap(i, c, d--);
                --c;
            }
            if (b > c) break;
            swap(i, b, c);
            ++b;
            --c;
        }
        int r = Math.min(a - p, b - a);
        int s = Math.min(d - c, q - d);
        int t = q + 1;
        swap(i, p, b - r, r);
        swap(i, b, t - s, s);
        m[0] = p + (b - a);
        m[1] = q - (d - c);
    }

    private static int med3(double[] a, int i, int j, int k) {
        return a[i] < a[j] ? (a[j] < a[k] ? j : a[i] < a[k] ? k : i) : (a[j] > a[k] ? j : a[i] > a[k] ? k : i);
    }

    private static int med3(double[] a, int[] i, int j, int k, int l) {
        return a[i[j]] < a[i[k]] ? (a[i[k]] < a[i[l]] ? k : a[i[j]] < a[i[l]] ? l : j) : (a[i[k]] > a[i[l]] ? k : a[i[j]] > a[i[l]] ? l : j);
    }

    private static void swap(double[] a, int i, int j) {
        double ai = a[i];
        a[i] = a[j];
        a[j] = ai;
    }

    private static void swap(double[] a, int i, int j, int n) {
        while (n > 0) {
            double ai = a[i];
            a[i++] = a[j];
            a[j++] = ai;
            --n;
        }
    }

    private static void insertionSort(double[] a, int p, int q) {
        for (int i = p; i <= q; ++i) for (int j = i; j > p && a[j - 1] > a[j]; --j) swap(a, j, j - 1);
    }

    private static void insertionSort(double[] a, int[] i, int p, int q) {
        for (int j = p; j <= q; ++j) for (int k = j; k > p && a[i[k - 1]] > a[i[k]]; --k) swap(i, k, k - 1);
    }

    private static void quickSort(double[] a, int p, int q, int[] m) {
        if (q - p <= NSMALL_SORT) {
            insertionSort(a, p, q);
        } else {
            m[0] = p;
            m[1] = q;
            quickPartition(a, m);
            int r = m[0];
            int s = m[1];
            if (p < r - 1) quickSort(a, p, r - 1, m);
            if (q > s + 1) quickSort(a, s + 1, q, m);
        }
    }

    private static void quickSort(double[] a, int[] i, int p, int q, int[] m) {
        if (q - p <= NSMALL_SORT) {
            insertionSort(a, i, p, q);
        } else {
            m[0] = p;
            m[1] = q;
            quickPartition(a, i, m);
            int r = m[0];
            int s = m[1];
            if (p < r - 1) quickSort(a, i, p, r - 1, m);
            if (q > s + 1) quickSort(a, i, s + 1, q, m);
        }
    }

    private static void quickPartition(double[] x, int[] m) {
        int p = m[0];
        int q = m[1];
        int n = q - p + 1;
        int k = (p + q) / 2;
        if (n > NSMALL_SORT) {
            int j = p;
            int l = q;
            if (n > NLARGE_SORT) {
                int s = n / 8;
                j = med3(x, j, j + s, j + 2 * s);
                k = med3(x, k - s, k, k + s);
                l = med3(x, l - 2 * s, l - s, l);
            }
            k = med3(x, j, k, l);
        }
        double y = x[k];
        int a = p, b = p;
        int c = q, d = q;
        while (true) {
            while (b <= c && x[b] <= y) {
                if (x[b] == y) swap(x, a++, b);
                ++b;
            }
            while (c >= b && x[c] >= y) {
                if (x[c] == y) swap(x, c, d--);
                --c;
            }
            if (b > c) break;
            swap(x, b, c);
            ++b;
            --c;
        }
        int r = Math.min(a - p, b - a);
        int s = Math.min(d - c, q - d);
        int t = q + 1;
        swap(x, p, b - r, r);
        swap(x, b, t - s, s);
        m[0] = p + (b - a);
        m[1] = q - (d - c);
    }

    private static void quickPartition(double[] x, int[] i, int[] m) {
        int p = m[0];
        int q = m[1];
        int n = q - p + 1;
        int k = (p + q) / 2;
        if (n > NSMALL_SORT) {
            int j = p;
            int l = q;
            if (n > NLARGE_SORT) {
                int s = n / 8;
                j = med3(x, i, j, j + s, j + 2 * s);
                k = med3(x, i, k - s, k, k + s);
                l = med3(x, i, l - 2 * s, l - s, l);
            }
            k = med3(x, i, j, k, l);
        }
        double y = x[i[k]];
        int a = p, b = p;
        int c = q, d = q;
        while (true) {
            while (b <= c && x[i[b]] <= y) {
                if (x[i[b]] == y) swap(i, a++, b);
                ++b;
            }
            while (c >= b && x[i[c]] >= y) {
                if (x[i[c]] == y) swap(i, c, d--);
                --c;
            }
            if (b > c) break;
            swap(i, b, c);
            ++b;
            --c;
        }
        int r = Math.min(a - p, b - a);
        int s = Math.min(d - c, q - d);
        int t = q + 1;
        swap(i, p, b - r, r);
        swap(i, b, t - s, s);
        m[0] = p + (b - a);
        m[1] = q - (d - c);
    }
}
