package net.sf.logdistiller;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.text.ParseException;
import java.util.regex.Matcher;
import net.sf.logdistiller.LogType.AttributeInfo;
import org.apache.commons.lang.ArrayUtils;

/**
 * Log events base class. Every new log type must implement an inherited class, with appropriate:
 * <ul>
 * <li>{@link Factory} to create events from the log stream</li>
 * <li>{@link LogType} to declare the log type and eventually available parameters to customize its format</li>
 * </ul>
 *
 * @see LogType
 * @see LogTypes
 */
public abstract class LogEvent {

    private final Factory factory;

    private final String rawLog;

    /** provided attributes */
    private String[] attributes;

    /** extended attributes */
    private Extension[] extensions;

    protected LogEvent(Factory factory, String rawLog) {
        this.factory = factory;
        this.rawLog = rawLog;
        this.extensions = new Extension[factory.getDescription().getExtensions().length];
    }

    /**
     * Set the provided attributes values.
     *
     * @param attributes attributes values
     */
    protected void setAttributes(String[] attributes) {
        int count = factory.getDescription().getAttributesCount();
        if (attributes.length != count) {
            throw new IllegalArgumentException("expected " + count + " attributes, got " + attributes.length);
        }
        this.attributes = attributes;
    }

    protected void checkInitialized() {
        if (attributes == null) {
            throw new IllegalStateException("log event not initialized: call setAttributes() to initialize it");
        }
    }

    /**
     * Get the raw log text from which this event was parsed.
     *
     * @return String
     */
    public String getRawLog() {
        return rawLog;
    }

    /**
     * Get a provided attribute value by its position index.
     *
     * @param pos the position index of the attribute.
     * @return the value of the attribute.
     */
    public String getAttribute(int pos) {
        checkInitialized();
        return attributes[pos];
    }

    /**
     * Get a clone copy of the provided attributes. Note the this method clones the values array.
     *
     * @return (a copy of) provided attributes
     */
    public String[] getAttributes() {
        checkInitialized();
        return (String[]) ArrayUtils.clone(attributes);
    }

    /**
     * Get provided attributes count.
     *
     * @return the count of provided attributes
     */
    public int getAttributesCount() {
        checkInitialized();
        return attributes.length;
    }

    /**
     * Extracts an attribute's provided value of the log event.
     *
     * @param attributeName the name of the attribute (provided only)
     * @return the value
     */
    public String getAttribute(String attributeName) {
        int index = factory.getDescription().getAttributeIndex(attributeName);
        if (index < 0) {
            throw new IllegalArgumentException("unknown provided attribute '" + attributeName + "'");
        }
        return getAttribute(index);
    }

    public String getValue(AttributeInfo info) {
        if (info.extended) {
            Extension extension = extensions[info.index];
            if (extension == null) {
                extension = new Extension(factory.getDescription().getExtensions()[info.index]);
                extensions[info.index] = extension;
            }
            return extension.getValue(info.regexpGroup);
        }
        return getAttribute(info.index);
    }

    public Factory getFactory() {
        return factory;
    }

    public void dump(PrintWriter out) {
        out.println("- raw log: " + getRawLog());
        for (int i = 0; i < getAttributesCount(); i++) {
            out.println("- " + getFactory().getDescription().getAttributeName(i) + ": " + getAttribute(i));
        }
    }

    public String dump() {
        StringWriter sw = new StringWriter();
        PrintWriter out = new PrintWriter(sw, false);
        dump(out);
        out.close();
        return sw.toString();
    }

    /**
     * Get the timestamp.
     *
     * @return the timestamp or "-" if no this log type does not have a timestamp
     * @see LogType.Description#getTimestampAttribute()
     */
    public String getTimestamp() {
        int index = factory.getDescription().getTimestampAttribute();
        return (index > 0) ? getAttribute(index) : "-";
    }

    /**
     * The base class for LogEvent factories, responsible for parsing an input stream into <code>LogEvent</code>s.
     */
    public abstract static class Factory {

        public static final String NEWLINE = System.getProperty("line.separator");

        protected final LogType.Description description;

        protected final String logSource;

        private LogEvent lastEvent;

        private LogEvent pushedbackEvent;

        protected Factory(LogType.Description description, String logSource) {
            this.description = description;
            this.logSource = logSource;
        }

        /**
         * gets the next LogEvent.
         *
         * @throws IOException
         * @throws ParseException
         * @return LogEvent the new LogEvent read, or <code>null</code> if none available any more
         */
        public LogEvent nextEvent() throws IOException, ParseException {
            LogEvent nextEvent = null;
            if (pushedbackEvent != null) {
                nextEvent = pushedbackEvent;
                pushedbackEvent = null;
            } else {
                try {
                    nextEvent = readNextEvent();
                } catch (ParseException pe) {
                    ParseException pe2 = new ParseException("error in '" + logSource + "' " + pe.getMessage(), pe.getErrorOffset());
                    pe2.initCause(pe);
                    throw pe2;
                } catch (RuntimeException re) {
                    throw new RuntimeException("error in '" + logSource + "' " + re.getMessage(), re);
                }
            }
            lastEvent = nextEvent;
            return nextEvent;
        }

        public void pushbackLastEvent() throws IllegalStateException {
            if (pushedbackEvent != null) {
                throw new IllegalStateException("The last event has already been pushed back");
            }
            if (lastEvent == null) {
                throw new IllegalStateException("There is no last event to push back");
            }
            pushedbackEvent = lastEvent;
        }

        public LogType.Description getDescription() {
            return description;
        }

        public String getLogSource() {
            return logSource;
        }

        protected abstract LogEvent readNextEvent() throws IOException, ParseException;
    }

    /**
     * Extended attributes calculated for a log event.
     *
     * @since 1.1
     */
    private class Extension {

        /**
         * Values of regexp match groups
         */
        private final String[] values;

        public Extension(Attributes.Extension definition) {
            values = new String[definition.getProvides().size()];
            Matcher matcher = definition.getRegexp().matcher(getAttribute(definition.getSource()));
            if (matcher.find()) {
                int count = matcher.groupCount();
                for (int i = 0; i < values.length; i++) {
                    String value = (i < count) ? matcher.group(i + 1) : "";
                    values[i] = (value == null) ? "" : value;
                }
            } else {
                for (int i = 0; i < values.length; i++) {
                    values[i] = "";
                }
            }
        }

        public String getValue(int index) {
            return values[index];
        }
    }
}
