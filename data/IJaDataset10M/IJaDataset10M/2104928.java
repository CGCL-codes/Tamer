package org.systemsbiology.utils;

import java.util.ArrayList;
import java.util.Collections;

/**
 * DOCU add javadoc comment
 * 
 * @author M. Vogelzang
 */
public class MannWhitney {

    protected static class Value implements Comparable {

        double value;

        byte origin;

        public Value(double value, byte origin) {
            this.value = value;
            this.origin = origin;
        }

        public int compareTo(Object o) {
            Value v = (Value) o;
            if (value > v.value) return 1;
            if (value < v.value) return -1;
            return 0;
        }
    }

    public static double getZScore(double[] array1, double[] array2) {
        double n1 = array1.length, n2 = array2.length;
        ArrayList list = new ArrayList();
        for (int i = 0; i < array1.length; i++) list.add(new Value(array1[i], (byte) 1));
        for (int i = 0; i < array2.length; i++) list.add(new Value(array2[i], (byte) 2));
        Collections.sort(list);
        int rank = 1;
        int rankcount;
        double ranksum;
        int index = 0;
        double U1 = 0, U2 = 0;
        while (index < list.size()) {
            double currentValue = ((Value) list.get(index++)).value;
            ranksum = rank++;
            rankcount = 1;
            while (index < list.size() && ((Value) list.get(index)).value <= currentValue) {
                index++;
                ranksum += rank++;
                rankcount++;
            }
            for (int i = index - rankcount; i < index; i++) {
                if (((Value) list.get(i)).origin == 1) U1 += ranksum / rankcount; else U2 += ranksum / rankcount;
            }
        }
        U1 = n1 * n2 + (n1 * (n1 + 1) / 2.0) - U1;
        U2 = n1 * n2 + (n2 * (n2 + 1) / 2.0) - U2;
        if (U1 > U2) return (U1 - n1 * n2 / 2.0) / Math.sqrt(n1 * n2 * (n1 + n2 + 1) / 12.0); else return -(U2 - n1 * n2 / 2.0) / Math.sqrt(n1 * n2 * (n1 + n2 + 1) / 12.0);
    }
}
