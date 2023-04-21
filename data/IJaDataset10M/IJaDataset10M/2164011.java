package org.susan.java.classes.finalclass;

import java.util.Random;

class Value {

    int i;

    public Value(int i) {
        this.i = i;
    }
}

public class FinalData {

    private static Random random = new Random();

    private String idString;

    public FinalData(String id) {
        this.idString = id;
    }

    @SuppressWarnings("unused")
    private final int VAL_ONE = 9;

    @SuppressWarnings("unused")
    private static final int VAL_TWO = 99;

    public static final int VAL_THREE = 39;

    private final int i4 = random.nextInt(20);

    static final int i5 = random.nextInt(20);

    @SuppressWarnings("unused")
    private Value v1 = new Value(11);

    private final Value v2 = new Value(22);

    @SuppressWarnings("unused")
    private static final Value v3 = new Value(33);

    private final int[] a = { 1, 2, 3, 4, 5, 6 };

    public String toString() {
        return idString + ": " + "i4 = " + i4 + ", i5 = " + i5;
    }

    public static void main(String args[]) {
        FinalData fd1 = new FinalData("fd1");
        fd1.v2.i++;
        fd1.v1 = new Value(9);
        for (int i = 0; i < fd1.a.length; i++) {
            fd1.a[i]++;
        }
        System.out.println(fd1);
        System.out.println("Creating new FinalData");
        FinalData fd2 = new FinalData("fd2");
        System.out.println(fd1);
        System.out.println(fd2);
    }
}
