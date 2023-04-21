package com.mlib.algorithm.sort;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Test {

    /**
	 * @param args
	 */
    public static void main(String[] args) {
        Integer[] val = { 32, 4, 2, 43, 23, 343, 5, 23, 1, 1, 1, 1, 1, 1, 1, 32, 4, 2, 43, 23, 343, 5, 23, 1, 1, 1, 1, 1, 1, 1 };
        List<Comparable<?>> olist = new ArrayList<Comparable<?>>(val.length);
        for (Integer t : val) {
            olist.add(t);
        }
        val = new Integer[10000];
        for (int i = 0; i < val.length; i++) val[i] = (int) (10000 * Math.random());
        System.out.println(val.length);
        Sorter<Integer> sort = new Select<Integer>();
        sort.sort(Arrays.copyOf(val, val.length));
        Sorter<Integer> sort1 = new Bubble<Integer>();
        sort1.sort(Arrays.copyOf(val, val.length));
        Sorter<Integer> sort2 = new Heap<Integer>();
        sort2.sort(Arrays.copyOf(val, val.length));
        Sorter<Integer> sort3 = new Insert<Integer>();
        sort3.sort(Arrays.copyOf(val, val.length));
        Sorter<Integer> sort4 = new Quick<Integer>();
        sort4.sort(Arrays.copyOf(val, val.length));
        Sorter<Integer> sort5 = new Shell<Integer>();
        sort5.sort(Arrays.copyOf(val, val.length));
        System.out.println(sort + ":" + sort.getCompareTimes());
        System.out.println(sort1 + ":" + sort1.getCompareTimes());
        System.out.println(sort2 + ":" + sort2.getCompareTimes());
        System.out.println(sort3 + ":" + sort3.getCompareTimes());
        System.out.println(sort4 + ":" + sort4.getCompareTimes());
        System.out.println(sort5 + ":" + sort5.getCompareTimes());
    }
}
