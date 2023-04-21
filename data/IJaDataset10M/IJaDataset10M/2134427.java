package com.lts.util;

import java.util.Enumeration;
import java.util.Vector;

public class SortTest {

    public static void main(String[] argv) {
        try {
            test();
        } catch (Throwable t) {
            t.printStackTrace();
        }
    }

    public static void test() {
        Bag b = new Bag();
        for (int i = 0; i < 20; i++) {
            Integer theInt = new Integer(i);
            b.addElement(theInt);
        }
        Vector v = new Vector();
        Enumeration enu = b.elements();
        while (enu.hasMoreElements()) {
            v.addElement(enu.nextElement());
        }
        System.out.println("Before sort:");
        enu = v.elements();
        while (enu.hasMoreElements()) {
            System.out.println(enu.nextElement());
        }
        System.out.println();
        System.out.println("After sort:");
        VectorSorter.sort(v, new IntegerCompareMethod());
        enu = v.elements();
        while (enu.hasMoreElements()) {
            System.out.println(enu.nextElement());
        }
        System.exit(0);
    }
}
