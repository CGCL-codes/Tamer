package org.privale.coreclients.depotclient;

import java.util.Random;

public class DistributionTest {

    public static void main(String args[]) {
        Random r = new Random();
        long counts[] = new long[16];
        for (int cnt = 0; cnt < 10000; cnt++) {
            double f0 = r.nextDouble();
            double f1 = r.nextDouble();
            double f2 = r.nextDouble();
            double f3 = r.nextDouble();
            double ft = f0 * f1 * f2 * f3;
            int idx = (int) ((double) counts.length * ft);
            counts[idx]++;
        }
        for (int idx = 0; idx < counts.length; idx++) {
            System.out.println();
            for (int cnt = 0; cnt < (counts[idx] / 100); cnt++) {
                System.out.print("=");
            }
            System.out.print(" " + counts[idx]);
        }
        System.out.println();
    }
}
