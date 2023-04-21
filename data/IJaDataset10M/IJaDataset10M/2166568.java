package net.sf.vlm;

import java.util.logging.*;
import net.sf.vlm.pwd.*;

/**
 * The Command class constructs commands which are to be sent to the server. It
 * works together with the
 * {@link net.sf.vlm.NotificationServer NotificationServer}, it also keeps
 * track of the current {@link #TrID transaction ID}. There are many static
 * constants in this class which are to be used by other classes.
 * 
 * @author wensi
 * @since Version 0.1
 * @see net.sf.vlm.NotificationServer
 */
public class Command {

    /**
     * The String constant of the newline, to make the use of newline consistent
     * throughout this project, the NL constant is used instead of actual
     * String, so it will be easier to modify if the protocol changes in the
     * future.
     */
    public static final String NL = "\r\n";

    /**
     * The protocol version used by VLM. It may not be the newest protocol since
     * the official server accepts clients with the protocol version prior to
     * MSNP8.
     */
    private static final String MSNP = "MSNP8";

    /**
     * The version of the client. It does not really matter because this is not
     * an official client.
     */
    private static final String MSNV = "8.1.0178";

    /**
     * The challenge code for encryption of the challenge sent by the server. It
     * can be changed but it must match the corresponding challenge String.
     */
    private static final String CIDC = "Q1P7W2E4J9R8U3S5";

    /**
     * The challenge String for encryption of the challenge sent by the server.
     * This can be changed too, but it must match the corresponding challenge
     * code.
     */
    private static final String CIDS = "msmsgs@msnmsgr.com";

    /**
     * Integer representation for the online status.
     */
    public static final int iNLN = 'N' * 100 + 'L' * 10 + 'N';

    /**
     * Integer representation for the offline status.
     */
    public static final int iOFF = 'O' * 100 + 'F' * 10 + 'F';

    /**
     * Integer representation for the hidden status.
     */
    public static final int iHDN = 'H' * 100 + 'D' * 10 + 'N';

    /**
     * Integer representation for the busy status.
     */
    public static final int iBSY = 'B' * 100 + 'S' * 10 + 'Y';

    /**
     * Integer representation for the idle status.
     */
    public static final int iIDL = 'I' * 100 + 'D' * 10 + 'L';

    /**
     * Integer representation for the be right back status.
     */
    public static final int iBRB = 'B' * 100 + 'R' * 10 + 'B';

    /**
     * Integer representation for the away status.
     */
    public static final int iAWY = 'A' * 100 + 'W' * 10 + 'Y';

    /**
     * Integer representation for the in a call status.
     */
    public static final int iPHN = 'P' * 100 + 'H' * 10 + 'N';

    /**
     * Integer representation for the out to lunch status.
     */
    public static final int iLUN = 'L' * 100 + 'U' * 10 + 'N';

    /**
     * String representation for the online status.
     */
    public static final String NLN = "NLN";

    /**
     * String representation for the offline status.
     */
    public static final String OFF = "OFF";

    /**
     * String representation for the hidden status.
     */
    public static final String HDN = "HDN";

    /**
     * String representation for the busy status.
     */
    public static final String BSY = "BSY";

    /**
     * String representation for the idle status.
     */
    public static final String IDL = "IDL";

    /**
     * String representation for the be right back status.
     */
    public static final String BRB = "BRB";

    /**
     * String representation for the away status.
     */
    public static final String AWY = "AWY";

    /**
     * String representation for the in a call status.
     */
    public static final String PHN = "PHN";

    /**
     * String representation for the out to lunch status.
     */
    public static final String LUN = "LUN";

    /**
     * A list of status used to construct the ComboBox.
     */
    public static final String[] status = { "Online", "Busy", "Be right back", "Away", "In a call", "Out to lunch", "Appear offline" };

    /**
     * The transaction ID used to keep track of the messages.
     */
    public static int TrID = 1;

    /**
     * The logger of this class.
     */
    private static Logger logger = Client.getLogger(Command.class.getName());

    /**
     * Performs conversion between the Integer representation of the status code
     * and the String representation of the status code.
     * 
     * @param code
     *            The Integer representation of the status code.
     * @return The String representation of the status code.
     */
    public static String decodeStatus(int code) {
        switch(code) {
            case iNLN:
                return "Online";
            case iOFF:
                return "Offline";
            case iHDN:
                return "Appear Offline";
            case iBSY:
                return "Busy";
            case iBRB:
                return "Be right back";
            case iIDL:
                return "Idle";
            case iAWY:
                return "Away";
            case iPHN:
                return "In a call";
            case iLUN:
                return "Out to lunch";
            default:
                return "Offline";
        }
    }

    /**
     * Resets the TrID to 1.
     */
    public static void resetTrID() {
        TrID = 1;
    }

    /**
     * Send this command to inform the server which protocol versions the client
     * supports. The server will then respond with a list of the protocols it
     * supports, out of the ones the client supplied. If the server supports
     * none of the given protocols the server will respond with a protocol of 0,
     * and will then disconnect.
     * 
     * @return The String representation of the command.
     */
    public static String VER() {
        String s = "VER " + (TrID++) + " " + MSNP + " CVR0";
        logger.fine(s);
        return s + NL;
    }

    /**
     * This command is used to send information about the client and it's
     * operating system to the server. For the official client, the server then
     * responds with the recommended version of the client to use, the minimum
     * safe version as well as 2 URLs, a link to the download page for the
     * updated version and a link to a page with information about the updated
     * version. If the client is unrecognized by the server, the recommended
     * version is 1.0.0000 and the minimum version is the version number sent by
     * the client in the initial CVR.
     * 
     * @param passport
     * @return The String representation of the command.
     */
    public static String CVR(String passport) {
        String LOC = "0x0409";
        String s = "CVR " + (TrID++) + " " + LOC + " " + System.getProperty("os.name").replace(' ', '_') + " " + System.getProperty("os.version") + " " + System.getProperty("os.arch") + " MSNMSGR " + MSNV + " MSMSGS " + passport;
        logger.fine(s);
        return s + NL;
    }

    /**
     * This command is used to authenticate a client with the notification
     * server. If the client is connected to a dispatch server, the initial USR
     * command sent by the client will be replied with a XFR command by the
     * server, followed by disconnection. The XFR will contain the address of a
     * suitable notification server that the client should connect to, to
     * continue authentication.
     * 
     * @param code
     * @param passport
     * @return The String representation of the command.
     */
    public static String USR(char code, String passport) {
        String s = "USR " + (TrID++) + " TWN " + code + " " + passport;
        logger.fine(s);
        return s + NL;
    }

    /**
     * This command is used to change the user's status. In addition, it sends
     * the principal's client ID number.
     * 
     * @param status
     * @return The String representation of the command.
     */
    public static String CHG(String status) {
        String s = "CHG " + (TrID++) + " " + status + " 268435492";
        logger.fine(s);
        return s + NL;
    }

    /**
     * Command sent when disconnecting from server. The server will then set the
     * user's presence to offline.
     * 
     * @return The String representation of the command.
     */
    public static String OUT() {
        String s = "OUT";
        logger.fine(s);
        return s + NL;
    }

    /**
     * Command to synchronize the client's contact lists. The client should send
     * this immediately after signon as the server wont send certain commands
     * until this is done.
     * 
     * @param cached
     * @return The String representation of the command.
     */
    public static String SYN(int cached) {
        String s = "SYN " + (TrID++) + " " + cached;
        logger.fine(s);
        return s + NL;
    }

    /**
     * Response to CHL command. QRY is a payload command.
     * 
     * @param challenge
     * @return The String representation of the command.
     */
    public static String QRY(String challenge) {
        String s = "QRY " + (TrID++) + " " + CIDS + " 32" + NL + MD5Encryption.encrypt(challenge + CIDC);
        logger.fine(s);
        return s;
    }

    /**
     * A ping to the server.
     * 
     * @return The String representation of the command.
     */
    public static String PNG() {
        String s = "PNG" + NL;
        logger.fine(s);
        return s;
    }

    /**
     * Dispatch/notification server redirection.
     * 
     * @return The String representation of the command.
     */
    public static String XFR() {
        String s = "XFR " + (TrID++) + " SB" + NL;
        logger.fine(s);
        return s;
    }

    /**
     * The REA command changes one's display name. It has two
     * parameters: the account name of the principal you want to modify, and the
     * new URL-encoded nickname that you want to assign to that principal. If
     * successful, the server will respond with another REA with a new version
     * number, the principal's account name, and the principal's new nickname.
     * 
     * @param contact The account name of the principal.
     * @param name The new name.
     * @return The String representation of the command.
     */
    public static String REA(String contact, String name) {
        String s = "REA " + (TrID++) + " " + contact + " " + name + NL;
        logger.fine(s);
        return s;
    }
}
