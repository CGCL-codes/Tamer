package tests.jfun.yan.xml;

public class Counting {

    private static int c = 1;

    public static int count(int i) {
        if (i != c) {
            throw new IllegalArgumentException("" + i + " expected, while at " + c);
        }
        return ++c;
    }
}
