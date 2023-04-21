package org.openmim.msn;

import org.openmim.mn.MessagingNetworkException;
import java.util.*;

public class Errors {

    private static final org.apache.log4j.Logger CAT = org.apache.log4j.Logger.getLogger(Errors.class.getName());

    public static String getVersionString() {
        String rev = "$Revision: 1.4 $";
        rev = rev.substring("$Revision: ".length(), rev.length() - 2);
        String cvsTag = "$Name:  $";
        cvsTag = cvsTag.substring("$Name: ".length(), cvsTag.length() - 2);
        rev += ", cvs tag: '" + cvsTag + "'";
        return rev;
    }

    private static final Hashtable errors = new Hashtable(35);

    public static MessagingNetworkException createException(ErrorInfo ei) {
        return new MessagingNetworkException(ei.errorMessage, ei.mimExceptionLogger, ei.mimExceptionEndUserReasonCode);
    }

    public static String getMessage(String errorCode) {
        ErrorInfo ei = getInfo(errorCode);
        return ei.errorMessage;
    }

    public static ErrorInfo getInfo(String errorCode) {
        ErrorInfo ei = (ErrorInfo) errors.get(errorCode);
        if (ei != null) return ei; else return new ErrorInfo(errorCode, "Unknown MSN server error: " + errorCode, true, true, MessagingNetworkException.CATEGORY_LOGGED_OFF_ON_BEHALF_OF_MESSAGING_SERVER_OR_PROTOCOL_ERROR, MessagingNetworkException.ENDUSER_LOGGED_OFF_DUE_TO_PROTOCOL_ERROR);
    }

    private static void add(String code, String msg, boolean killNS, boolean killSSS, int mimLogger, int mimEndUserCode) {
        errors.put(code, new ErrorInfo(code, msg, killNS, killSSS, mimLogger, mimEndUserCode));
    }

    /** add fatal exception */
    private static void add(String code, String msg) {
        errors.put(code, new ErrorInfo(code, msg, true, true, MessagingNetworkException.CATEGORY_LOGGED_OFF_ON_BEHALF_OF_MESSAGING_SERVER_OR_PROTOCOL_ERROR, MessagingNetworkException.ENDUSER_LOGGED_OFF_DUE_TO_PROTOCOL_ERROR));
    }

    static {
        add("200", "Syntax Error (probably a client bug)");
        add("201", "Invalid Parameter (probably a client bug)");
        add("205", "Invalid User_");
        add("206", "Fully Qualified Domain Name missing", true, true, MessagingNetworkException.CATEGORY_LOGGED_OFF_ON_BEHALF_OF_MESSAGING_SERVER_OR_PROTOCOL_ERROR, MessagingNetworkException.ENDUSER_LOGGED_OFF_DUE_TO_PROTOCOL_ERROR);
        add("207", "Already Login", true, true, MessagingNetworkException.CATEGORY_LOGGED_OFF_ON_BEHALF_OF_MESSAGING_SERVER_OR_PROTOCOL_ERROR, MessagingNetworkException.ENDUSER_LOGGED_OFF_DUE_TO_PROTOCOL_ERROR);
        add("208", "Invalid Username", true, true, MessagingNetworkException.CATEGORY_LOGGED_OFF_ON_BEHALF_OF_MESSAGING_SERVER_OR_PROTOCOL_ERROR, MessagingNetworkException.ENDUSER_LOGGED_OFF_DUE_TO_PROTOCOL_ERROR);
        add("209", "Invalid Friendly Name", true, true, MessagingNetworkException.CATEGORY_LOGGED_OFF_ON_BEHALF_OF_MESSAGING_SERVER_OR_PROTOCOL_ERROR, MessagingNetworkException.ENDUSER_LOGGED_OFF_DUE_TO_PROTOCOL_ERROR);
        add("210", "List Full", true, true, MessagingNetworkException.CATEGORY_LOGGED_OFF_ON_BEHALF_OF_MESSAGING_SERVER_OR_PROTOCOL_ERROR, MessagingNetworkException.ENDUSER_LOGGED_OFF_DUE_TO_PROTOCOL_ERROR);
        add("215", "Already there", true, true, MessagingNetworkException.CATEGORY_LOGGED_OFF_ON_BEHALF_OF_MESSAGING_SERVER_OR_PROTOCOL_ERROR, MessagingNetworkException.ENDUSER_LOGGED_OFF_DUE_TO_PROTOCOL_ERROR);
        add("216", "Not on list", true, true, MessagingNetworkException.CATEGORY_LOGGED_OFF_ON_BEHALF_OF_MESSAGING_SERVER_OR_PROTOCOL_ERROR, MessagingNetworkException.ENDUSER_LOGGED_OFF_DUE_TO_PROTOCOL_ERROR);
        add("217", "Your party is offline", false, true, MessagingNetworkException.CATEGORY_STILL_CONNECTED, MessagingNetworkException.ENDUSER_CANNOT_COMPLETE_REQUEST_RECIPIENT_IS_OFFLINE);
        add("218", "Already in the mode", true, true, MessagingNetworkException.CATEGORY_LOGGED_OFF_ON_BEHALF_OF_MESSAGING_SERVER_OR_PROTOCOL_ERROR, MessagingNetworkException.ENDUSER_LOGGED_OFF_DUE_TO_PROTOCOL_ERROR);
        add("219", "Already in opposite list", true, true, MessagingNetworkException.CATEGORY_LOGGED_OFF_ON_BEHALF_OF_MESSAGING_SERVER_OR_PROTOCOL_ERROR, MessagingNetworkException.ENDUSER_LOGGED_OFF_DUE_TO_PROTOCOL_ERROR);
        add("280", "Switchboard failed", false, true, MessagingNetworkException.CATEGORY_STILL_CONNECTED, MessagingNetworkException.ENDUSER_MESSAGING_SERVER_PROBLEMS_NOT_LOGGED_OFF);
        add("281", "Notify Transfer failed");
        add("300", "Required fields missing");
        add("302", "Not logged in");
        add("500", "Internal server error", true, true, MessagingNetworkException.CATEGORY_LOGGED_OFF_ON_BEHALF_OF_MESSAGING_SERVER_OR_PROTOCOL_ERROR, MessagingNetworkException.ENDUSER_LOGGED_OFF_MESSAGING_SERVER_PROBLEMS);
        add("501", "Database server error", true, true, MessagingNetworkException.CATEGORY_LOGGED_OFF_ON_BEHALF_OF_MESSAGING_SERVER_OR_PROTOCOL_ERROR, MessagingNetworkException.ENDUSER_LOGGED_OFF_MESSAGING_SERVER_PROBLEMS);
        add("510", "File operation error", true, true, MessagingNetworkException.CATEGORY_LOGGED_OFF_ON_BEHALF_OF_MESSAGING_SERVER_OR_PROTOCOL_ERROR, MessagingNetworkException.ENDUSER_LOGGED_OFF_MESSAGING_SERVER_PROBLEMS);
        add("520", "Memory allocation error", true, true, MessagingNetworkException.CATEGORY_LOGGED_OFF_ON_BEHALF_OF_MESSAGING_SERVER_OR_PROTOCOL_ERROR, MessagingNetworkException.ENDUSER_LOGGED_OFF_MESSAGING_SERVER_PROBLEMS);
        add("600", "Server busy", true, true, MessagingNetworkException.CATEGORY_LOGGED_OFF_ON_BEHALF_OF_MESSAGING_SERVER_OR_PROTOCOL_ERROR, MessagingNetworkException.ENDUSER_LOGGED_OFF_MESSAGING_SERVER_PROBLEMS);
        add("601", "Server unavailable", true, true, MessagingNetworkException.CATEGORY_LOGGED_OFF_ON_BEHALF_OF_MESSAGING_SERVER_OR_PROTOCOL_ERROR, MessagingNetworkException.ENDUSER_LOGGED_OFF_MESSAGING_SERVER_PROBLEMS);
        add("602", "Peer Notification server down", true, true, MessagingNetworkException.CATEGORY_LOGGED_OFF_ON_BEHALF_OF_MESSAGING_SERVER_OR_PROTOCOL_ERROR, MessagingNetworkException.ENDUSER_LOGGED_OFF_MESSAGING_SERVER_PROBLEMS);
        add("603", "Database connect error", true, true, MessagingNetworkException.CATEGORY_LOGGED_OFF_ON_BEHALF_OF_MESSAGING_SERVER_OR_PROTOCOL_ERROR, MessagingNetworkException.ENDUSER_LOGGED_OFF_MESSAGING_SERVER_PROBLEMS);
        add("604", "Server is going down (abandon ship)", true, true, MessagingNetworkException.CATEGORY_LOGGED_OFF_ON_BEHALF_OF_MESSAGING_SERVER_OR_PROTOCOL_ERROR, MessagingNetworkException.ENDUSER_LOGGED_OFF_MESSAGING_SERVER_PROBLEMS);
        add("707", "Error creating connection", true, true, MessagingNetworkException.CATEGORY_LOGGED_OFF_ON_BEHALF_OF_MESSAGING_SERVER_OR_PROTOCOL_ERROR, MessagingNetworkException.ENDUSER_LOGGED_OFF_MESSAGING_SERVER_PROBLEMS);
        add("711", "Unable to write", true, true, MessagingNetworkException.CATEGORY_LOGGED_OFF_ON_BEHALF_OF_MESSAGING_SERVER_OR_PROTOCOL_ERROR, MessagingNetworkException.ENDUSER_LOGGED_OFF_MESSAGING_SERVER_PROBLEMS);
        add("712", "Session overload", true, true, MessagingNetworkException.CATEGORY_LOGGED_OFF_ON_BEHALF_OF_MESSAGING_SERVER_OR_PROTOCOL_ERROR, MessagingNetworkException.ENDUSER_LOGGED_OFF_MESSAGING_SERVER_PROBLEMS);
        add("713", "User_ is too active", true, true, MessagingNetworkException.CATEGORY_LOGGED_OFF_ON_BEHALF_OF_MESSAGING_SERVER_OR_PROTOCOL_ERROR, MessagingNetworkException.ENDUSER_LOGGED_OFF_USER_IS_TOO_ACTIVE);
        add("714", "Too many sessions", false, true, MessagingNetworkException.CATEGORY_STILL_CONNECTED, MessagingNetworkException.ENDUSER_MESSAGING_SERVER_PROBLEMS_NOT_LOGGED_OFF);
        add("715", "Not expected");
        add("717", "Bad friend file");
        add("911", "Authentication failed", true, true, MessagingNetworkException.CATEGORY_NOT_CATEGORIZED, MessagingNetworkException.ENDUSER_CANNOT_LOGIN_INVALID_PASSWORD_OR_LOGIN_ID);
        add("913", "Not allowed when offline");
        add("920", "Not accepting new users");
    }
}
