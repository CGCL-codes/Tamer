package com.gorillalogic.dal.common.utils;

import com.gorillalogic.test.*;
import com.gorillalogic.dal.Log;
import java.io.PrintStream;
import java.util.Arrays;

/**
 * <code>IndexMap</code> maintains a growable array of integers,
 * optionally excluding duplicates.
 *
 * @author bpm
 * @version 1.0
 */
public final class IndexMap {

    long[] map = null;

    int alloc = -1;

    public String toString() {
        String state;
        if (map == null) {
            state = "(null)";
        } else {
            int len = map.length;
            int ma = alloc < 0 ? 0 : alloc;
            state = String.valueOf(ma) + '/' + len;
        }
        return "IndexMap " + state;
    }

    public static final IndexMap EMPTY = new IndexMap();

    public String contentToString() {
        StringBuffer buf = new StringBuffer();
        final int bound = 100;
        if (map != null) {
            int len = Math.min(alloc, bound);
            for (int i = 0; i < len; i++) {
                if (i > 0) buf.append(',');
                buf.append(map[i]);
            }
            if (alloc > bound) {
                buf.append(",...");
            }
        }
        return buf.toString();
    }

    public void dump() {
        final int bound = 100;
        PrintStream out = System.out;
        if (map == null) {
            out.println("[]");
        } else {
            out.print('[');
            int len = Math.min(alloc, bound);
            for (int i = 0; i < len; i++) {
                if (i > 0) out.print(',');
                out.print(String.valueOf(map[i]));
            }
            if (alloc > bound) {
                out.println(",...]");
            } else {
                out.println(']');
            }
        }
    }

    public int length() {
        return alloc < 0 ? 0 : alloc;
    }

    public long map(int x) {
        return map[x];
    }

    /**
	 * Return the first row matching the supplied value
	 *
	 * @param v the value to search for
	 * @return the index of the row, else -1 if not found
	 */
    public int posOfFirst(long v) {
        int sz = map == null ? 0 : map.length;
        for (int i = 0; i < sz; i++) {
            if (map[i] == v) {
                return i;
            }
        }
        return -1;
    }

    public interface Comparator {

        boolean gt(long x, long y);
    }

    public void sort() {
        if (alloc <= 1) return;
        Arrays.sort(map, 0, alloc);
    }

    public void sort(final Comparator comp) {
        if (alloc <= 1) return;
        Long[] longs = new Long[alloc];
        for (int i = 0; i < alloc; i++) {
            longs[i] = new Long(map[i]);
        }
        java.util.Comparator comparator = new java.util.Comparator() {

            public int compare(Object o1, Object o2) {
                Long l1 = (Long) o1;
                Long l2 = (Long) o2;
                if (comp.gt(l1.longValue(), l2.longValue())) {
                    return 1;
                } else {
                    return -1;
                }
            }
        };
        Arrays.sort(longs, 0, alloc, comparator);
        for (int i = 0; i < alloc; i++) {
            map[i] = longs[i].longValue();
        }
    }

    void bubbleSort(Comparator comp) {
        boolean changes = false;
        for (int i = 0; i < alloc - 1; i++) {
            int next = i + 1;
            if (comp.gt(map[i], map[next])) {
                long temp = map[next];
                map[next] = map[i];
                map[i] = temp;
                changes = true;
            }
        }
        if (changes) {
            bubbleSort(comp);
        }
    }

    void mergeSort(Comparator comp) {
        mergeSort(comp, map, 0, alloc - 1);
    }

    void mergeSort(Comparator comp, long a[], int lo, int hi) {
        if (lo == hi) {
            return;
        }
        int length = hi - lo + 1;
        int pivot = (lo + hi) / 2;
        mergeSort(comp, a, lo, pivot);
        mergeSort(comp, a, pivot + 1, hi);
        long working[] = new long[length];
        for (int i = 0; i < length; i++) {
            working[i] = a[lo + i];
        }
        int m1 = 0;
        int m2 = pivot - lo + 1;
        for (int i = 0; i < length; i++) {
            if (m2 <= hi - lo) {
                if (m1 <= pivot - lo) {
                    if (comp.gt(working[m1], working[m2])) {
                        a[i + lo] = working[m2++];
                    } else {
                        a[i + lo] = working[m1++];
                    }
                } else {
                    a[i + lo] = working[m2++];
                }
            } else {
                a[i + lo] = working[m1++];
            }
        }
    }

    public void clear() {
        map = null;
        alloc = -1;
    }

    public IndexMap clearForUpdate() {
        if (this == EMPTY) {
            return new IndexMap();
        }
        clear();
        return this;
    }

    public long add(long value, boolean allowDups) {
        if (!allowDups) {
            int len = map == null ? 0 : alloc;
            for (int i = 0; i < len; i++) {
                if (map[i] == value) {
                    return i;
                }
            }
        }
        ensureSpace(Math.max(alloc, 0));
        map[alloc] = value;
        return alloc++;
    }

    private void ensureSpace(int pos) {
        if (map == null) {
            int howMuch = Math.max(pos + 1, 8);
            map = new long[howMuch];
            alloc = 0;
        } else if (pos >= alloc) {
            int newMapLen = Math.max(pos + 1, map.length + 20);
            long[] newMap = new long[newMapLen];
            System.arraycopy(map, 0, newMap, 0, alloc);
            map = newMap;
        }
    }

    public void add(long[] value, boolean allowDups) {
        int sz = value == null ? 0 : value.length;
        for (int i = 0; i < sz; i++) {
            add(value[i], allowDups);
        }
    }

    public boolean remove(long value, boolean allowDups) {
        int len = length();
        boolean any = false;
        for (int i = 0; i < len; i++) {
            if (map[i] == value) {
                any = true;
                if (i < len - 1) {
                    System.arraycopy(map, i + 1, map, i, len - i - 1);
                }
                alloc--;
                len--;
                if (!allowDups) {
                    return true;
                }
            }
        }
        return any;
    }

    public boolean contains(long value) {
        int len = length();
        for (int i = 0; i < len; i++) {
            if (map[i] == value) {
                return true;
            }
        }
        return false;
    }

    public void set(int x, long value) {
        map[x] = value;
    }

    public void setSurely(int x, long value) {
        ensureSpace(x);
        if (x >= alloc) alloc = x + 1;
        set(x, value);
    }

    public static void main(String[] args) {
        Tester.run(IndexMap.class);
    }

    public static void testMapOps(Tester tester) {
        IndexMap map = new IndexMap();
        map.dump();
        map.add(5, false);
        map.dump();
        tester.testEq("first add len", 1, map.length());
        tester.testEq("first add val", 5, map.map(0));
        map.add(0, false);
        map.dump();
        tester.testEq("second add len", 2, map.length());
        tester.testEq("second add val", 5, map.map(0));
        tester.testEq("second add val", 0, map.map(1));
    }

    public static void testMapSequence(Tester tester) {
        IndexMap map = new IndexMap();
        final int LEN = 100;
        for (int i = 0; i < LEN; i++) {
            map.add(i, true);
        }
        map.dump();
        int a = 0;
        int b = 0;
        tester.testEq("len", map.length(), LEN);
        for (int i = 0; i < LEN; i++) {
            int exp = i;
            long got = map.map(i);
            if (i == 0 || exp != got) {
                tester.testEq("read" + i, exp, got);
            }
            a += i;
            b += map.map(i);
        }
        tester.testEq("sum", a, b);
        int[] nix = { 0, 5, 11, 23, 47, 48, 49, 50, 51, 77, 88, 97, 98, 99 };
        for (int i = 0; i < nix.length; i++) {
            map.remove(nix[i], true);
            int expLen = LEN - i - 1;
            int gotLen = map.length();
            if (i == 0 || expLen != gotLen) {
                tester.testEq("Fail on next" + i, expLen, gotLen);
            }
        }
        map.dump();
        tester.testEq("len2", map.length(), LEN - nix.length);
        int totalNix = 0;
        for (int i = 0; i < nix.length; i++) {
            totalNix += nix[i];
        }
        int totalAfterNix = 0;
        for (int i = 0; i < LEN - nix.length; i++) {
            totalAfterNix += map.map(i);
        }
        tester.testEq("nix", totalAfterNix, a - totalNix);
    }

    public static void testSort(Tester tester) {
        IndexMap map = new IndexMap();
        long[] xx = { 4, 7, 8, 2, 1, 22, 44, 12, 5, 55, 32 };
        map.add(xx, false);
        map.dump();
        map.sort();
        map.dump();
    }
}
