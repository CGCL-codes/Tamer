package net.hypotenubel.irc.msgutils;

import net.hypotenubel.irc.*;

/**
 * Wrapper for easy handling of RPL_LISTEND messages.
 * <p>
 * <b>Syntax:</b> {@code 323 "End of LIST"}
 * <p>
 * Replies RPL_LIST, RPL_LISTEND mark the actual replies with data and end of
 * the server's response to a LIST command. If there are no channels available
 * to return, only the end reply <b>must</b> be sent.
 *
 * @author Christoph Daniel Schulze
 * @version $Id: ListendReply.java 96 2006-09-13 22:20:24Z captainnuss $
 */
public class ListendReply {

    /**
     * Instantiation is not allowed.
     */
    private ListendReply() {
    }

    /**
     * Creates a new RPL_LISTEND message.
     *
     * @param msgNick    String object containing the nick of the guy this
     *                   message comes from. Should usually be "".
     * @param msgUser    String object containing the user name of the guy this
     *                   message comes from. Should usually be "".
     * @param msgHost    String object containing the host name of the guy this
     *                   message comes from. Should usually be "".
     */
    public static IRCMessage createMessage(String msgNick, String msgUser, String msgHost) {
        String[] args = new String[] { "End of LIST" };
        return new IRCMessage(msgNick, msgUser, msgHost, IRCMessageTypes.RPL_LISTEND, args);
    }
}
