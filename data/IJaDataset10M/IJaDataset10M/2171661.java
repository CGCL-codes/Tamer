package org.isakiev.wic.demo;

import java.util.Arrays;
import org.isakiev.wic.core.math.CoordinateMatrix;
import org.isakiev.wic.core.math.CoordinateVector;
import org.isakiev.wic.core.math.MultiIndex;
import org.isakiev.wic.j2kfacade.J2KFacade;
import org.isakiev.wic.performance.DataAccessTest;

/**
 * Main demo class
 * 
 * @author Ruslan Isakiev
 */
public class Main {

    public static void main(String[] args) {
        new WICDemo4();
    }

    public static void testContiguityClassesRepresentatives() {
        CoordinateMatrix m = new CoordinateMatrix(0, 2, 1, 0);
        CoordinateMatrix mi = m.invert();
        for (int x = -7; x < 7; x++) {
            for (int y = -7; y < 7; y++) {
                CoordinateVector vector = new CoordinateVector(x, y);
                CoordinateVector result = mi.multiply(vector);
                if ((0 <= result.getX()) && (result.getX() < 1) && (0 <= result.getY()) && (result.getY() < 1)) {
                    System.out.println("x = " + x + "; y = " + y);
                }
            }
        }
    }

    public static void testPerformance() {
        long n = 1000 * 1000;
        while (true) {
            DataAccessTest.test(n);
            n *= 10;
        }
    }

    public static void testJ2KFacade() {
        J2KFacade.printVersion();
        J2KFacade.printUsage();
    }

    public static void testMultiIndex() {
        System.out.println("-- MultiIndex demo --");
        MultiIndex index = new MultiIndex(3, 0, 2);
        while (index.hasNext()) {
            System.out.println(Arrays.toString(index.next()));
        }
    }
}
