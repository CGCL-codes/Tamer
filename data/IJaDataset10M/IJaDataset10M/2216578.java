package logbeam.model.services.logmessage;

import jonas.annotations.JonasElement;
import logbeam.model.LogMessage;

public class NewLogMessageRequest {

    private LogMessage logMessage;

    @JonasElement
    public LogMessage getLogMessage() {
        return logMessage;
    }

    public void setLogMessage(LogMessage logMessage) {
        this.logMessage = logMessage;
    }
}
