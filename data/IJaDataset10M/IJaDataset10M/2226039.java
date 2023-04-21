package de.teamwork.irc.msgutils;

import java.text.MessageFormat;
import java.text.ParseException;
import de.teamwork.irc.*;

/**
 * Wrapper for easy handling of ERR_YOUWILLBEBANNED messages.
 * <p>
 * <b>Syntax:</b> <code>466</code>
 * <p>
 * Sent by a server to a user to inform that access to the server will soon be
 * denied.
 *
 * @author Christoph Daniel Schulze
 * @version $Id: YouwillbebannedError.java 3 2003-01-07 14:16:38Z captainnuss $
 */
public class YouwillbebannedError {

    /**
     * Instantiation is not allowed.
     */
    private YouwillbebannedError() {
    }

    /**
     * Creates a new ERR_YOUWILLBEBANNED message.
     *
     * @param msgNick    String object containing the nick of the guy this
     *                   message comes from. Should usually be "".
     * @param msgUser    String object containing the user name of the guy this
     *                   message comes from. Should usually be "".
     * @param msgHost    String object containing the host name of the guy this
     *                   message comes from. Should usually be "".
     */
    public static IRCMessage createMessage(String msgNick, String msgUser, String msgHost) {
        return new IRCMessage(msgNick, msgUser, msgHost, IRCMessageTypes.ERR_YOUWILLBEBANNED, null);
    }
}
