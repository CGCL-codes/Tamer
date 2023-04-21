package org.gwtoolbox.commons.util.client;

/**
 * @author Uri Boness
 */
public interface Logger {

    public enum Level {

        TRACE(1), DEBUG(2), INFO(4), WARN(8), ERROR(16);

        private int value;

        Level(int value) {
            this.value = value;
        }

        public int getValue() {
            return value;
        }

        public boolean isEnabled(Level level) {
            return level.value >= value;
        }
    }

    void trace(String message);

    void trace(String message, Throwable t);

    boolean isTraceEnabled();

    void debug(String message);

    void debug(String message, Throwable t);

    boolean isDebugEnabled();

    void info(String message);

    void info(String message, Throwable t);

    boolean isInfoEnabled();

    void warn(String message);

    void warn(String message, Throwable t);

    boolean isWarnEnabled();

    void error(String message);

    void error(String message, Throwable t);

    boolean isErrorEnabled();
}
