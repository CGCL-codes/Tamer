package fr.fg.server.core;

import java.awt.Point;
import java.util.HashSet;
import java.util.List;
import fr.fg.server.data.Ally;
import fr.fg.server.data.Area;
import fr.fg.server.data.DataAccess;
import fr.fg.server.data.Effect;
import fr.fg.server.data.Fleet;
import fr.fg.server.data.Player;
import fr.fg.server.data.Structure;
import fr.fg.server.servlet.ServerController;
import fr.fg.server.util.Utilities;

public class UpdateTools {

    public static void queueEffectUpdate(Effect effect, int sourcePlayer) {
        queueEffectUpdate(effect, sourcePlayer, true);
    }

    public static void queueEffectUpdate(Effect effect, int sourcePlayer, boolean highPriority) {
        List<Integer> idPlayers = ConnectionManager.getInstance().getPlayersByArea(effect.getIdArea());
        Area area = effect.getArea();
        for (Integer idPlayer : idPlayers) {
            if (idPlayer == sourcePlayer) continue;
            if (ConnectionManager.getInstance().isConnected(idPlayer)) {
                Player player = DataAccess.getPlayerById(idPlayer);
                List<Point> allySystems = player.getAllySystems(area);
                List<Point> allyFleets = player.getAllyFleets(area);
                List<Point> allyWards = player.getAllyObserverWards(area);
                List<Point> allySpaceStations = player.getAllySpaceStations(area);
                List<Structure> allyStructures = player.getAllyStructures(area);
                if (player.isLocationVisible(area, effect.getX(), effect.getY(), effect.getRadius(), allySystems, allyFleets, allyWards, allySpaceStations, allyStructures)) {
                    UpdateManager.getInstance().addUpdate(player.getId(), Update.getEffectUpdate(effect), highPriority);
                    break;
                }
            }
        }
    }

    public static void queueServerShutdownUpdate() {
        HashSet<Integer> connectedPlayers = new HashSet<Integer>(ConnectionManager.getInstance().getConnectedPlayers());
        Update update = Update.getServerShutdownUpdate(ServerController.getShutdownDate() == -1 ? -1 : Math.max(0, (int) (ServerController.getShutdownDate() - Utilities.now())));
        for (int idPlayer : connectedPlayers) UpdateManager.getInstance().addUpdate(idPlayer, update);
    }

    public static void queueNewAllyNewsUpdate(Ally ally) {
        queueNewAllyNewsUpdate(ally, 0, true);
    }

    public static void queueNewAllyNewsUpdate(Ally ally, int idSourcePlayer) {
        queueNewAllyNewsUpdate(ally, idSourcePlayer, true);
    }

    public static void queueNewAllyNewsUpdate(Ally ally, int idSourcePlayer, boolean highPriority) {
        List<Player> members = ally.getMembers();
        synchronized (members) {
            for (Player member : members) {
                if (member.getId() != idSourcePlayer && ConnectionManager.getInstance().isConnected(member.getId())) {
                    UpdateManager.getInstance().addUpdate(member.getId(), Update.getNewNewsUpdate(), highPriority);
                }
            }
        }
    }

    public static void queueNewMessageUpdate(int idPlayer) {
        if (ConnectionManager.getInstance().isConnected(idPlayer)) UpdateManager.getInstance().addUpdate(idPlayer, Update.getNewMessageUpdate());
    }

    public static void queueAllyUpdate(int idPlayer, long lastUpdate) {
        queueAllyUpdate(idPlayer, lastUpdate, true);
    }

    public static void queueAllyUpdate(int idPlayer, long lastUpdate, boolean highPriority) {
        if (ConnectionManager.getInstance().isConnected(idPlayer)) UpdateManager.getInstance().addUpdate(idPlayer, Update.getAllyUpdate(lastUpdate), highPriority);
    }

    public static void queueChatChannelsUpdate(int idPlayer) {
        queueChatChannelsUpdate(idPlayer, true);
    }

    public static void queueChatChannelsUpdate(int idPlayer, boolean highPriority) {
        if (ConnectionManager.getInstance().isConnected(idPlayer)) UpdateManager.getInstance().addUpdate(idPlayer, Update.getChatChannelsUpdate(), highPriority);
    }

    public static void queueChatUpdate(int idPlayer, String data) {
        if (ConnectionManager.getInstance().isConnected(idPlayer)) UpdateManager.getInstance().addUpdate(idPlayer, Update.getChatUpdate(data));
    }

    public static void queueContractsUpdate(int idPlayer) {
        queueContractsUpdate(new int[] { idPlayer });
    }

    public static void queueContractsUpdate(int idPlayer, boolean highPriority) {
        queueContractsUpdate(new int[] { idPlayer }, highPriority);
    }

    public static void queueContractsUpdate(int[] idPlayers) {
        queueContractsUpdate(idPlayers, true);
    }

    public static void queueContractsUpdate(int[] idPlayers, boolean highPriority) {
        for (int idPlayer : idPlayers) if (ConnectionManager.getInstance().isConnected(idPlayer)) {
            Player player = DataAccess.getPlayerById(idPlayer);
            UpdateManager.getInstance().addUpdate(player.getId(), Update.getPlayerContractsUpdate(), false);
            UpdateManager.getInstance().addUpdate(player.getId(), Update.getContractStateUpdate(), highPriority);
        }
    }

    public static void queueXpUpdate(int idPlayer) {
        queueXpUpdate(idPlayer, true);
    }

    public static void queueXpUpdate(int idPlayer, boolean highPriority) {
        if (ConnectionManager.getInstance().isConnected(idPlayer)) UpdateManager.getInstance().addUpdate(idPlayer, Update.getXpUpdate(), highPriority);
    }

    public static void queueNewEventUpdate(int idPlayer) {
        queueNewEventUpdate(idPlayer, true);
    }

    public static void queueNewEventUpdate(int idPlayer, boolean highPriority) {
        if (ConnectionManager.getInstance().isConnected(idPlayer)) UpdateManager.getInstance().addUpdate(idPlayer, Update.getNewEventUpdate(), highPriority);
    }

    public static void queueNewEventUpdate(List<Player> players) {
        queueNewEventUpdate(players, true);
    }

    public static void queueNewEventUpdate(List<Player> players, boolean highPriority) {
        synchronized (players) {
            for (Player player : players) {
                if (ConnectionManager.getInstance().isConnected(player.getId())) UpdateManager.getInstance().addUpdate(player.getId(), Update.getNewEventUpdate(), highPriority);
            }
        }
    }

    public static void queuePlayerFleetsUpdate(int idPlayer) {
        queuePlayerFleetsUpdate(idPlayer, true);
    }

    public static void queuePlayerFleetsUpdate(int idPlayer, boolean highPriority) {
        if (ConnectionManager.getInstance().isConnected(idPlayer)) UpdateManager.getInstance().addUpdate(idPlayer, Update.getPlayerFleetsUpdate(), highPriority);
    }

    public static void queuePlayerFleetUpdate(Fleet fleet) {
        queuePlayerFleetUpdate(fleet.getIdOwner(), fleet.getId());
    }

    public static void queuePlayerFleetUpdate(int idPlayer, int idFleet) {
        queuePlayerFleetUpdate(idPlayer, idFleet, true);
    }

    public static void queuePlayerFleetUpdate(int idPlayer, int idFleet, boolean highPriority) {
        if (ConnectionManager.getInstance().isConnected(idPlayer)) UpdateManager.getInstance().addUpdate(idPlayer, Update.getPlayerFleetUpdate(idFleet), highPriority);
    }

    public static void queuePlayerSystemsUpdate(int idPlayer) {
        queuePlayerSystemsUpdate(idPlayer, true);
    }

    public static void queuePlayerGeneratorsUpdate(int idPlayer) {
        queuePlayerGeneratorsUpdate(idPlayer, true);
    }

    public static void queuePlayerGeneratorsUpdate(int idPlayer, boolean highPriority) {
        if (ConnectionManager.getInstance().isConnected(idPlayer)) UpdateManager.getInstance().addUpdate(idPlayer, Update.getPlayerGeneratorsUpdate(), highPriority);
    }

    public static void queuePlayerSystemsUpdate(int idPlayer, boolean highPriority) {
        if (ConnectionManager.getInstance().isConnected(idPlayer)) UpdateManager.getInstance().addUpdate(idPlayer, Update.getPlayerSystemsUpdate(), highPriority);
    }

    public static void queueProductsUpdate(List<Player> players) {
        queueProductsUpdate(players, true);
    }

    public static void queueProductsUpdate(List<Player> players, boolean highPriority) {
        synchronized (players) {
            for (Player player : players) {
                if (ConnectionManager.getInstance().isConnected(player.getId())) UpdateManager.getInstance().addUpdate(player.getId(), Update.getProductsUpdate(), highPriority);
            }
        }
    }

    public static void queueAreaUpdate(Player player, Player... players) {
        queueAreaUpdate(true, player, players);
    }

    public static void queueAreaUpdate(boolean highPriority, Player player, Player... players) {
        if (ConnectionManager.getInstance().isConnected(player.getId()) && player.getIdCurrentArea() != 0) UpdateManager.getInstance().addUpdate(player.getId(), Update.getAreaUpdate(), highPriority);
        for (Player player2 : players) {
            if (ConnectionManager.getInstance().isConnected(player2.getId()) && player.getIdCurrentArea() != 0) UpdateManager.getInstance().addUpdate(player2.getId(), Update.getAreaUpdate(), highPriority);
        }
    }

    public static void queueAreaUpdate(List<Player> players) {
        synchronized (players) {
            for (Player player : players) {
                if (ConnectionManager.getInstance().isConnected(player.getId())) UpdateManager.getInstance().addUpdate(player.getId(), Update.getAreaUpdate());
            }
        }
    }

    public static void queueAreaUpdate(int idArea, Point... locations) {
        queueAreaUpdate(idArea, 0, locations);
    }

    public static void queueAreaUpdate(int idArea, int sourcePlayer, Point... locations) {
        queueAreaUpdate(idArea, sourcePlayer, true, locations);
    }

    public static void queueAreaUpdate(int idArea, int sourcePlayer, boolean highPriority, Point... locations) {
        List<Integer> idPlayers = ConnectionManager.getInstance().getPlayersByArea(idArea);
        Area area = DataAccess.getAreaById(idArea);
        for (Integer idPlayer : idPlayers) {
            Player player = DataAccess.getPlayerById(idPlayer);
            if (player.getId() == sourcePlayer) continue;
            if (locations.length > 0) {
                List<Point> allySystems = player.getAllySystems(area);
                List<Point> allyFleets = player.getAllyFleets(area);
                List<Point> allyWards = player.getAllyObserverWards(area);
                List<Point> allySpaceStations = player.getAllySpaceStations(area);
                List<Structure> allyStructures = player.getAllyStructures(area);
                for (Point location : locations) {
                    if (player.isLocationVisible(area, location.x, location.y, allySystems, allyFleets, allyWards, allySpaceStations, allyStructures)) {
                        queueAreaUpdate(highPriority, player);
                        break;
                    }
                }
            } else {
                queueAreaUpdate(highPriority, player);
            }
        }
    }

    public static String formatUpdates(Player player, List<Update> updates) {
        StringBuffer json = new StringBuffer();
        json.append("[");
        boolean first = true;
        synchronized (updates) {
            updates: for (int i = 0; i < updates.size(); i++) {
                Update update = updates.get(i);
                if (update == null) continue;
                for (int j = 0; j < i; j++) if (update.equals(updates.get(j))) continue updates;
                if (first) first = false; else json.append(",");
                json.append(update.getData(player));
            }
        }
        json.append("]");
        return json.toString();
    }

    public static String formatUpdates(Player player, Update... updates) {
        StringBuffer json = new StringBuffer();
        json.append("[");
        boolean first = true;
        updates: for (int i = 0; i < updates.length; i++) {
            Update update = updates[i];
            if (update == null) continue;
            for (int j = 0; j < i; j++) if (update.equals(updates[j])) continue updates;
            if (first) first = false; else json.append(",");
            json.append(update.getData(player));
        }
        json.append("]");
        return json.toString();
    }
}
