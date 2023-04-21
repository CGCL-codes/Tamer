package org.freelords.server.phase;

import org.freelords.server.Server;

public interface PhaseOutput {

    /** Call this method after a phase has been executed to invoke changes on the server and therefore on the client */
    public void execute(Server server);
}
