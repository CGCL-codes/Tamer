package org.icehockeymanager.ihm.clients.ihmgui.gui.standings;

import java.awt.BorderLayout;
import java.util.logging.Level;
import javax.swing.JScrollPane;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableColumnModel;
import org.icehockeymanager.ihm.clients.ihmgui.controller.ClientController;
import org.icehockeymanager.ihm.clients.ihmgui.gui.lib.JIhmPanel;
import org.icehockeymanager.ihm.clients.ihmgui.gui.lib.JIhmTable;
import org.icehockeymanager.ihm.clients.ihmgui.ihm.league.TMLeagueStandingsTable;
import org.icehockeymanager.ihm.game.league.helper.LeagueElement;
import org.icehockeymanager.ihm.game.league.helper.LeagueStandingsTable;

/**
 * The PanelLeagueStandingsTable contains: - A panel that displays a table of a
 * leagueElement.
 * 
 * @author Bernhard von Gunten
 * @created December 29, 2001
 */
public class PanelLeagueStandingsTable extends JIhmPanel {

    /** The Constant serialVersionUID. */
    static final long serialVersionUID = IHM_SWING_COMPONENT_VERSION;

    /** LeagueElement displayed on this panel. */
    LeagueElement leagueElement = null;

    /** The border layout1. */
    BorderLayout borderLayout1 = new BorderLayout();

    /** The scroll pane. */
    JScrollPane scrollPane = new JScrollPane();

    /** The j league standings table. */
    JIhmTable jLeagueStandingsTable = new JIhmTable();

    /**
   * Creates the panel, saves the leagueElement and calls ihmInit.
   * 
   * @param leagueElement LeagueElement to show in this panel
   */
    public PanelLeagueStandingsTable(LeagueElement leagueElement) {
        try {
            this.leagueElement = leagueElement;
            jbInit();
            ihmInit();
        } catch (Exception e) {
            String msg = "GUI Exception!";
            logger.log(Level.SEVERE, msg, e);
            ClientController.getInstance().showError(msg, e);
        }
    }

    /**
   * JBuilder stuff.
   * 
   * @throws Exception the exception
   * 
   * @exception Exception
   * Exception
   */
    void jbInit() throws Exception {
        this.setLayout(borderLayout1);
        this.add(scrollPane, BorderLayout.CENTER);
        scrollPane.getViewport().add(jLeagueStandingsTable, null);
    }

    /**
   * Calls displayTable that shows the table.
   */
    private void ihmInit() {
        displayTable();
    }

    /**
   * Shows table of the leagueElement.
   */
    public void displayTable() {
        LeagueStandingsTable leagueStandingsTable = (LeagueStandingsTable) leagueElement.getLeagueStandings();
        TMLeagueStandingsTable standings = new TMLeagueStandingsTable(leagueStandingsTable.getStandings());
        this.jLeagueStandingsTable.setModel(standings);
        jLeagueStandingsTable.setColumnSelectionAllowed(false);
        jLeagueStandingsTable.activateSorting();
        TableColumnModel tcm = jLeagueStandingsTable.getColumnModel();
        tcm.getColumn(TMLeagueStandingsTable.COLUMN_RANK).setMaxWidth(30);
        tcm.getColumn(TMLeagueStandingsTable.COLUMN_GAMESPLAYED).setMaxWidth(30);
        tcm.getColumn(TMLeagueStandingsTable.COLUMN_GAMESWON).setMaxWidth(30);
        tcm.getColumn(TMLeagueStandingsTable.COLUMN_GAMESTIED).setMaxWidth(30);
        tcm.getColumn(TMLeagueStandingsTable.COLUMN_GAMESLOST).setMaxWidth(30);
        tcm.getColumn(TMLeagueStandingsTable.COLUMN_GOALSFOR).setMaxWidth(40);
        tcm.getColumn(TMLeagueStandingsTable.COLUMN_GOALSAGAINST).setMaxWidth(40);
        tcm.getColumn(TMLeagueStandingsTable.COLUMN_GOALSDIFFERENCE).setMaxWidth(30);
        tcm.getColumn(TMLeagueStandingsTable.COLUMN_POINTS).setMaxWidth(30);
        DefaultTableCellRenderer dtcr = new DefaultTableCellRenderer();
        dtcr.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        tcm.getColumn(TMLeagueStandingsTable.COLUMN_RANK).setCellRenderer(dtcr);
        tcm.getColumn(TMLeagueStandingsTable.COLUMN_GAMESPLAYED).setCellRenderer(dtcr);
        tcm.getColumn(TMLeagueStandingsTable.COLUMN_GAMESWON).setCellRenderer(dtcr);
        tcm.getColumn(TMLeagueStandingsTable.COLUMN_GAMESTIED).setCellRenderer(dtcr);
        tcm.getColumn(TMLeagueStandingsTable.COLUMN_GAMESLOST).setCellRenderer(dtcr);
        tcm.getColumn(TMLeagueStandingsTable.COLUMN_GOALSFOR).setCellRenderer(dtcr);
        tcm.getColumn(TMLeagueStandingsTable.COLUMN_GOALSAGAINST).setCellRenderer(dtcr);
        tcm.getColumn(TMLeagueStandingsTable.COLUMN_GOALSDIFFERENCE).setCellRenderer(dtcr);
        tcm.getColumn(TMLeagueStandingsTable.COLUMN_POINTS).setCellRenderer(dtcr);
    }

    /**
   * Overrides function, for refreshing the table if needed.
   */
    public void ihmUpdate() {
        displayTable();
    }
}
