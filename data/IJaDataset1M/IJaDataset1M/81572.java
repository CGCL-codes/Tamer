package org.tacticalTroopers.jme.server;

import org.tacticalTroopers.jme.common.message.Command;
import org.tacticalTroopers.jme.server.object.ServerGameArea;

public interface ClientHandler {

    public void sendGameFrame(ServerGameArea gameArea);

    public Command getCommand();

    public void setId(Integer id);

    public Integer getId();

    public void setGameArea(ServerGameArea gameArea);
}
