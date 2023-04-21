package org.serdom.xeland3d;

import java.util.Random;

public class RandomTester {

    /**
	 * @param args
	 */
    public static void main(String[] args) {
        int[] hist = new int[200];
        Random rnd = new Random(1);
        for (int i = 0; i < 20000; i++) {
            double result = rnd.nextGaussian();
            int index = (int) (result * 10) + 100;
            hist[index]++;
        }
        for (int i = 0; i < 200; i++) {
            System.out.print(hist[i] + " ");
        }
        System.out.println();
        hist = new int[200];
        rnd = new Random(1);
        for (int i = 0; i < 20000; i++) {
            double result = rnd.nextDouble();
            int index = (int) (result * 10) + 100;
            hist[index]++;
        }
        for (int i = 0; i < 200; i++) {
            System.out.print(hist[i] + " ");
        }
    }
}
