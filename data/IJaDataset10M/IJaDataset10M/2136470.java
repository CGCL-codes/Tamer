package net.hypotenubel.irc.msgutils;

import net.hypotenubel.irc.*;

/**
 * Wrapper for easy handling of MSG_PRIVMSG messages.
 * <p>
 * <b>Syntax:</b> {@code PRIVMSG &lt;msgtarget&gt; &lt;text&gt;}
 * <p>
 * PRIVMSG is used to send private messages between users, as well as to send
 * messages to channels. {@code msgtarget} is usually the nickname of the
 * recipient of the message, or a channel name.
 * <p>
 * The {@code msgtarget} parameter may also be a host mask
 * (#{@code mask}) or server mask (${@code mask}). In both cases the
 * server will only send the PRIVMSG to those who have a server or host matching
 * the mask. The mask <b>must</b> have at least 1 "." in it and no wildcards
 * following the last ".". This requirement exists to prevent people sending
 * messages to "#*" or "$*", which would broadcast to all users. Wildcards are
 * the '*' and '?' characters. This extension to the PRIVMSG command is only
 * available to operators.
 *
 * @author Christoph Daniel Schulze
 * @version $Id: PrivateMessage.java 91 2006-07-21 13:41:43Z captainnuss $
 */
public class PrivateMessage {

    /**
     * Instantiation is not allowed.
     */
    private PrivateMessage() {
    }

    /**
     * Creates a new MSG_PRIVMSG message with the supplied information.
     *
     * @param msgNick String object containing the nick of the guy this message
     *                comes from. Should usually be "".
     * @param msgUser String object containing the user name of the guy this
     *                message comes from. Should usually be "".
     * @param msgHost String object containing the host name of the guy this
     *                message comes from. Should usually be "".
     * @param msgtarget String object containing the recipient.
     * @param text      String object containing the message text.
     */
    public static IRCMessage createMessage(String msgNick, String msgUser, String msgHost, String msgtarget, String text) {
        String[] args = new String[] { msgtarget, text };
        return new IRCMessage(msgNick, msgUser, msgHost, IRCMessageTypes.MSG_PRIVMSG, args);
    }

    /**
     * Returns the message recipient.
     *
     * @return String containing the recipient.
     */
    public static String getMsgtarget(IRCMessage msg) {
        return msg.getArgs().get(0);
    }

    /**
     * Returns the message text.
     *
     * @return String containing the message text.
     */
    public static String getText(IRCMessage msg) {
        return msg.getArgs().get(1);
    }
}
