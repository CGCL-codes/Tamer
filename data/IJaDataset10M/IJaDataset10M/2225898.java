package ps.client.plugin.eq2.log;

import java.util.EventObject;

@SuppressWarnings("serial")
public class ParserEvent extends EventObject {

    public static final String NO_TRIGGER = "";

    LogLine logLine;

    String trigger = NO_TRIGGER;

    public ParserEvent(Object source, LogLine logLine, String trigger) {
        super(source);
        this.logLine = logLine;
        this.trigger = trigger;
    }

    public LogLine getLogLine() {
        return logLine;
    }

    public String getTrigger() {
        return trigger;
    }
}
