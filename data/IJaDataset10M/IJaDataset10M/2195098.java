package ru.caffeineim.protocols.icq.setting.enumerations;

/**
 * <p>Created by 09.01.2010
 *   @author Samolisov Pavel
 */
public class GlobalErrorsEnum {

    public static final int INVALID_SNAC = 1;

    public static final int RATE_TO_HOST = 2;

    public static final int RATE_TO_CLIENT = 3;

    public static final int NOT_LOGGED_ON = 4;

    public static final int SERVICE_UNAVAILABLE = 5;

    public static final int SERVICE_NOT_DEFINED = 6;

    public static final int OBSOLETE_SNAC = 7;

    public static final int NOT_SUPPORTED_BY_HOST = 8;

    public static final int NOT_SUPPORTED_BY_CLIENT = 9;

    public static final int REFUSED_BY_CLIENT = 10;

    public static final int RESPONSES_LOST = 12;

    public static final int REQUEST_DENIED = 13;

    public static final int BUSTED_SNAC_PAYLOAD = 14;

    public static final int INSUFFICIENT_RIGHTS = 15;

    public static final int IN_LOCAL_PERMIT_DENY = 16;

    public static final int TOO_EVIL_SENDER = 17;

    public static final int TOO_EVIL_RECEIVER = 18;

    public static final int USER_TEMP_UNAVAIL = 19;

    public static final int NO_MATCH = 20;

    public static final int LIST_OVERFLOW = 21;

    public static final int REQUEST_AMBIGOUS = 22;

    public static final int TIMEOUT = 26;

    public static final int GENERAL_FAILURE = 28;

    public static final int RESTRICTED_BY_PC = 31;

    public static final int REMOTE_RESTRICTED_BY_PC = 32;

    private static EnumerationsMap allerrors = new EnumerationsMap();

    static {
        allerrors.put(INVALID_SNAC, "Not a known SNAC");
        allerrors.put(RATE_TO_HOST, "Exceed the rate limit to server");
        allerrors.put(RATE_TO_CLIENT, "Exceed the rate limit to the remote user");
        allerrors.put(NOT_LOGGED_ON, "Remote user is not logged in");
        allerrors.put(SERVICE_UNAVAILABLE, "Normally available but something is wrong right now");
        allerrors.put(SERVICE_NOT_DEFINED, "Requested a service that does not exist");
        allerrors.put(OBSOLETE_SNAC, "This SNAC is known no longer supported");
        allerrors.put(NOT_SUPPORTED_BY_HOST, "Unknown SNAC");
        allerrors.put(NOT_SUPPORTED_BY_CLIENT, "Remote user is on but does not support the request");
        allerrors.put(REFUSED_BY_CLIENT, "Message is bigger then remote client wants");
        allerrors.put(RESPONSES_LOST, "Something really messed up");
        allerrors.put(REQUEST_DENIED, "Server said user or client is not allowed to do this");
        allerrors.put(BUSTED_SNAC_PAYLOAD, "SNAC is too small or is not in the right format");
        allerrors.put(INSUFFICIENT_RIGHTS, "User or client does not have the correct rights to make the request");
        allerrors.put(IN_LOCAL_PERMIT_DENY, "User is trying to interact with someone blocked by their own settings");
        allerrors.put(TOO_EVIL_SENDER, "Sender is too evil");
        allerrors.put(TOO_EVIL_RECEIVER, "Receiver is too evil");
        allerrors.put(USER_TEMP_UNAVAIL, "User is migrating or the server is down");
        allerrors.put(NO_MATCH, "Item was not found");
        allerrors.put(LIST_OVERFLOW, "Too many items were specified in a list");
        allerrors.put(REQUEST_AMBIGOUS, "Host could not figure out which item to operate on");
        allerrors.put(TIMEOUT, "Some kind of timeout");
        allerrors.put(GENERAL_FAILURE, "General failure");
        allerrors.put(RESTRICTED_BY_PC, "Restricted by parental controls");
        allerrors.put(REMOTE_RESTRICTED_BY_PC, "Remote user is restricted by parental controls");
    }

    private int code;

    public GlobalErrorsEnum(int code) {
        this.code = code;
    }

    public int getCode() {
        return code;
    }

    public String toString() {
        if (allerrors.containsKey(getCode())) return (String) allerrors.get(getCode()); else return "";
    }
}
