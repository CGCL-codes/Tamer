package net.sf.jerkbot.plugins.irclog;

/**
 * @author Yves Zoundi <yveszoundi at users dot sf dot net>
 *         [INSERT DESCRIPTION HERE]
 * @version 0.0.1
 */
class IRCLog {

    private final String channel;

    private final String message;

    private final String sender;

    public IRCLog(String channel, String sender, String message) {
        this.channel = channel;
        this.message = message;
        this.sender = sender;
    }

    public String getChannel() {
        return channel;
    }

    public String getMessage() {
        return message;
    }

    public String getSender() {
        return sender;
    }
}
