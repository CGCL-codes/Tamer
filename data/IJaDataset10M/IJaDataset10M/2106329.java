package com.novusradix.JavaPop.Messaging.Lobby;

import com.novusradix.JavaPop.Messaging.*;

/**
 *
 * @author gef
 */
public class LeaveGame extends Message {

    @Override
    public void execute() {
        serverGame.removePlayer(serverPlayer);
    }
}
