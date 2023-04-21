package com.dm.container;

import java.util.*;

class MyComparator implements Comparator {

    public int compare(Object o1, Object o2) {
        int a = ((Integer) o1).intValue();
        int b = ((Integer) o1).intValue();
        if (a == 3) return 1; else return -1;
    }
}

public class ArraysTest {

    public static void print(int[] a) {
        for (int i = 0; i < a.length; i++) {
            System.out.print(a[i] + " ");
        }
    }

    public static void main(String[] args) {
        int[] a1 = { 3, 4, 1, 2, 10, 7, 9, 8 };
        print(a1);
        Arrays.sort(a1);
        print(a1);
        Integer int1 = new Integer(2);
        int1 = int1 + 1;
        System.out.println(int1);
    }
}
