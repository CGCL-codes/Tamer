package java.util;

public class Benchmark {

    public static native void start();

    public static native boolean stop(String message, int sec);
}
