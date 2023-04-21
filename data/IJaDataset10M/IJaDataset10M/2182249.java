package com.borzak.cncmill;

public class MillingException extends RuntimeException {

    private static final long serialVersionUID = -8207580458694213335L;

    public static final int UNKNOWN = 0;

    public static final int SERIAL_ABORT = 1;

    public static final int LIMIT_ERROR = 2;

    public static final int INVALID_COMMAND = 3;

    public static final int NOT_IMPLEMENTED = 4;

    public static final int SOFT_LIMIT = 5;

    public static final int BROWNOUT_ERROR = 6;

    public static final int COMM_FAILURE = 7;

    private int reason = UNKNOWN;

    private static String REASON_DESCRIPTION[] = { "Unknown Exception", "Serial Character cancelled receive", "Limit Switch has been tripped", "Invalid Command - probably communications error", "Command not implemented - old firmware or communications error", "Software Location Limit Exceeded: ", "Hardware Brownout Reset", "Communications failure" };

    private String response = null;

    public MillingException() {
        super();
    }

    public MillingException(String arg0) {
        super(arg0);
    }

    public MillingException(Throwable arg0) {
        super(arg0);
    }

    public MillingException(String arg0, Throwable arg1) {
        super(arg0, arg1);
    }

    public MillingException(String response, int reason) {
        this(REASON_DESCRIPTION[reason]);
        this.reason = reason;
        this.response = response;
    }

    public MillingException(int reason, String text) {
        this(REASON_DESCRIPTION[reason] + text);
        this.reason = reason;
    }

    public int getReason() {
        return reason;
    }

    public String getResponse() {
        return response;
    }
}
