package com.novusradix.JavaPop.Messaging.Lobby;

import com.novusradix.JavaPop.Messaging.*;

/**
 *
 * @author gef
 */
public class GameOver extends Message {

    @Override
    public void execute() {
        client.quit();
    }
}
