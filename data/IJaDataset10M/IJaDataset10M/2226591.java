package server.util.tickevents;

import player.PlayerManager;

/**
 *
 * @author Michael Hanns
 *
 */
public class RegeneratePlayers implements TickerEvent {

    private PlayerManager players;

    public RegeneratePlayers(PlayerManager p) {
        this.players = p;
    }

    @Override
    public void executeEvent() {
        regenPlayers();
    }

    private void regenPlayers() {
        for (int x = 0; x < players.onlinePlayers(); x++) {
            players.getOnlinePlayerAtIndex(x).tickRegen();
        }
    }
}
