package org.knosoft.kexceptions;

@SuppressWarnings("serial")
public class KnosoftException extends Exception {

    String cause;

    String detail;

    String action;

    StackTraceElement where;

    public KnosoftException(String cause, String detail, String action) {
        super(cause);
        this.cause = cause;
        this.detail = detail;
        this.action = action;
        this.where = this.getStackTrace()[0];
    }

    public KnosoftException(String cause, String detail) {
        this(cause, detail, "Unknow");
    }

    public KnosoftException(String cause) {
        this(cause, "Unknow", "Unknow");
    }

    public KnosoftException(Throwable cause, String detail, String action) {
        super(cause);
        this.cause = cause.getMessage();
        this.detail = detail;
        this.action = action;
        this.where = this.getStackTrace()[0];
    }

    public KnosoftException(Throwable cause, String detail) {
        this(cause, detail, "Unknow");
    }

    public KnosoftException(Throwable cause) {
        this(cause, "Unknow", "Unknow");
    }
}
