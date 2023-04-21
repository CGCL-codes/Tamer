package org.retro.test;

import org.retro.neural.*;

public class NeuralTest {

    public static void main(String[] args) {
        System.out.println(" [ Running Neural Test ]");
        double[] zero = { 0, 1 };
        double[] one = { 1, 0 };
        double[][] a = { zero, zero, one, one };
        double[][] b = { zero, one, zero, one };
        double[][] c = { zero, one, one, zero };
        int nep = 1000;
        double eta = 0.9;
        double mu = 0.0;
        BPLayer i1 = new BPLayer(2);
        BPLayer i2 = new BPLayer(2);
        BPLayer o = new BPLayer(2);
        o.connect(i1, i2);
        o.randomize();
        i1.attach(a);
        i2.attach(b);
        o.attach(c);
        o.batch(nep, eta, mu, 100);
        double[][] yy = o.test();
        System.out.println();
        for (int i = 0; i < yy.length; ++i) {
            for (int j = 0; j < yy[i].length; ++j) {
                System.out.print(yy[i][j] + " ");
            }
            System.out.println();
        }
    }
}
