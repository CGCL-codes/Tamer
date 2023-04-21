package org.vastenhouw.util;

public class DebugAwtExceptionHandler {

    public static void register() {
        System.setProperty("sun.awt.exception.handler", DebugAwtExceptionHandler.class.getName());
    }

    public DebugAwtExceptionHandler() {
    }

    public void handle(Throwable t) {
        System.err.println("Exception occurred during event dispatching:");
        t.printStackTrace();
        if (Debug.out != System.out) {
            if (Debug.INFO) Debug.out.println("Exception occurred during event dispatching:");
            t.printStackTrace(Debug.out);
        }
    }
}
