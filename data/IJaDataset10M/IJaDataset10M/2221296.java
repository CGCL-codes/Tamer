package org.icehockeymanager.ihm.clients.devgui.gui.eventlog;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.table.*;
import org.icehockeymanager.ihm.clients.devgui.controller.*;
import org.icehockeymanager.ihm.clients.devgui.gui.lib.*;
import org.icehockeymanager.ihm.clients.devgui.ihm.eventlog.*;
import org.icehockeymanager.ihm.game.*;
import org.icehockeymanager.ihm.game.user.*;

/**
 * PanelEventLog is the users mailbox for event log entries.
 * 
 * @author Bernhard von Gunten
 * @created January, 2005
 */
public class PanelEventLog extends JIhmPanel {

    static final long serialVersionUID = -3977167487217399945L;

    BorderLayout borderLayout1 = new BorderLayout();

    JIhmTabbedPane tabbedPanePlayers = new JIhmTabbedPane();

    BorderLayout borderLayout9 = new BorderLayout();

    JIhmPanel panelInbox = new JIhmPanel();

    JIhmTable inboxTable = new JIhmTable();

    JScrollPane jScrollPane7 = new JScrollPane();

    ImageIcon playersIcon = new ImageIcon();

    ImageIcon playerattributesIcon = new ImageIcon();

    ImageIcon playerstatsIcon = new ImageIcon();

    ImageIcon contractsIcon = new ImageIcon();

    /**
   * Constructs the frame, sets the user, calls ihmInit
   * 
   * @param user
   *          User to show this frame for
   */
    public PanelEventLog(User user) {
        super(user);
        try {
            initGUI();
            ihmInit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
   * JBuilder stuff
   * 
   * @exception Exception
   *              Exception
   */
    private void initGUI() throws Exception {
        this.setSize(730, 500);
        this.setLayout(borderLayout1);
        playersIcon = new ImageIcon(ClientController.getInstance().getGuiResource("icons/players18x18.png"));
        playerattributesIcon = new ImageIcon(ClientController.getInstance().getGuiResource("icons/playerattributes18x18.png"));
        playerstatsIcon = new ImageIcon(ClientController.getInstance().getGuiResource("icons/playerstats18x18.png"));
        contractsIcon = new ImageIcon(ClientController.getInstance().getGuiResource("icons/finances18x18.png"));
        panelInbox.setLayout(borderLayout9);
        inboxTable.addMouseListener(new java.awt.event.MouseAdapter() {

            public void mouseClicked(MouseEvent e) {
                inboxTable_mouseClicked(e);
            }
        });
        tabbedPanePlayers.addTab(ClientController.getInstance().getTranslation("eventlog.inbox"), playerattributesIcon, panelInbox, ClientController.getInstance().getTranslation("eventlog.inbox"));
        panelInbox.add(jScrollPane7, BorderLayout.CENTER);
        jScrollPane7.getViewport().add(inboxTable, null);
        this.add(tabbedPanePlayers, BorderLayout.CENTER);
    }

    /**
   * Create the panelLeagueStandings and add it to the frame
   */
    private void ihmInit() {
        this.setTitleKey("title.eventlog");
        displayOwnLog();
    }

    /** Display the team */
    private void displayOwnLog() {
        this.inboxTable.setModel(new TMEventLog(GameController.getInstance().getScenario().getEventLog().getUserLog(getOwner(), GameController.getInstance().getScenario().getScheduler().getToday())));
        inboxTable.setColumnSelectionAllowed(false);
        inboxTable.activateSorting();
        TableColumnModel tcm = inboxTable.getColumnModel();
        tcm.getColumn(TMEventLog.COLUMN_DAY).setMinWidth(80);
        tcm.getColumn(TMEventLog.COLUMN_DAY).setMaxWidth(80);
    }

    /**
   * Show player behind selected row
   * 
   * @param e
   *          Source event
   */
    void inboxTable_mouseClicked(MouseEvent e) {
    }
}
