package org.thole.phiirc.irc.parser;

import org.thole.phiirc.irc.IRCConnection;

/**
 * 
 * @author hendrik
 * @date 31.03.2009
 *
 */
public abstract class AbstractIRCLineParser {

    private IRCConnection connection;

    protected AbstractIRCLineParser(final IRCConnection connection) {
        this.setConnection(connection);
    }

    public abstract void parse(String prefix, String cmd, String parameter);

    public IRCConnection getConnection() {
        return connection;
    }

    private void setConnection(final IRCConnection connection) {
        this.connection = connection;
    }
}
