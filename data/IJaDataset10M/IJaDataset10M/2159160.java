package com.mycila.log.nop;

import com.mycila.log.AbstractLogger;
import com.mycila.log.Level;
import com.mycila.log.Logger;

/**
 * @author Mathieu Carbou (mathieu.carbou@gmail.com)
 */
public final class NopLogger extends AbstractLogger {

    public static final Logger INSTANCE = new NopLogger();

    private NopLogger() {
    }

    @Override
    protected void doLog(Level level, Throwable throwable, Object message, Object... args) {
    }

    public boolean canLog(Level level) {
        return false;
    }
}
