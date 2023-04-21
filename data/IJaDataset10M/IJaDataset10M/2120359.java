package prc.bubulina.cruce.server;

import prc.bubulina.cruce.remote.Player;

public class PlayerFactory {

    private int val = 0;

    public Player getPlayer(String name) {
        return new Player(name, val++);
    }
}
