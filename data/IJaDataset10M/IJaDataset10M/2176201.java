package org.schwering.irc.manager.event;

import org.schwering.irc.manager.Channel;
import org.schwering.irc.manager.Connection;
import org.schwering.irc.manager.User;

/**
 * Fired when a CTCP CLIENTINFO request has been received.
 * @author Christoph Schwering &lt;schwering@gmail.com&gt;
 * @since 2.00
 * @version 1.00
 * @see CtcpListener#clientinfoRequestReceived(CtcpClientinfoRequestEvent)
 */
public class CtcpClientinfoRequestEvent {

    private Connection connection;

    private User sender;

    private User destUser;

    private Channel destChannel;

    private String command;

    private String rest;

    public CtcpClientinfoRequestEvent(Connection connection, User sender, User destUser, String command, String rest) {
        this.connection = connection;
        this.sender = sender;
        this.destUser = destUser;
        this.command = command;
        this.rest = rest;
    }

    public CtcpClientinfoRequestEvent(Connection connection, User sender, Channel destChannel, String command, String rest) {
        this.connection = connection;
        this.sender = sender;
        this.destChannel = destChannel;
        this.command = command;
        this.rest = rest;
    }

    public Connection getConnection() {
        return connection;
    }

    public User getSender() {
        return sender;
    }

    public Channel getDestinationChannel() {
        return destChannel;
    }

    public User getDestinationUser() {
        return destUser;
    }

    public String getCommand() {
        return command;
    }

    public String getArguments() {
        return rest;
    }

    public void reply(String clientinfo) {
        connection.sendCtcpReply(sender.getNick(), "CLIENTINFO", ":" + clientinfo);
    }
}
