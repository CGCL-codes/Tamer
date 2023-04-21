package net.sf.jml.exception;

import java.util.HashMap;
import java.util.Map;
import net.sf.jml.protocol.MsnIncomingMessage;
import net.sf.jml.protocol.MsnOutgoingMessage;
import net.sf.jml.util.JmlConstants;

/**
 * The exception that MSN protocol returned.
 * 
 * @author Roger Chen
 */
public class MsnProtocolException extends JmlException {

    private static Map<Integer, String> errorMap = new HashMap<Integer, String>();

    static {
        errorMap.put(200, "Invalid syntax");
        errorMap.put(201, "Invalid parameter");
        errorMap.put(205, "Invalid user");
        errorMap.put(206, "Domain name missing");
        errorMap.put(207, "Already logged in");
        errorMap.put(208, "Invalid user-name");
        errorMap.put(209, "Nickname change illegal");
        errorMap.put(210, "User list full");
        errorMap.put(213, "User already or not in group");
        errorMap.put(215, "User already on list");
        errorMap.put(216, "User not on list");
        errorMap.put(217, "User not on-line");
        errorMap.put(218, "Already in mode");
        errorMap.put(219, "User is in the opposite list");
        errorMap.put(223, "Too many groups");
        errorMap.put(224, "Invalid group");
        errorMap.put(225, "User not in group");
        errorMap.put(229, "Group name too long");
        errorMap.put(230, "Cannot remove group zero");
        errorMap.put(231, "Invalid group");
        errorMap.put(280, "Switchboard failed");
        errorMap.put(281, "Transfer to switchboard failed");
        errorMap.put(300, "Required field missing");
        errorMap.put(302, "Not logged in");
        errorMap.put(500, "Internal server error");
        errorMap.put(501, "Database server error");
        errorMap.put(502, "Command disabled");
        errorMap.put(510, "File operation failed");
        errorMap.put(520, "Memory allocation failed");
        errorMap.put(540, "Challenge response failed");
        errorMap.put(600, "Server is busy");
        errorMap.put(601, "Server is unavailable");
        errorMap.put(602, "Peer nameserver is down");
        errorMap.put(603, "Database connection failed");
        errorMap.put(604, "Server is going down");
        errorMap.put(605, "Server unavailable");
        errorMap.put(707, "Could not create connection");
        errorMap.put(710, "Bad CVR parameters sent");
        errorMap.put(711, "Write is blocking");
        errorMap.put(712, "Session is overloaded");
        errorMap.put(713, "Calling too rapidly");
        errorMap.put(714, "Too many sessions");
        errorMap.put(715, "Not expected");
        errorMap.put(717, "Bad friend file");
        errorMap.put(731, "Not expected");
        errorMap.put(800, "Changing too rapidly");
        errorMap.put(910, "Server too busy");
        errorMap.put(911, "Authentication failed");
        errorMap.put(912, "Server too busy");
        errorMap.put(913, "Not allowed when hiding");
        errorMap.put(914, "Server unavailable");
        errorMap.put(915, "Server unavailable");
        errorMap.put(916, "Server unavailable");
        errorMap.put(917, "Authentication failed");
        errorMap.put(918, "Server too busy");
        errorMap.put(919, "Server too busy");
        errorMap.put(920, "Not accepting new user");
        errorMap.put(921, "Server too busy");
        errorMap.put(922, "Server too busy");
        errorMap.put(923, "Kids Passport without parental consent");
        errorMap.put(924, "Passport account not yet verified");
        errorMap.put(928, "Bad ticket");
    }

    private final int errorCode;

    private final MsnIncomingMessage incoming;

    private final MsnOutgoingMessage outgoing;

    public MsnProtocolException(int errorCode, MsnIncomingMessage incoming, MsnOutgoingMessage outgoing) {
        this.errorCode = errorCode;
        this.incoming = incoming;
        this.outgoing = outgoing;
    }

    public int getErrorCode() {
        return errorCode;
    }

    public MsnIncomingMessage getIncomingMessage() {
        return incoming;
    }

    public MsnOutgoingMessage getOutgoingMessage() {
        return outgoing;
    }

    @Override
    public String toString() {
        String desc = errorMap.get(errorCode);
        if (desc == null) {
            desc = "Unknown error";
        }
        return "ErrorCode " + errorCode + " , " + desc + JmlConstants.LINE_SEPARATOR + "outgoing : " + outgoing + JmlConstants.LINE_SEPARATOR + "incoming : " + incoming;
    }
}
