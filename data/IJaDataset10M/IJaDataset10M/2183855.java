package org.sepp.api.datatypes;

public class ErrorType {

    private static final long serialVersionUID = -5906406240035077144L;

    public static final int UNKNOWN_ERROR = 0;

    public static final int NOT_JOINED_SEPP = 1;

    public static final int CONNECTION_NOT_AVAILABLE = 2;

    public static final int MESSAGE_TIME_OUT = 3;

    public static final int AUTHENTICATION_NOT_AVAILABLE = 4;

    public static final int APPLYING_SECURITY_FAILED = 5;

    private int maxType = 6;

    private int type = UNKNOWN_ERROR;

    private String description;

    public ErrorType(int type, String description) {
        if (type >= 0 && type < maxType) this.type = type;
        this.description = description;
    }

    public int getType() {
        return type;
    }

    public String getTypeDescription() {
        switch(type) {
            case 0:
                return "Unknown error";
            case 1:
                return "Not joined";
            case 2:
                return "Connection not available";
            case 3:
                return "Message time out";
            case 4:
                return "Authentication not available";
            default:
                return "Unknown error";
        }
    }

    public String getDescription() {
        return description;
    }
}
