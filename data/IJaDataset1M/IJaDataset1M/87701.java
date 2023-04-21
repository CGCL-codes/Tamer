package org.javaseis.util;

/**
 * Handy calls for printing small arrays such as 'position' arrays.
 */
public class DebugPrint {

    public static void println(String msg, int[] vals, int width) {
        System.out.println(getString(msg, vals, width));
    }

    public static String getString(String msg, int[] vals, int width) {
        String format = msg;
        for (int i = 0; i < vals.length; i++) {
            format += "%" + width + "d";
        }
        System.out.println("format = " + format);
        switch(vals.length) {
            case 1:
                return String.format(format, vals[0]);
            case 2:
                return String.format(format, vals[0], vals[1]);
            case 3:
                return String.format(format, vals[0], vals[1], vals[2]);
            case 4:
                return String.format(format, vals[0], vals[1], vals[2], vals[3]);
            case 5:
                return String.format(format, vals[0], vals[1], vals[2], vals[3], vals[4]);
            default:
                throw new IllegalArgumentException("Sorry, I'm not set up to handle all possible array lengths");
        }
    }
}
