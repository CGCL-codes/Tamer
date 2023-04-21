package de.teamwork.irc.msgutils;

import java.text.MessageFormat;
import java.text.ParseException;
import de.teamwork.irc.*;

/**
 * Wrapper for easy handling of RPL_TIME messages.
 * <p>
 * <b>Syntax:</b> <code>391 &lt;server&gt; &lt;string showing server�s local time&gt;</code>
 * <p>
 * When replying to the TIME message, a server <b>must</b> send the reply using
 * the RPL_TIME format above. The string showing the time need only contain the
 * correct day and time there. There is no further requirement for the time
 * string.
 *
 * @author Christoph Daniel Schulze
 * @version $Id: TimeReply.java 3 2003-01-07 14:16:38Z captainnuss $
 */
public class TimeReply {

    /**
     * Instantiation is not allowed.
     */
    private TimeReply() {
    }

    /**
     * Creates a new RPL_TIME message.
     *
     * @param msgNick    String object containing the nick of the guy this
     *                   message comes from. Should usually be "".
     * @param msgUser    String object containing the user name of the guy this
     *                   message comes from. Should usually be "".
     * @param msgHost    String object containing the host name of the guy this
     *                   message comes from. Should usually be "".
     * @param server     String containing the server name.
     * @param time       String containing the time.
     */
    public static IRCMessage createMessage(String msgNick, String msgUser, String msgHost, String server, String time) {
        String[] args = new String[] { server, time };
        return new IRCMessage(msgNick, msgUser, msgHost, IRCMessageTypes.RPL_TIME, args);
    }

    /**
     * Returns the server name.
     *
     * @return String containing the server name.
     */
    public static String getServer(IRCMessage msg) {
        return (String) msg.getArgs().elementAt(msg.getArgs().size() - 2);
    }

    /**
     * Returns the time.
     *
     * @return String containing the time.
     */
    public static String getTime(IRCMessage msg) {
        return (String) msg.getArgs().elementAt(msg.getArgs().size() - 1);
    }
}
