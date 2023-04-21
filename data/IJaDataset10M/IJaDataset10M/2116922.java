package org.virbo.jythonsupport;

import org.virbo.dsops.Ops;
import java.util.Random;
import org.virbo.dataset.QDataSet;

/**
 *
 * @author jbf
 */
public class TestOp {

    public static final int SIZE = 3000000;

    public static void main(String[] args) {
        for (int i = 0; i < 10; i++) {
            doRand();
        }
    }

    private static void doRand() {
        long t0 = System.currentTimeMillis();
        QDataSet rand = Ops.randn(SIZE);
        System.err.print(System.currentTimeMillis() - t0);
        t0 = System.currentTimeMillis();
        Random n = new Random();
        for (int i = 0; i < SIZE; i++) {
            n.nextGaussian();
        }
        System.err.print("  ");
        System.err.println(System.currentTimeMillis() - t0);
    }
}
