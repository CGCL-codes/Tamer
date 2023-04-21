package de.frostcode.visualmon.log.logback;

import java.text.DateFormat;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.ResourceBundle;
import javax.annotation.concurrent.Immutable;
import javax.annotation.concurrent.ThreadSafe;
import org.slf4j.LoggerFactory;
import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.spi.LoggingEvent;
import ch.qos.logback.core.AppenderBase;
import com.jamonapi.JAMonListenerFactory;
import com.jamonapi.Monitor;
import com.jamonapi.MonitorFactory;
import de.frostcode.visualmon.log.common.LogEntry;
import de.frostcode.visualmon.log.common.LogHeader;
import de.frostcode.visualmon.log.common.LogList;

/**
 * Appender implementation for logback which uses a JAMon monitor with a buffer listener to store
 * it's captured log messages. The list can be retrieved by a {@link LogbackProvider}.
 * <p>
 * The number of buffered messages can be configured as a property, for example:
 * <pre>
 * {@code
 * <appender name="VISUALMON" class="de.frostcode.visualmon.log.logback.LogbackAppender">
 *  <bufferSize>100</bufferSize>
 * </appender>
 *  }</pre>
 */
@ThreadSafe
public final class LogbackAppender extends AppenderBase<LoggingEvent> {

    static final String UNIT = "logback";

    private static final String LABEL_TOTAL = LogbackAppender.class.getName() + Level.ALL.toString();

    private static final Map<Integer, String> LABEL_LEVELS;

    private int bufferSize = 100;

    static {
        LABEL_LEVELS = new HashMap<Integer, String>(5);
        LABEL_LEVELS.put(Level.DEBUG.toInteger(), LogbackAppender.class.getName() + "." + Level.DEBUG.toString());
        LABEL_LEVELS.put(Level.ERROR.toInteger(), LogbackAppender.class.getName() + "." + Level.ERROR.toString());
        LABEL_LEVELS.put(Level.INFO.toInteger(), LogbackAppender.class.getName() + "." + Level.INFO.toString());
        LABEL_LEVELS.put(Level.TRACE.toInteger(), LogbackAppender.class.getName() + "." + Level.TRACE.toString());
        LABEL_LEVELS.put(Level.WARN.toInteger(), LogbackAppender.class.getName() + "." + Level.WARN.toString());
    }

    @Override
    public void start() {
        super.start();
        JAMonListenerFactory.put(new LogbackBufferListener());
        MonitorFactory.getMonitor(LABEL_TOTAL, UNIT).getListenerType(Monitor.VALUE).addListener(new LogbackBufferListener(bufferSize));
        for (String label : LABEL_LEVELS.values()) MonitorFactory.getMonitor(label, UNIT).getListenerType(Monitor.VALUE).addListener(new LogbackBufferListener(bufferSize));
    }

    @Override
    protected void append(final LoggingEvent event) {
        String message = (null == getLayout()) ? event.getFormattedMessage() : getLayout().doLayout(event);
        MonitorFactory.add(new LogbackMonKey(LABEL_LEVELS.get(event.getLevel().toInteger()), message, UNIT, event), 1);
        MonitorFactory.add(new LogbackMonKey(LABEL_TOTAL, message, UNIT, event), 1);
    }

    /**
   * Returns the configured number of buffered log messages. The default value is 100.
   * @return the configured number of buffered log messages
   */
    public int getBufferSize() {
        return bufferSize;
    }

    /**
   * Sets the number of log messages to buffer.
   * @param bufferSize the number of log messages to buffer
   */
    public void setBufferSize(final int bufferSize) {
        this.bufferSize = bufferSize;
    }

    static LogList getList(final Level level, final Locale locale) {
        String label = null;
        if (Level.ALL == level) label = LABEL_TOTAL; else if (LABEL_LEVELS.containsKey(level.toInteger())) label = LABEL_LEVELS.get(level.toInteger()); else throw new IllegalArgumentException("Level not handled: " + level);
        LogbackBufferListener listener = (LogbackBufferListener) MonitorFactory.getMonitor(label, UNIT).getListenerType(Monitor.VALUE).getListener(LogbackBufferListener.NAME);
        ResourceBundle bundle = ResourceBundle.getBundle("de.frostcode.visualmon.stats", locale);
        LogList list = new LogList(bundle.getString("logback.title"), bundle.getString("logback.empty"), new Header(locale));
        if (null == listener) {
            LoggerFactory.getLogger(LogbackAppender.class).info("Log list disabled because {} is not configured as an appender in logback", LogbackAppender.class.getName());
            return list;
        }
        DateFormat dateFormat = DateFormat.getDateTimeInstance(DateFormat.SHORT, DateFormat.LONG, locale);
        int num = 0;
        Object[][] rows = listener.getDetailData().getData();
        for (int i = rows.length - 1; i >= 0; i--) list.addEntry(new Entry(rows[i], num++, dateFormat));
        return list;
    }

    @Immutable
    private static final class Header implements LogHeader {

        private final ResourceBundle bundle;

        public Header(final Locale locale) {
            bundle = ResourceBundle.getBundle("de.frostcode.visualmon.stats", locale);
        }

        @Override
        public String getDateTitle() {
            return bundle.getString("logback.date");
        }

        @Override
        public String getExceptionTitle() {
            return bundle.getString("logback.exception");
        }

        @Override
        public String getLevelTitle() {
            return bundle.getString("logback.level");
        }

        @Override
        public String getLoggerTitle() {
            return bundle.getString("logback.logger");
        }

        @Override
        public String getMessageTitle() {
            return bundle.getString("logback.message");
        }

        @Override
        public String getThreadTitle() {
            return bundle.getString("logback.thread");
        }
    }

    @Immutable
    private static final class Entry implements LogEntry {

        private final int num;

        private final String logger, level, thread, message, date, exception;

        public Entry(final Object[] row, final int num, final DateFormat dateFormat) {
            this.num = num;
            logger = (String) row[0];
            level = row[1].toString();
            thread = (String) row[2];
            message = (String) row[3];
            date = dateFormat.format(row[4]);
            exception = null == row[5] ? "" : (String) row[5];
        }

        public int getNum() {
            return num;
        }

        public String getLogger() {
            return logger;
        }

        public String getLevel() {
            return level;
        }

        public String getThread() {
            return thread;
        }

        public String getMessage() {
            return message;
        }

        public String getException() {
            return exception;
        }

        public String getDate() {
            return date;
        }
    }
}
