package de.teamwork.irc.msgutils;

import java.text.MessageFormat;
import java.text.ParseException;
import de.teamwork.irc.*;

/**
 * Wrapper for easy handling of ERR_NOORIGIN messages.
 * <p>
 * <b>Syntax:</b> <code>409 "No origin specified"</code>
 * <p>
 * PING or PONG message missing the originator parameter.
 *
 * @author Christoph Daniel Schulze
 * @version $Id: NooriginError.java 3 2003-01-07 14:16:38Z captainnuss $
 */
public class NooriginError {

    /**
     * Instantiation is not allowed.
     */
    private NooriginError() {
    }

    /**
     * Creates a new ERR_NOORIGIN message.
     *
     * @param msgNick    String object containing the nick of the guy this
     *                   message comes from. Should usually be "".
     * @param msgUser    String object containing the user name of the guy this
     *                   message comes from. Should usually be "".
     * @param msgHost    String object containing the host name of the guy this
     *                   message comes from. Should usually be "".
     */
    public static IRCMessage createMessage(String msgNick, String msgUser, String msgHost) {
        String[] args = new String[] { "No origin specified" };
        return new IRCMessage(msgNick, msgUser, msgHost, IRCMessageTypes.ERR_NOORIGIN, args);
    }
}
