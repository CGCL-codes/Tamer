package org.jomp.prototype.server.engine;

import java.util.List;

/**
 * This class holds configuration parameters for a single battle like
 * PvE or PvP and other stuff.
 *
 * @author stefan.raubal
 */
public class BattleConfiguration {

    private List<Player> players;

    /**
     * @param players
     */
    public BattleConfiguration(List<Player> players) {
        this.players = players;
    }

    public List<Player> getPlayers() {
        return players;
    }
}
