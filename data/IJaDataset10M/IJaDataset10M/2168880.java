package de.teamwork.irc.msgutils;

import java.text.MessageFormat;
import java.text.ParseException;
import de.teamwork.irc.*;

/**
 * Wrapper for easy handling of ERR_NOSUCHCHANNEL messages.
 * <p>
 * <b>Syntax:</b> <code>403 &lt;channel name&gt; "No such channel"</code>
 * <p>
 * Used to indicate the given channel name is invalid.
 *
 * @author Christoph Daniel Schulze
 * @version $Id: NosuchchannelError.java 3 2003-01-07 14:16:38Z captainnuss $
 */
public class NosuchchannelError {

    /**
     * Instantiation is not allowed.
     */
    private NosuchchannelError() {
    }

    /**
     * Creates a new ERR_NOSUCHCHANNEL message.
     *
     * @param msgNick     String object containing the nick of the guy this
     *                    message comes from. Should usually be "".
     * @param msgUser     String object containing the user name of the guy this
     *                    message comes from. Should usually be "".
     * @param msgHost     String object containing the host name of the guy this
     *                    message comes from. Should usually be "".
     * @param channelname String containing the channel name.
     */
    public static IRCMessage createMessage(String msgNick, String msgUser, String msgHost, String channelname) {
        String[] args = new String[] { channelname, "No such channel" };
        return new IRCMessage(msgNick, msgUser, msgHost, IRCMessageTypes.ERR_NOSUCHCHANNEL, args);
    }

    /**
     * Returns the channel name.
     *
     * @return String containing the channel name.
     */
    public static String getChannelname(IRCMessage msg) {
        return (String) msg.getArgs().elementAt(0);
    }
}
