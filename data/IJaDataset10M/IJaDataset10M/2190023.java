package de.tudresden.inf.rn.mobilis.android.xhunt;

import java.util.ArrayList;
import android.graphics.Color;

/**
 * The Class Game.
 */
public class Game {

    /** The instance. */
    private static Game instance;

    /** The game id. */
    private String gameID;

    /** The current round. */
    private int currentRound;

    /** The game players. */
    private ArrayList<XHuntPlayer> gamePlayers;

    /** The chat id. */
    private String chatID;

    /** The chat password. */
    private String chatPassword;

    /** The start target. */
    private String startTarget;

    /** The taxi tickets. */
    private int taxiTickets;

    /** The bus tickets. */
    private int busTickets;

    /** The tram tickets. */
    private int tramTickets;

    /** The black tickets. */
    private int blackTickets;

    /** The remain player icons. */
    private ArrayList<Integer> remainIcons;

    /** The remain player colors. */
    private ArrayList<Integer> remainColors;

    /**
	 * Instantiates a new game.
	 */
    private Game() {
        gameID = null;
        currentRound = -1;
        gamePlayers = new ArrayList<XHuntPlayer>();
        remainIcons = new ArrayList<Integer>();
        remainIcons.add(R.drawable.ic_player_blue_36);
        remainIcons.add(R.drawable.ic_player_green_36);
        remainIcons.add(R.drawable.ic_player_orange_36);
        remainIcons.add(R.drawable.ic_player_red_36);
        remainIcons.add(R.drawable.ic_player_yellow_36);
        remainColors = new ArrayList<Integer>();
        remainColors.add(Color.BLUE);
        remainColors.add(Color.GREEN);
        remainColors.add(Color.rgb(220, 120, 30));
        remainColors.add(Color.RED);
        remainColors.add(Color.rgb(255, 225, 0));
    }

    /**
	 * Clear all.
	 */
    public void clearAll() {
        gameID = null;
        currentRound = -1;
        gamePlayers.clear();
        remainIcons.clear();
        remainIcons.add(R.drawable.ic_player_blue_36);
        remainIcons.add(R.drawable.ic_player_green_36);
        remainIcons.add(R.drawable.ic_player_orange_36);
        remainIcons.add(R.drawable.ic_player_red_36);
        remainIcons.add(R.drawable.ic_player_yellow_36);
        remainColors.clear();
        remainColors.add(Color.BLUE);
        remainColors.add(Color.GREEN);
        remainColors.add(Color.rgb(220, 120, 30));
        remainColors.add(Color.RED);
        remainColors.add(Color.rgb(255, 225, 0));
        chatID = null;
        chatPassword = null;
        startTarget = null;
        taxiTickets = 0;
        busTickets = 0;
        tramTickets = 0;
        blackTickets = 0;
    }

    /**
	 * Gets the single instance of Game.
	 * 
	 * @return single instance of Game
	 */
    public static Game getInstance() {
        if (instance == null) {
            instance = new Game();
        }
        return instance;
    }

    /**
	 * Adds the game player.
	 * 
	 * @param player the player
	 */
    public void addGamePlayer(XHuntPlayer player) {
        if (!gamePlayers.contains(player)) {
            if (player.getMrX()) {
                player.setPlayerIconID(R.drawable.ic_player_mrx_36);
                player.setPlayerColorID(Color.BLACK);
            } else if (remainIcons.size() > 0 && remainColors.size() > 0) {
                player.setPlayerIconID(remainIcons.get(0));
                remainIcons.remove(0);
                player.setPlayerColorID(remainColors.get(0));
                remainColors.remove(0);
            }
            gamePlayers.add(player);
        }
    }

    /**
	 * Removes the game player.
	 * 
	 * @param player the player
	 */
    public void removeGamePlayer(XHuntPlayer player) {
        remainIcons.add(player.getPlayerIconID());
        remainColors.add(player.getPlayerColorID());
        gamePlayers.remove(player);
    }

    /**
	 * Removes the game player.
	 * 
	 * @param jid the jid of the player
	 */
    public void removeGamePlayer(String jid) {
        XHuntPlayer player = getGamePlayerByJID(jid);
        remainIcons.add(player.getPlayerIconID());
        remainColors.add(player.getPlayerColorID());
        gamePlayers.remove(player);
    }

    /**
	 * Updates a player.
	 * 
	 * @param player the player
	 */
    public void updatePlayer(XHuntPlayer player) {
        boolean playerFound = false;
        for (XHuntPlayer p : gamePlayers) {
            if (player.getJid().equals(p.getJid())) {
                if (p.getMrX() && !player.getMrX()) {
                    p.setPlayerIconID(remainIcons.get(0));
                    p.setPlayerColorID(remainColors.get(0));
                } else if (!p.getMrX() && player.getMrX()) {
                    remainIcons.add(p.getPlayerIconID());
                    remainColors.add(p.getPlayerColorID());
                    p.setPlayerIconID(R.drawable.ic_player_mrx_36);
                    p.setPlayerColorID(Color.BLACK);
                }
                p.setName(player.getName());
                p.setModerator(player.getModerator());
                p.setMrX(player.getMrX());
                p.setReady(player.getReady());
                playerFound = true;
            }
        }
        if (!playerFound) {
            if (player.getMrX()) {
                player.setPlayerIconID(R.drawable.ic_player_mrx_36);
                player.setPlayerColorID(Color.BLACK);
            } else if (remainIcons.size() > 0 && remainColors.size() > 0) {
                player.setPlayerIconID(remainIcons.get(0));
                remainIcons.remove(0);
                player.setPlayerColorID(remainColors.get(0));
                remainColors.remove(0);
            }
            gamePlayers.add(player);
        }
    }

    /**
	 * Sets the game id.
	 * 
	 * @param id the new game id
	 */
    public void setGameID(String id) {
        this.gameID = id;
    }

    /**
	 * Sets the current round.
	 * 
	 * @param round the new current round
	 */
    public void setCurrentRound(int round) {
        this.currentRound = round;
    }

    /**
	 * Sets the chat id.
	 * 
	 * @param id the new chat id
	 */
    public void setChatID(String id) {
        this.chatID = id;
    }

    /**
	 * Sets the chat password.
	 * 
	 * @param pass the new chat password
	 */
    public void setChatPassword(String pass) {
        this.chatPassword = pass;
    }

    /**
	 * Sets the new game player list.
	 * 
	 * @param players the new new game player list
	 */
    public void setNewGamePlayerList(ArrayList<XHuntPlayer> players) {
        for (XHuntPlayer player : players) {
            if (player.getMrX()) {
                player.setPlayerIconID(R.drawable.ic_player_mrx_36);
                player.setPlayerColorID(Color.BLACK);
            } else if (remainIcons.size() > 0 && remainColors.size() > 0) {
                player.setPlayerIconID(remainIcons.get(0));
                remainIcons.remove(0);
                player.setPlayerColorID(remainColors.get(0));
                remainColors.remove(0);
            }
            gamePlayers.add(player);
        }
    }

    /**
	 * Gets the game players.
	 * 
	 * @return the game players
	 */
    public ArrayList<XHuntPlayer> getGamePlayers() {
        return gamePlayers;
    }

    /**
	 * Gets the game player by jid.
	 * 
	 * @param jid the jid of the player
	 * 
	 * @return the player
	 */
    public XHuntPlayer getGamePlayerByJID(String jid) {
        for (XHuntPlayer player : gamePlayers) {
            if (player.getJid().equals(jid)) {
                return player;
            }
        }
        return null;
    }

    /**
	 * Gets the game player by id.
	 * 
	 * @param playerID the players id
	 * 
	 * @return the player
	 */
    public XHuntPlayer getGamePlayerByID(int playerID) {
        XHuntPlayer foundPlayer = null;
        for (XHuntPlayer player : gamePlayers) {
            if (player.getPlayerID() == playerID) {
                foundPlayer = player;
            }
        }
        return foundPlayer;
    }

    /**
	 * Gets the game player by name.
	 * 
	 * @param name the name of the player
	 * 
	 * @return the player
	 */
    public XHuntPlayer getGamePlayerByName(String name) {
        XHuntPlayer foundPlayer = null;
        for (XHuntPlayer player : gamePlayers) {
            if (player.getName().equals(name)) {
                foundPlayer = player;
            }
        }
        return foundPlayer;
    }

    /**
	 * Gets the chat id.
	 * 
	 * @return the chat id
	 */
    public String getChatID() {
        return chatID;
    }

    /**
	 * Gets the chat password.
	 * 
	 * @return the chat password
	 */
    public String getChatPassword() {
        return chatPassword;
    }

    /**
	 * Gets the current round.
	 * 
	 * @return the current round
	 */
    public int getCurrentRound() {
        return currentRound;
    }

    /**
	 * Gets the game id.
	 * 
	 * @return the game id
	 */
    public String getGameID() {
        return gameID;
    }

    /**
	 * Sets the start target.
	 * 
	 * @param startTarget the new start target
	 */
    public void setStartTarget(String startTarget) {
        this.startTarget = startTarget;
    }

    /**
	 * Gets the start target.
	 * 
	 * @return the start target
	 */
    public String getStartTarget() {
        return startTarget;
    }

    /**
	 * Gets the taxi tickets.
	 * 
	 * @return the taxi tickets
	 */
    public int getTaxiTickets() {
        return taxiTickets;
    }

    /**
	 * Decrease taxi tickets.
	 * 
	 * @return true, if successful
	 */
    public boolean decreaseTaxiTickets() {
        if (this.taxiTickets > 0) {
            this.taxiTickets = this.taxiTickets - 1;
            return true;
        }
        return false;
    }

    /**
	 * Gets the bus tickets.
	 * 
	 * @return the bus tickets
	 */
    public int getBusTickets() {
        return busTickets;
    }

    /**
	 * Decrease bus tickets.
	 * 
	 * @return true, if successful
	 */
    public boolean decreaseBusTickets() {
        if (this.busTickets > 0) {
            this.busTickets = this.busTickets - 1;
            return true;
        }
        return false;
    }

    /**
	 * Gets the tram tickets.
	 * 
	 * @return the tram tickets
	 */
    public int getTramTickets() {
        return tramTickets;
    }

    /**
	 * Decrease tram tickets.
	 * 
	 * @return true, if successful
	 */
    public boolean decreaseTramTickets() {
        if (this.tramTickets > 0) {
            this.tramTickets = this.tramTickets - 1;
            return true;
        }
        return false;
    }

    /**
	 * Gets the black tickets.
	 * 
	 * @return the black tickets
	 */
    public int getBlackTickets() {
        return blackTickets;
    }

    /**
	 * Decrease black tickets.
	 * 
	 * @return true, if successful
	 */
    public boolean decreaseBlackTickets() {
        if (this.blackTickets > 0) {
            this.blackTickets = this.blackTickets - 1;
            return true;
        }
        return false;
    }
}
