package org.jazzteam.edu.algo.MassivLvl1;

public class SumMinusNPlusElements {

    public static int summ_element_positive(int[] array) {
        int positive = 0;
        for (int i = 0; i < array.length; i++) {
            if (array[i] > 0) {
                positive = positive + array[i];
            }
        }
        return positive;
    }

    public static int summ_element_negative(int[] array) {
        int negative = 0;
        for (int i = 0; i < array.length; i++) {
            if (array[i] < 0) {
                negative = negative + array[i];
            }
        }
        return negative;
    }

    public static void main(String[] args) {
        int[] mas = { 1, 5, 7, 8, 2, -1, -2, -5, 3, -5 };
        System.out.println(summ_element_positive(mas));
        System.out.println(summ_element_negative(mas));
    }
}
