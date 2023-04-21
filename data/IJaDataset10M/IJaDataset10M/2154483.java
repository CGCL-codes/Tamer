package logbeam.client.event;

import logbeam.model.LogFile;
import org.springframework.context.ApplicationEvent;

public class LogFileEvent extends ApplicationEvent {

    private static final long serialVersionUID = 2194349156399669340L;

    private LogFile logFile;

    public LogFileEvent(Object source, LogFile logFile) {
        super(source);
        this.logFile = logFile;
    }

    public LogFile getLogFile() {
        return logFile;
    }
}
