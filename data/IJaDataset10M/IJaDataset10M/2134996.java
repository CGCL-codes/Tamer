package net.aviv.javacowboys;

public class Trace {

    static boolean error = true;

    static boolean debug = true;

    public static void debug(String s) {
        if (debug) {
            System.out.println(s);
        }
    }

    public static void error(String s) {
        if (error) {
            System.out.println(s);
        }
    }
}
