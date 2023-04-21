package com.dukesoftware.utils.algorithm.sort;

public class InsertSort2 implements IIntegerSorter {

    @Override
    public void sort(int a[]) {
        a[a.length - 1] = Integer.MAX_VALUE;
        for (int i = a.length - 2; i >= 0; i--) {
            int j, x = a[i];
            for (j = i + 1; a[j] < x; j++) a[j - 1] = a[j];
            a[j - 1] = x;
        }
    }
}
