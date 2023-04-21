package f00f.net.irc.martyr.errors;

import f00f.net.irc.martyr.InCommand;

/**
 * Code: 471 ERR_CHANNELISFULL
 * &lt;channel&gt; :Cannot join channel (+l)
 * @author <a href="mailto:martyr@mog.se">Morgan Christiansson</a>
 * @version $Id: ChannelLimitError.java 85 2007-08-02 18:26:59Z jadestorm $
 * TODO: Rename to ChannelIsFullError to match style of others?
 */
public class ChannelLimitError extends GenericJoinError {

    public ChannelLimitError() {
    }

    protected ChannelLimitError(String chan, String comment) {
        super(chan, comment);
    }

    public String getIrcIdentifier() {
        return "471";
    }

    protected InCommand create(String channel, String comment) {
        return new ChannelLimitError(channel, comment);
    }
}
