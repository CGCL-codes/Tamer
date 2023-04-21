package com.exm.chp02;

/**
 * Relational ang logical operators
 * @author Developer
 */
public class RelLogOps {

    public static void main(String[] args) {
        int i, j;
        boolean b1, b2;
        i = 10;
        j = 11;
        if (i < j) System.out.println("i < j");
        if (i <= j) System.out.println("i <= j");
        if (i != j) System.out.println("i != j");
        if (i == j) System.out.println("i == j");
        if (i >= j) System.out.println("i >= j");
        if (i > j) System.out.println("i > j");
        b1 = true;
        b2 = false;
        if (b1 & b2) System.out.println("b1 & b2");
        if (!(b1 & b2)) System.out.println("!(b1 & b2)");
        if (b1 | b2) System.out.println("b1 | b2");
        if (b1 ^ b2) System.out.println("b1 ^ b2");
    }
}
