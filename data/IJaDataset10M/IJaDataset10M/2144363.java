package net.sf.l2j.gameserver;

import net.sf.l2j.Config;
import net.sf.l2j.gameserver.model.L2World;

public class OnlinePlayers {

    private static OnlinePlayers _instance;

    class AnnounceOnline implements Runnable {

        public void run() {
            int PLAYERS_ONLINE = L2World.getInstance().getAllPlayers().size() + Config.PLAYERS_ONLINE_TRICK;
            Announcements.getInstance().announceToAll("Online Players: " + PLAYERS_ONLINE);
            if (Config.ONLINE_PLAYERS_ANNOUNCE_INTERVAL > 0) ThreadPoolManager.getInstance().scheduleGeneral(new AnnounceOnline(), Config.ONLINE_PLAYERS_ANNOUNCE_INTERVAL);
        }
    }

    public static OnlinePlayers getInstance() {
        if (_instance == null) _instance = new OnlinePlayers();
        return _instance;
    }

    private OnlinePlayers() {
        if (Config.ONLINE_PLAYERS_ANNOUNCE_INTERVAL > 0) ThreadPoolManager.getInstance().scheduleGeneral(new AnnounceOnline(), Config.ONLINE_PLAYERS_ANNOUNCE_INTERVAL);
    }
}
