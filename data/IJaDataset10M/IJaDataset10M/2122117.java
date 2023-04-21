package uk.ac.essex.ia.util;

/**
 * A class that prints debugging messages. This can be used instead of
 * System.out lines. If you setDebugging to true then this willl print all
 * debugging messages.
 * @author Laurence Smith
 * @version 1.0
 */
public class DebugLog {

    private static boolean debugging = false;

    public static void setDebugging(boolean showDebugging) {
        debugging = showDebugging;
    }

    public static void println(String debugMessage) {
        if (debugging) System.out.println(debugMessage);
    }
}
