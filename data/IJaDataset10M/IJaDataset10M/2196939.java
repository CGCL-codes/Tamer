package com.amd.aparapi.test;

import com.amd.aparapi.Kernel;

public class TwoForLoops extends Kernel {

    public void run() {
        for (int i = 0; i < size; i++) {
            a[i] = i;
        }
        int sum = 0;
        for (int i = 0; i < size; i++) {
            sum += a[i];
        }
    }

    final int size = 100;

    int a[] = new int[size];
}
