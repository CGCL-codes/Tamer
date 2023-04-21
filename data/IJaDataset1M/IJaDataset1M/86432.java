package net.interfax;

public class InterFaxException extends Exception {

    private static final long serialVersionUID = 1L;

    private final int code;

    public InterFaxException(int code) {
        super(toString(code));
        this.code = code;
    }

    public InterFaxException(int code, Throwable cause) {
        super(toString(code), cause);
        this.code = code;
    }

    public InterFaxException() {
        this(0);
    }

    public long getCode() {
        return code;
    }

    /** @see http://www.interfax.net/en/dev/webservice/reference/web-service-return-codes */
    private static String toString(int code) {
        switch(code) {
            case -112:
                return "No valid recipients added or missing fax number";
            case -114:
                return "Postpone date too late (can be no later than 30 days from now";
            case -123:
                return "No valid documents attached";
            case -150:
                return "Internal System Error";
            case -310:
                return "User is not authorized to operate actions on RequestedUserID";
            case -311:
                return "Transaction does not belong to requested user";
            case -312:
                return "Email(s) already resent";
            case -1002:
                return "Number of types does not match number of document sizes string";
            case -1003:
                return "Authentication error";
            case -1004:
                return "Missing file type";
            case -1005:
                return "Transaction does not exist";
            case -1007:
                return "Size value is not numeric or not greater than 0";
            case -1008:
                return "Total sizes does not match filesdata length";
            case -1009:
                return "Image not available (may happen if the 'delete fax after completion' feature is active)";
            case -1030:
                return "Invalid Verb or VerbData";
            case -1062:
                return "Wrong session ID";
            case -3001:
                return "Invalid MessageID";
            case -3002:
                return "\"From\" parameter is larger than image size";
            case -3003:
                return "Image doesn't exist";
            case -3004:
                return "TIFF file is empty";
            case -3005:
                return "Requested chunk size is invalid (less than 1 or greater than the maximum value defined under \"System Limitations\")";
            case -3006:
                return "Max item is smaller than 1";
            case -3007:
                return "No permission for this action (In inbound method GetList, LType is set to AccountAllMessages or AccountNewMessages, when the username is not a Primary user)";
            default:
                return "Unknown error";
        }
    }
}
