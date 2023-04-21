package com.exm.chp03;

/**
 * Using break command to exit a loop
 * @author Developer
 */
public class BreakDemo {

    public static void main(String[] args) {
        int num = 100;
        for (int i = 0; i < num; i++) {
            if (i * i >= num) {
                System.out.print(" ...breaking");
                break;
            }
            System.out.print(i + " ");
        }
        System.out.println("\nOuter of the loop.");
    }
}
