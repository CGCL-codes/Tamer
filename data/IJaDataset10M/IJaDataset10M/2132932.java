package org.netbeams.dsp.util;

public class Log {

    public static void log(String msg) {
        System.out.println(msg);
    }

    public static void log(Throwable t) {
        t.printStackTrace(System.out);
    }

    public static void log(String msg, Throwable t) {
        System.out.println(msg);
        t.printStackTrace();
    }
}
