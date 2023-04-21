package net.hypotenubel.irc.msgutils;

import net.hypotenubel.irc.*;

/**
 * Wrapper for easy handling of ERR_UNAVAILRESOURCE messages.
 * <p>
 * <b>Syntax:</b> {@code 437 &lt;nick/channel&gt; "Nick/channel is temporarily unavailable"}
 * <p>
 * <ul>
 *   <li>
 *     Returned by a server to a user trying to join a channel currently blocked
 *     by the channel delay mechanism.
 *   </li>
 *   <li>
 *     Returned by a server to a user trying to change nickname when the desired
 *     nickname is blocked by the nick delay mechanism.
 *   </li>
 * </ul>
 *
 * @author Christoph Daniel Schulze
 * @version $Id: UnavailresourceError.java 91 2006-07-21 13:41:43Z captainnuss $
 */
public class UnavailresourceError {

    /**
     * Instantiation is not allowed.
     */
    private UnavailresourceError() {
    }

    /**
     * Creates a new ERR_UNAVAILRESOURCE message.
     *
     * @param msgNick    String object containing the nick of the guy this
     *                   message comes from. Should usually be "".
     * @param msgUser    String object containing the user name of the guy this
     *                   message comes from. Should usually be "".
     * @param msgHost    String object containing the host name of the guy this
     *                   message comes from. Should usually be "".
     * @param nick       String containing the nick or channel name.
     */
    public static IRCMessage createMessage(String msgNick, String msgUser, String msgHost, String nick) {
        String[] args = new String[] { nick, "Nick/channel is temporarily unavailable" };
        return new IRCMessage(msgNick, msgUser, msgHost, IRCMessageTypes.ERR_UNAVAILRESOURCE, args);
    }

    /**
     * Returns the nick or channel name.
     *
     * @return String containing the nick or channel name.
     */
    public static String getNick(IRCMessage msg) {
        return msg.getArgs().get(0);
    }
}
