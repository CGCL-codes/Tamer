package freelands.chat;

import freelands.NetworkClient;
import freelands.NonBlockingServer;
import freelands.actor.Player;
import freelands.protocol.ChatChannel;
import freelands.protocol.InGameCommand;
import java.nio.ByteBuffer;
import freelands.protocol.TextColor;
import freelands.protocol.message.toclient.RawMessage;
import java.nio.charset.Charset;

/**
 * This class handle mp, commands and chat
 * @see freelands.protocol.InGameCommand freelands.protocol.InGameCommand to know the commands available
 * @version 0.0.2
 * @since 0.0.2
 * @author Fraipont Michael
 */
public class ChatProcessor {

    /**
     * characters need to start a command, any character in this is need
     * @since 0.0.2
     */
    private static final char STARTCOMMAND = '#';

    /**
     * characters need to send message in a channel, any character in this is need
     * @since 0.0.2
     */
    private static final char STARTCHANNELCOMMAND = '@', FRENCHSTARTCHANNELCOMMAND = '!';

    /**
     * process the message who come from a player an send the corresponding message to another player
     * @param from player who send the private message
     * @param targetName
     * @param targetMsg
     * @since 0.0.2
     */
    public static void processSendPm(Player from, String targetName, String targetMsg) {
        if (targetName == null || targetName.equals("")) {
            return;
        }
        Player dist = NonBlockingServer.nameToPlayer(targetName);
        if (dist != null) {
            String sendMsg = "[PM to " + dist + ":" + targetMsg + "]";
            from.sendPacket(RawMessage.rawTextMessage(ChatChannel.SERVER, TextColor.c_green2, sendMsg));
            dist.sendPacket(RawMessage.rawTextMessage(ChatChannel.SERVER, TextColor.c_green2, "[PM from " + from.getContent().name + ":" + targetMsg + "]"));
        } else {
            from.sendPacket(RawMessage.rawTextMessage(ChatChannel.SERVER, TextColor.c_blue1, "Such a player isn't logged in the game."));
        }
    }

    /**
     * process the raw text message and dispatch the text to channels, commands or local message
     * this method check also rigths to use the command, and if not response it to the player
     * @param from player who send message
     * @param bb the raw text message
     */
    public static void processRawText(NetworkClient from, ByteBuffer bb) {
        String msg = freelands.protocol.message.toserver.RawMessage.extractString(bb, 3, bb.getShort(1) - 1);
        char firstChar = msg.charAt(0);
        if (STARTCOMMAND == firstChar) {
            String values[] = msg.split(" ", 2);
            String command = values[0].substring(1);
            String args = values.length == 1 ? "" : values[1];
            try {
                InGameCommand igc = InGameCommand.valueOf(command);
                if (igc.isGeneralCommand) {
                    igc.process(from, args);
                } else {
                    from.addMessageToPlayer(bb);
                }
            } catch (IllegalArgumentException npe) {
                from.addPacketTosend(RawMessage.rawTextMessage(ChatChannel.SERVER, TextColor.c_grey1, "Unknow command"));
            }
        } else if (STARTCHANNELCOMMAND == firstChar) {
            if (msg.length() == 1) {
                return;
            }
            char c2 = msg.charAt(1);
            int channel;
            if ((c2 == STARTCHANNELCOMMAND || c2 == FRENCHSTARTCHANNELCOMMAND) && msg.length() > 2) {
                String[] values = msg.split(" ", 2);
                if (values.length != 2) {
                    from.addPacketTosend(RawMessage.rawTextMessage(ChatChannel.SERVER, TextColor.c_grey1, "Usage : !!number message"));
                    return;
                } else {
                    try {
                        channel = Integer.valueOf(values[0].substring(2));
                    } catch (Exception nfe) {
                        from.addPacketTosend(RawMessage.rawTextMessage(ChatChannel.SERVER, TextColor.c_grey1, "Usage : !!number message"));
                        return;
                    }
                    if (!from.isInChannel(channel)) {
                        from.addPacketTosend(RawMessage.rawTextMessage(ChatChannel.SERVER, TextColor.c_grey1, "You are not connected at this channel"));
                        return;
                    }
                    msg = values[1];
                }
            } else {
                channel = from.getActiveChatChannel();
                msg = msg.substring(1);
            }
            if (channel < 0) {
                from.addPacketTosend(RawMessage.rawTextMessage(ChatChannel.SERVER, TextColor.c_grey1, "You're not connected at a channel"));
                return;
            }
            Channel.getChannel(channel).sendAtAll("[" + from.player.getContent().name + "]:" + msg);
        } else {
            from.addMessageToPlayer(bb);
        }
    }

    public static void processRawText(Player from, ByteBuffer bb) {
        String msg = freelands.protocol.message.toserver.RawMessage.extractString(bb, 3, bb.getShort(1) - 1);
        char firstChar = msg.charAt(0);
        if (STARTCOMMAND == firstChar) {
            String values[] = msg.split(" ", 2);
            String command = values[0].substring(1);
            String args = values.length == 1 ? "" : values[1];
            try {
                InGameCommand igc = InGameCommand.valueOf(command);
                igc.process(from, args);
            } catch (IllegalArgumentException npe) {
                assert (false);
            }
        } else {
            assert (STARTCHANNELCOMMAND != firstChar);
            String str = from.getContent().name + ": " + msg;
            ByteBuffer sendmsg = RawMessage.rawTextMessage(ChatChannel.LOCAL, TextColor.c_grey1, str);
            from.getCurrentMap().sendAtAllPlayers(sendmsg);
        }
    }

    private static final Charset iso88591 = Charset.forName("ISO-8859-1");
}
