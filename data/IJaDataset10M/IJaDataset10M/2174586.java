package batserver;

import batcommon.BattleGameTypes;
import batcommon.BattleShipShip;

public class BattleGame {

    public static final int BG_STATUS_NOADD = -1;

    public static final int BG_STATUS_EMPTY = 0;

    public static final int BG_STATUS_AWAIT = 1;

    public static final int BG_STATUS_STARTED = 2;

    public static final int BG_STATUS_GRID_ADDED = 3;

    public static final int BG_STATUS_GRID_FLEET_FULL = 4;

    public static final int BG_STATUS_ALL_GRID_FLEETS_FULL = 5;

    private static int class_gameID = 0;

    private int status;

    private int game_type;

    private int num_players;

    private int num_filled;

    private Player[] players;

    private BattleGrid[] grids;

    private int gameID;

    private int num_robins = 0;

    private int round_robin_idx = 0;

    private int[][] round_robin_list;

    public BattleGame() {
        this(BattleGameTypes.BG_10x10_2P);
    }

    public BattleGame(int game_t) {
        game_type = game_t;
        status = BG_STATUS_EMPTY;
        num_players = BattleGameTypes.getGameNumPlayers(game_type);
        num_filled = 0;
        players = new Player[num_players];
        grids = new BattleGrid[num_players];
        synchronized (this) {
            class_gameID++;
            gameID = class_gameID;
        }
    }

    public boolean isGameFilled() {
        if (num_filled == num_players || status == BG_STATUS_STARTED) return true; else return false;
    }

    public boolean isPlayerInThisGame(int player_id) {
        boolean return_val = false;
        int i;
        for (i = 0; i < num_filled; i++) {
            if (player_id == players[i].getPlayerID()) return true;
        }
        return return_val;
    }

    public int addPlayer(Player pl) {
        if (pl == null) {
            System.out.println("ERROR: null player in BattleGame.addPlayer");
            return BG_STATUS_NOADD;
        }
        if (num_filled < num_players) {
            players[num_filled] = pl;
            pl.setStatus(Player.PLAYER_IN_GAME);
            num_filled++;
            if (num_filled < num_players) status = BG_STATUS_AWAIT; else {
                status = BG_STATUS_STARTED;
                initializeRoundRobin();
            }
            return status;
        } else return BG_STATUS_NOADD;
    }

    public int removePlayer(Player pl) {
        if (pl == null || num_players == 0 || num_filled == 0) {
            System.out.println("ERROR: null player or no players in BattleGame.removePlayer");
            return BG_STATUS_NOADD;
        }
        int idx = getPlayerIndex(pl.getPlayerID());
        if (idx >= 0 && idx < num_filled) {
            for (int i = idx; i < (num_filled - 1); i++) {
                players[i] = players[i + 1];
            }
            players[num_filled - 1] = null;
            num_filled--;
        }
        status = BG_STATUS_AWAIT;
        return status;
    }

    public int addGridToPlayer(Player pl, int fleet_size) {
        if (pl == null || num_players == 0 || num_filled == 0) {
            System.out.println("ERROR: null player or no players in BattleGame.addGridToPlayer");
            return BG_STATUS_NOADD;
        }
        int idx = getPlayerIndex(pl.getPlayerID());
        if (idx >= 0 && idx < num_filled) {
            grids[idx] = new BattleGrid(game_type, fleet_size);
            return BG_STATUS_GRID_ADDED;
        }
        System.out.println("ERROR: no add BattleGame.addGridToPlayer");
        return BG_STATUS_NOADD;
    }

    public int addShipToPlayer(Player pl, String full_coords_print) {
        int status;
        if (pl == null || num_players == 0 || num_filled == 0) {
            System.out.println("ERROR: null player or no players in BattleGame.addGridToPlayer");
            return BG_STATUS_NOADD;
        }
        int idx = getPlayerIndex(pl.getPlayerID());
        if (idx >= 0 && idx < num_filled) {
            status = grids[idx].addShipToFleet(full_coords_print);
            return status;
        }
        System.out.println("ERROR: no add BattleGame.addGridToPlayer");
        return BG_STATUS_NOADD;
    }

    public boolean isPlayerGridFilled(Player pl) {
        if (pl == null || num_players == 0 || num_filled == 0) {
            System.out.println("ERROR: null player or no players in BattleGame.isPlayerGridFilled");
            return false;
        }
        int idx = getPlayerIndex(pl.getPlayerID());
        if (idx >= 0 && idx < num_filled) {
            return grids[idx].isFleetFull();
        }
        System.out.println("ERROR: no add BattleGame.isPlayerGridFilled");
        return false;
    }

    public int fireAtPlayer(Player pl, int row, int col) {
        int status;
        if (pl == null || num_players == 0 || num_filled == 0) {
            System.out.println("ERROR: null player or no players in BattleGame.fireAtPlayer");
            return BattleShipShip.BSS_NO_HIT;
        }
        int idx = getPlayerIndex(pl.getPlayerID());
        if (idx >= 0 && idx < num_filled) {
            status = grids[idx].fireShot(row, col);
            return status;
        }
        System.out.println("ERROR: no grid BattleGame.fireAtPlayer");
        return BattleShipShip.BSS_NO_HIT;
    }

    public String getPlayerUnhitCoords(Player pl) {
        int idx = getPlayerIndex(pl.getPlayerID());
        return grids[idx].getUnHitShipCoords();
    }

    public String getPlayerSunkShip(Player pl, int row, int col) {
        String name_coord_list = null;
        if (pl == null || num_players == 0 || num_filled == 0) {
            System.out.println("ERROR: null player or no players in BattleGame.getPlayerSunkShip");
            return name_coord_list;
        }
        int idx = getPlayerIndex(pl.getPlayerID());
        if (idx >= 0 && idx < num_filled) {
            name_coord_list = grids[idx].getSunkShipTypeAndCoords(row, col);
            return name_coord_list;
        }
        System.out.println("ERROR: no grid BattleGame.fireAtPlayer");
        return name_coord_list;
    }

    public boolean isPlayerSunk(Player pl) {
        if (pl == null || num_players == 0 || num_filled == 0) {
            System.out.println("ERROR: null player or no players in BattleGame.isPlayerGridFilled");
            return false;
        }
        int idx = getPlayerIndex(pl.getPlayerID());
        if (idx >= 0 && idx < num_filled) {
            return grids[idx].isFleetSunk();
        }
        System.out.println("ERROR: no add BattleGame.isPlayerGridFilled");
        return false;
    }

    public boolean areAllPlayerGridsFull() {
        int i;
        int my_counter = 0;
        for (i = 0; i < num_players; i++) {
            if (grids[i] == null) return false;
            if (grids[i].isFleetFull() == false) return false; else my_counter++;
        }
        return (my_counter == num_players);
    }

    private void initializeRoundRobin() {
        if (num_players == 1) {
            num_robins = 1;
            round_robin_list = new int[num_robins][2];
            round_robin_list[0][0] = players[0].getPlayerID();
            round_robin_list[0][1] = players[0].getPlayerID();
        } else {
            num_robins = num_players * (num_players - 1);
            round_robin_list = new int[num_robins][2];
            int offset;
            int i;
            int idx = 0;
            for (offset = 1; offset < num_players; offset++) {
                for (i = 0; i < num_players; i++) {
                    round_robin_list[idx][0] = players[i].getPlayerID();
                    round_robin_list[idx][1] = players[(i + offset) % num_players].getPlayerID();
                    idx++;
                }
            }
        }
    }

    public int getCurrentPlayer() {
        return round_robin_list[round_robin_idx][0];
    }

    public int getNextPlayer() {
        int players_left = getNumPlayersRemaining();
        int loop_counter = 0;
        if (players_left == 1 && num_players > 1) return 0; else if (players_left == 0) return 0;
        round_robin_idx++;
        round_robin_idx %= num_robins;
        while (round_robin_list[round_robin_idx][0] == -1 && round_robin_list[round_robin_idx][1] == -1 && loop_counter < num_robins) {
            loop_counter++;
            round_robin_idx++;
            round_robin_idx %= num_robins;
        }
        if (round_robin_list[round_robin_idx][0] == -1) return 0; else return round_robin_list[round_robin_idx][0];
    }

    public int getOpponent() {
        return round_robin_list[round_robin_idx][1];
    }

    public int getGameID() {
        return gameID;
    }

    public int getNumPlayers() {
        return num_players;
    }

    private void removeSunkCombosFromRoundRobin(int idx) {
        int playerid = players[idx].getPlayerID();
        for (int i = 0; i < num_robins; i++) {
            if (round_robin_list[i][0] == playerid || round_robin_list[i][1] == playerid) {
                round_robin_list[i][0] = -1;
                round_robin_list[i][1] = -1;
            }
        }
    }

    private int getNumPlayersRemaining() {
        int remaining = 0;
        for (int i = 0; i < num_players; i++) {
            if (grids[i].isFleetSunk() == false) {
                remaining++;
            } else {
                removeSunkCombosFromRoundRobin(i);
            }
        }
        return remaining;
    }

    public Player getPlayer(int play_idx) {
        if (play_idx >= 0 && play_idx < num_players) return players[play_idx]; else return null;
    }

    private int getPlayerIndex(int player_id) {
        int idx;
        for (idx = 0; idx < num_filled; idx++) {
            if (players[idx].getPlayerID() == player_id) return idx;
        }
        return -1;
    }

    public int getGameType() {
        return game_type;
    }

    public int getStatus() {
        return status;
    }

    public int getAwaiting() {
        return (num_players - num_filled);
    }

    public String getPlayerIDsString() {
        String list = "";
        for (int i = 0; i < num_filled; i++) {
            if (i > 0) list = list + ":";
            list = list + players[i].getPlayerID();
        }
        return list;
    }

    public String getPlayerHandlesString() {
        String list = "";
        for (int i = 0; i < num_filled; i++) {
            if (i > 0) list = list + ":";
            list = list + players[i].getHandle();
        }
        return list;
    }

    public String getSessionIDsString() {
        String list = "";
        for (int i = 0; i < num_filled; i++) {
            if (i > 0) list = list + ":";
            list = list + players[i].getSessionID();
        }
        return list;
    }

    public int[] getSessionIDs() {
        int[] list = new int[num_filled];
        for (int i = 0; i < num_filled; i++) {
            list[i] = players[i].getSessionID();
        }
        return list;
    }
}
