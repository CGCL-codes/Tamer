package com.googlecode.jwsm;

import java.util.logging.Logger;

public class Log {

    public static final void fine(String s) {
        get().fine(s);
    }

    public static final void info(String s) {
        get().info(s);
    }

    public static final void warn(String s) {
        get().warning(s);
    }

    public static final Logger get() {
        return Logger.getLogger("jwsm");
    }
}
