package jp.eflow.hisano.dalvikvm.jvmtests;

public class DoubleTest {

    public static void main(String[] args) {
        testMethod();
        testCompare();
        testConvert();
        testCalc();
        testArray();
        testStoreAndLoad();
        testConstant();
    }

    private static void testMethod() {
        System.out.println(add("1d + 2d = '", 1d, 2d, "'"));
    }

    private static String add(String header, double value1, double value2, String footer) {
        return header + add(value1, value2) + footer;
    }

    private static double add(double value1, double value2) {
        return value1 + value2;
    }

    private static void testCompare() {
        double value1 = Float.NaN;
        double value2 = Float.NaN;
        if (!(value1 < value2)) {
            System.out.println("Values are NaN.");
        }
        if (!(value1 > value2)) {
            System.out.println("Values are NaN.");
        }
        value1 = 0d;
        value2 = 0d;
        if (value1 <= value2) {
            System.out.println("0d <= 0d");
        }
        if (value1 >= value2) {
            System.out.println("0d >= 0d");
        }
        value1 = 0d;
        value2 = 1d;
        if (value1 < value2) {
            System.out.println("0d < 1d");
        }
        if (value1 > value2) {
            System.out.println("0d > 1d");
        }
        value1 = 1d;
        value2 = 0d;
        if (value1 < value2) {
            System.out.println("1d < 0d");
        }
        if (value1 > value2) {
            System.out.println("1d > 0d");
        }
    }

    private static void testConvert() {
        double d = 1.5d;
        System.out.println("(int)1.5d = " + (int) d);
        System.out.println("(long)1.5d = " + (long) d);
        System.out.println("(float)1.5d = " + (float) d);
        int i = 1;
        System.out.println("(double)1 = " + (double) i);
        long l = 1;
        System.out.println("(double)1L = " + (double) l);
        float f = 1f;
        System.out.println("(double)1f = " + (double) f);
    }

    private static void testCalc() {
        double d1 = 4d, d2 = 2d;
        System.out.println("4.0 + 2.0 = " + (d1 + d2));
        System.out.println("4.0 - 2.0 = " + (d1 - d2));
        System.out.println("4.0 * 2.0 = " + (d1 * d2));
        System.out.println("4.0 / 2.0 = " + (d1 / d2));
        System.out.println("4.0 % 2.0 = " + (d1 % d2));
        System.out.println("-(4.0) = " + (-d1));
    }

    private static void testArray() {
        double[] da = null;
        try {
            System.out.println(da[0]);
            System.out.println("A execution must not reach here because fa is null when loading.");
        } catch (NullPointerException e) {
        }
        try {
            da[0] = 0d;
            System.out.println("A execution must not reach here because fa is null when storeing.");
        } catch (NullPointerException e) {
        }
        da = new double[0];
        try {
            System.out.println(da[1]);
            System.out.println("A execution must not reach here because fa[1] is out of bounds when loading.");
        } catch (ArrayIndexOutOfBoundsException e) {
        }
        try {
            da[1] = 1d;
            System.out.println("A execution must not reach here because fa[1] is out of bounds when storing.");
        } catch (ArrayIndexOutOfBoundsException e) {
        }
        da = new double[1];
        da[0] = 2d;
        System.out.println("da[0] is '" + da[0] + "'.");
    }

    private static void testStoreAndLoad() {
        double d0 = 0;
        System.out.println("d0 = " + d0);
        double d1 = 1;
        System.out.println("d1 = " + d1);
        double d2 = 2;
        System.out.println("d2 = " + d2);
        double d3 = 3;
        System.out.println("d3 = " + d3);
        double d4 = 4;
        System.out.println("d4 = " + d4);
    }

    private static void testConstant() {
        System.out.println(0d);
        System.out.println(1d);
        System.out.println(2d);
    }
}
