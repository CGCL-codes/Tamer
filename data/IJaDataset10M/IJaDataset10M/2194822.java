package gridrm.stats;

import java.util.Random;

/**
 * Distribution.java This class generates various random variables for
 * distributions not directly supported in Java
 */
public class Distribution extends Random {

    public int nextPoisson(double lambda) {
        double elambda = Math.exp(-1 * lambda);
        double product = 1;
        int count = 0;
        int result = 0;
        while (product >= elambda) {
            product *= nextDouble();
            result = count;
            count++;
        }
        return result;
    }

    public double nextExponential(double b) {
        double randx;
        double result;
        randx = nextDouble();
        result = -1 * b * Math.log(randx);
        return result;
    }
}
