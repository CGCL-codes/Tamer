package fr.fg.server.core;

import java.util.ArrayList;
import java.util.List;
import fr.fg.server.data.AccountAction;
import fr.fg.server.data.Ally;
import fr.fg.server.data.Connection;
import fr.fg.server.data.DataAccess;
import fr.fg.server.data.Player;
import fr.fg.server.data.Sector;
import fr.fg.server.data.StarSystem;

public class PlayerTools {

    public static void closeAccount(Player player, boolean idle) {
        List<Connection> connections = DataAccess.getConnectionsByPlayer(player.getId());
        Connection lastConnection = null;
        long lastConnectionDate = 0;
        synchronized (connections) {
            for (Connection connection : connections) if (connection.getStart() > lastConnectionDate) {
                lastConnection = connection;
                lastConnectionDate = connection.getStart();
            }
        }
        int ip = lastConnection != null ? lastConnection.getIp() : 0;
        AccountAction accountAction = new AccountAction(player.getLogin(), idle ? AccountAction.ACTION_IDLE : AccountAction.ACTION_CLOSED, player.getEmail(), player.getBirthday(), ip, player.getPlayedTime(Player.SCALE_OVERALL), player.getCloseAccountReason());
        accountAction.save();
        if (player.getIdAlly() != 0) {
            Ally ally = player.getAlly();
            synchronized (player.getLock()) {
                player = DataAccess.getEditable(player);
                player.setIdAlly(0);
                player.setAllyRank(0);
                player.save();
            }
            if (ally != null) {
                if (ally.getMembers().size() == 1) {
                    ally.delete();
                    List<Sector> sectors = new ArrayList<Sector>(DataAccess.getAllSectors());
                    for (Sector sector : sectors) sector.updateInfluences();
                } else {
                    ally.updateInfluences();
                }
            }
        }
        List<StarSystem> systems = new ArrayList<StarSystem>(player.getSystems());
        for (StarSystem system : systems) {
            synchronized (system.getLock()) {
                system = DataAccess.getEditable(system);
                system.resetSettings();
                system.save();
            }
        }
        synchronized (player.getLock()) {
            player.delete();
        }
    }
}
