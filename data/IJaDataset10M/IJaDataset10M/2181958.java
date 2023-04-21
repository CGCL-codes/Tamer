package org.incava.util;

public class ANSI {

    private static String ESC = new String(new byte[] { 27 });

    public static String makeColor(int n) {
        return ESC + "[" + n + "m";
    }

    public static String NONE = makeColor(0);

    public static String RESET = makeColor(0);

    public static String BOLD = makeColor(1);

    public static String UNDERSCORE = makeColor(4);

    public static String UNDERLINE = makeColor(4);

    public static String BLINK = makeColor(5);

    public static String REVERSE = makeColor(7);

    public static String CONCEALED = makeColor(8);

    public static String BLACK = makeColor(30);

    public static String RED = makeColor(31);

    public static String GREEN = makeColor(32);

    public static String YELLOW = makeColor(33);

    public static String BLUE = makeColor(34);

    public static String MAGENTA = makeColor(35);

    public static String CYAN = makeColor(36);

    public static String WHITE = makeColor(37);

    public static String ON_BLACK = makeColor(40);

    public static String ON_RED = makeColor(41);

    public static String ON_GREEN = makeColor(42);

    public static String ON_YELLOW = makeColor(43);

    public static String ON_BLUE = makeColor(44);

    public static String ON_MAGENTA = makeColor(45);

    public static String ON_CYAN = makeColor(46);

    public static String ON_WHITE = makeColor(47);

    public static void main(String[] args) {
        String ESC = new String(new byte[] { 27 });
        System.out.println(ESC + "[34mblue" + ESC + "[0m");
        System.out.println(BOLD + "bold" + RESET);
    }
}
