package org.wsmostudio.bpmo.ui.editor.command;

import org.eclipse.gef.commands.Command;
import org.wsmostudio.bpmo.model.connectors.GraphConnector;

/**
 * A command to disconnect (remove) a connection from its endpoints.
 * The command can be undone or redone.
 * @author Elias Volanakis
 */
public class ConnectionDeleteCommand extends Command {

    /** Connection instance to disconnect. */
    private final GraphConnector connection;

    /** 
 * Create a command that will disconnect a connection from its endpoints.
 * @param conn the connection instance to disconnect (non-null)
 * @throws IllegalArgumentException if conn is null
 */
    public ConnectionDeleteCommand(GraphConnector conn) {
        if (conn == null) {
            throw new IllegalArgumentException();
        }
        setLabel("connection deletion");
        this.connection = conn;
    }

    public void execute() {
        connection.disconnect();
    }

    public void undo() {
        connection.reconnect();
    }
}
