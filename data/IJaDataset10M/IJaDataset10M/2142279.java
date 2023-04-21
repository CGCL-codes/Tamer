package com.xsky.common.util;

public class Sort {

    public int[] paopao(int[] source) {
        int temp;
        for (int i = 0; i < source.length; i++) {
            for (int j = source.length - 1; j > i; j--) {
                if (source[i] < source[j]) {
                    temp = source[i];
                    source[i] = source[j];
                    source[j] = temp;
                }
            }
        }
        return source;
    }
}
