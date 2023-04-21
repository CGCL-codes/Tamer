package de.teamwork.irc.msgutils;

import java.text.MessageFormat;
import java.text.ParseException;
import de.teamwork.irc.*;

/**
 * Wrapper for easy handling of ERR_NOTREGISTERED messages.
 * <p>
 * <b>Syntax:</b> <code>451 "You have not registered"</code>
 * <p>
 * Returned by the server to indicate that the client <b>must</p> be registered
 * before the server will allow it to be parsed in detail.
 *
 * @author Christoph Daniel Schulze
 * @version $Id: NotregisteredError.java 3 2003-01-07 14:16:38Z captainnuss $
 */
public class NotregisteredError {

    /**
     * Instantiation is not allowed.
     */
    private NotregisteredError() {
    }

    /**
     * Creates a new ERR_NOTREGISTERED message.
     *
     * @param msgNick    String object containing the nick of the guy this
     *                   message comes from. Should usually be "".
     * @param msgUser    String object containing the user name of the guy this
     *                   message comes from. Should usually be "".
     * @param msgHost    String object containing the host name of the guy this
     *                   message comes from. Should usually be "".
     */
    public static IRCMessage createMessage(String msgNick, String msgUser, String msgHost) {
        String[] args = new String[] { "You have not registered" };
        return new IRCMessage(msgNick, msgUser, msgHost, IRCMessageTypes.ERR_NOTREGISTERED, args);
    }
}
