package org.sinaxe;

import java.io.PrintStream;
import org.sinaxe.runtime.SinaxeDebugger;
import org.xml.sax.SAXParseException;

public class SinaxeErrorHandler {

    public static final int FULLTRACELEVEL = 1;

    private static SinaxeErrorHandler instance = new SinaxeErrorHandler();

    private static PrintStream pStream = System.err;

    private SinaxeErrorHandler() {
    }

    public static void setPrintStream(PrintStream pStream) {
        SinaxeErrorHandler.pStream = pStream;
    }

    private static StackTraceElement getCallerSTE() {
        Throwable t = new Throwable();
        StackTraceElement elements[] = t.getStackTrace();
        for (int i = 0; i < elements.length; i++) if (!elements[i].getClassName().equals(instance.getClass().getName())) return elements[i];
        return null;
    }

    private static void printCause(Throwable cause) {
        printCause(cause, 0);
    }

    private static void printCause(Throwable cause, int depth) {
        if (cause != null && depth <= 10) {
            pStream.print("   Caused by error ");
            pStream.print("(" + cause.getClass().getName() + ") ");
            pStream.println(cause.getMessage());
            if (SinaxeDebugger.level(1)) pStream.println("              @ " + cause.getStackTrace()[0]);
            if (SinaxeDebugger.level(FULLTRACELEVEL)) {
                StackTraceElement trace[] = cause.getStackTrace();
                for (int i = 1; i < trace.length; i++) pStream.println("                " + trace[i]);
            }
            printCause(cause.getCause(), depth + 1);
        }
    }

    private static void printMsg(String msg, Exception e) {
        pStream.print(msg);
        if (e instanceof SAXParseException) {
            SAXParseException parseEx = (SAXParseException) e;
            pStream.print(" in File '" + parseEx.getSystemId() + "' @ Line: " + parseEx.getLineNumber());
            if (parseEx.getColumnNumber() >= 0) pStream.print(" Col: " + parseEx.getColumnNumber());
        }
        pStream.println();
        if (SinaxeDebugger.level(1)) pStream.println("              @ " + getCallerSTE());
        if (e != null) {
            printCause(e);
            if (SinaxeDebugger.getLevel() < FULLTRACELEVEL) pStream.println("   (Increase debug level for complete stack traces.)");
        }
    }

    public static void fatal(String msg) {
        fatal(msg, null);
    }

    public static void fatal(String msg, Exception e) {
        printMsg("Sinaxe FATAL ERROR: " + msg, e);
        System.exit(1);
    }

    public static void error(String msg) {
        error(msg, null);
    }

    public static void error(String msg, Exception e) {
        printMsg("Sinaxe ERROR: " + msg, e);
    }

    public static void warning(String msg) {
        warning(msg, null);
    }

    public static void warning(String msg, Exception e) {
        printMsg("Sinaxe WARNING: " + msg, e);
    }
}
