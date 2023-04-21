package org.openremote.controller.protocol.port;

/**
 * Exception related to ports.
 * 
 */
public class PortException extends Exception {

    private static final long serialVersionUID = 1L;

    public static final int INVALID_MESSAGE = -2;

    public static final int SERVICE_TIMEOUT = -3;

    public static final int SERVICE_FAILED = -4;

    public static final int ALREADY_LISTENING = -5;

    public static final int INVALID_CONFIGURATION = -6;

    private int code;

    private int rootCode;

    public PortException(int code) {
        this(code, 0);
    }

    public PortException(int code, int rootCode) {
        this.code = code;
        this.rootCode = rootCode;
    }

    public int getCode() {
        return this.code;
    }

    public int getRootCode() {
        return this.rootCode;
    }
}
