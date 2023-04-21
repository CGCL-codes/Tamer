package webirc.client.commands;

import webirc.client.Channel;

/**
 * @author Ayzen
 * @version 1.0 19.01.2007 19:37:23
 */
public class TopicCommand extends IRCCommand {

    public static String getName() {
        return "TOPIC";
    }

    private Channel channel;

    private String topic;

    public TopicCommand(Channel channel, String topic) {
        name = getName();
        params = ' ' + channel.toString() + " :" + topic;
    }

    public TopicCommand(String prefix, String command, String params) {
        super(prefix, command, params);
        name = getName();
        channel = new Channel(nextParam());
        topic = lastParam();
    }

    public Channel getChannel() {
        return channel;
    }

    public String getTopic() {
        return topic;
    }
}
