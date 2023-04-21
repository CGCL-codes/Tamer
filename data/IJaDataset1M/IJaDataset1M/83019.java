package net.hypotenubel.irc.msgutils;

import net.hypotenubel.irc.*;

/**
 * Wrapper for easy handling of MSG_KILL messages.
 * <p>
 * <b>Syntax:</b> {@code KILL &lt;nickname&gt; &lt;comment&gt;}
 * <p>
 * The KILL command is used to cause a client-server connection to be closed by
 * the server which has the actual connection. Servers generate KILL messages on
 * nickname collisions. It <b>may</b> also be available available to users who
 * have the operator status.
 * <p>
 * Clients which have automatic reconnect algorithms effectively make this
 * command useless since the disconnection is only brief. It does however break
 * the flow of data and can be used to stop large amounts of <i>flooding</i>
 * from abusive users or accidents. Abusive users usually don't care as they
 * will reconnect promptly and resume their abusive behaviour. To prevent this
 * command from being abused, any user may elect to receive KILL messages
 * generated for others to keep an <i>eye</i> on would be trouble spots.
 * <p>
 * In an arena where nicknames are <b>required</b> to be globally unique at all
 * times, KILL messages are sent whenever <i>duplicates</i> are detected (that
 * is an attempt to register two users with the same nickname) in the hope that
 * both of them will disappear and only 1 reappear. When a client is removed as
 * the result of a KILL message, the server <b>should</b> add the nickname to
 * the list of unavailable nicknames in an attempt to avoid clients to reuse
 * this name immediately which is usually the pattern of abusive behaviour often
 * leading to useless "KILL loops". See the "IRC Server Protocol" document
 * [IRC-SERVER] for more information on this procedure.
 * <p>
 * The comment given <b>must</b> reflect the actual reason for the KILL. For
 * server-generated KILLs it usually is made up of details concerning the
 * origins of the two conflicting nicknames. For users it is left up to them to
 * provide an adequate reason to satisfy others who see it. To
 * prevent/discourage fake KILLs from being generated to hide the identify of
 * the KILLer, the comment also shows a 'kill-path' which is updated by each
 * server it passes through, each prepending its name to the path.
 *
 * @author Christoph Daniel Schulze
 * @version $Id: KillMessage.java 91 2006-07-21 13:41:43Z captainnuss $
 */
public class KillMessage {

    /**
     * Instantiation is not allowed.
     */
    private KillMessage() {
    }

    /**
     * Creates a new MSG_KILL message.
     *
     * @param msgNick  String object containing the nick of the guy this message
     *                 comes from. Should usually be "".
     * @param msgUser  String object containing the user name of the guy this
     *                 message comes from. Should usually be "".
     * @param msgHost  String object containing the host name of the guy this
     *                 message comes from. Should usually be "".
     * @param nickname String object containing the nick name.
     * @param comment  String object containing the kill comment.
     */
    public static IRCMessage createMessage(String msgNick, String msgUser, String msgHost, String nickname, String comment) {
        String[] args = new String[] { nickname, comment };
        return new IRCMessage(msgNick, msgUser, msgHost, IRCMessageTypes.MSG_KILL, args);
    }

    /**
     * Returns the nick name.
     *
     * @return String containing the nick name.
     */
    public static String getNickname(IRCMessage msg) {
        return msg.getArgs().get(0);
    }

    /**
     * Returns the kill comment.
     *
     * @return String containing the kill comment.
     */
    public static String getComment(IRCMessage msg) {
        return msg.getArgs().get(1);
    }
}
