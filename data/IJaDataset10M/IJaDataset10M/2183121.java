package net.hypotenubel.irc.msgutils;

import net.hypotenubel.irc.*;

/**
 * Wrapper for easy handling of MSG_LUSERS messages.
 * <p>
 * <b>Syntax:</b> {@code LUSERS [ &lt;mask&gt; [ &lt;target&gt; ] ]}
 * <p>
 * The LUSERS command is used to get statistics about the size of the IRC
 * network. If no parameter is given, the reply will be about the whole net. If
 * a {@code mask} is specified, then the reply will only concern the part
 * of the network formed by the servers matching the mask. Finally, if the
 * {@code target} parameter is specified, the request is forwarded to that
 * server which will generate the reply.
 * <p>
 * Wildcards are allowed in the {@code target} parameter.
 *
 * @author Christoph Daniel Schulze
 * @version $Id: LusersMessage.java 91 2006-07-21 13:41:43Z captainnuss $
 */
public class LusersMessage {

    /**
     * Instantiation is not allowed.
     */
    private LusersMessage() {
    }

    /**
     * Creates a new MSG_LUSERS message.
     *
     * @param msgNick String object containing the nick of the guy this message
     *                comes from. Should usually be "".
     * @param msgUser String object containing the user name of the guy this
     *                message comes from. Should usually be "".
     * @param msgHost String object containing the host name of the guy this
     *                message comes from. Should usually be "".
     */
    public static IRCMessage createMessage(String msgNick, String msgUser, String msgHost) {
        return new IRCMessage(msgNick, msgUser, msgHost, IRCMessageTypes.MSG_LUSERS, null);
    }

    /**
     * Creates a new MSG_MOTD message.
     *
     * @param msgNick String object containing the nick of the guy this message
     *                comes from. Should usually be "".
     * @param msgUser String object containing the user name of the guy this
     *                message comes from. Should usually be "".
     * @param msgHost String object containing the host name of the guy this
     *                message comes from. Should usually be "".
     * @param mask    String object containing a server mask.
     */
    public static IRCMessage createMessage(String msgNick, String msgUser, String msgHost, String mask) {
        String[] args = new String[] { mask };
        return new IRCMessage(msgNick, msgUser, msgHost, IRCMessageTypes.MSG_MOTD, args);
    }

    /**
     * Creates a new MSG_MOTD message.
     *
     * @param msgNick String object containing the nick of the guy this message
     *                comes from. Should usually be "".
     * @param msgUser String object containing the user name of the guy this
     *                message comes from. Should usually be "".
     * @param msgHost String object containing the host name of the guy this
     *                message comes from. Should usually be "".
     * @param mask    String object containing a server mask.
     * @param target  String object containing the target server name.
     */
    public static IRCMessage createMessage(String msgNick, String msgUser, String msgHost, String mask, String target) {
        String[] args = new String[] { mask, target };
        return new IRCMessage(msgNick, msgUser, msgHost, IRCMessageTypes.MSG_MOTD, args);
    }

    /**
     * Returns the server mask, if any.
     *
     * @return String containing the server mask or "" if none is given.
     */
    public static String getMask(IRCMessage msg) {
        try {
            return msg.getArgs().get(0);
        } catch (IndexOutOfBoundsException e) {
            return "";
        }
    }

    /**
     * Returns the target server name, if any.
     *
     * @return String containing the target server name or "" if none is given.
     */
    public static String getTarget(IRCMessage msg) {
        try {
            return msg.getArgs().get(1);
        } catch (IndexOutOfBoundsException e) {
            return "";
        }
    }
}
