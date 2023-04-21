package org.thole.phiirc.client.controller;

import org.thole.phiirc.client.model.SelfUser;
import org.thole.phiirc.client.model.Server;
import org.thole.phiirc.client.view.interfaces.IClientUI;
import org.thole.phiirc.irc.IRCConnection;

public class Connector {

    private IClientUI clientUI;

    private Server server;

    private SelfUser myUser;

    private IRCConnection connection;

    public Connector() {
        clientUI = Controller.getInstance().getClient();
        this.setServer(new Server());
        this.setMyUser(new SelfUser());
        this.setConnection(new IRCConnection(this, this.getServer().getUrl(), this.getServer().getPort(), this.getServer().getPort(), "", this.getMyUser().getNick(), this.getMyUser().getName(), this.getMyUser().getNick()));
    }

    /**
	 * connect... what else?
	 */
    public void connect() {
        connection.connect();
    }

    public Server getServer() {
        return server;
    }

    private void setServer(final Server server) {
        this.server = server;
    }

    public SelfUser getMyUser() {
        return myUser;
    }

    private void setMyUser(final SelfUser myUser) {
        this.myUser = myUser;
    }

    public IRCConnection getConnection() {
        return connection;
    }

    private void setConnection(final IRCConnection connection) {
        this.connection = connection;
    }
}
