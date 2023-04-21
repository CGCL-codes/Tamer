package au.id.jericho.lib.html;

final class LoggerFactory {

    private static LoggerProvider defaultLoggerProvider = null;

    public static Logger getLogger(final String name) {
        return getLoggerProvider().getLogger(name);
    }

    public static Logger getLogger(final Class loggedClass) {
        return getLogger(loggedClass.getName());
    }

    public static LoggerProvider getLoggerProvider() {
        return (Config.LoggerProvider != null) ? Config.LoggerProvider : getDefaultLoggerProvider();
    }

    private static LoggerProvider getDefaultLoggerProvider() {
        if (defaultLoggerProvider == null) defaultLoggerProvider = determineDefaultLoggerProvider();
        return defaultLoggerProvider;
    }

    private static LoggerProvider determineDefaultLoggerProvider() {
        if (isClassAvailable("org.slf4j.impl.StaticLoggerBinder")) {
            if (isClassAvailable("org.slf4j.impl.JDK14LoggerFactory")) return LoggerProvider.JAVA;
        }
        return LoggerProvider.JAVA;
    }

    private static boolean isClassAvailable(final String className) {
        try {
            Class.forName(className);
            return true;
        } catch (Throwable t) {
            return false;
        }
    }
}
