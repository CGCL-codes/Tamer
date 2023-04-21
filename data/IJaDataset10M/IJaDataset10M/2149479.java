package net.kano.joscar.snaccmd.chat;

import net.kano.joscar.flapcmd.SnacCommand;
import net.kano.joscar.snaccmd.conn.SnacFamilyInfo;

/**
 * A base class for commands in the "chat" <code>0x0e</code> SNAC family.
 */
public abstract class ChatCommand extends SnacCommand {

    /** The family code for this SNAC family. */
    public static final int FAMILY_CHAT = 0x000e;

    /** A set of SNAC family information for this family. */
    public static final SnacFamilyInfo FAMILY_INFO = new SnacFamilyInfo(FAMILY_CHAT, 0x0001, 0x0010, 0x739);

    /** A command subtype for sending a message to a chat room. */
    public static final int CMD_SEND_CHAT_MSG = 0x0005;

    /** A command subtype for a room information update notification. */
    public static final int CMD_ROOM_UPDATE = 0x0002;

    /** A command subtype for a command sent when users enter a chat room. */
    public static final int CMD_USERS_JOINED = 0x0003;

    /** A command subtype for a command sent when users exit a chat room. */
    public static final int CMD_USERS_LEFT = 0x0004;

    /** A command subtype for a chat message command. */
    public static final int CMD_RECV_CHAT_MSG = 0x0006;

    /**
     * Creates a new SNAC command in the chat family.
     *
     * @param command the SNAC command subtype
     */
    protected ChatCommand(int command) {
        super(FAMILY_CHAT, command);
    }
}
