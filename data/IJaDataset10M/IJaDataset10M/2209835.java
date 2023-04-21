package wotlas.common;

/** Utility for the chat
 *
 * @author Petrus
 * @see wotlas.client.screen.JChatRoom
 * @see wotlas.client.screen.JChatPanel
 * @see wotlas.common.Player
 */
public class PlayerState {

    public static final byte DISCONNECTED = 0;

    public static final byte CONNECTED = 1;

    public static final byte AWAY = 2;

    public String fullName;

    public byte value;

    public PlayerState(String fullName, byte value) {
        this.fullName = fullName;
        this.value = value;
    }

    public PlayerState() {
        this.fullName = "";
        this.value = PlayerState.DISCONNECTED;
    }
}
