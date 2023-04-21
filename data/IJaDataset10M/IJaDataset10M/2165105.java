package com.rlsoftwares.virtualdeck.network.messages;

import com.rlsoftwares.network.Message;
import com.rlsoftwares.virtualdeck.Player;

/**
 *
 * @author Rodrigo
 */
public class MessagePlayerAuthenticated extends Message {

    private Player player;

    public Player getPlayer() {
        return player;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }
}
