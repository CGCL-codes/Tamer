package net.hypotenubel.irc.msgutils;

import java.util.ArrayList;
import java.util.StringTokenizer;
import net.hypotenubel.irc.*;

/**
 * Wrapper for easy handling of MSG_WHOIS messages.
 * <p>
 * <b>Syntax:</b> {@code WHOIS [ &lt;target&gt; ] &lt;mask&gt; *( "," &lt;mask&gt; )}
 * <p>
 * This command is used to query information about particular user. The server
 * will answer this command with several numeric messages indicating different
 * statuses of each user which matches the mask (if you are entitled to see
 * them). If no wildcard is present in the {@code mask}, any information
 * about that nick which you are allowed to see is presented.
 * <p>
 * If the {@code target} parameter is specified, it sends the query to a
 * specific server. It is useful if you want to know how long the user in
 * question has been idle as only local server (i.e., the server the user is
 * directly connected to) knows that information, while everything else is
 * globally known.
 * <p>
 * Wildcards are allowed in the {@code target} parameter.
 *
 * @author Christoph Daniel Schulze
 * @version $Id: WhoisMessage.java 91 2006-07-21 13:41:43Z captainnuss $
 */
public class WhoisMessage {

    /**
     * Instantiation is not allowed.
     */
    private WhoisMessage() {
    }

    /**
     * Creates a new MSG_WHOIS message.
     *
     * @param msgNick String object containing the nick of the guy this message
     *                comes from. Should usually be "".
     * @param msgUser String object containing the user name of the guy this
     *                message comes from. Should usually be "".
     * @param msgHost String object containing the host name of the guy this
     *                message comes from. Should usually be "".
     * @param mask    String object containing the mask.
     */
    public static IRCMessage createMessage(String msgNick, String msgUser, String msgHost, String mask) {
        String[] args = new String[] { mask };
        return new IRCMessage(msgNick, msgUser, msgHost, IRCMessageTypes.MSG_WHOIS, args);
    }

    /**
     * Creates a new MSG_WHOIS message.
     *
     * @param msgNick String object containing the nick of the guy this message
     *                comes from. Should usually be "".
     * @param msgUser String object containing the user name of the guy this
     *                message comes from. Should usually be "".
     * @param msgHost String object containing the host name of the guy this
     *                message comes from. Should usually be "".
     * @param mask    String object containing the mask.
     * @param target  String object containing the server name.
     */
    public static IRCMessage createMessage(String msgNick, String msgUser, String msgHost, String mask, String target) {
        String[] args = new String[] { target, mask };
        return new IRCMessage(msgNick, msgUser, msgHost, IRCMessageTypes.MSG_WHOIS, args);
    }

    /**
     * Creates a new MSG_WHOIS message.
     *
     * @param msgNick String object containing the nick of the guy this message
     *                comes from. Should usually be "".
     * @param msgUser String object containing the user name of the guy this
     *                message comes from. Should usually be "".
     * @param msgHost String object containing the host name of the guy this
     *                message comes from. Should usually be "".
     * @param masks   ArrayList containing a list of masks.
     */
    public static IRCMessage createMessage(String msgNick, String msgUser, String msgHost, ArrayList masks) {
        StringBuffer msk = new StringBuffer((String) masks.get(0));
        for (int i = 1; i < masks.size(); i++) msk.append("," + (String) masks.get(i));
        String[] args = new String[] { msk.toString() };
        return new IRCMessage(msgNick, msgUser, msgHost, IRCMessageTypes.MSG_WHOIS, args);
    }

    /**
     * Creates a new MSG_WHOIS message.
     *
     * @param msgNick String object containing the nick of the guy this message
     *                comes from. Should usually be "".
     * @param msgUser String object containing the user name of the guy this
     *                message comes from. Should usually be "".
     * @param msgHost String object containing the host name of the guy this
     *                message comes from. Should usually be "".
     * @param masks   ArrayList containing a list of masks.
     * @param target  String object containing the server name.
     */
    public static IRCMessage createMessage(String msgNick, String msgUser, String msgHost, ArrayList masks, String target) {
        StringBuffer msk = new StringBuffer((String) masks.get(0));
        for (int i = 1; i < masks.size(); i++) msk.append("," + (String) masks.get(i));
        String[] args = new String[] { target, msk.toString() };
        return new IRCMessage(msgNick, msgUser, msgHost, IRCMessageTypes.MSG_WHOIS, args);
    }

    /**
     * Indicates if a WHOIS message contains a mask list instead of a single
     * mask.
     *
     * @return {@code boolean} indicating whether there's a mask list
     *         ({@code true}) or not ({@code false}).
     */
    public static boolean containsMaskList(IRCMessage msg) {
        return (msg.getArgs().get(msg.getArgs().size() - 1)).indexOf(",") >= 0;
    }

    /**
     * Returns the mask String.
     *
     * @return String containing the mask.
     */
    public static String getMask(IRCMessage msg) {
        return msg.getArgs().get(msg.getArgs().size() - 1);
    }

    /**
     * Returns the mask list.
     *
     * @return ArrayList containing the masks.
     */
    public static ArrayList<String> getMasks(IRCMessage msg) {
        StringTokenizer t = new StringTokenizer(msg.getArgs().get(msg.getArgs().size() - 1), ",", false);
        ArrayList<String> list = new ArrayList<String>(10);
        while (t.hasMoreTokens()) list.add(t.nextToken());
        return list;
    }

    /**
     * Returns the target server name, if any.
     *
     * @return String containing the target server name or "" if none is given.
     */
    public static String getTarget(IRCMessage msg) {
        if (msg.getArgs().size() == 2) return msg.getArgs().get(0); else return "";
    }
}
