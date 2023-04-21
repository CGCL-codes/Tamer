package net.hypotenubel.irc.msgutils;

import java.util.Date;
import net.hypotenubel.irc.*;

/**
 * Wrapper for easy handling of RPL_TOPICSET messages.
 * <p>
 * <b>Syntax:</b> {@code 333 &lt;channel&gt; &lt;username&gt; &lt;timecode&gt;}
 * <p>
 * This is an IRC message which isn't officially part of the protocol. However,
 * many servers use it to inform users of the fact who set the channel's topic
 * and when he did so on entering the channel.
 * <p>
 * <b>Note:</b> Real IRC servers put the nick name of the user in front of the
 *              channel.
 *
 * @author Christoph Daniel Schulze
 * @version $Id: TopicsetReply.java 91 2006-07-21 13:41:43Z captainnuss $
 */
public class TopicsetReply {

    /**
     * Instantiation is not allowed.
     */
    private TopicsetReply() {
    }

    /**
     * Creates a new RPL_TOPIC message.
     *
     * @param msgNick    String object containing the nick of the guy this
     *                   message comes from. Should usually be "".
     * @param msgUser    String object containing the user name of the guy this
     *                   message comes from. Should usually be "".
     * @param msgHost    String object containing the host name of the guy this
     *                   message comes from. Should usually be "".
     * @param channel    String containing the channel name.
     * @param username   String containing the user name of the user who set the
     *                   topic.
     * @param timecode   time in seconds since the epoch when the topic was set.
     */
    public static IRCMessage createMessage(String msgNick, String msgUser, String msgHost, String channel, String username, long timecode) {
        String[] args = new String[] { "-", channel, username, String.valueOf(timecode) };
        return new IRCMessage(msgNick, msgUser, msgHost, IRCMessageTypes.RPL_TOPICSET, args);
    }

    /**
     * Creates a new RPL_TOPIC message.
     *
     * @param msgNick    String object containing the nick of the guy this
     *                   message comes from. Should usually be "".
     * @param msgUser    String object containing the user name of the guy this
     *                   message comes from. Should usually be "".
     * @param msgHost    String object containing the host name of the guy this
     *                   message comes from. Should usually be "".
     * @param channel    String containing the channel name.
     * @param username   String containing the user name of the user who set the
     *                   topic.
     * @param date       specifies when the topic was set.
     */
    public static IRCMessage createMessage(String msgNick, String msgUser, String msgHost, String channel, String username, Date date) {
        String[] args = new String[] { "-", channel, username, String.valueOf(date.getTime() / 1000) };
        return new IRCMessage(msgNick, msgUser, msgHost, IRCMessageTypes.RPL_TOPICSET, args);
    }

    /**
     * Returns the channel name.
     *
     * @return String containing the channel name.
     */
    public static String getChannel(IRCMessage msg) {
        return msg.getArgs().get(msg.getArgs().size() - 3);
    }

    /**
     * Returns the name of the guy who set the topic.
     *
     * @return String containing the user name.
     */
    public static String getUserName(IRCMessage msg) {
        return msg.getArgs().get(msg.getArgs().size() - 2);
    }

    /**
     * Returns the point of time when the topic has been set.
     *
     * @return Date specifying when the topic has been set.
     */
    public static Date getTimecode(IRCMessage msg) {
        String timecode = msg.getArgs().get(msg.getArgs().size() - 1);
        try {
            long tcode = Long.parseLong(timecode);
            return new Date(tcode * 1000);
        } catch (NumberFormatException e) {
            return new Date();
        }
    }
}
