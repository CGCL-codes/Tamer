package db;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.apache.log4j.Level;
import logging.Logging;

public class Top10Highscore {

    private ArrayList<DBGameUser> playerList = new ArrayList<DBGameUser>();

    private static Logging logger = Logging.getInstance();

    public Top10Highscore() {
        super();
        List<DBGameUser> dbplayerList = new ArrayList<DBGameUser>();
        try {
            dbplayerList = DBServiceFactory.getInstance().getTopTenGameUser();
        } catch (DBException e) {
            logger.log(Level.ERROR, this, e.toString());
        }
        Iterator<DBGameUser> it = dbplayerList.iterator();
        while (it.hasNext()) {
            DBGameUser user = it.next();
            DBGameUser player = new DBGameUser();
            player.setName(user.getName());
            player.setScore(user.getScore());
            playerList.add(player);
        }
    }

    public ArrayList<DBGameUser> getPlayerList() {
        return playerList;
    }

    public void setPlayerList(ArrayList<DBGameUser> playerList) {
        this.playerList = playerList;
    }
}
