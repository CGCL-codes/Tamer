package net.jetrix.messages.channel;

/**
 * A team change message.
 *
 * @author Emmanuel Bourg
 * @version $Revision: 798 $, $Date: 2009-02-18 10:24:28 -0500 (Wed, 18 Feb 2009) $
 */
public class TeamMessage extends ChannelMessage {

    /** the name of the team */
    private String name;

    public TeamMessage() {
    }

    public TeamMessage(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
