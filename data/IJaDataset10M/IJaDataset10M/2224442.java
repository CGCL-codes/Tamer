package it.timehero.test;

import it.timehero.util.Helper;

/**
 * @author AM
 * @project timehero
 */
public class TestDado {

    /**
	 * Test del 1d6 di Timehero
	 * @param args
	 */
    public static void main(String[] args) {
        for (int i = 0; i < 10; i++) {
            System.out.println("Dado : " + Helper.getRisultatoDado());
        }
    }
}
