package com.mycila.log.nop;

import com.mycila.log.Logger;
import com.mycila.log.LoggerProvider;

/**
 * @author Mathieu Carbou (mathieu.carbou@gmail.com)
 */
public final class NopLoggerProvider implements LoggerProvider {

    private static final LoggerProvider INSTANCE = new NopLoggerProvider();

    public NopLoggerProvider() {
    }

    public Logger get(String name) {
        return NopLogger.INSTANCE;
    }

    public static LoggerProvider get() {
        return INSTANCE;
    }
}
