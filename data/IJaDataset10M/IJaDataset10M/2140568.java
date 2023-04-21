package com.krobothsoftware.psn.model;

import java.io.IOException;
import com.krobothsoftware.psn.FriendProcessor;
import com.krobothsoftware.psn.PlaystationNetwork;
import com.krobothsoftware.psn.PlaystationNetworkException;
import com.krobothsoftware.psn.PlaystationNetworkGameException;
import com.krobothsoftware.psn.model.list.GameList;

/**
 * The Class FriendData. This holds all information for friend. Used UK Verison
 * 
 * @author Kyle Kroboth
 */
public class FriendData extends DefaultModel implements Friend {

    private boolean isQuickFriend;

    private String currentPresence;

    private String currentGame;

    private String currentAvatar;

    private String comment;

    private boolean isPlaystationPlus;

    private int level;

    private int platinum;

    private int gold;

    private int silver;

    private int bronze;

    public FriendData(PlaystationNetwork psn, String psnId, FriendProcessor friendProcessor) {
        super(psn, psnId, friendProcessor);
    }

    @Override
    public boolean isQuickFriend() {
        return isQuickFriend;
    }

    @Override
    public int getCurrentPresence() {
        if (currentPresence.equalsIgnoreCase("offline")) {
            return Friend.OFFLINE;
        } else if (currentPresence.equalsIgnoreCase("online") || currentPresence.equalsIgnoreCase("online-ingame")) {
            return Friend.ONLINE;
        } else if (currentPresence.equalsIgnoreCase("online-away") || currentPresence.equalsIgnoreCase("online-ingame-away")) {
            return Friend.AWAY;
        }
        return 1;
    }

    @Override
    public String getCurrentPresenseString() {
        return currentPresence;
    }

    @Override
    public boolean isOnline() {
        return (!currentPresence.equals("offline"));
    }

    @Override
    public String getCurrentGame() {
        return currentGame;
    }

    @Override
    public String getCurrentAvatar() {
        return "http://secure.eu.playstation.com/" + currentAvatar;
    }

    @Override
    public String getComment() {
        return comment;
    }

    @Override
    public boolean isPlaystationPlus() {
        return isPlaystationPlus;
    }

    @Override
    public int getLevel() {
        return level;
    }

    @Override
    public int getPlatinum() {
        return platinum;
    }

    @Override
    public int getGold() {
        return gold;
    }

    @Override
    public int getSilver() {
        return silver;
    }

    @Override
    public int getBronze() {
        return bronze;
    }

    /**
	 * Gets the trophy total.
	 * 
	 * @return the trophy total
	 */
    public int getTrophyTotal() {
        return platinum + gold + silver + bronze;
    }

    @Override
    public String toString() {
        return "FriendData [onlineId=" + psnId + ", currentPresence=" + currentPresence + ", currentGame=" + currentGame + "]";
    }

    /**
	 * Returns a new instance of FriendData
	 * 
	 * @param onlineId
	 *            the online id
	 * @param isQuickFriend
	 *            the is quick friend
	 * @Override public GameList<GameData> getGames() 
	 * @Override public TrophyList<TrophyData> getTrophies()param currentPresence
	 *           the current presence
	 * @param currentGame
	 *            the current game
	 * @param currentAvatar
	 *            the current avatar
	 * @param comment
	 *            the comment
	 * @param isPlaystationPlus
	 *            the is playstation plus
	 * @param level
	 *            the level
	 * @param platinum
	 *            the platinum
	 * @param gold
	 *            the gold
	 * @param silver
	 *            the silver
	 * @param bronze
	 *            the bronze
	 * @return the friend data
	 */
    public static FriendData create(PlaystationNetwork psn, FriendProcessor friendProcessor, String onlineId, boolean isQuickFriend, String currentPresence, String currentGame, String currentAvatar, String comment, boolean isPlaystationPlus, int level, int platinum, int gold, int silver, int bronze) {
        FriendData friendData = new FriendData(psn, onlineId, friendProcessor);
        friendData.isQuickFriend = isQuickFriend;
        friendData.currentPresence = currentPresence;
        friendData.currentGame = currentGame;
        friendData.currentAvatar = currentAvatar;
        friendData.comment = comment;
        friendData.isPlaystationPlus = isPlaystationPlus;
        friendData.level = level;
        friendData.platinum = platinum;
        friendData.gold = gold;
        friendData.silver = silver;
        friendData.bronze = bronze;
        return friendData;
    }

    @Override
    public GameList<GameData> getGames() throws IOException, PlaystationNetworkException, PlaystationNetworkGameException {
        return friendProcessor.getGames(psnId);
    }
}
