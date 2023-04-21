package net.hypotenubel.irc.msgutils;

import net.hypotenubel.irc.*;

/**
 * Wrapper for easy handling of RPL_LUSEROP messages.
 * <p>
 * <b>Syntax:</b> {@code 252 &lt;integer&gt; "operator(s) online"}
 * <p>
 * In processing an LUSERS message, the server sends a set of replies from
 * RPL_LUSERCLIENT, RPL_LUSEROP, RPL_USERUNKNOWN, RPL_LUSERCHANNELS and
 * RPL_LUSERME. When replying, a server <b>must</b> send back RPL_LUSERCLIENT
 * and RPL_LUSERME. The other replies are only sent back if a non-zero count is
 * found for them.
 *
 * @author Christoph Daniel Schulze
 * @version $Id: LuseropReply.java 91 2006-07-21 13:41:43Z captainnuss $
 */
public class LuseropReply {

    /**
     * Instantiation is not allowed.
     */
    private LuseropReply() {
    }

    /**
     * Creates a new RPL_LUSEROP message.
     *
     * @param msgNick    String object containing the nick of the guy this
     *                   message comes from. Should usually be "".
     * @param msgUser    String object containing the user name of the guy this
     *                   message comes from. Should usually be "".
     * @param msgHost    String object containing the host name of the guy this
     *                   message comes from. Should usually be "".
     * @param operators  {@code int} containing the number of operators.
     *                   Must be greater then or equal to 0.
     */
    public static IRCMessage createMessage(String msgNick, String msgUser, String msgHost, int operators) {
        if (operators < 0) operators = 0;
        String[] args = new String[] { String.valueOf(operators), "operator(s) online" };
        return new IRCMessage(msgNick, msgUser, msgHost, IRCMessageTypes.RPL_LUSEROP, args);
    }

    /**
     * Returns the number of operators.
     *
     * @return {@code int} specifying the number of clients.
     */
    public static int getClients(IRCMessage msg) {
        try {
            return Integer.parseInt(msg.getArgs().get(msg.getArgs().size() - 2));
        } catch (NumberFormatException f) {
            return 0;
        }
    }
}
