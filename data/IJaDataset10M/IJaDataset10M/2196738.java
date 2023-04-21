package net.hypotenubel.irc.msgutils;

import net.hypotenubel.irc.*;

/**
 * Wrapper for easy handling of RPL_TRACECLASS messages.
 * <p>
 * <b>Syntax:</b> {@code 209 "Class" &lt;class&gt; &lt;count&gt;}
 * <p>
 * The RPL_TRACE* are all returned by the server in response to the TRACE
 * message. How many are returned is dependent on the TRACE message and whether
 * it was sent by an operator or not. There is no predefined order for which
 * occurs first. Replies RPL_TRACEUNKNOWN, RPL_TRACECLASS and
 * RPL_TRACEHANDSHAKE are all used for connections which have not been fully
 * established and are either unknown, still attempting to connect or in the
 * process of completing the <i>server handshake</i>. RPL_TRACELINK is sent by
 * any server which handles a TRACE message and has to pass it on to another
 * server. The list of RPL_TRACELINKs sent in response to a TRACE command
 * traversing the IRC network should reflect the actual connectivity of the
 * servers themselves along that path. RPL_TRACENEWTYPE is to be used for any
 * connection which does not fit in the other categories but is being displayed
 * anyway.
 * <p>
 * RPL_TRACEEND is sent to indicate the end of the list.
 *
 * @author Christoph Daniel Schulze
 * @version $Id: TraceclassReply.java 91 2006-07-21 13:41:43Z captainnuss $
 */
public class TraceclassReply {

    /**
     * Instantiation is not allowed.
     */
    private TraceclassReply() {
    }

    /**
     * Creates a new RPL_TRACECLASS message.
     *
     * @param msgNick    String object containing the nick of the guy this
     *                   message comes from. Should usually be "".
     * @param msgUser    String object containing the user name of the guy this
     *                   message comes from. Should usually be "".
     * @param msgHost    String object containing the host name of the guy this
     *                   message comes from. Should usually be "".
     * @param trcclass   String containing the class.
     * @param count      String containing the count.
     */
    public static IRCMessage createMessage(String msgNick, String msgUser, String msgHost, String trcclass, String count) {
        String[] args = new String[] { "Class", trcclass, count };
        return new IRCMessage(msgNick, msgUser, msgHost, IRCMessageTypes.RPL_TRACECLASS, args);
    }

    /**
     * Returns the class.
     *
     * @return String containing the class.
     */
    public static String getTrcclass(IRCMessage msg) {
        return msg.getArgs().get(msg.getArgs().size() - 2);
    }

    /**
     * Returns the count.
     *
     * @return String containing the count.
     */
    public static String getCount(IRCMessage msg) {
        return msg.getArgs().get(msg.getArgs().size() - 1);
    }
}
