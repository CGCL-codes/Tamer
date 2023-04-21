package org.cake.game.geom.decomp.glutess;

class PriorityQSort extends PriorityQ {

    PriorityQHeap heap;

    Object[] keys;

    int[] order;

    int size, max;

    boolean initialized;

    PriorityQ.Leq leq;

    public PriorityQSort(PriorityQ.Leq leq) {
        heap = new PriorityQHeap(leq);
        keys = new Object[PriorityQ.INIT_SIZE];
        size = 0;
        max = PriorityQ.INIT_SIZE;
        initialized = false;
        this.leq = leq;
    }

    void pqDeletePriorityQ() {
        if (heap != null) heap.pqDeletePriorityQ();
        order = null;
        keys = null;
    }

    private static boolean LT(PriorityQ.Leq leq, Object x, Object y) {
        return (!PriorityQHeap.LEQ(leq, y, x));
    }

    private static boolean GT(PriorityQ.Leq leq, Object x, Object y) {
        return (!PriorityQHeap.LEQ(leq, x, y));
    }

    private static void Swap(int[] array, int a, int b) {
        if (true) {
            int tmp = array[a];
            array[a] = array[b];
            array[b] = tmp;
        } else {
        }
    }

    private static class Stack {

        int p, r;
    }

    boolean pqInit() {
        int p, r, i, j;
        int piv;
        Stack[] stack = new Stack[50];
        for (int k = 0; k < stack.length; k++) {
            stack[k] = new Stack();
        }
        int top = 0;
        int seed = 2016473283;
        order = new int[size + 1];
        p = 0;
        r = size - 1;
        for (piv = 0, i = p; i <= r; ++piv, ++i) {
            order[i] = piv;
        }
        stack[top].p = p;
        stack[top].r = r;
        ++top;
        while (--top >= 0) {
            p = stack[top].p;
            r = stack[top].r;
            while (r > p + 10) {
                seed = Math.abs(seed * 1539415821 + 1);
                i = p + seed % (r - p + 1);
                piv = order[i];
                order[i] = order[p];
                order[p] = piv;
                i = p - 1;
                j = r + 1;
                do {
                    do {
                        ++i;
                    } while (GT(leq, keys[order[i]], keys[piv]));
                    do {
                        --j;
                    } while (LT(leq, keys[order[j]], keys[piv]));
                    Swap(order, i, j);
                } while (i < j);
                Swap(order, i, j);
                if (i - p < r - j) {
                    stack[top].p = j + 1;
                    stack[top].r = r;
                    ++top;
                    r = i - 1;
                } else {
                    stack[top].p = p;
                    stack[top].r = i - 1;
                    ++top;
                    p = j + 1;
                }
            }
            for (i = p + 1; i <= r; ++i) {
                piv = order[i];
                for (j = i; j > p && LT(leq, keys[order[j - 1]], keys[piv]); --j) {
                    order[j] = order[j - 1];
                }
                order[j] = piv;
            }
        }
        max = size;
        initialized = true;
        heap.pqInit();
        return true;
    }

    int pqInsert(Object keyNew) {
        int curr;
        if (initialized) {
            return heap.pqInsert(keyNew);
        }
        curr = size;
        if (++size >= max) {
            Object[] saveKey = keys;
            max <<= 1;
            Object[] pqKeys = new Object[max];
            System.arraycopy(keys, 0, pqKeys, 0, keys.length);
            keys = pqKeys;
            if (keys == null) {
                keys = saveKey;
                return Integer.MAX_VALUE;
            }
        }
        assert curr != Integer.MAX_VALUE;
        keys[curr] = keyNew;
        return -(curr + 1);
    }

    Object pqExtractMin() {
        Object sortMin, heapMin;
        if (size == 0) {
            return heap.pqExtractMin();
        }
        sortMin = keys[order[size - 1]];
        if (!heap.pqIsEmpty()) {
            heapMin = heap.pqMinimum();
            if (LEQ(leq, heapMin, sortMin)) {
                return heap.pqExtractMin();
            }
        }
        do {
            --size;
        } while (size > 0 && keys[order[size - 1]] == null);
        return sortMin;
    }

    Object pqMinimum() {
        Object sortMin, heapMin;
        if (size == 0) {
            return heap.pqMinimum();
        }
        sortMin = keys[order[size - 1]];
        if (!heap.pqIsEmpty()) {
            heapMin = heap.pqMinimum();
            if (PriorityQHeap.LEQ(leq, heapMin, sortMin)) {
                return heapMin;
            }
        }
        return sortMin;
    }

    boolean pqIsEmpty() {
        return (size == 0) && heap.pqIsEmpty();
    }

    void pqDelete(int curr) {
        if (curr >= 0) {
            heap.pqDelete(curr);
            return;
        }
        curr = -(curr + 1);
        assert curr < max && keys[curr] != null;
        keys[curr] = null;
        while (size > 0 && keys[order[size - 1]] == null) {
            --size;
        }
    }
}
