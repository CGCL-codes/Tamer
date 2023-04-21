package org.peaseplate.utils.log.internal;

import org.peaseplate.utils.log.LogLevel;

/**
 * Logs to the System.out and System.err
 * 
 * @author Manfred HANTSCHEL
 */
public class SystemLogHandler extends AbstractLogHandler {

    private final LogLevel level = LogLevel.DEBUG;

    /**
	 * {@inheritDoc}
	 */
    @Override
    public boolean isEnabled(final LogLevel level, final String name) {
        return (level.ordinal() > this.level.ordinal());
    }

    /**
	 * {@inheritDoc}
	 */
    @Override
    public void log(final LogLevel level, final String name, final String message) {
        if (isEnabled(level, name)) {
            System.out.println(String.format("%5s | %-120s | %s", level.toString(), message, name));
        }
    }

    @Override
    public void log(final LogLevel level, final String name, final String message, final Throwable e) {
        if (isEnabled(level, name)) {
            System.out.println(String.format("%5s | %-120s | %s", level.toString(), message, name));
            e.printStackTrace(System.out);
        }
    }
}
