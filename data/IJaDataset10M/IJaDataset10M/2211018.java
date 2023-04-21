package org.jogre.webapp.data;

import org.apache.struts.util.MessageResources;
import org.jogre.server.data.SnapShot;

/**
 * Immutable online game data object used in the JOGRE web system.
 * 
 * @author  Bob Marks
 * @version Beta 0.3
 */
public class OnlineGame {

    private String gameKey;

    private int numOfUsers;

    private int numOfTables;

    private String gameName;

    private String gameSynopsis;

    private String gameRules;

    private String gameGenre;

    /**
	 * Constructor which takes a snapshot and message resources.
	 * 
	 * @param snapshot
	 * @param resources
	 */
    public OnlineGame(SnapShot snapshot, MessageResources resources) {
        this.gameKey = snapshot.getGameKey();
        this.numOfUsers = snapshot.getNumOfUsers();
        this.numOfTables = snapshot.getNumOfTables();
        this.gameName = resources.getMessage(gameKey);
        this.gameSynopsis = resources.getMessage(gameKey + ".synopsis");
        this.gameRules = resources.getMessage(gameKey + ".rules");
        this.gameGenre = resources.getMessage(gameKey + ".genre");
    }

    public String getGameKey() {
        return gameKey;
    }

    public int getNumOfUsers() {
        return numOfUsers;
    }

    public int getNumOfTables() {
        return numOfTables;
    }

    public String getGameName() {
        return gameName;
    }

    public String getGameSynopsis() {
        return gameSynopsis;
    }

    public String getGameRules() {
        return gameRules;
    }

    public String getGameGenre() {
        return gameGenre;
    }
}
