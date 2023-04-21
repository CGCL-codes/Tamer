package genericirc.irccore;

/**
 * Message
 * @author Steve "Uru" West <uruwolf@users.sourceforge.net>
 * @version 2011-07-26
 */
public class Message {

    private String user;

    private String hostmask;

    private String channel;

    private String message;

    public Message(String user, String hostmask, String channel, String message) {
        this.user = user;
        this.hostmask = hostmask;
        this.channel = channel;
        this.message = message;
    }

    /**
     * @return the user
     */
    public String getUser() {
        return user;
    }

    /**
     * @return the hostmask
     */
    public String getHostmask() {
        return hostmask;
    }

    /**
     * @return the channel
     */
    public String getChannel() {
        return channel;
    }

    /**
     * @return the message
     */
    public String getMessage() {
        return message;
    }
}
