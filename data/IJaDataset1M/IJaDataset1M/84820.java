package cn.hansfly.puzzle;

public class OddGrass {

    public static void main(String[] args) {
        long t1 = System.currentTimeMillis();
        for (long i = 1; i < 1000000000; ++i) isOdd(i);
        long t2 = System.currentTimeMillis();
        System.out.println(t2 - t1);
        t1 = System.currentTimeMillis();
        for (long i = 1; i < 1000000000; ++i) isOdd2(i);
        t2 = System.currentTimeMillis();
        System.out.println(t2 - t1);
    }

    public static boolean isOdd(long a) {
        return (a & 1) != 0;
    }

    public static boolean isOdd2(long a) {
        return !(a % 2 == 0);
    }
}
