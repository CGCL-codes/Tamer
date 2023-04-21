package com.sjakkforum;

import java.util.ArrayList;
import java.util.List;

/**
 * A player database.
 * @author Kjartan Mikkelsen (kjartanmi@gmail.com)
 */
public class PlayerBase {

    private List<Player> players = null;

    private String name = "";

    public PlayerBase(String name) {
        this.name = name;
        players = new ArrayList<Player>();
    }

    public PlayerBase(String name, List<Player> players) {
        this.players = players;
        this.name = name;
    }

    public void addPlayer(Player player) {
        players.add(player);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Player> getPlayers() {
        return players;
    }
}
