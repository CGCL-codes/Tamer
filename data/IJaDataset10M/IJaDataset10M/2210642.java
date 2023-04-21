package pt.jkaiui.core.messages;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import pt.jkaiui.core.KaiString;
import pt.jkaiui.manager.I_InMessage;
import pt.jkaiui.manager.I_OutMessage;

/**
 *
 * @author  pedro
 */
public class ChatModeOut extends Message implements I_OutMessage {

    public ChatModeOut() {
    }

    /**
     * Holds value of property room.
     */
    private KaiString room;

    /**
     * Getter for property room.
     * @return Value of property room.
     */
    public KaiString getRoom() {
        return this.room;
    }

    /**
     * Setter for property room.
     * @param room New value of property room.
     */
    public void setRoom(pt.jkaiui.core.KaiString room) {
        this.room = room;
    }

    public String send() {
        String out = "KAI_CLIENT_CHATMODE;" + getRoom() + ";";
        return out;
    }
}
