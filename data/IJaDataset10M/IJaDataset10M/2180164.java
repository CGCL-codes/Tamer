package defiant.common.tools;

import static defiant.common.tools.SysProperties.*;

public final class Logger {

    private Logger() {
    }

    public static final void log(String msg, Throwable ex) {
        System.err.println(msg);
        ex.printStackTrace(System.err);
    }

    public static final void log(String msg) {
        if (DEBUG) {
            System.out.println(msg);
        }
    }

    public static final void log(Throwable ex) {
        ex.printStackTrace(System.err);
    }
}
