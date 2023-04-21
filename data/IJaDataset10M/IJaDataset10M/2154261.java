package org.icehockeymanager.ihm.clients.devgui.ihm.player;

import javax.swing.table.*;
import org.icehockeymanager.ihm.clients.devgui.controller.*;
import org.icehockeymanager.ihm.game.player.*;
import org.icehockeymanager.ihm.lib.*;

/**
 * Table model to show the actual leagueStandings.
 * 
 * @author Bernhard von Gunten
 * @created December 29, 2001
 */
public class TMPlayerContracts extends AbstractTableModel implements IhmTableModelSorter {

    static final long serialVersionUID = 8030463151397474340L;

    /** Column rank */
    public static final int COLUMN_RANK = 0;

    /** Column position */
    public static final int COLUMN_POSITION = 1;

    /** Column player */
    public static final int COLUMN_PLAYER = 2;

    /** Column owner */
    public static final int COLUMN_TEAM = 3;

    /** Column cost */
    public static final int COLUMN_COSTS = 4;

    /** Column end date */
    public static final int COLUMN_END_DATE = 5;

    /** Column is on transfer list */
    public static final int COLUMN_TRANSFERLIST = 6;

    /** Column count */
    private static final int COLUMN_COUNT = 7;

    /** PlayerStats of this table */
    private Player[] players;

    /**
   * Constructor for the TMPlayerStats object
   * 
   * @param players
   *          Description of the Parameter
   */
    public TMPlayerContracts(Player[] players) {
        this.players = players;
        ihmSort(COLUMN_END_DATE, false);
    }

    /**
   * Returns column count
   * 
   * @return The columnCount value
   */
    public int getColumnCount() {
        return COLUMN_COUNT;
    }

    /**
   * Returns the value of a field.
   * 
   * @param row
   *          The row
   * @param column
   *          The collumn
   * @return The valueAt value
   */
    public Object getValueAt(int row, int column) {
        switch(column) {
            case COLUMN_RANK:
                return String.valueOf(players[row].getTeamJerseyNumber());
            case COLUMN_PLAYER:
                {
                    Player player = (Player) players[row].getPlayerContractCurrent().getPartyB();
                    return player.getPlayerInfo().getPlayerName();
                }
            case COLUMN_POSITION:
                {
                    Player player = (Player) players[row].getPlayerContractCurrent().getPartyB();
                    return PlayerTools.getPositionStr(player.getPlayerAttributes());
                }
            case COLUMN_TEAM:
                {
                    Player player = (Player) players[row].getPlayerContractCurrent().getPartyB();
                    return PlayerTools.getPositionStr(player.getPlayerAttributes());
                }
            case COLUMN_END_DATE:
                {
                    try {
                        return Tools.dateToString(players[row].getPlayerContractCurrent().getEndDate().getTime(), Tools.DATE_FORMAT_EU_DATE);
                    } catch (Exception err) {
                        return "##.##.####";
                    }
                }
            case COLUMN_COSTS:
                {
                    return String.valueOf(Tools.doubleToStringC(players[row].getPlayerContractCurrent().getAmount()));
                }
            case COLUMN_TRANSFERLIST:
                {
                    if (players[row].isOnTransferList()) {
                        return "*";
                    } else {
                        return "";
                    }
                }
            default:
                return null;
        }
    }

    /**
   * Returns row count
   * 
   * @return The rowCount value
   */
    public int getRowCount() {
        return players.length;
    }

    /**
   * Sorts playerStats by collumn and fires a tableDataChanged event
   * 
   * @param collumnNr
   *          Collumn to sort
   * @param standard
   *          Standard sort order
   */
    public void ihmSort(int collumnNr, boolean standard) {
        internalSort(collumnNr, standard);
        this.fireTableDataChanged();
    }

    /**
   * Returns the player of a row passed by
   * 
   * @param row
   *          The row of the wanted player
   * @return The player value
   */
    public Player getPlayer(int row) {
        Player player = players[row];
        return player;
    }

    /**
   * Sorts playerStats by collumn and fires a tableDataChanged event
   * 
   * @param collumnNr
   *          Collumn to sort
   * @param standard
   *          Standard sort order
   */
    private void internalSort(int collumnNr, boolean standard) {
        int sortCriteria = 0;
        boolean ascending = true;
        switch(collumnNr) {
            case COLUMN_END_DATE:
                {
                    sortCriteria = Player.SORT_CONTRACT_CURRENT_END_DATE;
                    ascending = false;
                    break;
                }
            case COLUMN_COSTS:
                {
                    sortCriteria = Player.SORT_CONTRACT_CURRENT_COSTS;
                    ascending = false;
                    break;
                }
            default:
                {
                    return;
                }
        }
        for (int i = 0; i < players.length; i++) {
            players[i].setSortCriteria(sortCriteria);
            if (standard) {
                players[i].setSortOrder(ascending);
            } else {
                players[i].setSortOrder(!ascending);
            }
        }
        java.util.Arrays.sort(players);
    }

    /**
   * Gets the columnName attribute of the TMPlayerStats object
   * 
   * @param collumnNr
   *          The column nr
   * @return The columnName value
   */
    public String getColumnName(int collumnNr) {
        switch(collumnNr) {
            case COLUMN_RANK:
                return ClientController.getInstance().getTranslation("ihm.tmRank");
            case COLUMN_POSITION:
                return ClientController.getInstance().getTranslation("tmPlayerStats.position");
            case COLUMN_PLAYER:
                return ClientController.getInstance().getTranslation("ihm.player");
            case COLUMN_TEAM:
                return ClientController.getInstance().getTranslation("ihm.team");
            case COLUMN_END_DATE:
                return ClientController.getInstance().getTranslation("tmPlayerContracts.enddate");
            case COLUMN_COSTS:
                return ClientController.getInstance().getTranslation("tmPlayerContracts.costs");
            case COLUMN_TRANSFERLIST:
                return ClientController.getInstance().getTranslation("tmPlayerContracts.transferlist");
            default:
                return null;
        }
    }
}
