package com.jmonkey.office.lexi.support;

/**
 * This class provides rudimentary diagnostics / trace printing.
 */
public class Code {

    /** This flag enables traceprints via 'debug' calls */
    private static final boolean DEBUG = false;

    /** This flag enables traceprints via 'event' calls */
    private static final boolean DEBUG_EVENTS = false;

    public static void debug(String msg) {
        if (DEBUG) {
            System.err.println("debug: " + msg);
        }
    }

    public static void message(String msg) {
        System.err.println(msg);
    }

    public static void failed(String msg) {
        System.err.println("FAILED: " + msg);
        System.exit(1);
    }

    public static void failed(Throwable ex) {
        System.err.println("FAILED: " + ex);
        ex.printStackTrace();
        System.exit(1);
    }

    public static void event(Object event) {
        if (DEBUG_EVENTS) {
            System.err.println("event: " + event);
        }
    }
}
