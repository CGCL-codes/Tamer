package gem.util;

/**
 * @author Ozgun Babur
 */
public class Factorial {

    private static double[] fact = new double[171];

    static {
        fact[0] = 1;
        for (int i = 1; i < fact.length; i++) {
            fact[i] = fact[i - 1] * i;
        }
    }

    public static double calc(double x) {
        int i = (int) Math.round(x);
        return fact[i];
    }

    public static void main(String[] args) {
        double a = 1;
        for (int i = 2; i < 200; i++) {
            a *= i;
            System.out.println(i + "\t" + a);
        }
    }
}
