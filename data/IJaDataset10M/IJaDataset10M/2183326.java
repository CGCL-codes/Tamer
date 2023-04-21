package net.hypotenubel.irc.msgutils;

import net.hypotenubel.irc.*;

/**
 * Wrapper for easy handling of ERR_WILDTOPLEVEL messages.
 * <p>
 * <b>Syntax:</b> {@code 414 &lt;mask&gt; "Wildcard in toplevel domain"}
 * <p>
 * 412 - 415 are returned by PRIVMSG to indicate that the message wasn't
 * delivered for some reason. ERR_NOTOPLEVEL and ERR_WILDTOPLEVEL are errors
 * that are returned when an invalid use of "PRIVMSG $&lt;server&gt;" or
 * "PRIVMSG #&lt;host&gt;" is attempted.
 *
 * @author Christoph Daniel Schulze
 * @version $Id: WildtoplevelError.java 96 2006-09-13 22:20:24Z captainnuss $
 */
public class WildtoplevelError {

    /**
     * Instantiation is not allowed.
     */
    private WildtoplevelError() {
    }

    /**
     * Creates a new ERR_WILDTOPLEVEL message.
     *
     * @param msgNick     String object containing the nick of the guy this
     *                    message comes from. Should usually be "".
     * @param msgUser     String object containing the user name of the guy this
     *                    message comes from. Should usually be "".
     * @param msgHost     String object containing the host name of the guy this
     *                    message comes from. Should usually be "".
     * @param mask        String containing the mask.
     */
    public static IRCMessage createMessage(String msgNick, String msgUser, String msgHost, String mask) {
        String[] args = new String[] { mask, "Wildcard in toplevel domain" };
        return new IRCMessage(msgNick, msgUser, msgHost, IRCMessageTypes.ERR_WILDTOPLEVEL, args);
    }

    /**
     * Returns the mask.
     *
     * @return String containing the mask.
     */
    public static String getMask(IRCMessage msg) {
        return msg.getArgs().get(0);
    }
}
