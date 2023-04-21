package erki.talk.clients.erki.event;

/**
 * This event is fired if a user left the chat with or without leaving a quit
 * message.
 * 
 * @author Edgar Kalkowski
 */
public class UserQuitEvent extends Event {

    private final String nick;

    private final String message;

    /**
     * Create a new {@code UserQuitEvent} for a user that left without leaving a
     * quit message.
     * 
     * @param nick
     *        The nickname of the user who left.
     */
    public UserQuitEvent(String nick) {
        this(nick, null);
    }

    /**
     * Create a new {@code UserQuitEvent} for a user that left a quit message.
     * 
     * @param nick
     *        The nickname of the user who quit.
     * @param message
     *        The user's quit message.
     */
    public UserQuitEvent(String nick, String message) {
        this.nick = nick;
        this.message = message;
    }

    /** @return The nickname of the user who quit. */
    public String getNick() {
        return nick;
    }

    /**
     * @return The quit message the user left or {@code null} if the user did
     *         not leave a quit message.
     */
    public String getMessage() {
        return message;
    }
}
