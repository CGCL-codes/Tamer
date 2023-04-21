package se.antimon.colourcontrols;

public class MyLogger {

    public static void debug(String string) {
        System.out.println("Debug: " + string);
    }

    public static void error(String string, Exception e) {
        System.err.println(string);
        e.printStackTrace();
    }
}
