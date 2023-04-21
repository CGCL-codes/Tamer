package protoj.build;

import java.io.IOException;
import org.apache.log4j.ConsoleAppender;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.apache.log4j.PatternLayout;
import org.apache.log4j.RollingFileAppender;

public final class AntLogging {

    private String appenderPattern;

    public AntLogging() {
        this.appenderPattern = "%d{HH:mm:ss,SSS} [%t] %m%n";
        if (isConsoleLoggingEnabled()) {
            addRootConsoleAppender("outAppender", ConsoleAppender.SYSTEM_OUT);
        }
        setRootLoggerLevel(getDefaultLoggerLevel());
    }

    public ConsoleAppender addRootConsoleAppender(String name, String target) {
        ConsoleAppender appender = new ConsoleAppender(new PatternLayout(appenderPattern), target);
        appender.setName(name);
        Logger.getRootLogger().addAppender(appender);
        return appender;
    }

    public RollingFileAppender addRootFileAppender(String logFile) {
        try {
            RollingFileAppender appender = new RollingFileAppender(new PatternLayout(appenderPattern), logFile);
            appender.setName(logFile);
            Logger.getRootLogger().addAppender(appender);
            return appender;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public String getDefaultLoggerLevel() {
        String value = System.getProperty("protojSetLoggerLevel", "INFO");
        return value;
    }

    public boolean isConsoleLoggingEnabled() {
        String value = System.getProperty("protojSetConsoleLoggingEnabled", Boolean.TRUE.toString());
        boolean isConsoleLoggingEnabled = Boolean.parseBoolean(value);
        return isConsoleLoggingEnabled;
    }

    public void setRootLoggerLevel(String level) {
        Logger.getRootLogger().setLevel(Level.toLevel(level));
    }

    public String getAppenderPattern() {
        return appenderPattern;
    }
}
