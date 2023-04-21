package de.teamwork.irc.msgutils;

import java.text.MessageFormat;
import java.text.ParseException;
import de.teamwork.irc.*;

/**
 * Wrapper for easy handling of RPL_NOTOPIC messages.
 * <p>
 * <b>Syntax:</b> <code>331 &lt;channel&gt; "No topic is set"</code>
 * <p>
 * When sending a TOPIC message to determine the channel topic, one of two
 * replies is sent. If the topic is set, RPL_TOPIC is sent back else
 * RPL_NOTOPIC.
 *
 * @author Christoph Daniel Schulze
 * @version $Id: NotopicReply.java 3 2003-01-07 14:16:38Z captainnuss $
 */
public class NotopicReply {

    /**
     * Instantiation is not allowed.
     */
    private NotopicReply() {
    }

    /**
     * Creates a new RPL_NOTOPIC message.
     *
     * @param msgNick    String object containing the nick of the guy this
     *                   message comes from. Should usually be "".
     * @param msgUser    String object containing the user name of the guy this
     *                   message comes from. Should usually be "".
     * @param msgHost    String object containing the host name of the guy this
     *                   message comes from. Should usually be "".
     * @param channel    String containing the channel name.
     */
    public static IRCMessage createMessage(String msgNick, String msgUser, String msgHost, String channel) {
        String[] args = new String[] { channel, "No topic is set" };
        return new IRCMessage(msgNick, msgUser, msgHost, IRCMessageTypes.RPL_NOTOPIC, args);
    }

    /**
     * Returns the channel name.
     *
     * @return String containing the channel name.
     */
    public static String getChannel(IRCMessage msg) {
        return (String) msg.getArgs().elementAt(msg.getArgs().size() - 2);
    }
}
