package org.gwm.splice.client.logger;

public class LogItem {

    public static final int INFO = 0;

    public static final int WARN = 1;

    public static final int ERROR = 2;

    public static final int SEVERE = 3;

    public static final int FATAL = 4;

    public int status;

    public String message;

    public Object source;

    public LogItem(int status, String message, Object source) {
        super();
        this.status = status;
        this.message = message;
        this.source = source;
    }

    public LogItem(int status, String message) {
        super();
        this.status = status;
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public Object getSource() {
        return source;
    }

    public void setSource(Object source) {
        this.source = source;
    }
}
